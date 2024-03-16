package nl.consumergram.consumergramv2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.consumergram.consumergramv2.dtos.InputUpvoteDto;
import nl.consumergram.consumergramv2.dtos.OutputUpvoteDto;
import nl.consumergram.consumergramv2.services.UpvoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UpvoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpvoteService upvoteService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        OutputUpvoteDto outputUpvoteDto = new OutputUpvoteDto();
        when(upvoteService.upvoteBlogPost(any(InputUpvoteDto.class))).thenReturn(outputUpvoteDto);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void upvoteBlogPost() throws Exception {
        InputUpvoteDto inputUpvoteDto = new InputUpvoteDto();
        inputUpvoteDto.setBlogPostId(1L);
        inputUpvoteDto.setUsername("testUsername");

        mockMvc.perform(post("/upvotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUpvoteDto)))
                .andExpect(status().isCreated());
    }
}