package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import graphql.relay.Connection;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.CreatePostInput;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.post.generated.types.PostFilter;
import io.wakelesstuna.post.generated.types.PostInput;
import io.wakelesstuna.postdgs.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * This class is the DataFetcher for {@link Post}.
 * It handles the mutations and queries of the post type.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsComponent
public class PostDataFetcher {

    private final PostService postService;

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreatePost)
    public Post createPost(@InputArgument CreatePostInput createPostInput, DgsDataFetchingEnvironment dfe) {
        MultipartFile file = dfe.getArgument("input");
        return postService.createPost(createPostInput, file);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.DeletePost)
    public Boolean deletePost(@InputArgument PostInput postInput) {
        return postService.deletePost(postInput);
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.Posts)
    public List<Post> posts(@InputArgument PostFilter postFilter) {
        if (postFilter != null) return postService.getPosts(postFilter);
        return postService.getPosts();
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.Post)
    public Post post(@InputArgument UUID postId) {
        return postService.getPost(postId);
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.PaginationPosts)
    public Connection<Post> paginatonPost(@InputArgument Integer first, @InputArgument String after) {
        return postService.getPaginationPost(first, after);
    }
}
