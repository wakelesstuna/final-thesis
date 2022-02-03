package io.wakelesstuna.userdgs.persistence;

import io.wakelesstuna.user.generated.types.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity object for a user.
 * This is what get persisted to the database.
 *
 * @author oscar.steen.forss
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserEntity {

    @Id
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String profilePic;
    private String description;
    private LocalDateTime createdAt;

    /**
     * Maps the UserEntity to the graphql type user {@link User}
     *
     * @return User
     */
    public User mapToUserType() {
        return User.newBuilder()
                .id(this.id)
                .username(this.username)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phone(this.phone)
                .email(this.email)
                .description(this.description)
                .password(this.password)
                .profilePic(this.profilePic)
                .createdAt(this.createdAt)
                .build();
    }
}
