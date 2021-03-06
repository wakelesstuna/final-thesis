package io.wakelesstuna.userdgs.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import io.wakelesstuna.user.generated.DgsConstants;
import io.wakelesstuna.user.generated.types.Story;
import io.wakelesstuna.user.generated.types.User;
import io.wakelesstuna.userdgs.services.ServiceHelper;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * This is a data fetcher for adding a user field to the extended type story.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@AllArgsConstructor
public class StoryDataFetcher {

    private final ServiceHelper serviceHelper;

    @DgsEntityFetcher(name = DgsConstants.STORY.TYPE_NAME)
    public Story story(Map<String, Object> values) {
        return Story.newBuilder()
                .id(UUID.fromString((String) values.get("id")))
                .userId(UUID.fromString((String) values.get("userId")))
                .build();
    }

    @DgsData(parentType = DgsConstants.STORY.TYPE_NAME)
    public User user(DgsDataFetchingEnvironment dfe) {
        Story story = dfe.getSource();
        return serviceHelper.getUser(story.getUserId()).mapToUserType();
    }
}
