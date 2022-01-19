package io.wakelesstuna.storycdnserver.resource;

import io.wakelesstuna.storycdnserver.service.AppConstants;
import io.wakelesstuna.storycdnserver.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;


/**
 * @author oscar.steen.forss
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.VIDEO_RESOURCE_MAPPING)
public class VideoResource {

    private final VideoService videoService;

    @PostMapping("/upload/{userId}")
    public String uploadVideo(@PathVariable String userId, @RequestParam("file") MultipartFile multipartFile) {
        return videoService.uploadVideo(userId, multipartFile);
    }

    @GetMapping("/stream/{fileType}/{fileName}/{path}")
    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                                    @PathVariable("fileType") String fileType,
                                                    @PathVariable("fileName") String fileName,
                                                    @PathVariable("path") String path) {
        return Mono.just(videoService.prepareContent(fileName, fileType, path, httpRangeList));
    }

    @PostMapping("/delete/{fileType}/{fileName}/{userId}")
    public ResponseEntity<?> deleteVideo(@PathVariable("fileType") String fileType,
                                         @PathVariable("fileName") String fileName,
                                         @PathVariable("userId") String userId) {
        return videoService.deleteVideo(fileName, fileType, userId);
    }

}
