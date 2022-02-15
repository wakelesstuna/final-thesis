package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.*;
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
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsComponent
public class PostDataFetcher {

    private final PostService postService;

    @DgsMutation
    public Post createPost(@InputArgument CreatePostInput createPostInput, DgsDataFetchingEnvironment dfe) {
        MultipartFile file = dfe.getArgument("input");
        return postService.createPost(createPostInput, file);
    }

    @DgsMutation
    public Boolean deletePost(@InputArgument PostInput postInput) {
        return postService.deletePost(postInput);
    }

    @DgsQuery
    public List<Post> posts(@InputArgument PostFilter postFilter) {
        if (postFilter != null) return postService.getPosts(postFilter);
        return postService.getPosts();
    }

    @DgsQuery
    public Post post(@InputArgument UUID postId) {
        return postService.getPost(postId);
    }

    @DgsQuery
    public Connection<Post> paginationPosts(@InputArgument Integer first, @InputArgument String after) {
        return postService.getPaginationPost(first, after);
    }
}
