package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.CreateStoryInput;
import io.wakelesstuna.post.generated.types.Story;
import io.wakelesstuna.post.generated.types.StoryInput;
import io.wakelesstuna.postdgs.service.StoryService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
public class StoryDataFetcher {

    private final StoryService storyService;

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateStory)
    public Story createStory(@InputArgument CreateStoryInput createStoryInput) {
        return storyService.createStory(createStoryInput);
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.DeleteStory)
    public String deleteStory(@InputArgument StoryInput storyInput) {
        return storyService.deleteStory(storyInput);
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.Stories)
    public List<Story> stories() {
        return storyService.getStories();
    }

}
