package com.ageinghippy.controller.mvc;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        mockMvc.perform(get(rootUri +"/2"))
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

        mockMvc.perform(get(rootUri +"/2")
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
    void updateUserDetails() {
        //todo implement
    }


}
