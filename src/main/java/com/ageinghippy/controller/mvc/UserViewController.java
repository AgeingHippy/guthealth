package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createNewUser(@ModelAttribute UserPrinciple userPrinciple,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            userPrinciple = userPrincipleService.createPasswordUser(userPrinciple);

            model.addAttribute("username", userPrinciple.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "User registered successfully.");
            return "redirect:/login";
        } catch (Exception e) {
            String errorMessage;
            if (e.getClass() == DataIntegrityViolationException.class) {
                errorMessage = "User registration failed. Username not unique";
            } else {
                errorMessage = e.getClass().getSimpleName() + " - " + "User registration failed.";
            }
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            userPrinciple.setPassword(null);
            redirectAttributes.addFlashAttribute("user", userPrinciple);
            return "redirect:/user/new";
        }

    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {

        model.addAttribute("userPrinciple", (UserPrinciple) authentication.getPrincipal());

        return "profile.html";
    }

    @PostMapping("/userMeta/{id}")
    public String updateProfile(Authentication authentication, @PathVariable Long id, @ModelAttribute UserMeta userMeta) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        if (!Objects.equals(id, userPrinciple.getUserMeta().getId())) {
            throw new IllegalArgumentException("UserMeta ID mismatch");
        }

        userPrincipleService.updateUserMeta(userPrinciple, userMeta);

        return "redirect:/user/profile";
    }

}
