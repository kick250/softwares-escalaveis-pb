package infra.global.repositories;

import infra.global.entities.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentsRepository extends JpaRepository<AttachmentEntity, Long> {

    public int countAllByDeletedFalse();
}
