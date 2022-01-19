package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import graphql.GraphQLException;
import io.wakelesstuna.post.generated.types.Comment;
import io.wakelesstuna.post.generated.types.CommentInput;
import io.wakelesstuna.post.generated.types.CreateCommentInput;
import io.wakelesstuna.postdgs.dataloader.CommentsDataLoader;
import io.wakelesstuna.postdgs.persistance.CommentEntity;
import io.wakelesstuna.postdgs.persistance.CommentRepository;
import io.wakelesstuna.postdgs.persistance.PostEntity;
import io.wakelesstuna.postdgs.persistance.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This is a service class that handle the logic for the {@link CommentEntity}.
 *
 * @author oscar.steen.forss
 */
@Slf4j
@RequiredArgsConstructor
@DgsComponent
public class CommentService {

    private final ServiceHelper serviceHelper;
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;

    /**
     * Creates a comment, checks if user and post exists
     * or else throws error
     *
     * @param input information about creating a comment
     * @return Comment
     * @throws GraphQLException if user or post does not exists
     */
    public Comment createComment(CreateCommentInput input) {
        // TODO: 2022-01-14 Send request to user-dgs to see if user exsits

        if (!postRepo.existsById(input.getPostId())) throw new GraphQLException("Post does not exists!");

        CommentEntity newComment = CommentEntity.builder()
                .id(UUID.randomUUID())
                .userId(input.getUserId())
                .postId(input.getPostId())
                .comment(input.getComment())
                .createdAt(serviceHelper.getLocalDateTime())
                .build();

        CommentEntity comment = commentRepo.save(newComment);
        log.info("Comment created");
        return comment.mapToCommentType();
    }

    /**
     * Check if a post exists, if it exists counts all the comments and return the result.
     * If post does not exists throws error
     *
     * @param postId id of the post to count comments for
     * @return Integer
     * @throws GraphQLException if post not found
     */
    public Integer getTotalComments(UUID postId) {
        PostEntity postEntity = serviceHelper.getPost(postId);
        return commentRepo.countAllByPostId(postEntity.getId());
    }

    /**
     * This method is used by the {@link CommentsDataLoader} to
     * fetch all comments as a list for a post
     *
     * @param postIds List of post ids to find all the the comments for
     * @return Map of the result of the search with ids as keys and a list of comments as value.
     */
    public Map<UUID, List<Comment>> commentsForPosts(List<UUID> postIds) {
        Map<UUID, List<Comment>> map = new ConcurrentHashMap<>();
        for (UUID id : postIds) {
            List<Comment> comments = commentRepo.findAllByPostId(id).stream()
                    .map(CommentEntity::mapToCommentType)
                    .collect(Collectors.toList());
            map.put(id, comments);
        }
        log.info("In service: {}", map);
        return map;
    }

    /**
     * This method is used to delete a comment from the database.
     * Returns a String of Http status ACCEPTED if successful.
     *
     * @param input object containing information about the delete process {@link CommentInput}
     * @return String
     */
    public String deleteComment(CommentInput input) {
        log.info("Start deleting comment");
        serviceHelper.doesUserExists(input.getUserId());
        var post = serviceHelper.getPost(input.getPostId());
        var comment = commentRepo.findByPostIdAndId(post.getId(), input.getCommentId()).orElseThrow(() -> new GraphQLException("No comment found with id: " + input.getCommentId()));
        if (!comment.getUserId().equals(input.getUserId())) throw new GraphQLException("User does not own comment");

        commentRepo.delete(comment);
        return HttpStatus.ACCEPTED.toString();
    }
}
