package io.wakelesstuna.postdgs.dataloader;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataLoader;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.postdgs.instrumentation.ExecutionIdPropagationExecutor;
import io.wakelesstuna.postdgs.service.PostService;
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
@RequiredArgsConstructor
@DgsComponent
@DgsDataLoader(name = DgsConstants.USER.Posts)
public class PostDataLoader implements MappedBatchLoader<UUID, List<Post>> {

    private final PostService postService;

    private static final Executor postsThreadPool =
            ExecutionIdPropagationExecutor.wrap(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
            );

    @Override
    public CompletionStage<Map<UUID, List<Post>>> load(Set<UUID> ids) {
        return CompletableFuture.supplyAsync(() ->
                        postService.postsForUsers(new ArrayList<>(ids)),
                postsThreadPool);
    }
}
