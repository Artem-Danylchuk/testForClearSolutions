package com.testForCS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.testForCS.dto.CreateUserDTO;
import com.testForCS.dto.UpdateUserDTO;
import com.testForCS.dto.UserDTO;
import com.testForCS.entity.User;
import com.testForCS.service.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserServiceImpl userService;

    @Test
    @SneakyThrows
    public void testNewUser() {
        CreateUserDTO createUserDTO = new CreateUserDTO("artem9@g.com", "Artem", "Dan", LocalDate.parse("2000-05-12"));

        String result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(createUserDTO))
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO userDTO = objectMapper.readValue(result, UserDTO.class);
        assertEquals("Artem", userDTO.getName());
        assertEquals("Dan", userDTO.getSurname());
        assertEquals("artem9@g.com", userDTO.getEmail());
        assertEquals(LocalDate.parse("2000-05-12"), userDTO.getBirthDate());

        User user = userService.findById(userDTO.getId());
        assertNotNull(user);
        assertEquals("Artem", user.getName());
        assertEquals("Dan", user.getSurname());
        assertEquals("artem9@g.com", user.getEmail());
        assertEquals(LocalDate.parse("2000-05-12"), user.getBirthDate());
    }

    @Test
    @SneakyThrows
    public void testCreateUserEmailConflict() {
        CreateUserDTO createUserDTO = new CreateUserDTO("artem-conflict@g.com", "Artem", "Dan", LocalDate.parse("2000-05-12"));
        userService.addUser(createUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createUserDTO))
        ).andExpect(status().isConflict());
    }


    @Test
    @SneakyThrows
    public void testUpdateUser() {
        CreateUserDTO createUserDTO = new CreateUserDTO("artem3@g.com", "Artem", "Dan", LocalDate.parse("2000-05-12"));
        userService.addUser(createUserDTO);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO("NewName", "NewSurname", LocalDate.parse("1995-05-12"));
        userService.update(1L, updateUserDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateUserDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void testPartialUpdateUser() {
        CreateUserDTO createUserDTO = new CreateUserDTO("artem@g.com", "Artem", "Dan", LocalDate.parse("2000-05-12"));
        userService.addUser(createUserDTO);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "newArtem");
        updates.put("surname", "newDan");
        updates.put("birthDate", "1995-05-12");

        userService.update(1L, updates);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updates)))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void testGetUserById() {
        CreateUserDTO createUserDTO = new CreateUserDTO("artemTest1@g.com", "Artem", "Dan", LocalDate.parse("2000-05-12"));

        User user = userService.addUser(createUserDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", user.getId()));
    }

    @Test
    @SneakyThrows
    public void testSearchUsers() {
        CreateUserDTO createUserDTO1 = new CreateUserDTO("arte1m@g.com", "Artem", "Dan", LocalDate.parse("2000-05-12"));
        CreateUserDTO createUserDTO2 = new CreateUserDTO("artem2@g.com", "Artem2", "Dan2", LocalDate.parse("1995-05-12"));
        CreateUserDTO createUserDTO3 = new CreateUserDTO("arte3m@g.com", "Artem3", "Dan3", LocalDate.parse("2002-05-12"));
        CreateUserDTO createUserDTO4 = new CreateUserDTO("artem4@g.com", "Artem4", "Dan4", LocalDate.parse("1998-05-12"));
        userService.addUser(createUserDTO1);
        userService.addUser(createUserDTO2);
        userService.addUser(createUserDTO3);
        userService.addUser(createUserDTO4);

        LocalDate fromDate = LocalDate.of(1995, 1, 1);
        LocalDate toDate = LocalDate.of(2005, 1, 1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/search")
                        .param("from", fromDate.toString())
                        .param("to", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray());
    }

    @Test
    @SneakyThrows
    public void testDeleteUser() {
        CreateUserDTO createUserDTO = new CreateUserDTO("artem1@g.com", "Artem", "Dan", LocalDate.parse("2000-05-12"));
        userService.addUser(createUserDTO);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", 1))
                .andExpect(status().isNoContent());
    }
}