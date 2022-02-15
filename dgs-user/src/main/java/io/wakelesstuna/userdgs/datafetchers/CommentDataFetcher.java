package io.wakelesstuna.userdgs.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import io.wakelesstuna.user.generated.DgsConstants;
import io.wakelesstuna.user.generated.types.Comment;
import io.wakelesstuna.user.generated.types.User;
import io.wakelesstuna.userdgs.services.ServiceHelper;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * This is a data fetcher for adding a user field to the extended type comment.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@AllArgsConstructor
public class CommentDataFetcher {

    private final ServiceHelper serviceHelper;

    @DgsEntityFetcher(name = DgsConstants.COMMENT.TYPE_NAME)
    public Comment comment(Map<String, Object> values) {
        return Comment.newBuilder()
                .id(UUID.fromString((String) values.get("id")))
                .userId(UUID.fromString((String) values.get("userId")))
                .build();
    }

    @DgsData(parentType = DgsConstants.COMMENT.TYPE_NAME)
    public User user(DgsDataFetchingEnvironment dfe) {
        Comment comment = dfe.getSource();
        return serviceHelper.getUser(comment.getUserId()).mapToUserType();
    }
}
