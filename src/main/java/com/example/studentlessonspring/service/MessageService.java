package com.example.studentlessonspring.service;


import com.example.studentlessonspring.entity.Message;

import java.util.Optional;

public interface MessageService {
    Message save(Message message);
    Optional<Message> findMessageByFromUserId(int id);
    Optional<Message> findMessageByToUserId(int id);
}
