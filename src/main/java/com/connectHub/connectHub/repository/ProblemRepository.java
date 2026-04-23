//package com.connectHub.connectHub.repository;
//
//import com.connectHub.connectHub.model.Problem;
//import com.connectHub.connectHub.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;
//
//public interface ProblemRepository extends JpaRepository<Problem, Long> {
//    List<Problem> findByUser(User user);
//}


package com.connectHub.connectHub.repository;

import com.connectHub.connectHub.model.Problem;
import com.connectHub.connectHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByUser(User user);

    List<Problem> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    List<Problem> findByTags_NameIgnoreCase(String tagName);
}