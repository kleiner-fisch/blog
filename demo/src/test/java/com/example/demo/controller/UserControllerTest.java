package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;
    private List<User> users = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        user1 = new User(1L, "user1", "abc", "mail", Collections.emptyList());
        user1 = new User(2L, "user2", "abc", "mail", Collections.emptyList());
        users.add(user1);
        users.add(user2);

    }

    @AfterEach
    private void tearDown() {
        users.clear();
    }

    @Test
    void testGetUser() throws Exception{
        when(userService.getUser(anyLong())).thenReturn(user1);
        this.mockMvc.perform(get("/users/1")).andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        Page<User> page = new PageImpl(users);
        when(userService.getAllUsers()).thenReturn(page);
        this.mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception{
        when(userService.deleteUser(1L)).thenReturn(1L);
        this.mockMvc.perform(delete("/users/1")).andExpect(status().isOk());
    }

    /**
     * Custom serialization function to use in tests. 
     * 
     * We do not serialize the user password, when sending User objects to the client. 
     * Hence, here we need a custom serialization that _does_ serialize the user password.
     */
    private String jsonify(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("CustomCarSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(User.class, new CustomCarSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(o);
    }

    @Test
    void testCreateUser() throws Exception{
        when(userService.createUser(user1)).thenReturn(user1.getUserId());
        this.mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonify(user1)))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception{
        when(userService.updateUser(1L, user1)).thenReturn(1L);
        this.mockMvc.perform(put("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonify(user1)))
            .andExpect(status().isOk());
    }


    /**
     * Custom serializer that does serialize the user password.
     */
    class CustomCarSerializer extends StdSerializer<User> {

        public CustomCarSerializer() {
            this(null);
        }

        public CustomCarSerializer(Class<User> t) {
            super(t);
        }

        @Override
        public void serialize(
                User user, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("userId", user.getUserId());
            jsonGenerator.writeStringField("username", user.getUsername());
            jsonGenerator.writeStringField("password", user.getPassword());
            jsonGenerator.writeStringField("mail", user.getMail());
            jsonGenerator.writeEndObject();
        }
    }

}
