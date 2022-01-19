package io.wakelesstuna.postdgs.dataloader;

import com.netflix.graphql.dgs.DgsDataLoader;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.postdgs.instrumentation.ExecutionIdPropagationExecutor;
import io.wakelesstuna.postdgs.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This is a DataLoader Class for the field {@value DgsConstants.USER#Bookmarks}.
 * It fetches lists of posts that users have bookmarked and return it to
 * the data fetcher for the field {@value DgsConstants.USER#Bookmarks}.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsDataLoader(name = DgsConstants.USER.Bookmarks)
public class BookmarkDataLoader implements MappedBatchLoader<UUID, List<Post>> {

    private final BookmarkService bookmarkService;

    private static final Executor bookmarksThreadPool =
            ExecutionIdPropagationExecutor.wrap(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    /**
     * This method will be called once, even if multiple datafetchers use the load() method on the DataLoader.
     * This way reviews can be loaded for all the Shows in a single call instead of per individual Show.
     */
    @Override
    public CompletionStage<Map<UUID, List<Post>>> load(Set<UUID> ids) {
        return CompletableFuture.supplyAsync(() ->
                        bookmarkService.bookmarksForUsers(new ArrayList<>(ids)),
                bookmarksThreadPool);
    }
}
