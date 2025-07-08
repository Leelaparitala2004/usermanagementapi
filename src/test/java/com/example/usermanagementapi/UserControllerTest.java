package com.example.usermanagementapi;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.example.usermanagementapi.model.User;
import com.example.usermanagementapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepositry;

    private User user1;
    private User user2;
    private List<User> manyUsers;

    @BeforeEach
    void setUp() {
        userRepositry.deleteAll();
        objectMapper.registerModule(new JavaTimeModule());

        User initialUser1 = new User("John Doe", "Johndeo@gmail.com");
        User initialUser2 = new User("Jane Smith", "Jane@gmail.com");
        List<User> savedInitialUsers = userRepositry.saveAll(List.of(initialUser1, initialUser2));

        this.user1 = savedInitialUsers.get(0);
        this.user2 = savedInitialUsers.get(1);

        manyUsers = IntStream.rangeClosed(1, 25)
                .mapToObj(i -> new User("User " + i, "user" + i + "@example.com"))
                .collect(Collectors.toList());
        userRepositry.saveAll(manyUsers);
    }

    @Test
    public void TestGetUserByIdFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(user1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }
    @Test
    public void TestCreateUser() throws Exception {
        User newUser1 = new User("Alice Johnson", "alicejon@gmail.com");
        User newUser2 = new User("Bob Brown", "Bob.brown@gmail.com");
        List<User> newUsers = Arrays.asList(newUser1, newUser2);
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUsers)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id",notNullValue()))
                .andExpect(jsonPath("$[1].id",notNullValue()))
                .andExpect(jsonPath("$[0].name", is(newUser1.getName())))
                .andExpect(jsonPath("$[0].email", is(newUser1.getEmail())))
                .andExpect(jsonPath("$[1].name", is(newUser2.getName())))
                .andExpect(jsonPath("$[1].email", is(newUser2.getEmail())));

    }
    @Test
    public void TestDeleteUserFound() throws Exception {

        Long userId = user1.getId();

        // Ensure user exists
        if(userRepositry.existsById(userId)) {
            mockMvc.perform(delete("/api/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }else{
            throw new Exception("User not found for deletion");
        }

        // Verify user is deleted
        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    public void TestDeleteUserNotFound() throws Exception{
        Long nonExistentId = 999L; // Assuming this ID does not exist in the database
        mockMvc.perform(get("/api/users/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


}