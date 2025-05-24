package com.ageinghippy.service;

import com.ageinghippy.DataSetupHelper;
import com.ageinghippy.model.entity.Role;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.repository.RoleRepository;
import com.ageinghippy.repository.UserPrincipleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPrincipleServiceTest {

    @Mock
    private UserPrincipleRepository userPrincipleRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserPrincipleService userPrincipleService;

    private DataSetupHelper dsh = new DataSetupHelper();

    @BeforeEach
    void setUp() {
        UserPrincipleService service = new UserPrincipleService(
                userPrincipleRepository,
                null,
                roleRepository,
                null,
                null);

        userPrincipleService = spy(service);
    }

    @Test
    public void registerActiveUser() {
        Role userRole = dsh.getRole("ROLE_USER");
        when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.ofNullable(userRole));
        doAnswer(returnsFirstArg()).when(userPrincipleService).saveUserPrinciple(any(UserPrinciple.class));

        //GIVEN a user with GUEST role
        UserPrinciple userPrinciple = dsh.getPrinciple("guest");
        int existingRoleCount = userPrinciple.getAuthorities().size();

        //WHEN the user is registered as an active user
        userPrinciple = userPrincipleService.registerActiveUser(userPrinciple);

        //THEN the user will still have the GUEST role and also have the USER role assigned
        assert (userPrinciple.getAuthorities().size() == existingRoleCount + 1);
        assert (userPrinciple.getAuthorities().contains(dsh.getRole("ROLE_GUEST")));
        assert (userPrinciple.getAuthorities().contains(userRole));

        verify(roleRepository, times(1)).findByAuthority("ROLE_USER");
    }

    @Test
    public void registerActiveUser_alreadyHasRole() {
        Role userRole = dsh.getRole("ROLE_USER");
        when(roleRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.ofNullable(userRole));

        //GIVEN a user with USER role
        UserPrinciple userPrinciple = dsh.getPrinciple("basic");
        int existingRoleCount = userPrinciple.getAuthorities().size();

        //WHEN the user is registered as an active user
        userPrinciple = userPrincipleService.registerActiveUser(userPrinciple);

        //THEN the user will still have the USER role and no new roles have been assigned
        assert (userPrinciple.getAuthorities().size() == existingRoleCount);
        assert (userPrinciple.getAuthorities().contains(userRole));
        verify(roleRepository, times(1)).findByAuthority("ROLE_USER");
    }

}