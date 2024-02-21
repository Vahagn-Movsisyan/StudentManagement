package com.example.studentlessonspring.service.impl;

import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import com.example.studentlessonspring.exceptions.EmailIsPresentException;
import com.example.studentlessonspring.exceptions.PasswordNotMuchException;
import com.example.studentlessonspring.repository.UserRepository;
import com.example.studentlessonspring.service.UserService;
import com.example.studentlessonspring.util.MultipartUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @Override
    public User register(User user, MultipartFile multipartFile, String confirmPassword) throws IOException, EmailIsPresentException, PasswordNotMuchException {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
            throw new EmailIsPresentException("Email is already in use");
        } else if (!user.getPassword().equals(confirmPassword)) {
            throw new PasswordNotMuchException("Passwords do not match");
        }

        MultipartUtil.processImageUpload(user, multipartFile, uploadDirectory);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user, MultipartFile multipartFile) throws IOException {
        MultipartUtil.processImageUpload(user, multipartFile, uploadDirectory);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findUserByUserType(UserType userType) {
        return userRepository.findUserByUserType(userType);
    }

    @Override
    public void deletePicture(int id) {
        Optional<User> byId = userRepository.findById(id);
        byId.ifPresent(user -> {
            String picName = user.getPicName();

            if (picName != null) {
                user.setPicName(null);
                userRepository.save(user);
            }

            File file = new File(uploadDirectory, picName);
            if (file.exists()) {
                file.delete();
            }
        });
    }
}
