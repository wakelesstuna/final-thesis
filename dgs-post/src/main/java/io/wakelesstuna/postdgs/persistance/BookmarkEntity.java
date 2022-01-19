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
 * Entity object for a bookmark.
 * This is what get persisted to the database.
 *
 * @author oscar.steen.forss
 */
@Entity
@Table(name = "bookmark_post")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class BookmarkEntity {

    @Id
    private UUID id;
    private UUID userId;
    private UUID postId;
    private LocalDateTime createAt;
}
