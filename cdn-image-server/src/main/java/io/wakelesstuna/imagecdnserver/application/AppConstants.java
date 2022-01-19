package io.wakelesstuna.imagecdnserver.application;


import org.springframework.context.annotation.Configuration;

/**
 * This class holds the constants for the application.
 *
 * @author oscar.steen.forss
 */
@Configuration
public class AppConstants {

    public static class Paths {
        public static final String BASE_IMAGE_RESOURCE = "cdn/server/v1";
        public static final String UPLOAD_FILE_RESOURCE = "/upload";
        public static final String DOWNLOAD_FILE_RESOURCE = "/image/{imageId}";
        public static final String DELETE_FILE_RESOURCE = "/delete";

    }
}
