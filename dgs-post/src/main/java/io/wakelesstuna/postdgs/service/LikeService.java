package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.types.errors.ErrorType;
import io.wakelesstuna.post.generated.types.LikeInput;
import io.wakelesstuna.postdgs.exceptions.MyCustomException;
import io.wakelesstuna.postdgs.persistance.LikeEntity;
import io.wakelesstuna.postdgs.persistance.LikeRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@DgsComponent
public class LikeService {

    private final ServiceHelper serviceHelper;
    private final LikeRepository likeRepo;

    /**
     * @param likeInput
     * @return
     */
    public String likePost(LikeInput likeInput) {
        log.info("likeInput: {}", likeInput);
        var post = serviceHelper.getPost(likeInput.getPostId());

        if (!serviceHelper.doesUserExists(likeInput.getUserId())) {
            throw new MyCustomException("User does not exits", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ErrorType.BAD_REQUEST);

        }

        if (likeRepo.existsByUserIdAndAndPostId(likeInput.getUserId(), post.getId()))
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

    public String unLikePost(LikeInput likeInput) {
        var post = serviceHelper.getPost(likeInput.getPostId());
        var like = likeRepo.findByPostIdAndUserId(post.getId(), likeInput.getUserId());
        likeRepo.delete(like);
        log.info("Like removed to post");
        return HttpStatus.ACCEPTED.toString();
    }

    public boolean isPostLiked(LikeInput input) {
        return likeRepo.existsByUserIdAndAndPostId(input.getUserId(), input.getPostId());
    }

    public Map<UUID, Integer> likesForPosts(List<UUID> postIds) {
        var likesMap = postIds.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(),
                        likeRepo::countAllByPostId,
                        (a, b) -> a,
                        ConcurrentHashMap::new));
        log.info("Fetching like count for post");
        return likesMap;
    }

    public Integer getTotalLikes(UUID postId) {
        log.info("Fetching all likes for post: {}", postId);
        return likeRepo.countAllByPostId(postId);
    }
}
