package infra.global.relational.entities;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class Attachable {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id")
    @Setter
    @Getter
    protected AttachmentEntity attachmentEntity;
}
