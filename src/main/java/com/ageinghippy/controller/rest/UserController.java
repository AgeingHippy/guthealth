package com.ageinghippy.controller.rest;

import com.ageinghippy.model.dto.UserPrincipleDTOSimple;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserPrincipleService userPrincipleService;

    @PostMapping("/password")
    public String updatePassword(Authentication authentication, @RequestBody String password) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        userPrincipleService.updatePassword(userPrinciple, password);

        return "Password successfully updated";
    }

    @PostMapping("/{id}/password")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateUserPassword(@PathVariable Long id, @RequestBody String password) {
        UserPrinciple userPrinciple = userPrincipleService.getUserPrincipleById(id);

        userPrincipleService.updatePassword(userPrinciple, password);

        return "Password successfully updated";
    }

    @PostMapping("/register")
    public String registerActiveUser(Authentication authentication) {
        UserPrinciple userPrinciple = userPrincipleService.castToUserPrinciple(authentication.getPrincipal());
        try {
            userPrincipleService.registerActiveUser(userPrinciple);
            return "Successfully registered as an active user. Please log out and back in to activate the change";
        } catch (Exception e) {
            return "Failed to register as an active user";
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserPrincipleDTOSimple> getUsers() {
        return userPrincipleService.getUsers();
    }

}
