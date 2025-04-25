package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.CustomUserPrincipal;
import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {
    private final UserPrincipleService userPrincipleService;

    @GetMapping("/new")
    public String showRegisterView(Model model) {
        model.addAttribute("user",
                UserPrinciple.builder().userMeta(UserMeta.builder().build()).build());

        return "/register";
    }

    @PostMapping("/create")
    public String createNewUser(@ModelAttribute UserPrinciple userPrinciple, Model model) {
        userPrinciple = userPrincipleService.createPasswordUser(userPrinciple);

        model.addAttribute("username", userPrinciple.getUsername());

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        model.addAttribute("userPrinciple", customUserPrincipal.getUserPrinciple());

        return "profile.html";
    }

    @PostMapping("/userMeta/{id}")
    public String updateProfile(Authentication authentication, @PathVariable Long id, @ModelAttribute UserMeta userMeta) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        UserPrinciple userPrinciple = customUserPrincipal.getUserPrinciple();
        if (!Objects.equals(id, userPrinciple.getUserMeta().getId())) {
            throw new IllegalArgumentException("UserMeta ID mismatch");
        }

        userPrincipleService.updateUserMeta(userPrinciple, userMeta);

        return "redirect:/user/profile";
    }
}
