package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.BookmarkInput;
import io.wakelesstuna.postdgs.service.BookmarkService;
import io.wakelesstuna.postdgs.service.PostService;
import lombok.RequiredArgsConstructor;

/**
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
public class BookmarkDataFetcher {

    private final BookmarkService bookmarkService;
    private final PostService postService;

    @DgsMutation(field = DgsConstants.MUTATION.BookmarkPost)
    public String bookmarkPost(@InputArgument BookmarkInput bookmarkInput) {
        return bookmarkService.bookmarkPost(bookmarkInput);
    }

    @DgsMutation(field = DgsConstants.MUTATION.UnBookmarkPost)
    public String unBookmarkPost(@InputArgument BookmarkInput bookmarkInput) {
        return bookmarkService.unBookmarkPost(bookmarkInput);
    }

    @DgsQuery(field = DgsConstants.QUERY.IsBookmarked)
    public boolean isBookmarked(@InputArgument BookmarkInput bookmarkInput) {
        return postService.isBookmarkedByUser(bookmarkInput);
    }
}
