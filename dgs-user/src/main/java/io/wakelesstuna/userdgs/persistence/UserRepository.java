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

    @Query(nativeQuery = true,
            value = "SELECT * from users " +
                    "WHERE users.username NOT LIKE :username " +
                    "order by random() " +
                    "limit :limit")
    List<UserEntity> findRandomButNotWith(@Param("username") String username,@Param("limit") int limit);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameOrEmailOrPhone(String username, String email, String phone);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String email);
}
