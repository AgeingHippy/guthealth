package com.ageinghippy.controller.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class ApplicationViewController {

    @GetMapping(value = {"/","/home"})
    public String gotoIndex(Model model) {
        return "redirect:/index";
    }

    @GetMapping(value = {"/index"})
    public String showIndex(Model model) {
        return "/index";
    }


}
