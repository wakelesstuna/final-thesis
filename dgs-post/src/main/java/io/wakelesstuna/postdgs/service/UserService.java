package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import io.wakelesstuna.postdgs.persistance.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * @author oscar.steen.forss
 */
@DgsComponent
@Slf4j
@AllArgsConstructor
public class UserService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final LikeRepository likeRepo;
    private final BookmarkRepository bookmarkRepo;
    private final StoryRepository storyRepo;

    @Transactional
    public String deleteUserInformation(UUID userId) {
        postRepo.deleteAllByUserId(userId);
        log.info("Deleting all posts of user {}", userId);

        commentRepo.deleteAllByUserId(userId);
        log.info("Deleting all comments of user {}", userId);

        likeRepo.deleteAllByUserId(userId);
        log.info("Deleting all likes of user {}", userId);

        bookmarkRepo.deleteAllByUserId(userId);
        log.info("Deleting all bookmarks of user {}", userId);

        storyRepo.deleteAllByUserId(userId);
        log.info("Deleting all stories of user {}", userId);

        return HttpStatus.ACCEPTED.getReasonPhrase();
    }
}
