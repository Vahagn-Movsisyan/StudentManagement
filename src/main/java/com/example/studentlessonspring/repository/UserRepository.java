package com.example.studentlessonspring.repository;

import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findUserByUserType(UserType userType);
}
