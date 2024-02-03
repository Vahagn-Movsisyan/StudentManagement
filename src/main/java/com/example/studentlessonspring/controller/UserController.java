package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.Lesson;
import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import com.example.studentlessonspring.repository.LessonRepository;
import com.example.studentlessonspring.repository.UserRepository;
import com.example.studentlessonspring.security.SpringUser;
import com.example.studentlessonspring.util.MultipartUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/delete/picture/")
    public String deletePicture(@RequestParam("id") int id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            String picName = user.getPicName();
            if (picName != null) {
                user.setPicName(null);
                userRepository.save(user);
                File file = new File(uploadDirectory, picName);
                if (file.exists()) {
                    file.delete();
                }
            }
            return "redirect:/update/user/page/" + user.getId();
        } else {
            return "redirect:/userProfile";
        }
    }

    @GetMapping("/students")
    public String students(ModelMap modelMap) {
        modelMap.addAttribute("usersByType", userRepository.findUserByUserType(UserType.STUDENT));
        return "students";
    }

    @GetMapping("/teachers")
    public String teachers(ModelMap modelMap) {
        modelMap.addAttribute("usersByType", userRepository.findUserByUserType(UserType.TEACHER));
        return "teachers";
    }

    @GetMapping("/login/page")
    public String loginPage() {
        return "redirect:/login";
    }

    @GetMapping("/user/profile")
    public String usersProfile() {
        return "userProfile";
    }

    @GetMapping("/my/students")
    public String myStudents(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        if (user.getUserType() == UserType.TEACHER) {
            List<User> studentsList = new ArrayList<>();
            for (Lesson lesson : lessonRepository.findLessonByTeacherId(user.getId())) {
                User student = lesson.getStudent();
                if (student != null) {
                    studentsList.add(student);
                }
            }
            modelMap.addAttribute("students", studentsList);
            return "myStudents";
        }
        return "redirect:/teachers";
    }

    @GetMapping("/update/user/page/{id}")
    public String updateUser(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<User> userById = userRepository.findById(id);
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
        return "register";
    }

    @PostMapping("/user/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam String confirmPassword,
                               @RequestParam("picture") MultipartFile multipartFile) {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
            return "redirect:/register?msg=Email is already in use";
        } else if (!user.getPassword().equals(confirmPassword)) {
            return "redirect:/register?msg=Passwords do not match";
        }

        try {
            MultipartUtil.processImageUpload(user, multipartFile, uploadDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/user/login?msg=Invalid picture please try again";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @PostMapping("/update/user")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam("picture")
                             MultipartFile multipartFile) throws IOException {
        MultipartUtil.processImageUpload(user, multipartFile, uploadDirectory);
        userRepository.save(user);
        return "redirect:/user/profile";
    }
}
