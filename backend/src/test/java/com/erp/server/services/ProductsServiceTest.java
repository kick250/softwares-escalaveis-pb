package com.erp.server.services;

import infra.global.relational.entities.AttachmentEntity;
import infra.global.relational.entities.ProductEntity;
import com.erp.server.exceptions.ProductDescriptionRequiredException;
import com.erp.server.exceptions.ProductImageRequiredException;
import com.erp.server.exceptions.ProductNameRequiredException;
import com.erp.server.exceptions.ProductNotFoundException;
import infra.global.relational.repositories.AttachmentsJpaRepository;
import infra.global.relational.repositories.ProductsJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductsServiceTest {
    private ProductsService productsService;
    private ProductsJpaRepository productsRepository;
    private AttachmentsJpaRepository attachmentsJpaRepository;
    private RedisTemplate<String, byte[]> redisImageTemplate;
    private RedisTemplate<String, String> redisImageTypeTemplate;

    @BeforeEach
    void setUp() {
        productsRepository = mock(ProductsJpaRepository.class);
        attachmentsJpaRepository = mock(AttachmentsJpaRepository.class);
        redisImageTemplate = mock(RedisTemplate.class);
        when(redisImageTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
        redisImageTypeTemplate = mock(RedisTemplate.class);
        when(redisImageTypeTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));

        productsService = new ProductsService(productsRepository, attachmentsJpaRepository, redisImageTemplate, redisImageTypeTemplate);
    }

    @Test
    public void testGetAll() {
        List<ProductEntity> productEntities = new ArrayList<>();
        ProductEntity productEntity1 = mock(ProductEntity.class);
        ProductEntity productEntity2 = mock(ProductEntity.class);
        productEntities.add(productEntity1);
        productEntities.add(productEntity2);

        Mockito.when(productsRepository.findAllByDeletedFalse()).thenReturn(productEntities);

        List<ProductEntity> loadedProductEntities = productsService.getAll();
        assertEquals(2, loadedProductEntities.size());
        assertTrue(loadedProductEntities.contains(productEntity1));
        assertTrue(loadedProductEntities.contains(productEntity2));
    }

    @Test
    public void testGetById() throws ProductNotFoundException {
        Long productId = 1L;
        ProductEntity productEntity = mock(ProductEntity.class);
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));

        ProductEntity loadedProductEntity = productsService.getById(productId);
        assertEquals(productEntity, loadedProductEntity);
    }

    @Test
    public void testGetById_throwsException() {
        Long productId = 1L;
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productsService.getById(productId));
    }

    @Test
    public void testCreate() throws ProductImageRequiredException, ProductDescriptionRequiredException, ProductNameRequiredException {
        String name = "Test Product";
        String description = "Test Description";
        byte[] image = new byte[]{1, 2, 3};
        String imageType = "image/png";

        productsService.create(name, description, image, imageType);

        verify(attachmentsJpaRepository).save(Mockito.any());
        verify(productsRepository).save(Mockito.any());
    }

    @Test
    public void testCreate_withBlankName() {
        String name = "";
        String description = "Test Description";
        byte[] image = new byte[]{1, 2, 3};
        String imageType = "image/png";

        Exception exception = assertThrows(ProductNameRequiredException.class, () -> productsService.create(name, description, image, imageType));

        verify(attachmentsJpaRepository, never()).save(Mockito.any());
        verify(productsRepository, never()).save(Mockito.any());
        assertEquals("O nome do produto é obrigatório", exception.getMessage());
    }

    @Test
    public void testCreate_withBlankDescription() {
        String name = "Test Product";
        String description = "";
        byte[] image = new byte[]{1, 2, 3};
        String imageType = "image/png";

        Exception exception = assertThrows(ProductDescriptionRequiredException.class, () -> productsService.create(name, description, image, imageType));

        verify(attachmentsJpaRepository, never()).save(Mockito.any());
        verify(productsRepository, never()).save(Mockito.any());

        assertEquals("A descrição do produto é obrigatória", exception.getMessage());
    }

    @Test
    public void testCreate_withNullImage() {
        String name = "Test Product";
        String description = "Test Description";
        byte[] image = null;
        String imageType = "image/png";

        Exception exception = assertThrows(ProductImageRequiredException.class, () -> productsService.create(name, description, image, imageType));

        verify(attachmentsJpaRepository, never()).save(Mockito.any());
        verify(productsRepository, never()).save(Mockito.any());
        assertEquals("A imagem do produto é obrigatória", exception.getMessage());
    }

    @Test
    public void testUpdate() throws ProductNotFoundException, ProductDescriptionRequiredException, ProductNameRequiredException {
        Long productId = 1L;
        String name = "Updated Product";
        String description = "Updated Description";
        byte[] image = new byte[]{4, 5, 6};
        String imageContentType = "image/jpeg";
        AttachmentEntity oldAttachmentEntity = mock(AttachmentEntity.class);

        ProductEntity productEntity = mock(ProductEntity.class);
        when(productEntity.getAttachmentEntity()).thenReturn(oldAttachmentEntity);
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));

        productsService.update(productId, name, description, image, imageContentType);

        verify(productsRepository).save(productEntity);
        verify(attachmentsJpaRepository, Mockito.times(2)).save(Mockito.any());
        verify(productEntity).setName(name);
        verify(productEntity).setDescription(description);
        verify(productEntity).setAttachmentEntity(Mockito.any());
        verify(oldAttachmentEntity).delete();
        verify(redisImageTemplate).delete("product:image:" + productId);
        verify(redisImageTypeTemplate).delete("product:image:type:" + productId);
    }

    @Test
    public void testUpdate_withBlankName() {
        Long productId = 1L;
        String name = "";
        String description = "Updated Description";
        byte[] image = new byte[]{4, 5, 6};
        String imageContentType = "image/jpeg";

        Exception exception = assertThrows(ProductNameRequiredException.class, () -> productsService.update(productId, name, description, image, imageContentType));

        verify(productsRepository, never()).save(Mockito.any());
        verify(attachmentsJpaRepository, never()).save(Mockito.any());
        assertEquals("O nome do produto é obrigatório", exception.getMessage());
    }

    @Test
    public void testUpdate_withBlankDescription() {
        Long productId = 1L;
        String name = "Updated Product";
        String description = "";
        byte[] image = new byte[]{4, 5, 6};
        String imageContentType = "image/jpeg";

        Exception exception = assertThrows(ProductDescriptionRequiredException.class, () -> productsService.update(productId, name, description, image, imageContentType));

        verify(productsRepository, never()).save(Mockito.any());
        verify(attachmentsJpaRepository, never()).save(Mockito.any());
        assertEquals("A descrição do produto é obrigatória", exception.getMessage());
    }

    @Test
    public void testDeleteById() throws ProductNotFoundException {
        Long productId = 1L;

        ProductEntity productEntity = mock(ProductEntity.class);
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));

        productsService.deleteById(productId);

        verify(productEntity).delete();
        verify(productsRepository).save(productEntity);
    }

    @Test
    public void testDeleteById_throwsException() {
        Long productId = 1L;
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productsService.deleteById(productId));
    }

    @Test
    public void testGetProductImage_returnsCachedImage() throws ProductNotFoundException {
        Long productId = 1L;
        byte[] cachedImage = new byte[]{1, 2, 3};
        when(redisImageTemplate.opsForValue().get("product:image:" + productId)).thenReturn(cachedImage);

        byte[] result = productsService.getProductImage(productId);

        assertArrayEquals(cachedImage, result);
        verify(redisImageTemplate.opsForValue()).get("product:image:" + productId);
        verifyNoMoreInteractions(productsRepository);
    }

    @Test
    public void testGetProductImage_whenImageIsNotCached_loadImageAndCache() throws ProductNotFoundException {
        Long productId = 1L;
        byte[] image = new byte[]{1, 2, 3};
        String imageType = "image/png";
        ProductEntity productEntity = mock(ProductEntity.class);

        when(productEntity.hasAttachment()).thenReturn(true);
        when(productEntity.getImageBase64()).thenReturn(Base64.getEncoder().encodeToString(image));
        when(productEntity.getImageType()).thenReturn(imageType);
        when(redisImageTemplate.opsForValue().get("product:image:" + productId)).thenReturn(null);
        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));

        byte[] result = productsService.getProductImage(productId);

        verify(redisImageTemplate.opsForValue()).get("product:image:" + productId);
        verify(productsRepository).findByIdAndDeletedFalse(productId);
        verify(redisImageTemplate.opsForValue()).set("product:image:" + productId, image, 60 * 60, TimeUnit.SECONDS);
        verify(redisImageTypeTemplate.opsForValue()).set("product:image:type:" + productId, imageType, 60 * 60, TimeUnit.SECONDS);
        assertNotNull(result);
    }

    @Test
    public void testGetProductImageType_returnCachedProductImageType() throws ProductNotFoundException {
        Long productId = 1L;
        String cachedImageType = "image/png";
        when(redisImageTypeTemplate.opsForValue().get("product:image:type:" + productId)).thenReturn(cachedImageType);

        String result = productsService.getProductImageType(productId);

        assertEquals(cachedImageType, result);
        verify(redisImageTypeTemplate.opsForValue()).get("product:image:type:" + productId);
        verifyNoMoreInteractions(productsRepository);
    }

    @Test
    public void testGetProductImageType_whenImageTypeIsNotCached_loadImageTypeAndCache() throws ProductNotFoundException {
        Long productId = 1L;
        byte[] image = new byte[]{1, 2, 3};
        String imageType = "image/png";
        ProductEntity productEntity = mock(ProductEntity.class);

        when(productEntity.hasAttachment()).thenReturn(true);
        when(productEntity.getImageBase64()).thenReturn(Base64.getEncoder().encodeToString(image));
        when(productEntity.getImageType()).thenReturn(imageType);
        when(redisImageTemplate.opsForValue().get("product:image:type:" + productId)).thenReturn(null);
        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));

        String result = productsService.getProductImageType(productId);

        verify(redisImageTypeTemplate.opsForValue()).get("product:image:type:" + productId);
        verify(productsRepository).findByIdAndDeletedFalse(productId);
        verify(redisImageTemplate.opsForValue()).set("product:image:" + productId, image, 60 * 60, TimeUnit.SECONDS);
        verify(redisImageTypeTemplate.opsForValue()).set("product:image:type:" + productId, imageType, 60 * 60, TimeUnit.SECONDS);
        assertEquals(imageType, result);
    }
}