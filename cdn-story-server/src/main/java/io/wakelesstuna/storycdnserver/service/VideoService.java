package io.wakelesstuna.storycdnserver.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.wakelesstuna.storycdnserver.service.AppConstants.*;

/**
 * @author oscar.steen.forss
 */
@Component
@Slf4j
public class VideoService {

    @Value("${server.base.url}")
    private String serverBaseUrl;

    /**
     * Create a file to file system with the user id as the directory and
     * generates a download url that is returned.
     *
     * @param userId String id of the user that uploaded the file
     * @param file   MultipartFile that you want to store
     * @return String with the download url
     */
    public String uploadVideo(String userId, MultipartFile file) {
        createUserDirectoryIfNotExits(userId);

        final String generatedFileName = UUID.randomUUID() + "+" + file.getOriginalFilename();
        final String downloadUrl = generateDownloadUrl(file, userId, generatedFileName);

        try {
            byte[] data = file.getBytes();
            Path path1 = Paths.get(getFilePath() + "/" + userId, generatedFileName);
            log.info("Starting writing to file");
            Files.write(path1, data);
            log.info("Finish writing to file");
            return downloadUrl;
        } catch (IOException e) {
            log.error("Error uploading file to disk", e);
            return "Error uploading file to disk";
        }
    }

    /**
     * Generates a download url for the file example
     * "http://localhost:5000/cdn/story/stream/mp4/7a215343-cd4d-482a-960c-7423fcb4a835+test2/OTI0YzY3LTE0YzktNGM2LThjYy1jMjZmNDY1NGNmZGI="
     *
     * @param file              MultipartFile, the uploaded file.
     * @param userId            String of the users id
     * @param generatedFileName The unique generated filename for the file
     * @return String with the download url
     */
    public String generateDownloadUrl(MultipartFile file, String userId, String generatedFileName) {
        String encodedUserId = Base64.getEncoder().encodeToString(userId.getBytes(StandardCharsets.UTF_8));
        String DOWNLOAD_URL = serverBaseUrl + VIDEO_RESOURCE_MAPPING + "/stream/%s/%s/%s";
        String[] split = Objects.requireNonNull(file.getContentType()).split("/");
        String[] fileName = Objects.requireNonNull(Objects.requireNonNull(generatedFileName).split("\\."));
        final String downloadUrl = String.format(DOWNLOAD_URL, split[1], fileName[0], encodedUserId);
        log.info("Download link: {}", downloadUrl);
        return downloadUrl;
    }

    /**
     * Prepares the content for video streaming.
     *
     * @param fileName String.
     * @param fileType String.
     * @param path     String Base64 encoded userId
     * @param range    String the range were
     * @return ResponseEntity<byte [ ]>
     */
    public ResponseEntity<byte[]> prepareContent(String fileName, String fileType, String path, String range) {
        String encodedUserId = new String(Base64.getDecoder().decode(path));
        long rangeStart = 0;
        long rangeEnd;
        byte[] data;
        Long fileSize;
        String fullFileName = "/" + encodedUserId + "/" + fileName + "." + fileType;
        try {
            fileSize = getFileSize(fullFileName);
            if (range == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .header(CONTENT_TYPE, VIDEO_CONTENT + fileType)
                        .header(CONTENT_LENGTH, String.valueOf(fileSize))
                        .body(readByteRange(fullFileName, rangeStart, fileSize - 1));
            }
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = fileSize - 1;
            }
            if (fileSize < rangeEnd) {
                rangeEnd = fileSize - 1;
            }
            data = readByteRange(fullFileName, rangeStart, rangeEnd);
        } catch (IOException e) {
            log.error("Exception while reading the file {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, VIDEO_CONTENT + fileType)
                .header(ACCEPT_RANGES, BYTES)
                .header(CONTENT_LENGTH, contentLength)
                .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                .body(data);
    }

    /**
     * Ready file byte by byte.
     *
     * @param filename String.
     * @param start    long.
     * @param end      long.
     * @return byte array.
     * @throws IOException exception.
     */
    public byte[] readByteRange(String filename, long start, long end) throws IOException {
        Path path = Paths.get(getFilePath(), filename);
        try (InputStream inputStream = (Files.newInputStream(path));
             ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[BYTE_RANGE];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                bufferedOutputStream.write(data, 0, nRead);
            }
            bufferedOutputStream.flush();
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
            return result;
        }
    }

    /**
     * Get the filePath.
     *
     * @return String.
     */
    private String getFilePath() {
        URL url = this.getClass().getResource(VIDEO);
        return new File(url.getFile()).getAbsolutePath();
    }

    /**
     * Content length.
     *
     * @param fileName String.
     * @return Long.
     */
    public Long getFileSize(String fileName) {
        return Optional.ofNullable(fileName)
                .map(file -> Paths.get(getFilePath(), file))
                .map(this::sizeFromFile)
                .orElse(0L);
    }

    /**
     * Getting the size from the path.
     *
     * @param path Path.
     * @return Long.
     */
    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ioException) {
            log.error("Error while getting the file size", ioException);
        }
        return 0L;
    }

    /**
     * Creates a directory if it doesnt exits
     *
     * @param userId String.
     */
    private void createUserDirectoryIfNotExits(String userId) {
        try {
            if (Files.exists(Path.of(getFilePath() + "/" + userId))) {
                log.info("Directory already exits");
                return;
            }
            Files.createDirectories(Path.of(getFilePath() + "/" + userId));
            log.info("Creating new directory: \"/" + userId + "\"");
        } catch (IOException e) {
            log.error("Error creating directory", e);
        }
    }

    public ResponseEntity<?> deleteVideo(String fileName, String fileType, String userId) {
        final String pathToFile = "/" + userId + "/" + fileName + "." + fileType;
        log.info("Path to delete: {}", Path.of(getFilePath() + pathToFile));
        try {
            Files.delete(Path.of(getFilePath() + pathToFile));
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (IOException e) {
            log.error("Could not delete file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ErrorResponse.builder()
                            .message("Could not delete file")
                            .error(NoSuchFileException.class.getName())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

    @Builder
    static class ErrorResponse {
        String message;
        String error;
        String status;
        int statusCode;
    }
}
