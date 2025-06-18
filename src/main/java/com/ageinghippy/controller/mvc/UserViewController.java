package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
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
                log.error("createNewUser", e);
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

    @GetMapping({"","/", "/{id}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showUserMaintenanceView(Model model, @PathVariable(required = false) Long id) {
        if (!model.containsAttribute("userPrinciple") ) {
            UserPrinciple userPrinciple;
            if (id != null) {
                userPrinciple = userPrincipleService.getUserPrincipleById(id);
            } else {
                userPrinciple = UserPrinciple.builder().userMeta(UserMeta.builder().build()).build();
            }
            model.addAttribute("userPrinciple", userPrinciple);
        }

        return "user-maintenance";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateUserDetails(@PathVariable Long id,
                                    @ModelAttribute UserPrinciple userPrinciple,
                                    RedirectAttributes redirectAttributes) {
        if (!Objects.equals(id, userPrinciple.getId())) {
            throw new IllegalArgumentException("Principle ID mismatch");
        }

        try {
            userPrincipleService.updateUserPrinciple(id, userPrinciple);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully.");
            return "redirect:/user/" + id;
        } catch (Exception e) {
            String errorMessage = e.getClass().getSimpleName() + " - " + "User updated failed.";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("userPrinciple", userPrinciple);
            return "redirect:/user/" + id;
        }
    }

}
