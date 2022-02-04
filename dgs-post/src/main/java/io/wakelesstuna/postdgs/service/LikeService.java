package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import io.wakelesstuna.post.generated.types.LikeInput;
import io.wakelesstuna.postdgs.exceptions.MyCustomException;
import io.wakelesstuna.postdgs.persistance.LikeEntity;
import io.wakelesstuna.postdgs.persistance.LikeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a service class that handle the logic for the {@link LikeEntity}.
 *
 * @author oscar.steen.forss
 */
@Slf4j
@AllArgsConstructor
@DgsComponent
public class LikeService {

    private final ServiceHelper serviceHelper;
    private final LikeRepository likeRepo;

    /**
     * Adds a like to a post, checks if the post and user exists.
     * Also check if the user already like the post.
     * Returns "202 ACCEPTED" if all went well.
     *
     * @param likeInput LikeInput information about the user.
     * @return String
     */
    public String likePost(LikeInput likeInput) {
        var post = serviceHelper.getPost(likeInput.getPostId());

        if (!serviceHelper.doesUserExists(likeInput.getUserId())) {
            throw new MyCustomException("User does not exits", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);

        }

        if (likeRepo.existsByUserIdAndPostId(likeInput.getUserId(), post.getId()))
            throw MyCustomException.builder()
                    .message("User already liked this post")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .errorType(ErrorType.BAD_REQUEST).build();

        var like = LikeEntity.builder()
                .id(UUID.randomUUID())
                .userId(likeInput.getUserId())
                .postId(post.getId())
                .createdAt(serviceHelper.getLocalDateTime())
                .build();
        likeRepo.save(like);
        log.info("Like added to post");
        return HttpStatus.ACCEPTED.toString();
    }

    /**
     * Unlike a post.
     * Returns "202 ACCEPTED" if all goes well.
     *
     * @param likeInput LikeInput information about what post to like.
     * @return String
     */
    public String unLikePost(LikeInput likeInput) {
        var post = serviceHelper.getPost(likeInput.getPostId());
        var like = likeRepo.findByPostIdAndUserId(post.getId(), likeInput.getUserId());
        likeRepo.delete(like);
        log.info("Like removed to post");
        return HttpStatus.ACCEPTED.toString();
    }

    /**
     * Checks if a post is liked.
     *
     * @param input LikeInput information about likes.
     * @return Boolean.
     */
    public Boolean isPostLiked(LikeInput input) {
        return likeRepo.existsByUserIdAndPostId(input.getUserId(), input.getPostId());
    }

    /**
     * Counts all the likes for a list of posts.
     *
     * @param postIds List of UUID, ids of posts.
     * @return Map<UUID, Integer>
     */
    public Map<UUID, Integer> likesForPosts(List<UUID> postIds) {
        var likesMap = postIds.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(),
                        likeRepo::countAllByPostId,
                        (a, b) -> a,
                        ConcurrentHashMap::new));
        log.info("Fetching like count for post");
        return likesMap;
    }

    /**
     * Counts all the likes for a post.
     *
     * @param postId UUID id of the post.
     * @return Integer.
     */
    public Integer getTotalLikes(UUID postId) {
        log.info("Fetching all likes for post: {}", postId);
        return likeRepo.countAllByPostId(postId);
    }
}
