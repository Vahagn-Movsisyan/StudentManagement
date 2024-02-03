package com.example.studentlessonspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String picName;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private List<Lesson> lessonListAsTeacher;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Lesson> lessonListAsStudent;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}
