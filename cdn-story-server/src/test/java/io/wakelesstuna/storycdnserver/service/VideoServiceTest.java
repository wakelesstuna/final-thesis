package io.wakelesstuna.storycdnserver.service;

import ch.qos.logback.core.util.ContentTypeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author oscar.steen.forss
 */
class VideoServiceTest {

    @Mock
    VideoService videoService;

    @BeforeEach
    void setUp() {
        videoService = new VideoService();
    }

    @Test
    void generateDownloadUrlTest() {
     /*   String expected = "http://localhost:5000/video/stream/mp4/foo";
        MockMultipartFile file = new MockMultipartFile("Test", "foo", "video/mp4", (byte[]) null);
        String actual = videoService.generateDownloadUrl(file, "we");
        assertEquals(expected, actual);*/
    }
}