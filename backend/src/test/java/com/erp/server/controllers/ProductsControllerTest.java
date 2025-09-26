package com.erp.server.controllers;

import infra.global.entities.AttachmentEntity;
import infra.global.entities.ProductEntity;
import infra.global.entities.UserEntity;
import com.erp.server.factories.AttachmentsFactory;
import com.erp.server.factories.ProductsFactory;
import com.erp.server.factories.UsersFactory;
import infra.global.repositories.AttachmentsRepository;
import infra.global.repositories.ProductsRepository;
import infra.global.repositories.UsersRepository;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProductsFactory productsFactory = new ProductsFactory();
    private final AttachmentsFactory attachmentsFactory = new AttachmentsFactory();

    private String token;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private AttachmentsRepository attachmentsRepository;
    @Autowired
    private TokenService tokenService;

    private ProductEntity createdProductEntity1;
    private ProductEntity createdProductEntity2;

    @BeforeEach
    void setUp() {
        usersRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsRepository.deleteAll();

        UsersFactory usersFactory = new UsersFactory();
        UserEntity userEntity = usersFactory.createUser();

        usersRepository.save(userEntity);

        token = "Bearer " + tokenService.generateToken(userEntity);

        AttachmentEntity attachmentEntity1 = attachmentsFactory.createAttachment();
        AttachmentEntity attachmentEntity2 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachmentEntity1);
        attachmentsRepository.save(attachmentEntity2);

        createdProductEntity1 = productsFactory.createProduct();
        createdProductEntity1.setAttachmentEntity(attachmentEntity1);
        createdProductEntity2 = productsFactory.createProduct();
        createdProductEntity2.setAttachmentEntity(attachmentEntity2);

        productsRepository.save(createdProductEntity1);
        productsRepository.save(createdProductEntity2);
    }

    @AfterEach
    void tearDown() {
        usersRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsRepository.deleteAll();
    }

    @Test
    public void testGetProducts() throws Exception {
        mockMvc.perform(get("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(2))
                .andExpect(jsonPath("$.products[0].id").value(createdProductEntity1.getId()))
                .andExpect(jsonPath("$.products[0].name").value(createdProductEntity1.getName()))
                .andExpect(jsonPath("$.products[0].description").value(createdProductEntity1.getDescription()))
                .andExpect(jsonPath("$.products[0].imagePath").value("/products/" + createdProductEntity1.getId() + "/image"))
                .andExpect(jsonPath("$.products[1].id").value(createdProductEntity2.getId()))
                .andExpect(jsonPath("$.products[1].name").value(createdProductEntity2.getName()))
                .andExpect(jsonPath("$.products[1].description").value(createdProductEntity2.getDescription()))
                .andExpect(jsonPath("$.products[1].imagePath").value("/products/" + createdProductEntity2.getId() + "/image"));
    }

    @Test
    public void testGetProducts_whenOneProductIsDeleted() throws Exception {
        createdProductEntity1.delete();
        productsRepository.save(createdProductEntity1);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(1))
                .andExpect(jsonPath("$.products[0].id").value(createdProductEntity2.getId()))
                .andExpect(jsonPath("$.products[0].name").value(createdProductEntity2.getName()))
                .andExpect(jsonPath("$.products[0].description").value(createdProductEntity2.getDescription()))
                .andExpect(jsonPath("$.products[0].imagePath").value("/products/" + createdProductEntity2.getId() + "/image"));
    }

    @Test
    public void testGetProductById() throws Exception {
        mockMvc.perform(get("/products/" + createdProductEntity1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdProductEntity1.getId()))
                .andExpect(jsonPath("$.name").value(createdProductEntity1.getName()))
                .andExpect(jsonPath("$.description").value(createdProductEntity1.getDescription()))
                .andExpect(jsonPath("$.imagePath").value("/products/" + createdProductEntity1.getId() + "/image"));
    }

    @Test
    public void testGetProductById_whenProductNotFound() throws Exception {
        mockMvc.perform(get("/products/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "Nome de teste";
        String description = "Descrição de teste";

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.count());

        mockMvc.perform(multipart("/products")
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token))
                .andExpect(status().isCreated());

        assertEquals(3, productsRepository.count());
        ProductEntity productEntity = productsRepository.findAll().getLast();
        assertEquals(name, productEntity.getName());
        assertEquals(description, productEntity.getDescription());
        assertEquals(3, attachmentsRepository.count());
    }

    @Test
    public void testCreate_whenNameIsBlank() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "";
        String description = "Descrição de teste";

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.count());

        mockMvc.perform(multipart("/products")
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testCreate_whenDescriptionIsBlank() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "Nome de teste";
        String description = "";

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.count());

        mockMvc.perform(multipart("/products")
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testCreate_whenImageIsNull() throws Exception {
        String name = "Nome de teste";
        String description = "Descrição de teste";

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.count());

        mockMvc.perform(multipart("/products")
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testUpdate() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "Nome atualizado";
        String description = "Descrição atualizada";

        mockMvc.perform(multipart("/products/" + createdProductEntity1.getId())
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isOk());

        ProductEntity updatedProductEntity = productsRepository.findById(createdProductEntity1.getId()).orElseThrow();
        assertEquals(name, updatedProductEntity.getName());
        assertEquals(description, updatedProductEntity.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(3, attachmentsRepository.count());
    }

    @Test
    public void testUpdate_whenNameIsBlank() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "";
        String description = "Descrição atualizada";

        mockMvc.perform(multipart("/products/" + createdProductEntity1.getId())
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isBadRequest());

        ProductEntity updatedProductEntity = productsRepository.findById(createdProductEntity1.getId()).orElseThrow();
        assertNotEquals(name, updatedProductEntity.getName());
        assertNotEquals(description, updatedProductEntity.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testUpdate_whenDescriptionIsBlank() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "Nome atualizado";
        String description = "";

        mockMvc.perform(multipart("/products/" + createdProductEntity1.getId())
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isBadRequest());

        ProductEntity updatedProductEntity = productsRepository.findById(createdProductEntity1.getId()).orElseThrow();
        assertNotEquals(name, updatedProductEntity.getName());
        assertNotEquals(description, updatedProductEntity.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testUpdate_whenImageIsNull() throws Exception {
        String name = "Nome atualizado";
        String description = "Descrição atualizada";

        mockMvc.perform(multipart("/products/" + createdProductEntity1.getId())
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isOk());

        ProductEntity updatedProductEntity = productsRepository.findById(createdProductEntity1.getId()).orElseThrow();
        assertEquals(name, updatedProductEntity.getName());
        assertEquals(description, updatedProductEntity.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/products/" + createdProductEntity1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());

        assertEquals(2, productsRepository.count());
        assertEquals(1, attachmentsRepository.countAllByDeletedFalse());
    }

    @Test
    public void testDelete_whenProductNotFound() throws Exception {
        mockMvc.perform(delete("/products/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());

        assertEquals(2, productsRepository.count());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
    }

    @Test
    public void testGetProductImage() throws Exception {
        mockMvc.perform(get("/products/" + createdProductEntity1.getId() + "/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProductImage_whenWithoutToken() throws Exception {
        mockMvc.perform(get("/products/" + createdProductEntity1.getId() + "/image")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProductImage_whenProductNotFound() throws Exception {
        mockMvc.perform(get("/products/9999/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }
}