package com.ageinghippy.controller.mvc;

import com.ageinghippy.model.entity.UserMeta;
import com.ageinghippy.model.entity.UserPrinciple;
import com.ageinghippy.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        model.addAttribute("username",userPrinciple.getUsername());

        return "redirect:/login";
    }
}
