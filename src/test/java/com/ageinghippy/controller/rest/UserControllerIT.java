package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.UserPrincipleDTOSimple;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("h2")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String baseUrl = "/api/v1/users";

    @Test
    @WithUserDetails("admin")
    void getListOfUsers() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<UserPrincipleDTOSimple> resultList =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });

        assertEquals(resultList.size(), 4);
        //check a portion of the contents
        assert (resultList.contains(new UserPrincipleDTOSimple(1L, "admin", "Bob")));
        assert (resultList.contains(new UserPrincipleDTOSimple(2L, "basic", "Bill")));
        assert (resultList.contains(new UserPrincipleDTOSimple(3L, "guest", "Betty")));
        assert (resultList.contains(new UserPrincipleDTOSimple(4L, "system", "SYSTEM")));
    }

    @Test
    @WithUserDetails("basic")
    void getListOfUsers_forbidden() throws Exception {
        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
