package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(UserViewController.class)
@ContextConfiguration
public class UserViewControllerTest {
    @MockitoBean
    private UserPrincipleService userPrincipleService;

    @Autowired
    private MockMvc mockMvc;

    private String rootUri = "/user";
    private final DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
        //todo - following not yet required
        doAnswer(invocation -> dsh.getPrinciple(((User) invocation.getArgument(0)).getUsername()))
                .when(userPrincipleService).castToUserPrinciple(any());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void showUserMaintenanceView() throws Exception {
        UserPrinciple basicPrinciple = dsh.getPrinciple(2L);
        when(userPrincipleService.getUserPrincipleById(2L)).thenReturn(basicPrinciple);

        mockMvc.perform(get(rootUri + "/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user-maintenance"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("userPrinciple", basicPrinciple));

        verify(userPrincipleService, times(1)).getUserPrincipleById(2L);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void showUserMaintenanceView_redirectAttributes() throws Exception {
        UserPrinciple basicPrinciple = dsh.getPrinciple(2L);

        mockMvc.perform(get(rootUri + "/2")
                        .flashAttr("userPrinciple", basicPrinciple))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user-maintenance"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("userPrinciple", basicPrinciple));

        verify(userPrincipleService, times(0)).getUserPrincipleById(2L);
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateUserDetails() throws Exception {
        UserPrinciple basicPrinciple = dsh.getPrinciple(2L);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0, Long.class);
            UserPrinciple userPrinciple = invocation.getArgument(1, UserPrinciple.class);
            assertEquals(2L, id);
            assertEquals(2L, userPrinciple.getId());
            assertEquals("usernameBob", userPrinciple.getUsername());
            return basicPrinciple;
        }).when(userPrincipleService).updateUserPrinciple(any(), any());


        mockMvc.perform(post(rootUri + "/2")
                        .param("id", "2")
                        .param("username", "usernameBob")
                        .param("accountNonExpired", "true")
                        .param("userMeta.id", "2")
                        .param("userMeta.email", "bill@home.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/2"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(userPrincipleService, times(1)).updateUserPrinciple(any(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateUserDetails_error_redirect_with_flash() throws Exception {
        doThrow(new NoSuchElementException()).when(userPrincipleService).updateUserPrinciple(any(), any());

        mockMvc.perform(post(rootUri + "/2")
                        .param("id", "2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/2"))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attributeExists("userPrinciple"));

        verify(userPrincipleService, times(1)).updateUserPrinciple(any(), any());
    }


}
