package io.wakelesstuna.userdgs.persistence;

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
 * Entity object for a follow.
 * This is what get persisted to the database.
 *
 * @author oscar.steen.forss
 */
@Entity
@Table(name = "follow_user")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class FollowEntity {

    @Id
    private UUID id;
    private UUID userId;
    private UUID followId;
    private LocalDateTime createdAt;
}
