package com.example.studentlessonspring.repository;

import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findUserByUserType(UserType userType);
}
