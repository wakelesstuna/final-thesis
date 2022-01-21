package io.wakelesstuna.postdgs.service;

import graphql.GraphQLException;
import io.wakelesstuna.postdgs.exceptions.MyCustomException;
import io.wakelesstuna.postdgs.persistance.PostEntity;
import io.wakelesstuna.postdgs.persistance.PostRepository;
import io.wakelesstuna.postdgs.service.dto.ImageFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author oscar.steen.forss
 */
@ExtendWith(MockitoExtension.class)
class ServiceHelperTest {

    @MockBean
    ServiceHelper serviceHelper;

    @Mock
    PostRepository postRepo;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        serviceHelper = new ServiceHelper(Clock.systemUTC(), postRepo, "http://localhost:8005", restTemplate);
    }

    UUID uuid = UUID.fromString("0226fdea-c1ff-47b3-afd5-f47d9cc2327e");
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

    @Test
    void shouldGenerateAnImageFileWithUserId() {
        ImageFile imageFile = serviceHelper.generateAvatarImageFile(userId);

        assertEquals(imageFile.getUserId(), userId);
    }

    @Test
    void shouldGenerateAnImageFileWithCorrectFileName() {
        ImageFile imageFile = serviceHelper.generateAvatarImageFile(userId);
        String[] split = imageFile.getFileName().split("/");

        assertDoesNotThrow(() -> UUID.fromString(split[1]));
        assertEquals(split[0], "random-avatar");
    }

    @Test
    void shouldReturnTrueIfPostExits() {
        when(postRepo.existsById(any(UUID.class)))
                .thenReturn(true);
        assertTrue(serviceHelper.postExists(uuid));
    }

    @Test
    void fileSizeToBigShouldThrowException() {
        assertThrows(MyCustomException.class, () -> serviceHelper.checkIfImageSize(8000000L));
    }

    @Test
    void fileSizeShouldNotThrowException() {
        assertDoesNotThrow(() -> serviceHelper.checkIfImageSize(2000000L));
    }

    @Test
    void shouldReturnTrueIfUserExits() {
        assertTrue(serviceHelper.doesUserExists(uuid));
    }

    @Test
    void getPostShouldThrowException() {
        when(postRepo.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(MyCustomException.class, () -> serviceHelper.getPost(uuid));
    }

    @Test
    void getPostShouldReturnPostEntity() {
        when(postRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(PostEntity.builder().id(uuid).caption("Best Post Ever!").build()));

        PostEntity post = serviceHelper.getPost(uuid);
        assertNotNull(post);
    }

    @Test
    void getPostShouldReturnRightErrorInfo() {
        when(postRepo.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        try {
            serviceHelper.getPost(uuid);
        } catch (MyCustomException e) {
            assertEquals("No post found", e.getMessage());
        }
    }

    @Test
    void shouldGenerateImageFile() throws IOException {
        Path path = Paths.get("src/test/resources/test.txt");
        String name = "test.txt";
        String originalFileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = Files.readAllBytes(path);

        MultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, content);

        ImageFile imageFile = serviceHelper.generateImageFile(uuid, file);

        assertNotNull(imageFile);
        assertEquals("test", imageFile.getFileName());
    }

    @Test
    void shouldDeleteImage() {
        String url = "http://localhost:8005/cdn/server/v1/image/"+uuid;
        String deleteUrl = "http://localhost:8005/cdn/server/v1/delete/" + uuid + "/" + userId;

        PostEntity post = PostEntity.builder()
                .id(uuid)
                .userId(userId)
                .imageUrl(url)
                .build();

        when(restTemplate.postForEntity(deleteUrl, null, String.class))
                .thenReturn(ResponseEntity.ok(HttpStatus.ACCEPTED.toString()));

        String actual = serviceHelper.deleteImage(post);
        assertEquals("202 ACCEPTED", actual);
    }

    @Test
    void shouldDeleteImageThrowException() {
        String url = "http://localhost:8005/cdn/server/v1/image/"+uuid;
        String deleteUrl = "http://localhost:8005/cdn/server/v1/delete/" + uuid + "/" + userId;

        PostEntity post = PostEntity.builder()
                .id(uuid)
                .userId(userId)
                .imageUrl(url)
                .build();

        when(restTemplate.postForEntity(deleteUrl, null, String.class))
                .thenThrow(ResourceAccessException.class);

        String actual = serviceHelper.deleteImage(post);
        assertEquals("Service unavailable, could not delete image", actual);
    }

    @Test
    void shouldUploadImageCorrect() throws IOException {
        Path path = Paths.get("src/test/resources/test.txt");
        String name = "test.txt";
        String originalFileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = Files.readAllBytes(path);

        MultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, content);

        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok(HttpStatus.ACCEPTED.toString()));

        String actual = serviceHelper.uploadImage(userId, file);

        assertEquals("202 ACCEPTED", actual);
    }

    @Test
    void shouldGenerateAvatarImageFileIfFileIsNull() {

        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok(HttpStatus.ACCEPTED.toString()));

        String actual = serviceHelper.uploadImage(userId, null);

        assertEquals("202 ACCEPTED", actual);
    }

    @Test
    void shouldThrowGraphQLExceptionWhenResourceAccessExceptionThrown() throws IOException {
        Path path = Paths.get("src/test/resources/test.txt");
        String name = "test.txt";
        String originalFileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = Files.readAllBytes(path);

        MultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, content);

        when(restTemplate.postForEntity(anyString(), any(), any()))
                .thenThrow(ResourceAccessException.class);

        String actual = "";
        try {
            serviceHelper.uploadImage(userId, file);
        } catch (GraphQLException e) {
             actual = e.getMessage();
        }

        assertEquals("Service unavailable, could not upload image", actual);
    }
}