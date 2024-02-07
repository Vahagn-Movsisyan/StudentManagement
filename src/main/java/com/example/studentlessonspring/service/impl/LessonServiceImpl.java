package com.example.studentlessonspring.service.impl;

import com.example.studentlessonspring.entity.Lesson;
import com.example.studentlessonspring.repository.LessonRepository;
import com.example.studentlessonspring.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Optional<Lesson> findById(int id) {
        return lessonRepository.findById(id);
    }

    @Override
    public List<Lesson> findLessonByTeacherId(int id) {
        return lessonRepository.findLessonByTeacherId(id);
    }

    @Override
    public List<Lesson> findLessonByStudentId(int id) {
        return lessonRepository.findLessonByStudentsId(id);
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        lessonRepository.deleteById(id);
    }
}
