package com.erp.server.repositories;

import com.erp.server.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentsRepository extends JpaRepository<Attachment, Long> {

    public int countAllByDeletedFalse();
}
