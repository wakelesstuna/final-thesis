package io.wakelesstuna.userdgs.services.dto;

import lombok.*;

import java.util.UUID;

/**
 * Simple pojo for transfer file data to the external image server
 *
 * @author oscar.steen.forss
 */

@Builder
@AllArgsConstructor
public class ImageFile {
    UUID userId;
    String url;
    String fileName;
    String fileType;
    Long fileSize;
    byte[] data;
}
