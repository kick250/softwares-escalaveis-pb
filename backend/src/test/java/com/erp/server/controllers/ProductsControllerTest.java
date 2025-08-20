package com.erp.server.controllers;

import com.erp.server.entities.Attachment;
import com.erp.server.entities.Product;
import com.erp.server.entities.User;
import com.erp.server.factories.AttachmentsFactory;
import com.erp.server.factories.ProductsFactory;
import com.erp.server.factories.UsersFactory;
import com.erp.server.repositories.AttachmentsRepository;
import com.erp.server.repositories.ProductsRepository;
import com.erp.server.repositories.UsersRepository;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private Product createdProduct1;
    private Product createdProduct2;

    @BeforeEach
    void setUp() {
        usersRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsRepository.deleteAll();

        UsersFactory usersFactory = new UsersFactory();
        User user = usersFactory.createUser();

        usersRepository.save(user);

        token = "Bearer " + tokenService.generateToken(user);

        Attachment attachment1 = attachmentsFactory.createAttachment();
        Attachment attachment2 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachment1);
        attachmentsRepository.save(attachment2);

        createdProduct1 = productsFactory.createProduct();
        createdProduct1.setAttachment(attachment1);
        createdProduct2 = productsFactory.createProduct();
        createdProduct2.setAttachment(attachment2);

        productsRepository.save(createdProduct1);
        productsRepository.save(createdProduct2);
    }

    @Test
    public void testGetProducts() throws Exception {
        mockMvc.perform(get("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(2))
                .andExpect(jsonPath("$.products[0].id").value(createdProduct1.getId()))
                .andExpect(jsonPath("$.products[0].name").value(createdProduct1.getName()))
                .andExpect(jsonPath("$.products[0].description").value(createdProduct1.getDescription()))
                .andExpect(jsonPath("$.products[0].imagePath").value("/products/" + createdProduct1.getId() + "/image"))
                .andExpect(jsonPath("$.products[1].id").value(createdProduct2.getId()))
                .andExpect(jsonPath("$.products[1].name").value(createdProduct2.getName()))
                .andExpect(jsonPath("$.products[1].description").value(createdProduct2.getDescription()))
                .andExpect(jsonPath("$.products[1].imagePath").value("/products/" + createdProduct2.getId() + "/image"));
    }

    @Test
    public void testGetProducts_whenOneProductIsDeleted() throws Exception {
        createdProduct1.delete();
        productsRepository.save(createdProduct1);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(1))
                .andExpect(jsonPath("$.products[0].id").value(createdProduct2.getId()))
                .andExpect(jsonPath("$.products[0].name").value(createdProduct2.getName()))
                .andExpect(jsonPath("$.products[0].description").value(createdProduct2.getDescription()))
                .andExpect(jsonPath("$.products[0].imagePath").value("/products/" + createdProduct2.getId() + "/image"));
    }

    @Test
    public void testGetProductById() throws Exception {
        mockMvc.perform(get("/products/" + createdProduct1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdProduct1.getId()))
                .andExpect(jsonPath("$.name").value(createdProduct1.getName()))
                .andExpect(jsonPath("$.description").value(createdProduct1.getDescription()))
                .andExpect(jsonPath("$.imagePath").value("/products/" + createdProduct1.getId() + "/image"));
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
        Product product = productsRepository.findAll().getLast();
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
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

        mockMvc.perform(multipart("/products/" + createdProduct1.getId())
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isOk());

        Product updatedProduct = productsRepository.findById(createdProduct1.getId()).orElseThrow();
        assertEquals(name, updatedProduct.getName());
        assertEquals(description, updatedProduct.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(3, attachmentsRepository.count());
    }

    @Test
    public void testUpdate_whenNameIsBlank() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "";
        String description = "Descrição atualizada";

        mockMvc.perform(multipart("/products/" + createdProduct1.getId())
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isBadRequest());

        Product updatedProduct = productsRepository.findById(createdProduct1.getId()).orElseThrow();
        assertNotEquals(name, updatedProduct.getName());
        assertNotEquals(description, updatedProduct.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testUpdate_whenDescriptionIsBlank() throws Exception {
        var imageFile = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes());
        String name = "Nome atualizado";
        String description = "";

        mockMvc.perform(multipart("/products/" + createdProduct1.getId())
                        .file(imageFile)
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isBadRequest());

        Product updatedProduct = productsRepository.findById(createdProduct1.getId()).orElseThrow();
        assertNotEquals(name, updatedProduct.getName());
        assertNotEquals(description, updatedProduct.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testUpdate_whenImageIsNull() throws Exception {
        String name = "Nome atualizado";
        String description = "Descrição atualizada";

        mockMvc.perform(multipart("/products/" + createdProduct1.getId())
                        .param("name", name)
                        .param("description", description)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", token)
                        .with(req -> { req.setMethod("PUT"); return req; }))
                .andExpect(status().isOk());

        Product updatedProduct = productsRepository.findById(createdProduct1.getId()).orElseThrow();
        assertEquals(name, updatedProduct.getName());
        assertEquals(description, updatedProduct.getDescription());
        assertEquals(2, attachmentsRepository.countAllByDeletedFalse());
        assertEquals(2, attachmentsRepository.count());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/products/" + createdProduct1.getId())
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
        mockMvc.perform(get("/products/" + createdProduct1.getId() + "/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProductImage_whenWithoutToken() throws Exception {
        mockMvc.perform(get("/products/" + createdProduct1.getId() + "/image")
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