package io.wakelesstuna.userdgs.dataloader;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataLoader;
import io.wakelesstuna.user.generated.DgsConstants;
import io.wakelesstuna.user.generated.types.User;
import io.wakelesstuna.userdgs.instrumentation.ExecutionIdPropagationExecutor;
import io.wakelesstuna.userdgs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This is a DataLoader Class for the field {@value io.wakelesstuna.user.generated.DgsConstants.POST#User}.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsComponent
@DgsDataLoader(name = DgsConstants.POST.User)
public class UsersDataLoader implements MappedBatchLoader<UUID, User> {

    private final UserService userService;

    private static final Executor userThreadPool =
            ExecutionIdPropagationExecutor.wrap(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    @Override
    public CompletionStage<Map<UUID, User>> load(Set<UUID> ids) {
        return CompletableFuture.supplyAsync(() ->
                        userService.getUserOfPost(new ArrayList<>(ids)),
                userThreadPool);
    }
}
