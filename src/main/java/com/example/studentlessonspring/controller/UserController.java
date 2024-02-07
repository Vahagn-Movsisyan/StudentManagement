package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.Lesson;
import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import com.example.studentlessonspring.exceptions.EmailIsPresentException;
import com.example.studentlessonspring.exceptions.PasswordNotMuchException;
import com.example.studentlessonspring.security.SpringUser;
import com.example.studentlessonspring.service.LessonService;
import com.example.studentlessonspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LessonService lessonService;

    @GetMapping("/delete/picture/")
    public String deletePicture(@RequestParam("id") int id) {
        userService.deletePicture(id);
        return "redirect:/update/user/page/" + id;
    }

    @GetMapping("/students")
    public String students(ModelMap modelMap) {
        modelMap.addAttribute("usersByType", userService.findUserByUserType(UserType.STUDENT));
        return "students";
    }

    @GetMapping("/teachers")
    public String teachers(ModelMap modelMap) {
        modelMap.addAttribute("usersByType", userService.findUserByUserType(UserType.TEACHER));
        return "teachers";
    }

    @GetMapping("/login/page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/profile")
    public String usersProfile() {
        return "userProfile";
    }

    @GetMapping("/my/students")
    public String myStudents(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        Set<User> studentsSet = new HashSet<>();
        if (user.getUserType() == UserType.TEACHER) {
            for (Lesson lesson : lessonService.findLessonByTeacherId(user.getId())) {
                if (lesson != null) {
                    studentsSet.addAll(lesson.getStudents());
                }
            }
            modelMap.addAttribute("students", studentsSet);
            return "myStudents";
        }
        return "redirect:/teachers";
    }

    @GetMapping("/update/user/page/{id}")
    public String updateUser(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<User> userById = userService.findById(id);
        if (userById.isPresent()) {
            modelMap.addAttribute("user", userById.get());
        } else {
            return "redirect:/userProfile";
        }
        return "updateUser";
    }

    @GetMapping("/register")
    public String registerPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        modelMap.addAttribute("userTypes", UserType.values());
        return "login";
    }

    @PostMapping("/user/register")
    public String registerUser(
            @ModelAttribute User user,
            @RequestParam String confirmPassword,
            @RequestParam("picture") MultipartFile multipartFile) {

        try {
            userService.register(user, multipartFile, confirmPassword);
        } catch (IOException e) {
            return "redirect:/user/login?msg=Invalid picture please try again";
        } catch (EmailIsPresentException e) {
            return "redirect:/register?msg=Email is already in use";
        } catch (PasswordNotMuchException e) {
            return "redirect:/register?msg=Passwords do not match";
        }
        return "redirect:/";
    }

    @PostMapping("/update/user")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam("picture")
                             MultipartFile multipartFile) throws IOException {
        userService.update(user, multipartFile);
        return "redirect:/user/profile";
    }
}
