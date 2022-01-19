package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.post.generated.types.Story;
import io.wakelesstuna.post.generated.types.User;
import io.wakelesstuna.postdgs.dataloader.BookmarkDataLoader;
import io.wakelesstuna.postdgs.dataloader.PostDataLoader;
import io.wakelesstuna.postdgs.dataloader.StoriesDataLoader;
import io.wakelesstuna.postdgs.service.BookmarkService;
import io.wakelesstuna.postdgs.service.PostService;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class is for describing how the fields for the type extension User
 * is fetched. Type User is extended from the user-dgs service
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

    private final PostService postService;
    private final BookmarkService bookmarkService;

    @DgsEntityFetcher(name = DgsConstants.USER.TYPE_NAME)
    public User user(Map<String, Object> values) {
        return User.newBuilder()
                .id(UUID.fromString((String) values.get("id")))
                .build();
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.TotalPosts)
    public Integer totalPosts(DgsDataFetchingEnvironment dfe) {
        User user = dfe.getSource();
        return postService.getTotalPostOfUser(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.Posts)
    public CompletableFuture<Post> posts(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, Post> dataLoader = dfe.getDataLoader(PostDataLoader.class);
        User user = dfe.getSource();
        return dataLoader.load(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.TotalBookmarks)
    public Integer totalBookmarks(DgsDataFetchingEnvironment dfe) {
        User user = dfe.getSource();
        return bookmarkService.getTotalBookmarks(user.getId());
    }

    @DgsData(parentType = DgsConstants.USER.TYPE_NAME, field = DgsConstants.USER.Bookmarks)
    public CompletableFuture<Post> bookmarks(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, Post> dataLoader = dfe.getDataLoader(BookmarkDataLoader.class);
        User user = dfe.getSource();
        return dataLoader.load(user.getId());
    }

    @DgsData(parentType = DgsConstants.STORY.TYPE_NAME, field = DgsConstants.USER.Stories)
    public CompletableFuture<Story> stories(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, Story> dataloader = dfe.getDataLoader(StoriesDataLoader.class);
        Story story = dfe.getSource();
        return dataloader.load(story.getId());
    }

}
