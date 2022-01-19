package io.wakelesstuna.postdgs.dataloader;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataLoader;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.Story;
import io.wakelesstuna.postdgs.instrumentation.ExecutionIdPropagationExecutor;
import io.wakelesstuna.postdgs.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
@DgsDataLoader(name = DgsConstants.USER.Stories)
public class StoriesDataLoader implements MappedBatchLoader<UUID, List<Story>> {

    private final StoryService storyService;

    private static final Executor storiesThreadPool =
            ExecutionIdPropagationExecutor.wrap(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
            );

    @Override
    public CompletionStage<Map<UUID, List<Story>>> load(Set<UUID> ids) {
        return CompletableFuture.supplyAsync(() ->
                storyService.storiesForUsers(new ArrayList<>(ids)),
                storiesThreadPool);
    }
}
