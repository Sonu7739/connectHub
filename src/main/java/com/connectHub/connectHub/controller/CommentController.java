package com.connectHub.connectHub.controller;

import com.connectHub.connectHub.model.Comment;
import com.connectHub.connectHub.model.Solution;
import com.connectHub.connectHub.model.User;
import com.connectHub.connectHub.repository.CommentRepository;
import com.connectHub.connectHub.repository.SolutionRepository;
import com.connectHub.connectHub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    private final CommentRepository commentRepository;
    private final SolutionRepository solutionRepository;
    private final UserRepository userRepository;

    public CommentController(CommentRepository commentRepository,
                             SolutionRepository solutionRepository,
                             UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.solutionRepository = solutionRepository;
        this.userRepository = userRepository;
    }

    @ResponseBody
    @PostMapping("/api/solution/{solutionId}/comment")
    public Map<String, Object> addCommentAjax(@PathVariable Long solutionId,
                                              @RequestParam("content") String content,
                                              Authentication authentication) {

        Solution solution = solutionRepository.findById(solutionId).orElseThrow();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setSolution(solution);
        comment.setUser(user);

        commentRepository.save(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("solutionId", solutionId);
        response.put("username", user.getUsername());
        response.put("content", comment.getContent());

        return response;
    }
}