package com.erp.server.services;

import com.erp.server.entities.Attachment;
import com.erp.server.entities.Product;
import com.erp.server.exceptions.ProductDescriptionRequiredException;
import com.erp.server.exceptions.ProductImageRequiredException;
import com.erp.server.exceptions.ProductNameRequiredException;
import com.erp.server.exceptions.ProductNotFoundException;
import com.erp.server.repositories.AttachmentsRepository;
import com.erp.server.repositories.ProductsRepository;
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
    private ProductsRepository productsRepository;
    private AttachmentsRepository attachmentsRepository;
    private RedisTemplate<String, byte[]> redisImageTemplate;
    private RedisTemplate<String, String> redisImageTypeTemplate;

    @BeforeEach
    void setUp() {
        productsRepository = mock(ProductsRepository.class);
        attachmentsRepository = mock(AttachmentsRepository.class);
        redisImageTemplate = mock(RedisTemplate.class);
        when(redisImageTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
        redisImageTypeTemplate = mock(RedisTemplate.class);
        when(redisImageTypeTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));

        productsService = new ProductsService(productsRepository, attachmentsRepository, redisImageTemplate, redisImageTypeTemplate);
    }

    @Test
    public void testGetAll() {
        List<Product> products = new ArrayList<>();
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        products.add(product1);
        products.add(product2);

        Mockito.when(productsRepository.findAllByDeletedFalse()).thenReturn(products);

        List<Product> loadedProducts = productsService.getAll();
        assertEquals(2, loadedProducts.size());
        assertTrue(loadedProducts.contains(product1));
        assertTrue(loadedProducts.contains(product2));
    }

    @Test
    public void testGetById() throws ProductNotFoundException {
        Long productId = 1L;
        Product product = mock(Product.class);
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));

        Product loadedProduct = productsService.getById(productId);
        assertEquals(product, loadedProduct);
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

        verify(attachmentsRepository).save(Mockito.any());
        verify(productsRepository).save(Mockito.any());
    }

    @Test
    public void testCreate_withBlankName() {
        String name = "";
        String description = "Test Description";
        byte[] image = new byte[]{1, 2, 3};
        String imageType = "image/png";

        Exception exception = assertThrows(ProductNameRequiredException.class, () -> productsService.create(name, description, image, imageType));

        verify(attachmentsRepository, never()).save(Mockito.any());
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

        verify(attachmentsRepository, never()).save(Mockito.any());
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

        verify(attachmentsRepository, never()).save(Mockito.any());
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
        Attachment oldAttachment = mock(Attachment.class);

        Product product = mock(Product.class);
        when(product.getAttachment()).thenReturn(oldAttachment);
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));

        productsService.update(productId, name, description, image, imageContentType);

        verify(productsRepository).save(product);
        verify(attachmentsRepository, Mockito.times(2)).save(Mockito.any());
        verify(product).setName(name);
        verify(product).setDescription(description);
        verify(product).setAttachment(Mockito.any());
        verify(oldAttachment).delete();
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
        verify(attachmentsRepository, never()).save(Mockito.any());
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
        verify(attachmentsRepository, never()).save(Mockito.any());
        assertEquals("A descrição do produto é obrigatória", exception.getMessage());
    }

    @Test
    public void testDeleteById() throws ProductNotFoundException {
        Long productId = 1L;

        Product product = mock(Product.class);
        Mockito.when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));

        productsService.deleteById(productId);

        verify(product).delete();
        verify(productsRepository).save(product);
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
        Product product = mock(Product.class);

        when(product.hasAttachment()).thenReturn(true);
        when(product.getImageBase64()).thenReturn(Base64.getEncoder().encodeToString(image));
        when(product.getImageType()).thenReturn(imageType);
        when(redisImageTemplate.opsForValue().get("product:image:" + productId)).thenReturn(null);
        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));

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
        Product product = mock(Product.class);

        when(product.hasAttachment()).thenReturn(true);
        when(product.getImageBase64()).thenReturn(Base64.getEncoder().encodeToString(image));
        when(product.getImageType()).thenReturn(imageType);
        when(redisImageTemplate.opsForValue().get("product:image:type:" + productId)).thenReturn(null);
        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));

        String result = productsService.getProductImageType(productId);

        verify(redisImageTypeTemplate.opsForValue()).get("product:image:type:" + productId);
        verify(productsRepository).findByIdAndDeletedFalse(productId);
        verify(redisImageTemplate.opsForValue()).set("product:image:" + productId, image, 60 * 60, TimeUnit.SECONDS);
        verify(redisImageTypeTemplate.opsForValue()).set("product:image:type:" + productId, imageType, 60 * 60, TimeUnit.SECONDS);
        assertEquals(imageType, result);
    }
}