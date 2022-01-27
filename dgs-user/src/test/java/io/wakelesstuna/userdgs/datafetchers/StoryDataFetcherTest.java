package io.wakelesstuna.userdgs.datafetchers;

import com.jayway.jsonpath.DocumentContext;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.codegen.EntitiesGraphQLQuery;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import graphql.schema.Coercing;
import io.wakelesstuna.user.generated.client.CommentRepresentation;
import io.wakelesstuna.user.generated.client.EntitiesProjectionRoot;
import io.wakelesstuna.user.generated.client.StoryRepresentation;
import io.wakelesstuna.user.generated.types.Comment;
import io.wakelesstuna.user.generated.types.Story;
import io.wakelesstuna.userdgs.persistence.UserEntity;
import io.wakelesstuna.userdgs.scalar.DateTimeScalar;
import io.wakelesstuna.userdgs.scalar.UUIDScalar;
import io.wakelesstuna.userdgs.services.ServiceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author oscar.steen.forss
 */
@SpringBootTest(classes = {DgsAutoConfiguration.class, StoryDataFetcher.class, DateTimeScalar.class, UUIDScalar.class})
class StoryDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    ServiceHelper serviceHelper;

    Map<Class<?>, Coercing<?, ?>> scalars = new HashMap<>();

    UUID userId = UUID.fromString("473eea09-69f7-40bf-bea0-f3380e926ecd");
    UUID storyId = UUID.fromString("d597ea0a-a541-4134-b4d5-e9da4cf3e96f");

    @BeforeEach
    void setUp() {
        scalars.put(UUID.class, new UUIDScalar());
        UserEntity user = UserEntity.builder()
                .id(userId)
                .username("testUser")
                .email("test@email.com")
                .phone("07070000000")
                .password("test00")
                .build();

        Mockito.when(serviceHelper.getUser(any(UUID.class)))
                .thenAnswer(invocation -> user);
    }

    @Test
    void shouldReturnUsername() {
        var expected = "testUser";
        EntitiesGraphQLQuery entitiesQuery = new EntitiesGraphQLQuery.Builder()
                .addRepresentationAsVariable(StoryRepresentation.newBuilder()
                        .id(storyId).userId(userId).build()).build();

        GraphQLQueryRequest request = new GraphQLQueryRequest(
                entitiesQuery,
                new EntitiesProjectionRoot().onStory().id().userId().user().username(),
                scalars
        );

        DocumentContext context = dgsQueryExecutor.executeAndGetDocumentContext(request.serialize(), entitiesQuery.getVariables());

        GraphQLResponse response = new GraphQLResponse(context.jsonString());

        Story story = response.extractValueAsObject("data._entities[0]", Story.class);
        assertEquals(expected, story.getUser().getUsername());
    }

}