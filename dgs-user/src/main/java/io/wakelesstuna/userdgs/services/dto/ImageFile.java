package io.wakelesstuna.userdgs.services.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.util.UUID;

/**
 * Simple pojo for transfer file data to the external image server
 *
 * @author oscar.steen.forss
 */
@Builder
@NoArgsConstructor
@Setter
@Getter
public class ImageFile {
    UUID userId;
    String url;
    String fileName;
    String fileType;
    Long fileSize;
    byte[] data;

    @JsonCreator
    public ImageFile(UUID userId, String url, String fileName, String fileType, Long fileSize, byte[] data) {
        this.userId = userId;
        this.url = url;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
    }
}
