package com.example.studentlessonspring.repository;

import com.example.studentlessonspring.entity.Message;
import com.example.studentlessonspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository  extends JpaRepository<Message, Integer> {
    List<Message> findMessageByFromUserIdAndToUserId(int fromUserId, int toUserId);
}
