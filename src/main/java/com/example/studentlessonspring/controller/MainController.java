package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.UserType;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class MainController {

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public  @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        File file = new File(uploadDirectory, picName);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }

    @GetMapping("/")
    public String loginPage(ModelMap modelMap) {
        modelMap.addAttribute("userTypes", UserType.values());
        return "login";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
