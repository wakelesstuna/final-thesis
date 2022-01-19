package io.wakelesstuna.postdgs.persistance;

import io.wakelesstuna.post.generated.types.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity object for a comment.
 * This is what get persisted to the database.
 *
 * @author oscar.steen.forss
 */
@Entity
@Table(name = "comment_post")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class CommentEntity {

    @Id
    private UUID id;
    private UUID userId;
    private UUID postId;
    private String comment;
    private LocalDateTime createdAt;

    /**
     * Maps the UserEntity to the graphql type user {@link Comment}
     *
     * @return Comment
     */
    public Comment mapToCommentType() {
        return Comment.newBuilder()
                .id(this.id)
                .userId(this.userId)
                .postId(this.postId)
                .comment(this.comment)
                .createdAt(this.createdAt)
                .build();
    }
}
