package infra.global.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="Attachment")
@Table(name="attachments")
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private boolean deleted = false;
    @Getter
    @Lob
    @Column(columnDefinition = "TEXT")
    private String fileBase64;
    @Getter
    private String fileType;

    public AttachmentEntity(String fileBase64, String fileType) {
        this.fileBase64 = fileBase64;
        this.fileType = fileType;
    }

    public void delete() {
        this.deleted = true;
    }
}
