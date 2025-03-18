package com.prometheus.brainbash.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name="quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    private String description;
    
    private String image;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions = new HashSet<>();
    
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    
    @ManyToMany
    @JoinTable(
        name = "quiz_developers",
        joinColumns = @JoinColumn(name = "quiz_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> developers = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;
    
    @Enumerated(EnumType.STRING)
    private DifficultyRating difficultyRating;
    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    private List<UserQuizScore> scores = new ArrayList<>();

    
    
    // Extra Methods
    public void addQuestion(Question question) {
    	questions.add(question);
    }
    
}
