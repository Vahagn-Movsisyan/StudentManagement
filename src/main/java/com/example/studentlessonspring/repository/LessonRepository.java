package com.example.studentlessonspring.repository;

import com.example.studentlessonspring.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findLessonByTeacherId(int userId);
    List<Lesson> findLessonByStudentId(int userId);
}
