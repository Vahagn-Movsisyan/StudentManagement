package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.Lesson;
import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import com.example.studentlessonspring.repository.LessonRepository;
import com.example.studentlessonspring.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final LessonRepository lessonRepository;

    @GetMapping("/add/lesson/page")
    public String addLessonPage() {
        return "addLesson";
    }

    @PostMapping("/add/lesson")
    public String addLesson(@ModelAttribute Lesson lesson, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        lesson.setUser(user);
        lessonRepository.save(lesson);
        return "redirect:/my/lessons";
    }

    @GetMapping("/my/lessons")
    public String lessons(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        List<Lesson> lessons = lessonRepository.findLessonByUserId(user.getId());
        modelMap.put("lessons", lessons);
        return "myLessons";
    }

    @GetMapping("/all/lessons")
    public String allLessons(ModelMap modelMap) {
        List<Lesson> lessonRepositoryAll = lessonRepository.findAll();
        modelMap.addAttribute("lessons", lessonRepositoryAll);
        return "lessons";
    }

    @GetMapping("/update/lesson/page/{id}")
    public String updateLessonPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Lesson> lessonById = lessonRepository.findById(id);
        if (lessonById.isPresent()) {
            modelMap.addAttribute("lesson", lessonById.get());
        } else {
            return "redirect:/my/lessons";
        }
        return "updateLesson";
    }

    @PostMapping("/update/lesson")
    public String updateLesson(@ModelAttribute Lesson lesson, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        lesson.setUser(user);
        lessonRepository.save(lesson);
        return "redirect:/my/lessons";
    }

    @GetMapping("/delete/lessons/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        lessonRepository.deleteById(id);
        return "redirect:/my/lessons";
    }
}
