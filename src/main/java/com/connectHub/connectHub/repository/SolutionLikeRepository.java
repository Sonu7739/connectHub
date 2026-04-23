package com.connectHub.connectHub.repository;

import com.connectHub.connectHub.model.Solution;
import com.connectHub.connectHub.model.SolutionLike;
import com.connectHub.connectHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionLikeRepository extends JpaRepository<SolutionLike, Long> {
    boolean existsBySolutionAndUser(Solution solution, User user);
    long countBySolution(Solution solution);
    void deleteBySolutionAndUser(Solution solution, User user);

    void deleteBySolution(Solution solution);
}