package com.erp.server.factories;

import infra.global.relational.entities.AttachmentEntity;
import org.instancio.Instancio;
import org.instancio.Select;

import java.util.Base64;

public class AttachmentsFactory {

    public AttachmentEntity createAttachment() {
        byte[] fileContent = new byte[0];
        return Instancio.of(AttachmentEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("fileBase64"), Base64.getEncoder().encodeToString(fileContent))
                .set(Select.field("fileType"), "image/png")
                .create();
    }
}
