package com.erp.server.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
public class ProductCreateRequest {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private MultipartFile image;


    public byte[] getImageBytes() throws IOException {
        return image.getBytes();
    }

    public String getImageContentType() {
        return image.getContentType();
    }
}
