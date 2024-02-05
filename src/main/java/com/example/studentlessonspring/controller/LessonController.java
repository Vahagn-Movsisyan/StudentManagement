package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.Lesson;
import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import com.example.studentlessonspring.security.SpringUser;
import com.example.studentlessonspring.service.LessonService;
import com.example.studentlessonspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final UserService userService;

    @GetMapping("/add/lesson/page")
    public String addLessonPage() {
        return "addLesson";
    }

    @GetMapping("/my/lessons")
    public String lessons(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        if (user.getUserType() == UserType.TEACHER) {
            List<Lesson> lessons = lessonService.findLessonByTeacherId(user.getId());
            modelMap.put("lessons", lessons);
        } else if (user.getUserType() == UserType.STUDENT) {
            List<Lesson> lessons = lessonService.findLessonByStudentId(user.getId());
            modelMap.put("lessons", lessons);
        }
        return "myLessons";
    }

    @GetMapping("/update/lesson/page/{id}")
    public String updateLessonPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Lesson> lessonById = lessonService.findById(id);
        if (lessonById.isPresent()) {
            modelMap.addAttribute("lesson", lessonById.get());
        } else {
            return "redirect:/my/lessons";
        }
        return "updateLesson";
    }

    @GetMapping("/all/lessons")
    public String allLessons(ModelMap modelMap) {
        List<Lesson> lessonRepositoryAll = lessonService.findAll();
        modelMap.addAttribute("lessons", lessonRepositoryAll);
        return "lessons";
    }

    @GetMapping("/delete/lessons/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        lessonService.deleteById(id);
        return "redirect:/my/lessons";
    }

    //For User
    @GetMapping("/register/lesson/{id}")
    public String registerLesson(@PathVariable("id") int id, @AuthenticationPrincipal SpringUser springUser) {
        User student = springUser.getUser();
        Optional<Lesson> lessonById = lessonService.findById(id);

        lessonById.ifPresent(lesson -> {
            lesson.setStudent(student);

            User teacher = lesson.getTeacher();
            teacher.getLessonListAsTeacher().add(lesson);
            teacher.getLessonListAsStudent().add(lesson);

            userService.save(student);
            userService.save(teacher);
            lessonService.save(lesson);

        });
        return "redirect:/user/profile";
    }

    @PostMapping("/add/lesson")
    public String addLesson(@ModelAttribute Lesson lesson, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        lesson.setTeacher(user);
        lessonService.save(lesson);
        return "redirect:/my/lessons";
    }

    @PostMapping("/update/lesson")
    public String updateLesson(@ModelAttribute Lesson lesson, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        lesson.setTeacher(user);
        lessonService.save(lesson);
        return "redirect:/my/lessons";
    }
}
