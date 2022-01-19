package io.wakelesstuna.postdgs.service;

import com.netflix.graphql.dgs.DgsComponent;
import io.wakelesstuna.post.generated.types.CreateStoryInput;
import io.wakelesstuna.post.generated.types.Story;
import io.wakelesstuna.post.generated.types.StoryInput;
import io.wakelesstuna.postdgs.persistance.StoryEntity;
import io.wakelesstuna.postdgs.persistance.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
@Slf4j
public class StoryService {

    private final ServiceHelper serviceHelper;
    private final StoryRepository storyRepository;

    public Story createStory(CreateStoryInput createStoryInput) {
        StoryEntity story = StoryEntity.builder()
                .id(UUID.randomUUID())
                .userId(createStoryInput.getUserId())
                .storyUrl(createStoryInput.getStoryUrl())
                .createdAt(serviceHelper.getLocalDateTime())
                .build();

        storyRepository.saveAndFlush(story);
        log.info("Created new story");
        return story.mapToStoryType();
    }

    public Map<UUID, List<Story>> storiesForUsers(ArrayList<UUID> userIds) {
        return userIds.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(),
                        id -> storyRepository.findAllByUserId(id).stream()
                .map(StoryEntity::mapToStoryType)
                .collect(Collectors.toList()),
                        (a,b) -> a,
                        ConcurrentHashMap::new));
    }

    public List<Story> getStories() {
        log.info("Fetching all stories");
        return storyRepository.findAll().stream()
                .map(StoryEntity::mapToStoryType)
                .sorted(Comparator.comparing(Story::getCreatedAt).reversed())
                .collect(Collectors.toList());

    }


    public String deleteStory(StoryInput storyInput) {
        StoryEntity storyToDelete = storyRepository.findByUserIdAndId(storyInput.getUserId(), storyInput.getStoryId()).orElseThrow(() -> new NoSuchElementException("No story found"));

        storyRepository.delete(storyToDelete);
        log.info("Story deleted");
        return HttpStatus.ACCEPTED.toString();
    }
}
