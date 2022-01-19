package io.wakelesstuna.userdgs.dataloader;

import com.netflix.graphql.dgs.DgsDataLoader;
import io.wakelesstuna.user.generated.DgsConstants;
import io.wakelesstuna.user.generated.types.User;
import io.wakelesstuna.userdgs.instrumentation.ExecutionIdPropagationExecutor;
import io.wakelesstuna.userdgs.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This is a DataLoader Class for the field {@value io.wakelesstuna.user.generated.DgsConstants.USER#Followers}.
 * It fetches lists of users that the user has as followers and returns its value to
 * the data fetcher for the field {@value io.wakelesstuna.user.generated.DgsConstants.USER#Followers}.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsDataLoader(name = DgsConstants.USER.Followers)
public class FollowersDataLoader implements MappedBatchLoader<UUID, List<User>> {

    private final FollowService followService;

    private static final Executor followersThreadPool =
            ExecutionIdPropagationExecutor.wrap(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    /**
     * This method will be called once, even if multiple datafetchers use the load() method on the DataLoader.
     * This way reviews can be loaded for all the Shows in a single call instead of per individual Show.
     */
    @Override
    public CompletionStage<Map<UUID, List<User>>> load(Set<UUID> ids) {
        return CompletableFuture.supplyAsync(() ->
                        followService.followersForUsers(new ArrayList<>(ids)),
                followersThreadPool);
    }
}
