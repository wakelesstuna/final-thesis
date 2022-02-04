package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import graphql.GraphQLException;
import io.wakelesstuna.post.generated.types.BookmarkInput;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.postdgs.dataloader.BookmarkDataLoader;
import io.wakelesstuna.postdgs.persistance.BookmarkEntity;
import io.wakelesstuna.postdgs.persistance.BookmarkRepository;
import io.wakelesstuna.postdgs.persistance.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a service class that handle the logic for the {@link BookmarkEntity}.
 *
 * @author oscar.steen.forss
 */
@Slf4j
@AllArgsConstructor
@DgsComponent
public class BookmarkService {

    private final ServiceHelper serviceHelper;
    private final BookmarkRepository bookmarkRepo;
    private final PostRepository postRepo;

    /**
     * This method creates a bookmark. It first checks that the user and post exists.
     * Then it checks that the user doesn't already have the post as bookmarked.
     * Then it creates a bookmark and persists it to the database.
     * Returns Http status ACCEPTED as a string
     *
     * @param input Object {@link BookmarkInput} with information about bookmarking a post
     * @return String
     */
    public String bookmarkPost(BookmarkInput input) {
        if (!serviceHelper.postExists(input.getPostId()) &&
                !serviceHelper.doesUserExists(input.getUserId())) {
            throw new GraphQLException("Post or User does not exists");
        }

        if (bookmarkRepo.findByUserIdAndPostId(input.getUserId(), input.getPostId()).isPresent()) {
            throw new GraphQLException("Post already bookmarked by user");
        }

        var bookmark = BookmarkEntity.builder()
                .id(UUID.randomUUID())
                .postId(input.getPostId())
                .userId(input.getUserId())
                .createAt(serviceHelper.getLocalDateTime())
                .build();
        bookmarkRepo.save(bookmark);

        return HttpStatus.ACCEPTED.toString();
    }

    /**
     * Un marks a post as bookmarked for a user if the user have it as bookmarked.
     * Else throws an error.
     * Returns Http status ACCEPTED as a string
     *
     * @param input Object {@link BookmarkInput} with information un bookmarking a post
     * @return String
     */
    public String unBookmarkPost(BookmarkInput input) {

        var bookmarkToDelete = bookmarkRepo.findByUserIdAndPostId(input.getUserId(), input.getPostId()).orElseThrow(() -> new GraphQLException("No bookmark found"));
        bookmarkRepo.delete(bookmarkToDelete);
        return HttpStatus.ACCEPTED.toString();
    }

    /**
     * Get the total count for a users bookmarks.
     *
     * @param id id of the user to fetch bookmarks for.
     * @return Integer
     */
    public Integer getTotalBookmarks(UUID id) {
        return bookmarkRepo.countAllByUserId(id);
    }

    /**
     * This method is used by the {@link BookmarkDataLoader} to fetch
     * multiply Lists of post for multiply users.
     *
     * @param userIds List of user ids to fetch all bookmarked posts for.
     * @return Map of the result of the search with ids as keys and a list of posts as value.
     */
    @Transactional
    public Map<UUID, List<Post>> bookmarksForUsers(List<UUID> userIds) {
        List<BookmarkEntity> allByUserIds = bookmarkRepo.findAllByUserIdIn(userIds);
        return userIds.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(),
                        id -> allByUserIds.stream().filter(b -> b.getUserId().equals(id))
                                .map(b -> postRepo.getById(b.getPostId()).mapToPostType())
                                .collect(Collectors.toList()),
                        (a, b) -> a,
                        ConcurrentHashMap::new));
    }
}
