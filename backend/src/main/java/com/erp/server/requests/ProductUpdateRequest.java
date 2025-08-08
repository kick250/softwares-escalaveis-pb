package com.erp.server.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
public class ProductUpdateRequest {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    private MultipartFile image;

    public boolean hasImage() {
        return image != null;
    }

    public byte[] getImageBytes() throws IOException {
        return image != null ? image.getBytes() : null;
    }

    public String getImageContentType() {
        return image != null ? image.getContentType() : null;
    }
}
