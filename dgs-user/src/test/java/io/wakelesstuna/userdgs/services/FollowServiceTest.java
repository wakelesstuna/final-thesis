package io.wakelesstuna.userdgs.services;

import graphql.GraphQLException;
import io.wakelesstuna.user.generated.types.FollowInput;
import io.wakelesstuna.user.generated.types.User;
import io.wakelesstuna.userdgs.persistence.FollowEntity;
import io.wakelesstuna.userdgs.persistence.FollowRepository;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * This is a test class for {@link FollowService}
 *
 * @author oscar.steen.forss
 */
@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    FollowService followService;

    @Mock
    ServiceHelper serviceHelper;
    @Mock
    FollowRepository followRepo;
    @Mock
    UserRepository userRepo;

    UUID userOneId = UUID.fromString("473eea09-69f7-40bf-bea0-f3380e926ecd");
    UUID userTwoId = UUID.fromString("c203fdae-b889-41c0-8f2f-712e95a1efd6");
    UUID userThreeId = UUID.fromString("64d6aada-c605-4070-8b30-78f9f3693e75");
    UUID userFourId = UUID.fromString("727f4ddb-9e8b-4f7e-9926-40d260b96994");
    UserEntity userOne;
    UserEntity userTwo;
    UserEntity userThree;
    UserEntity userFour;
    FollowEntity followOne;
    FollowEntity followTwo;
    FollowEntity followThree;
    FollowEntity followFour;
    FollowEntity followFive;
    FollowEntity followSix;
    FollowEntity followSeven;
    FollowEntity followEight;

    @BeforeEach
    void setUp() {
        followService = new FollowService(serviceHelper, followRepo, userRepo);

        userOne = UserEntity.builder()
                .id(userOneId)
                .username("user 1")
                .build();
        userTwo = UserEntity.builder()
                .id(userTwoId)
                .username("user 2")
                .build();
        userThree = UserEntity.builder()
                .id(userThreeId)
                .username("user 3")
                .build();
        userFour = UserEntity.builder()
                .id(userFourId)
                .username("user 4")
                .build();



    }

    @Test
    void userOneShouldFollowUserTwo() {
        var expected = String.format("user %s started following user %s", userOneId, userTwoId);
        var request = FollowInput.newBuilder()
                .userId(userOneId)
                .followId(userTwoId)
                .build();
        Mockito.when(userRepo.getById(userOneId))
                .thenAnswer(invocation -> userOne);
        Mockito.when(userRepo.getById(userTwoId))
                .thenAnswer(invocation -> userTwo);
        String actual = followService.followUser(request);
        assertEquals(expected, actual);
        Mockito.verify(userRepo, Mockito.times(2)).getById(any());
        Mockito.verify(followRepo, Mockito.times(1)).saveAndFlush(any());
    }

    @Test
    void unfollowShouldReturnStringWithCorrectMessage() {
        var expected = "Stopped following user";
        Mockito.when(followRepo.getFollowByUserIdAndFollowId(userOneId, userTwoId))
                .thenAnswer(invocation -> Optional.of(FollowEntity.builder()
                        .userId(userOneId)
                        .followId(userTwoId)
                        .build()));
        var request = FollowInput.newBuilder()
                .userId(userOneId)
                .followId(userTwoId)
                .build();
        var actual = followService.unFollowUser(request);
        assertEquals(expected, actual);
        Mockito.verify(followRepo, Mockito.times(1)).getFollowByUserIdAndFollowId(any(), any());
        Mockito.verify(followRepo, Mockito.times(1)).delete(any());
    }

    @Test
    void unfollowShouldThrowErrorWhenUserNotFollowingAnotherUser() {
        Mockito.when(followRepo.getFollowByUserIdAndFollowId(userOneId, userTwoId))
                .thenAnswer(invocation -> Optional.empty());
        var request = FollowInput.newBuilder()
                .userId(userOneId)
                .followId(userTwoId)
                .build();
        assertThrows(GraphQLException.class, () -> followService.unFollowUser(request));
        Mockito.verify(followRepo, Mockito.times(1)).getFollowByUserIdAndFollowId(any(), any());
        Mockito.verify(followRepo, Mockito.times(0)).delete(any());
    }

    @Test
    void shouldReturnTotalCountOfUsersFollowers() {
        var expected = 3;
        Mockito.when(followRepo.countAllByFollowId(userOneId))
                .thenAnswer(invocation -> 3);
        var actual = followService.getTotalFollowers(userOneId);
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnTotalCountOfUsersFollowing() {
        var expected = 3;
        Mockito.when(followRepo.countAllByUserId(userOneId))
                .thenAnswer(invocation -> 3);
        var actual = followService.getTotalFollowing(userOneId);
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnMapWithTwoIdsAndListOfFollowers() {
        var expectedMapLength = 2;
        followFive = FollowEntity.builder().userId(userTwoId).followId(userOneId).build();
        followSix = FollowEntity.builder().userId(userThreeId).followId(userOneId).build();
        followSeven = FollowEntity.builder().userId(userThreeId).followId(userTwoId).build();
        followEight = FollowEntity.builder().userId(userFourId).followId(userTwoId).build();

        List<UUID> listOfUsersIdsToFindFollowers = List.of(userOneId, userTwoId);

        List<FollowEntity> followEntitiesForUserOne = List.of(followFive, followSix);
        List<FollowEntity> followEntitiesForUserTwo = List.of(followSeven, followEight);

        List<UUID> userOneFollowersIds = List.of(userTwo.getId(), userThree.getId());
        List<UUID> userTwoFollowersIds = List.of(userThree.getId(), userFour.getId());

        List<UserEntity> userOneFollowers = List.of(userTwo, userThree);
        List<UserEntity> userTwoFollowers = List.of(userThree, userFour);

        Mockito.when(followRepo.getAllByFollowId(userOneId))
                .thenAnswer(invocation -> followEntitiesForUserOne);
        Mockito.when(followRepo.getAllByFollowId(userTwoId))
                .thenAnswer(invocation -> followEntitiesForUserTwo);
        Mockito.when(userRepo.findAllById(userOneFollowersIds))
                .thenAnswer(invocation -> userOneFollowers);
        Mockito.when(userRepo.findAllById(userTwoFollowersIds))
                .thenAnswer(invocation -> userTwoFollowers);

        Map<UUID, List<User>> map = followService.followersForUsers(listOfUsersIdsToFindFollowers);
        List<User> usersThatFollowsUserOne = map.get(userOneId);
        List<User> usersThatFollowsUserTwo = map.get(userTwoId);

        assertEquals(expectedMapLength, map.size());
        assertTrue(usersThatFollowsUserOne.contains(userTwo.mapToUserType()));
        assertTrue(usersThatFollowsUserOne.contains(userThree.mapToUserType()));
        assertTrue(usersThatFollowsUserTwo.contains(userThree.mapToUserType()));
        assertTrue(usersThatFollowsUserTwo.contains(userFour.mapToUserType()));
    }

    @Test
    void shouldReturnMapWithTwoIdsAndListOfFollowings() {
        var expectedMapLength = 2;
        followOne = FollowEntity.builder().userId(userOneId).followId(userTwoId).build();
        followTwo = FollowEntity.builder().userId(userOneId).followId(userThreeId).build();
        followThree = FollowEntity.builder().userId(userTwoId).followId(userThreeId).build();
        followFour = FollowEntity.builder().userId(userTwoId).followId(userFourId).build();

        List<UUID> listOfUsersIdsToFindFollowers = List.of(userOneId, userTwoId);

        List<FollowEntity> followEntitiesForUserOne = List.of(followOne, followTwo);
        List<FollowEntity> followEntitiesForUserTwo = List.of(followThree, followFour);

        List<UUID> userOneFollowersIds = List.of(userTwo.getId(), userThree.getId());
        List<UUID> userTwoFollowersIds = List.of(userThree.getId(), userFour.getId());

        List<UserEntity> userOneFollowers = List.of(userTwo, userThree);
        List<UserEntity> userTwoFollowers = List.of(userThree, userFour);

        Mockito.when(followRepo.getAllByUserId(userOneId))
                .thenAnswer(invocation -> followEntitiesForUserOne);
        Mockito.when(followRepo.getAllByUserId(userTwoId))
                .thenAnswer(invocation -> followEntitiesForUserTwo);
        Mockito.when(userRepo.findAllById(userOneFollowersIds))
                .thenAnswer(invocation -> userOneFollowers);
        Mockito.when(userRepo.findAllById(userTwoFollowersIds))
                .thenAnswer(invocation -> userTwoFollowers);

        Map<UUID, List<User>> map = followService.followingsForUsers(listOfUsersIdsToFindFollowers);
        List<User> usersThatFollowsUserOne = map.get(userOneId);
        List<User> usersThatFollowsUserTwo = map.get(userTwoId);

        assertEquals(expectedMapLength, map.size());
        assertTrue(usersThatFollowsUserOne.contains(userTwo.mapToUserType()));
        assertTrue(usersThatFollowsUserOne.contains(userThree.mapToUserType()));
        assertTrue(usersThatFollowsUserTwo.contains(userThree.mapToUserType()));
        assertTrue(usersThatFollowsUserTwo.contains(userFour.mapToUserType()));
    }
}