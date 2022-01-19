package io.wakelesstuna.postdgs.service;

import io.wakelesstuna.postdgs.persistance.PostEntity;
import io.wakelesstuna.postdgs.persistance.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author oscar.steen.forss
 */
@ExtendWith(MockitoExtension.class)

class ServiceHelperTest {

    @MockBean
    ServiceHelper serviceHelper;

    @Mock
    PostRepository postRepo;

    @BeforeEach
    void setUp() {
        serviceHelper = new ServiceHelper(Clock.systemUTC(), postRepo, "http://localhost:8005");
    }

    UUID userId = UUID.fromString("924c67ed-14c9-41c6-8cca-c26f4654cfdb");
    UUID imageId = UUID.fromString("bd69b77a-62b3-4c71-8834-3e3cb58fe86d");

    @Test
    void buildDeleteUrl() {
        String expected = "http://localhost:8005/cdn/server/v1/delete/bd69b77a-62b3-4c71-8834-3e3cb58fe86d/924c67ed-14c9-41c6-8cca-c26f4654cfdb";
        String imageUrl = "http://localhost:8005/cdn/server/v1/image/" + imageId;
        PostEntity post = PostEntity.builder().userId(userId).imageUrl(imageUrl).build();
        String actual = serviceHelper.buildDeleteUrl(post);
        assertEquals(expected, actual);
    }

    @Test
    void extractImageId() {
        final String expected = "bd69b77a-62b3-4c71-8834-3e3cb58fe86d";
        String imageUrl = "http://localhost:8005/cdn/server/v1/image/bd69b77a-62b3-4c71-8834-3e3cb58fe86d";
        String actual = serviceHelper.extractImageId(imageUrl);

        assertEquals(expected, actual);
    }


}