package io.wakelesstuna.postdgs.persistance;

import io.wakelesstuna.post.generated.types.Post;
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
 * @author oscar.steen.forss
 */
@Entity
@Table(name = "post")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class PostEntity {

    @Id
    private UUID id;
    private UUID userId;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;

    /**
     * Maps the UserEntity to the graphql type {@link Post}
     *
     * @return Post
     */
    public Post mapToPostType() {
        return Post.newBuilder()
                .id(this.id)
                .userId(this.userId)
                .imageUrl(this.imageUrl)
                .caption(this.caption)
                .createdAt(this.createdAt)
                .build();
    }
}
