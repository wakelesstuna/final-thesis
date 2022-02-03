package io.wakelesstuna.userdgs.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link UserEntity}
 *
 * @author oscar.steen.forss
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Fetches random users from the database.
     *
     * @param username String
     * @param limit int
     * @return List of UserEntities
     */
    @Query(nativeQuery = true,
            value = "SELECT * from users " +
                    "WHERE users.username NOT LIKE :username " +
                    "order by random() " +
                    "limit :limit")
    List<UserEntity> findRandomButNotWith(@Param("username") String username, @Param("limit") int limit);

    /**
     * Used to find a {@link UserEntity} by username, email or phone.
     *
     * @param username String
     * @param email String
     * @param phone String
     * @return UserEntity
     */
    Optional<UserEntity> findByUsernameOrEmailOrPhone(String username, String email, String phone);

    /**
     * Used to check if a username exists.
     *
     * @param username String
     * @return boolean
     */
    boolean existsByUsername(String username);

    /**
     * Used to check if a email exists.
     *
     * @param email String
     * @return boolean
     */
    boolean existsByEmail(String email);

    /**
     * Used to check if a phone exists.
     *
     * @param phone String
     * @return boolean
     */
    boolean existsByPhone(String phone);
}
