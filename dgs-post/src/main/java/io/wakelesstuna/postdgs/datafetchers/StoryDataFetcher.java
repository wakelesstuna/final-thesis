package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.*;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.CreateStoryInput;
import io.wakelesstuna.post.generated.types.Story;
import io.wakelesstuna.post.generated.types.StoryInput;
import io.wakelesstuna.postdgs.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * This class is the DataFetcher for {@link Story}.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
public class StoryDataFetcher {

    private final StoryService storyService;

    @DgsMutation
    public Story createStory(@InputArgument CreateStoryInput createStoryInput) {
        return storyService.createStory(createStoryInput);
    }

    @DgsMutation
    public String deleteStory(@InputArgument StoryInput storyInput) {
        return storyService.deleteStory(storyInput);
    }

    @DgsQuery
    public List<Story> stories() {
        return storyService.getStories();
    }

}
