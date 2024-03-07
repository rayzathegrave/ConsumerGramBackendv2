package nl.consumergram.consumergramv2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.consumergram.consumergramv2.dtos.UserDto;
import nl.consumergram.consumergramv2.services.AuthorityService;
import nl.consumergram.consumergramv2.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorityService authorityService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUsername");
        when(userService.getUsers()).thenReturn(Collections.singletonList(userDto));
        when(userService.getUser(anyString())).thenReturn(userDto);
        when(userService.createUser(any(UserDto.class))).thenReturn("testUsername");
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/users/testUsername"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUsername");
        userDto.setPassword("testPassword");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUsername");
        userDto.setPassword("testPassword");

        mockMvc.perform(put("/users/testUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/testUsername"))
                .andExpect(status().isNoContent());
    }
}