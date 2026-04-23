package com.connectHub.connectHub.controller;

import com.connectHub.connectHub.model.User;
import com.connectHub.connectHub.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final UserRepository userRepository;

    public SearchController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<User> users = userRepository.findByUsernameContainingIgnoreCase(keyword);
            model.addAttribute("users", users);
        }
        model.addAttribute("keyword", keyword);
        return "search";
    }
}