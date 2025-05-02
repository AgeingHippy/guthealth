package com.ageinghippy.controller.rest;

import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserPrincipleService userPrincipleService;

    @PostMapping("/password")
    public String updatePassword(Authentication authentication, @RequestBody String password) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        userPrincipleService.updatePassword(userPrinciple, password);

        return "Password successfully updated";
    }
}
