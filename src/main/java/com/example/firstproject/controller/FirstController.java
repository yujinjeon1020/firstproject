package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

    @GetMapping("/hi")
    public String niceToMeetYou(Model model) {
        model.addAttribute("username", "유진");
        return "greetings"; // /templates/greetings.mustache 호출
    }
    
    @GetMapping("/bye")
    public String seeYouNext(Model model) {
        model.addAttribute("username", "전유진");
        return "goodbye";   //  /templates/goodbye.mustache 호출
    }
}
