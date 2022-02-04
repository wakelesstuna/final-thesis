package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import graphql.relay.*;
import io.wakelesstuna.post.generated.types.*;
import io.wakelesstuna.postdgs.connection.CursorUtil;
import io.wakelesstuna.postdgs.exceptions.PostNotFoundException;
import io.wakelesstuna.postdgs.persistance.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a service class that handle the logic for the {@link PostEntity}.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final ServiceHelper serviceHelper;
    private final CursorUtil cursorUtil;
    private final PostRepository postRepo;
    private final BookmarkRepository bookmarkRepo;
    private final CommentRepository commentRepo;
    private final LikeRepository likeRepo;

    private int originalListSize;
    private int cursorIndex;

    /**
     * Creates a post.
     *
     * @param input CreatePostInput information about the post that is to be created.
     * @param file  MultiPartFile image file for the post.
     * @return Post
     */
    @Transactional
    public Post createPost(CreatePostInput input, MultipartFile file) {
        UUID postId = UUID.randomUUID();
        log.info("User id when created post: {}", input.getUserId());
        String imageUrl = getImageUrl(input, file);

        PostEntity newPost = PostEntity.builder()
                .id(postId)
                .userId(input.getUserId())
                .imageUrl(imageUrl)
                .caption(input.getCaption())
                .createdAt(serviceHelper.getLocalDateTime())
                .build();
        postRepo.saveAndFlush(newPost);
        log.info("New post created {}", newPost.getId());
        return newPost.mapToPostType();
    }

    /**
     * Fetches a post by id.
     *
     * @param postId UUID id of the post.
     * @return Post
     */
    public Post getPost(UUID postId) {
        return serviceHelper.getPost(postId).mapToPostType();
    }

    /**
     * Calls post repository for the count of all posts for a user
     *
     * @param id id of the user.
     * @return Integer
     */
    public Integer getTotalPostOfUser(UUID id) {
        log.info("Counting all posts for user");
        return postRepo.countAllByUserId(id);
    }

    /**
     * Fetches all the posts for a user based on the user id.
     *
     * @param postFilter PostFilter filter what posts to get.
     * @return List of Post
     */
    public List<Post> getPosts(PostFilter postFilter) {
        log.info("Fetching all posts for user {}", postFilter.getUserId());
        return postRepo.findAllByUserId(postFilter.getUserId()).stream()
                .map(PostEntity::mapToPostType)
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Fetches a list of posts.
     *
     * @return List of Post
     */
    public List<Post> getPosts() {
        log.info("Fetching all posts");
        return postRepo.findAll().stream()
                .map(PostEntity::mapToPostType)
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * This method is used to fetch posts with pagination. It uses a cursor to
     * determinate where to start to fetch all posts. How many posts that are loaded for
     * each page is determent by the "first" variable.
     *
     * @param first  Integer how many post per page.
     * @param cursor String cursor of a post.
     * @return Connection<Post>
     */
    public Connection<Post> getPaginationPost(@NotNull Integer first, @Nullable String cursor) {
        if (cursor == null) {
            cursorIndex = 0;
        }
        List<Edge<Post>> edges = getPostsWithCursor(cursor).stream()
                .map(post -> new DefaultEdge<>(post, cursorUtil.createCursorWith(post.getId())))
                .limit(first)
                .collect(Collectors.toList());

        var pageInfo = new DefaultPageInfo(
                cursorUtil.getFirstCursorFrom(edges),
                cursorUtil.getLastCursorFrom(edges),
                cursor != null,
                hasNextPage(originalListSize, cursorIndex, first));

        return new DefaultConnection<>(edges, pageInfo);
    }

    /**
     * Check if the list of post can load a next page.
     *
     * @param originalListSize Integer size of the hole list.
     * @param cursorIndex      Integer Index of the cursor post.
     * @param first            Integer How many you want to load for the next page.
     * @return Boolean
     */
    private Boolean hasNextPage(Integer originalListSize, Integer cursorIndex, Integer first) {
        return cursorIndex + 1 + first < originalListSize;
    }

    /**
     * Fetches a list of post depending on the cursor.
     *
     * @param cursor String
     * @return List<Post>
     */
    private List<Post> getPostsWithCursor(@Nullable String cursor) {
        List<Post> posts;
        if (cursor == null) {
            posts = getPosts();
            resetCursorIndex();
            originalListSize = posts.size();
        } else {
            posts = getPostsAfter(cursorUtil.decode(cursor));
        }
        return posts;
    }

    /**
     * Fetches a list of post, after the id that is sent in as parameter.
     *
     * @param id UUID id of the post.
     * @return List<Post>
     */
    private List<Post> getPostsAfter(UUID id) {
        List<Post> posts = getPosts();
        cursorIndex = getCursorIndex(posts, id);
        originalListSize = posts.size();
        return dropPostBeforeCursor(posts, id);
    }

    /**
     * Gets the index of the cursor of the list.
     *
     * @param list List<Post> list in which to find the index.
     * @param id   UUID id of the post to find the index of.
     * @return Integer.
     */
    private Integer getCursorIndex(List<Post> list, UUID id) {
        return IntStream.range(0, list.size()).filter(i -> list.get(i).getId().equals(id))
                .findFirst()
                .orElse(0);
    }

    /**
     * Resets the cursor index.
     */
    private void resetCursorIndex() {
        cursorIndex = 0;
    }

    /**
     * Drops all the posts in a list before and id.
     *
     * @param list List<Post> list of which to drop from.
     * @param id   UUID id of the post to drop before.
     * @return List<Post>
     */
    private List<Post> dropPostBeforeCursor(List<Post> list, UUID id) {
        return list.stream()
                .dropWhile(post -> post.getId().compareTo(id) != 0)
                .skip(1)
                .collect(Collectors.toList());
    }

    /**
     * Makes one query to the database to fetch all post for all the users in the id list.
     * Then map those to a Map where the ids of the users are the keys and the values are
     * a list of their posts.
     *
     * @param userIds ids of users to fetch posts for
     * @return Map<UUID, List<Post>>
     */
    public Map<UUID, List<Post>> postsForUsers(List<UUID> userIds) {
        List<PostEntity> allByUserIds = postRepo.findByUserIdIn(userIds);
        return userIds.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(),
                        id -> allByUserIds.stream().filter(p -> p.getUserId().equals(id))
                                .map(PostEntity::mapToPostType)
                                .collect(Collectors.toList()),
                        (a, b) -> a,
                        ConcurrentHashMap::new));
    }

    /**
     * Check if the user has bookmarked a post returns true if user has.
     *
     * @param input userId and PostId
     * @return boolean
     */
    public boolean isBookmarkedByUser(BookmarkInput input) {
        log.info("Checking post bookmarked for user");
        return bookmarkRepo.existsByUserIdAndPostId(input.getUserId(), input.getPostId());
    }

    /**
     * This method is used to create the image url for the post. I Did not get file uploading to
     * work with Apollo Federation {@link <a href="https://www.apollographql.com/docs/federation/">Apollo Federation docs</a>}
     * So i send a request to an external cdn server to upload the image before and to get a url for
     * the image from the server that i send into the createPostRequest.
     *
     * @param input information about creating a post
     * @param file  file for the image to upload (not working with Apollo Federation yet)
     * @return String
     */
    private String getImageUrl(CreatePostInput input, MultipartFile file) {
        if (file != null) {
            return serviceHelper.uploadImage(input.getUserId(), file);
        } else if (input.getImageUrl() != null) {
            return input.getImageUrl();
        }
        return "";
    }

    /**
     * Deletes a post.
     *
     * @param postInput PostInput information about the post to delete.
     * @return Boolean.
     */
    @Transactional
    public Boolean deletePost(PostInput postInput) {

        PostEntity post = postRepo.findByUserIdAndId(postInput.getUserId(), postInput.getPostId())
                .orElseThrow(() -> PostNotFoundException.builder()
                        .message("No post found that matches the credentials")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errorType(ErrorType.BAD_REQUEST)
                        .build());

        List<LikeEntity> likesToDelete = likeRepo.findAllByPostId(postInput.getPostId());
        likeRepo.deleteAll(likesToDelete);

        List<CommentEntity> commentsToDelete = commentRepo.findAllByPostId(postInput.getPostId());
        commentRepo.deleteAll(commentsToDelete);

        List<BookmarkEntity> bookmarksToDelete = bookmarkRepo.findAllByPostId(postInput.getPostId());
        bookmarkRepo.deleteAll(bookmarksToDelete);

        try {
            serviceHelper.deleteImage(post);
        } catch (Exception e) {
            log.error("No image to delete");
        }
        postRepo.delete(post);
        log.info("Post deleted: {}", post.getId());

        return true;
    }
}
