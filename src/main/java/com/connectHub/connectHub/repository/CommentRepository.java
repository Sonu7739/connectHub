package com.connectHub.connectHub.repository;

import com.connectHub.connectHub.model.Comment;
import com.connectHub.connectHub.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySolutionOrderByCreatedAtAsc(Solution solution);
}
