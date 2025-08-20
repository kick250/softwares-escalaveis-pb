package com.erp.server.services;

import com.erp.server.entities.Attachment;
import com.erp.server.entities.Product;
import com.erp.server.exceptions.ProductDescriptionRequiredException;
import com.erp.server.exceptions.ProductImageRequiredException;
import com.erp.server.exceptions.ProductNameRequiredException;
import com.erp.server.exceptions.ProductNotFoundException;
import com.erp.server.repositories.AttachmentsRepository;
import com.erp.server.repositories.ProductsRepository;
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

    public List<Product> getAll() {
        return productsRepository.findAllByDeletedFalse();
    }

    public Product getById(Long id) throws ProductNotFoundException {
        return productsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public void create(String name, String description, byte[] image, String imageType) throws ProductNameRequiredException, ProductDescriptionRequiredException, ProductImageRequiredException {
        if (name == null || name.isEmpty()) throwProductNameRequiredException();
        if (description == null || description.isEmpty()) throwProductDescriptionRequiredException();
        if (image == null || imageType == null) throwProductImageRequiredException();

        Attachment attachment = new Attachment(Base64.getEncoder().encodeToString(image), imageType);
        attachmentsRepository.save(attachment);
        Product product = new Product(name, description, attachment);
        productsRepository.save(product);
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

        Attachment attachment = null;
        Attachment oldAttachment = null;
        if (image != null && imageContentType != null) {
            attachment = new Attachment(Base64.getEncoder().encodeToString(image), imageContentType);
            attachmentsRepository.save(attachment);
        }

        Product product = getById(id);
        product.setName(name);
        product.setDescription(description);
        if (attachment != null) {
            oldAttachment = product.getAttachment();
            oldAttachment.delete();
            product.setAttachment(attachment);
            clearCachedImage(id);
        }
        productsRepository.save(product);
        if (oldAttachment != null) {
            attachmentsRepository.save(oldAttachment);
        }
    }

    private void clearCachedImage(Long productId) {
        String imageKey = getImageKey(productId);
        String imageTypeKey = getImageTypeKey(productId);
        redisImageTemplate.delete(imageKey);
        redisImageTypeTemplate.delete(imageTypeKey);
    }

    public void deleteById(Long id) throws ProductNotFoundException {
        Product product = getById(id);
        product.delete();
        productsRepository.save(product);
    }

    public byte[] getProductImage(Long productId) throws ProductNotFoundException {
        byte[] cachedImage = getCachedImage(productId);
        if (cachedImage != null) return cachedImage;

        Product product = cacheProductData(productId);
        if (product == null) return null;

        return getDecodedImage(product);
    }

    private Product cacheProductData(Long productId) throws ProductNotFoundException {
        Product product = getById(productId);
        if (!product.hasAttachment()) return null;

        byte[] imageBytes = getDecodedImage(product);
        cacheImage(productId, imageBytes, product.getImageType());
        return product;
    }

    private static byte[] getDecodedImage(Product product) {
        return Base64.getDecoder().decode(product.getImageBase64());
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

        Product product = cacheProductData(productId);
        if (product == null) return null;

        return product.getImageType();
    }

    private String getCachedImageType(Long productId) {
        return redisImageTypeTemplate.opsForValue().get(getImageTypeKey(productId));
    }
}
