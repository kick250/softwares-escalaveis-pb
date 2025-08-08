package com.erp.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name="Product")
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
public class Product extends Attachable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    private Long id;
    private boolean deleted = false;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @OneToMany
    @JoinColumn(name = "product_id")
    private List<StockItem> stockItems;


    public Product(String name, String description, Attachment attachment) {
        this.name = name;
        this.description = description;
        this.attachment = attachment;
    }

    public void delete() {
        this.deleted = true;
        if (attachment != null) {
            attachment.delete();
        }
    }

    public boolean hasAttachment() {
        return attachment != null;
    }

    public String getImageBase64() {
        if (attachment != null)
            return attachment.getFileBase64();

        return null;
    }

    public String getImageType() {
        if (attachment != null)
            return attachment.getFileType();

        return null;
    }
}
