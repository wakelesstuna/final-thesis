package io.wakelesstuna.postdgs.persistance;


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
 * Entity object for a like.
 * This is what get persisted to the database.
 *
 * @author oscar.steen.forss
 */
@Entity
@Table(name = "like_post")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class LikeEntity {

    @Id
    private UUID id;
    private UUID postId;
    private UUID userId;
    private LocalDateTime createdAt;
}

