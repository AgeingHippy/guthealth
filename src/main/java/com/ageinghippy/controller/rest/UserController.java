package com.ageinghippy.controller.rest;

import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserPrincipleService userPrincipleService;

    @PostMapping("/password")
    public String updatePassword(Authentication authentication, @RequestBody String password) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

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
}
