package com.erp.server.services;

import infra.global.entities.AttachmentEntity;
import infra.global.entities.ProductEntity;
import com.erp.server.exceptions.ProductDescriptionRequiredException;
import com.erp.server.exceptions.ProductImageRequiredException;
import com.erp.server.exceptions.ProductNameRequiredException;
import com.erp.server.exceptions.ProductNotFoundException;
import infra.global.repositories.AttachmentsRepository;
import infra.global.repositories.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final AttachmentsRepository attachmentsRepository;
    private final RedisTemplate<String, byte[]> redisImageTemplate;
    private final RedisTemplate<String, String> redisImageTypeTemplate;

    private final long IMAGE_CACHE_EXPIRATION = 60 * 60;

    public List<ProductEntity> getAll() {
        return productsRepository.findAllByDeletedFalse();
    }

    public ProductEntity getById(Long id) throws ProductNotFoundException {
        return productsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public void create(String name, String description, byte[] image, String imageType) throws ProductNameRequiredException, ProductDescriptionRequiredException, ProductImageRequiredException {
        if (name == null || name.isEmpty()) throwProductNameRequiredException();
        if (description == null || description.isEmpty()) throwProductDescriptionRequiredException();
        if (image == null || imageType == null) throwProductImageRequiredException();

        AttachmentEntity attachmentEntity = new AttachmentEntity(Base64.getEncoder().encodeToString(image), imageType);
        attachmentsRepository.save(attachmentEntity);
        ProductEntity productEntity = new ProductEntity(name, description, attachmentEntity);
        productsRepository.save(productEntity);
    }

    private void throwProductImageRequiredException() throws ProductImageRequiredException {
        throw new ProductImageRequiredException();
    }

    private void throwProductDescriptionRequiredException() throws ProductDescriptionRequiredException {
        throw new ProductDescriptionRequiredException();
    }

    private void throwProductNameRequiredException() throws ProductNameRequiredException {
        throw new ProductNameRequiredException();
    }

    public void update(Long id, String name, String description, byte[] image, String imageContentType) throws ProductNotFoundException, ProductNameRequiredException, ProductDescriptionRequiredException {
        if (name == null || name.isEmpty()) throwProductNameRequiredException();
        if (description == null || description.isEmpty()) throwProductDescriptionRequiredException();

        AttachmentEntity attachmentEntity = null;
        AttachmentEntity oldAttachmentEntity = null;
        if (image != null && imageContentType != null) {
            attachmentEntity = new AttachmentEntity(Base64.getEncoder().encodeToString(image), imageContentType);
            attachmentsRepository.save(attachmentEntity);
        }

        ProductEntity productEntity = getById(id);
        productEntity.setName(name);
        productEntity.setDescription(description);
        if (attachmentEntity != null) {
            oldAttachmentEntity = productEntity.getAttachmentEntity();
            oldAttachmentEntity.delete();
            productEntity.setAttachmentEntity(attachmentEntity);
            clearCachedImage(id);
        }
        productsRepository.save(productEntity);
        if (oldAttachmentEntity != null) {
            attachmentsRepository.save(oldAttachmentEntity);
        }
    }

    private void clearCachedImage(Long productId) {
        String imageKey = getImageKey(productId);
        String imageTypeKey = getImageTypeKey(productId);
        redisImageTemplate.delete(imageKey);
        redisImageTypeTemplate.delete(imageTypeKey);
    }

    public void deleteById(Long id) throws ProductNotFoundException {
        ProductEntity productEntity = getById(id);
        productEntity.delete();
        productsRepository.save(productEntity);
    }

    public byte[] getProductImage(Long productId) throws ProductNotFoundException {
        byte[] cachedImage = getCachedImage(productId);
        if (cachedImage != null) return cachedImage;

        ProductEntity productEntity = cacheProductData(productId);
        if (productEntity == null) return null;

        return getDecodedImage(productEntity);
    }

    private ProductEntity cacheProductData(Long productId) throws ProductNotFoundException {
        ProductEntity productEntity = getById(productId);
        if (!productEntity.hasAttachment()) return null;

        byte[] imageBytes = getDecodedImage(productEntity);
        cacheImage(productId, imageBytes, productEntity.getImageType());
        return productEntity;
    }

    private static byte[] getDecodedImage(ProductEntity productEntity) {
        return Base64.getDecoder().decode(productEntity.getImageBase64());
    }

    private void cacheImage(Long productId, byte[] imageBytes, String imageType) {
        String imageKey = getImageKey(productId);
        String imageTypeKey = getImageTypeKey(productId);
        redisImageTemplate.opsForValue().set(imageKey, imageBytes, IMAGE_CACHE_EXPIRATION, TimeUnit.SECONDS);
        redisImageTypeTemplate.opsForValue().set(imageTypeKey, imageType, IMAGE_CACHE_EXPIRATION, TimeUnit.SECONDS);
    }

    private byte[] getCachedImage(Long productId) {
        String key = getImageKey(productId);
        return getBytes(key);
    }

    private byte[] getBytes(String key) {
        return redisImageTemplate.opsForValue().get(key);
    }

    private String getImageKey(Long productId) {
        return "product:image:" + productId;
    }

    private String getImageTypeKey(Long productId) {
        return "product:image:type:" + productId;
    }

    public String getProductImageType(Long productId) throws ProductNotFoundException {
        String cachedImageType = getCachedImageType(productId);
        if (cachedImageType != null) return cachedImageType;

        ProductEntity productEntity = cacheProductData(productId);
        if (productEntity == null) return null;

        return productEntity.getImageType();
    }

    private String getCachedImageType(Long productId) {
        return redisImageTypeTemplate.opsForValue().get(getImageTypeKey(productId));
    }
}
