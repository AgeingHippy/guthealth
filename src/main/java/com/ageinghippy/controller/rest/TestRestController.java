package com.ageinghippy.controller.rest;

import com.ageinghippy.model.CustomUserPrincipal;
import com.ageinghippy.model.entity.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Date;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestRestController {

//    private final Properties properties;

    @GetMapping("/time")
    public String getTest(Authentication authentication) {
        String userName = "Unknown User";
        if (authentication != null) {
            UserPrinciple userPrinciple = ((CustomUserPrincipal) authentication.getPrincipal()).getUserPrinciple();
            userName = userPrinciple.getUsername();
        }

        return MessageFormat.format("{0} accessed server on {1}", userName  ,(new Date()).toString());
    }

}
