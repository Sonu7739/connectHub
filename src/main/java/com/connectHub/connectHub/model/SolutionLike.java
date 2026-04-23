package com.connectHub.connectHub.model;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"solution_id", "user_id"})
)
public class SolutionLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SolutionLike() {
    }

    public SolutionLike(Solution solution, User user) {
        this.solution = solution;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Solution getSolution() {
        return solution;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
