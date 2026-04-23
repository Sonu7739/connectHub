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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProblemController {

    private final ProblemRepository problemRepository;
    private final SolutionRepository solutionRepository;
    private final UserRepository userRepository;

    public ProblemController(ProblemRepository problemRepository,
                             SolutionRepository solutionRepository,
                             UserRepository userRepository) {
        this.problemRepository = problemRepository;
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/problem/{id}")
    public String viewProblemDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Problem problem = problemRepository.findById(id).orElseThrow();
        List<Solution> solutions = solutionRepository.findByProblem(problem);

        model.addAttribute("problemData", problem);
        model.addAttribute("solutions", solutions);

        String currentUsername = authentication != null ? authentication.getName() : null;
        model.addAttribute("currentUsername", currentUsername);

        return "problem-details";
    }

    @ResponseBody
    @PostMapping("/api/problem/{problemId}/solution/add")
    public Map<String, Object> addSolutionAjax(@PathVariable Long problemId,
                                               @RequestParam("content") String content,
                                               Authentication authentication) {

        Problem problem = problemRepository.findById(problemId).orElseThrow();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        Solution solution = new Solution();
        solution.setContent(content);
        solution.setProblem(problem);
        solution.setUser(user);

        solutionRepository.save(solution);

        Map<String, Object> response = new HashMap<>();
        response.put("solutionId", solution.getId());
        response.put("problemId", problemId);
        response.put("content", solution.getContent());
        response.put("username", user.getUsername());
        response.put("bestAnswer", solution.isBestAnswer());

        return response;
    }

    @GetMapping("/problem/{id}/edit")
    public String editProblemPage(@PathVariable Long id, Model model, Authentication authentication) {
        Problem problem = problemRepository.findById(id).orElseThrow();

        if (!problem.getUser().getUsername().equals(authentication.getName())) {
            return "redirect:/home";
        }

        model.addAttribute("problem", problem);
        return "edit-problem";
    }

    @PostMapping("/problem/{id}/edit")
    public String updateProblem(@PathVariable Long id,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                Authentication authentication) {
        Problem problem = problemRepository.findById(id).orElseThrow();

        if (!problem.getUser().getUsername().equals(authentication.getName())) {
            return "redirect:/home";
        }

        problem.setTitle(title);
        problem.setDescription(description);
        problemRepository.save(problem);

        return "redirect:/problem/" + id;
    }

    @PostMapping("/problem/{id}/delete")
    public String deleteProblem(@PathVariable Long id, Authentication authentication) {
        Problem problem = problemRepository.findById(id).orElseThrow();

        if (!problem.getUser().getUsername().equals(authentication.getName())) {
            return "redirect:/home";
        }

        problemRepository.delete(problem);
        return "redirect:/home";
    }
}