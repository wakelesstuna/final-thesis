package io.wakelesstuna.postdgs.dataloader;

import com.netflix.graphql.dgs.DgsDataLoader;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.postdgs.instrumentation.ExecutionIdPropagationExecutor;
import io.wakelesstuna.postdgs.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This is a DataLoader Class for the field {@value DgsConstants.POST#TotalLikes}.
 * It fetches the total likes for a post and returns it to
 * the data fetcher for the field {@value DgsConstants.POST#TotalLikes}.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsDataLoader(name = DgsConstants.POST.TotalLikes)
public class TotalLikesDataLoader implements MappedBatchLoader<UUID, Integer> {

    private final LikeService likeService;

    private static final Executor totalLikesThreadPool =
            ExecutionIdPropagationExecutor.wrap(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    /**
     * This method will be called once, even if multiple datafetchers use the load() method on the DataLoader.
     * This way Likes can be loaded for all the Users in a single call instead of per individual User.
     */
    @Override
    public CompletionStage<Map<UUID, Integer>> load(Set<UUID> ids) {
        return CompletableFuture.supplyAsync(() ->
                        likeService.likesForPosts(new ArrayList<>(ids)),
                totalLikesThreadPool);
    }
}
