package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.Lesson;
import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import com.example.studentlessonspring.repository.LessonRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
    public String addLesson(@ModelAttribute Lesson lesson, HttpSession session) {
        User user = (User) session.getAttribute("user");
        lesson.setUser(user);
        lessonRepository.save(lesson);
        return "redirect:/my/lessons";
    }

    @GetMapping("/my/lessons")
    public String lessons(ModelMap modelMap, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Lesson> lessons = lessonRepository.findLessonByUserId(user.getId());
        modelMap.put("lessons", lessons);
        return "myLessons";
    }

    @GetMapping("/all/lessons")
    public String allLessons(ModelMap modelMap, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() == UserType.STUDENT) {
            modelMap.addAttribute("lessons", lessonRepository.findAll());
            return "lessonsForStudent";
        } else if (user.getUserType() == UserType.TEACHER) {
            modelMap.addAttribute("lessons", lessonRepository.findAll());
            return "lessons";
        }
      return "redirect:/home";
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
    public String updateLesson(@ModelAttribute Lesson lesson, HttpSession session) {
        User user = (User) session.getAttribute("user");
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
