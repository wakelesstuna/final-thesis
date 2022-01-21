package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import io.wakelesstuna.post.generated.client.PostGraphQLQuery;
import io.wakelesstuna.post.generated.client.PostProjectionRoot;
import io.wakelesstuna.post.generated.client.PostsGraphQLQuery;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.postdgs.persistance.PostRepository;
import io.wakelesstuna.postdgs.scalar.DateTimeScalar;
import io.wakelesstuna.postdgs.scalar.UUIDScalar;
import io.wakelesstuna.postdgs.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author oscar.steen.forss
 */
@SpringBootTest(classes = {DgsAutoConfiguration.class, PostDataFetcher.class, DateTimeScalar.class, UUIDScalar.class})
class PostDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    PostService postService;

    @BeforeEach
    void setUp() {
        Mockito.when(postService.getPosts())
                .thenAnswer(invocation ->
                        List.of(Post.newBuilder().caption("Best Post Ever"),
                                Post.newBuilder().caption("Testing is fun!")));
    }

    @Test
    void createPost() {
    }

    @Test
    void posts() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                PostsGraphQLQuery.newRequest().build(),
                new PostProjectionRoot().caption()
        );

        List<String> captions = dgsQueryExecutor.executeAndExtractJsonPath(graphQLQueryRequest.serialize(), "data.posts[*].caption");
        assertEquals("Testing is fun!", captions.get(1));
        assertEquals(2, captions.size());
    }
}