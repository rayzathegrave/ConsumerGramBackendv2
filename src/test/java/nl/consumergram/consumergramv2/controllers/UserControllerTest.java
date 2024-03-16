package nl.consumergram.consumergramv2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.consumergram.consumergramv2.dtos.UserDto;
import nl.consumergram.consumergramv2.models.Authority;
import nl.consumergram.consumergramv2.services.AuthorityService;
import nl.consumergram.consumergramv2.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void getUser() throws Exception {
        mockMvc.perform(get("/users/testUsername"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_whenUsernameNotTaken() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUsername");
        userDto.setPassword("12345678%");

        when(userService.userExists(anyString())).thenReturn(false); // Mock user does not exist
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto.getUsername());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_whenUsernameAlreadyExists() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUsername");
        userDto.setPassword("12345678%");

        when(userService.userExists(anyString())).thenReturn(true); // Mock user already exists

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void updateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUsername");
        userDto.setPassword("123456789%");

        mockMvc.perform(put("/users/testUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/testUsername"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void createAdmin() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testAdmin");
        userDto.setPassword("testPassword");

        mockMvc.perform(post("/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }


}