package com.example.studentlessonspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "lesson")
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime duration;
    private double price;
    private Date startDate;

    @ManyToOne
    private User user;
}
