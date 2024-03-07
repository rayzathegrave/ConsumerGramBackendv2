package nl.consumergram.consumergramv2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.consumergram.consumergramv2.dtos.InputBlogpostDto;
import nl.consumergram.consumergramv2.dtos.OutputBlogpostDto;
import nl.consumergram.consumergramv2.services.BlogPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogPostService blogPostService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        OutputBlogpostDto blogPost = new OutputBlogpostDto();
        blogPost.setUsername("testUsername");
        when(blogPostService.getBlogPost(anyString(), any(Long.class))).thenReturn(blogPost);
        when(blogPostService.getBlogPostByUsername(anyString())).thenReturn(Collections.singletonList(blogPost));
        when(blogPostService.getAllBlogs()).thenReturn(Collections.singletonList(blogPost));
        when(blogPostService.createBlogPost(any(InputBlogpostDto.class))).thenReturn(blogPost);
    }

    @Test
    void getBlogPost() throws Exception {
        mockMvc.perform(get("/blog-posts/testUsername/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBlogPostByUsername() throws Exception {
        mockMvc.perform(get("/blog-posts/testUsername"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBlogs() throws Exception {
        mockMvc.perform(get("/blog-posts"))
                .andExpect(status().isOk());
    }

    @Test
    void createBlogPost() throws Exception {
        InputBlogpostDto blogPost = new InputBlogpostDto();
        blogPost.setUsername("testUsername");
        blogPost.setCaption("testCaption");
        blogPost.setPrice("testPrice");
        blogPost.setYesNoOption(true);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        MockMultipartFile usernamePart = new MockMultipartFile("username", "", "text/plain", "testUsername".getBytes());
        MockMultipartFile captionPart = new MockMultipartFile("caption", "", "text/plain", "testCaption".getBytes());
        MockMultipartFile pricePart = new MockMultipartFile("price", "", "text/plain", "testPrice".getBytes());
        MockMultipartFile yesNoOptionPart = new MockMultipartFile("yesNoOption", "", "text/plain", "true".getBytes());
        MockMultipartFile categoriesPart = new MockMultipartFile("categories", "", "text/plain", "Cars".getBytes());

        mockMvc.perform(multipart("/blog-posts/testUsername")
                        .file(file)
                        .file(usernamePart)
                        .file(captionPart)
                        .file(pricePart)
                        .file(yesNoOptionPart)
                        .file(categoriesPart))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteBlogPost() throws Exception {
        mockMvc.perform(delete("/blog-posts/testUsername/1"))
                .andExpect(status().isNoContent());
    }
}