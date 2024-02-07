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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_lesson",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private List<Lesson> lessonListAsStudent;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.EAGER)
    private List<Message> sendMessages;

    @OneToMany(mappedBy = "toUser", fetch = FetchType.EAGER)
    private List<Message> receiveMessages;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}
