package io.wakelesstuna.postdgs.dataloader;

import com.netflix.graphql.dgs.DgsDataLoader;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.Comment;
import io.wakelesstuna.postdgs.instrumentation.ExecutionIdPropagationExecutor;
import io.wakelesstuna.postdgs.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This is a DataLoader Class for the field {@value DgsConstants.POST#Comments}.
 * It fetches lists of comments for a post and returns its value to
 * the data fetcher for the field {@value DgsConstants.POST#Comments}.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsDataLoader(name = DgsConstants.POST.Comments)
public class CommentsDataLoader implements MappedBatchLoader<UUID, List<Comment>> {

    private final CommentService commentService;

    private static final Executor commentsThreadPool =
            ExecutionIdPropagationExecutor.wrap(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    /**
     * This method will be called once, even if multiple datafetchers use the load() method on the DataLoader.
     * This way Comments can be loaded for all the Post in a single call instead of per individual Post.
     */
    @Override
    public CompletionStage<Map<UUID, List<Comment>>> load(Set<UUID> ids) {
        return CompletableFuture.supplyAsync(() ->
                        commentService.commentsForPosts(new ArrayList<>(ids)),
                commentsThreadPool);
    }
}
