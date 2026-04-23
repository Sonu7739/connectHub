//package com.connectHub.connectHub.repository;
//
//import com.connectHub.connectHub.model.Solution;
//import org.springframework.data.jpa.repository.JpaRepository;
//import com.connectHub.connectHub.model.User;
//import com.connectHub.connectHub.model.Problem;
//
//
//import java.util.List;
//public interface SolutionRepository extends JpaRepository<Solution, Long> {
//    List<Solution> findByProblem(Problem problem);
//    List<Solution> findByUser(User user);
//}
package com.connectHub.connectHub.repository;

import com.connectHub.connectHub.model.Problem;
import com.connectHub.connectHub.model.Solution;
import com.connectHub.connectHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByProblem(Problem problem);
    List<Solution> findByUser(User user);
    long countByUser(User user);
}