package io.wakelesstuna.imagecdnserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity of a Image.
 * This is what gets persisted to the Database.
 *
 * @author oscar.steen.forss
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Image {

    @Id
    private UUID id;
    private UUID ownerId;
    private String url;
    private String fileName;
    private String fileType;
    private Long fileSize;
    @Lob
    private byte[] data;
    private LocalDateTime createdAt;
}
