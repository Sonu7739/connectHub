package com.connectHub.connectHub.controller;

import com.connectHub.connectHub.model.Solution;
import com.connectHub.connectHub.model.SolutionLike;
import com.connectHub.connectHub.model.User;
import com.connectHub.connectHub.repository.SolutionLikeRepository;
import com.connectHub.connectHub.repository.SolutionRepository;
import com.connectHub.connectHub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SolutionController {

    private final SolutionRepository solutionRepository;
    private final SolutionLikeRepository solutionLikeRepository;
    private final UserRepository userRepository;

    public SolutionController(SolutionRepository solutionRepository,
                              SolutionLikeRepository solutionLikeRepository,
                              UserRepository userRepository) {
        this.solutionRepository = solutionRepository;
        this.solutionLikeRepository = solutionLikeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @ResponseBody
    @PostMapping("/api/solution/{solutionId}/like")
    public Map<String, Object> toggleLikeAjax(@PathVariable Long solutionId, Authentication authentication) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        boolean liked;

        if (solutionLikeRepository.existsBySolutionAndUser(solution, user)) {
            solutionLikeRepository.deleteBySolutionAndUser(solution, user);
            liked = false;
        } else {
            solutionLikeRepository.save(new SolutionLike(solution, user));
            liked = true;
        }

        long likeCount = solutionLikeRepository.countBySolution(solution);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        response.put("likeCount", likeCount);
        response.put("solutionId", solutionId);

        return response;
    }

    @GetMapping("/solution/{id}/edit")
    public String editSolutionPage(@PathVariable Long id, Model model, Authentication authentication) {
        Solution solution = solutionRepository.findById(id).orElseThrow();

        if (!solution.getUser().getUsername().equals(authentication.getName())) {
            return "redirect:/home";
        }

        model.addAttribute("solution", solution);
        return "edit-solution";
    }

    @PostMapping("/solution/{id}/edit")
    public String updateSolution(@PathVariable Long id,
                                 @RequestParam("content") String content,
                                 Authentication authentication) {
        Solution solution = solutionRepository.findById(id).orElseThrow();

        if (!solution.getUser().getUsername().equals(authentication.getName())) {
            return "redirect:/home";
        }

        solution.setContent(content);
        solutionRepository.save(solution);

        return "redirect:/problem/" + solution.getProblem().getId() + "#solution-" + id;
    }

    @Transactional
    @PostMapping("/solution/{id}/delete")
    public String deleteSolution(@PathVariable Long id, Authentication authentication) {
        Solution solution = solutionRepository.findById(id).orElseThrow();

        if (!solution.getUser().getUsername().equals(authentication.getName())) {
            return "redirect:/home";
        }

        Long problemId = solution.getProblem().getId();

        solutionLikeRepository.deleteBySolution(solution);
        solutionRepository.delete(solution);

        return "redirect:/problem/" + problemId;
    }
}