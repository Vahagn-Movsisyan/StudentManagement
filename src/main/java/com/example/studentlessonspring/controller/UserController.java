package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.Lesson;
import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import com.example.studentlessonspring.repository.LessonRepository;
import com.example.studentlessonspring.repository.UserRepository;
import com.example.studentlessonspring.util.MultipartUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping("/user/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpSession session) {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            User getUserByEmail = userByEmail.get();
            if (getUserByEmail.getEmail().equals(user.getEmail()) && getUserByEmail.getPassword().equals(user.getPassword())) {
                session.setAttribute("user", getUserByEmail);
                return "redirect:/home";
            }
        }
        else {
            model.addAttribute("errorLogin", "Invalid email or password");
            return "redirect:/";
        }
        return "redirect:/";
    }

    @PostMapping("/user/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam String confirmPassword,
                               @RequestParam("picture") MultipartFile multipartFile,
                               Model model,
                               HttpSession session) {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
            model.addAttribute("errorRegister", "Email is already in use");
            return "redirect:/";
        } else if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("errorRegister", "Passwords do not match");
            return "redirect:/";
        }

        try {
            MultipartUtil.processImageUpload(user, multipartFile, uploadDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorRegister", "Error uploading profile picture");
            return "redirect:/";
        }

        userRepository.save(user);
        session.setAttribute("user", user);
        model.addAttribute("currentUserType", user.getUserType());
        return "redirect:/home";
    }

    @GetMapping("/user/profile")
    public String usersProfile(ModelMap modelMap, HttpSession session) {
        User user = (User) session.getAttribute("user");
        user.setId(user.getId());
        Optional<User> userById = userRepository.findById(user.getId());
        if (userById.isPresent()) {
            modelMap.addAttribute("user", userById.get());
        } else {
            return "redirect:/users";
        }
        return "userProfile";
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

    @PostMapping("/update/user")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam("picture")
                             MultipartFile multipartFile) throws IOException {
        MultipartUtil.processImageUpload(user, multipartFile, uploadDirectory);
        userRepository.save(user);
        return "redirect:/user/profile";
    }

    @GetMapping("/register/lesson/{id}")
    public String registerLesson(@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Lesson> lessonById = lessonRepository.findById(id);
        if (lessonById.isPresent()) {
            List<Lesson> lessons = new ArrayList<>();
            lessons.add(lessonById.get());
            user.setLesson(lessons);
            userRepository.save(user);
        }
        return "redirect:/user/profile";
    }
}
