package io.wakelesstuna.postdgs.persistance;

import io.wakelesstuna.post.generated.types.Story;
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
@Table(name = "story")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class StoryEntity {

    @Id
    private UUID id;
    private UUID userId;
    private String storyUrl;
    private LocalDateTime createdAt;

    /**
     * Maps the StoryEntity to the graphql type {@link Story}.
     *
     * @return Story.
     */
    public Story mapToStoryType() {
        return Story.newBuilder()
                .id(this.id)
                .userId(this.userId)
                .storyUrl(this.storyUrl)
                .createdAt(this.createdAt)
                .build();
    }
}
