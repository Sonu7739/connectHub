package com.connectHub.connectHub.controller;

import com.connectHub.connectHub.model.Problem;
import com.connectHub.connectHub.model.Tag;
import com.connectHub.connectHub.model.User;
import com.connectHub.connectHub.repository.ProblemRepository;
import com.connectHub.connectHub.repository.TagRepository;
import com.connectHub.connectHub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public HomeController(ProblemRepository problemRepository,
                          UserRepository userRepository,
                          TagRepository tagRepository) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Problem> problems;

        if (keyword != null && !keyword.trim().isEmpty()) {
            problems = problemRepository
                    .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        } else {
            problems = problemRepository.findAll();
        }

        model.addAttribute("problem", new Problem());
        model.addAttribute("problems", problems);
        model.addAttribute("keyword", keyword);

        return "home";
    }

    @ResponseBody
    @PostMapping("/api/problem/add")
    public Map<String, Object> addProblemAjax(@RequestParam("title") String title,
                                              @RequestParam("description") String description,
                                              @RequestParam(value = "tagNames", required = false) String tagNames,
                                              Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        Problem problem = new Problem();
        problem.setTitle(title);
        problem.setDescription(description);
        problem.setUser(user);

        Set<Tag> tags = new HashSet<>();

        if (tagNames != null && !tagNames.trim().isEmpty()) {
            String[] splitTags = tagNames.split(",");

            for (String tagName : splitTags) {
                String cleaned = tagName.trim();

                if (!cleaned.isEmpty()) {
                    Tag tag = tagRepository.findByName(cleaned)
                            .orElseGet(() -> tagRepository.save(new Tag(cleaned)));
                    tags.add(tag);
                }
            }
        }

        problem.setTags(tags);
        problemRepository.save(problem);

        Map<String, Object> response = new HashMap<>();
        response.put("problemId", problem.getId());
        response.put("title", problem.getTitle());
        response.put("description", problem.getDescription());
        response.put("username", user.getUsername());

        List<String> tagList = problem.getTags().stream()
                .map(Tag::getName)
                .toList();
        response.put("tags", tagList);

        return response;
    }
}