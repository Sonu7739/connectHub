package com.connectHub.connectHub.controller;

import com.connectHub.connectHub.model.Problem;
import com.connectHub.connectHub.model.Solution;
import com.connectHub.connectHub.model.User;
import com.connectHub.connectHub.repository.ProblemRepository;
import com.connectHub.connectHub.repository.SolutionRepository;
import com.connectHub.connectHub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final SolutionRepository solutionRepository;

    public ProfileController(UserRepository userRepository,
                             ProblemRepository problemRepository,
                             SolutionRepository solutionRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.solutionRepository = solutionRepository;
    }

    @GetMapping("/profile")
    public String myProfile(Authentication authentication, Model model) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        List<Problem> userProblems = problemRepository.findByUser(user);
        List<Solution> userSolutions = solutionRepository.findByUser(user);

        model.addAttribute("userData", user);
        model.addAttribute("isOwner", true);
        model.addAttribute("problemCount", userProblems.size());
        model.addAttribute("solutionCount", userSolutions.size());
        model.addAttribute("userProblems", userProblems);
        model.addAttribute("userSolutions", userSolutions);

        return "profile";
    }

    @GetMapping("/profile/{username}")
    public String userProfile(@PathVariable String username,
                              Authentication authentication,
                              Model model) {
        User user = userRepository.findByUsername(username).orElseThrow();

        List<Problem> userProblems = problemRepository.findByUser(user);
        List<Solution> userSolutions = solutionRepository.findByUser(user);

        boolean isOwner = authentication != null && authentication.getName().equals(username);

        model.addAttribute("userData", user);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("problemCount", userProblems.size());
        model.addAttribute("solutionCount", userSolutions.size());
        model.addAttribute("userProblems", userProblems);
        model.addAttribute("userSolutions", userSolutions);

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String bio,
                                @RequestParam String college,
                                @RequestParam String skills,
                                @RequestParam(required = false) String profileImage,
                                Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        user.setBio(bio);
        user.setCollege(college);
        user.setSkills(skills);
        user.setProfileImage(profileImage);

        userRepository.save(user);

        return "redirect:/profile";
    }
}