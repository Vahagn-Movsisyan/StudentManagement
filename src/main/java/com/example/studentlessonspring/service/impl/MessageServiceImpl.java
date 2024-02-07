package com.example.studentlessonspring.service.impl;

import com.example.studentlessonspring.entity.Message;
import com.example.studentlessonspring.repository.MessageRepository;
import com.example.studentlessonspring.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    @Override
    public Message save(Message message) {
        message.setLocalDateTime(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> findMessageByFromUserId(int id) {
        return messageRepository.findMessageByFromUserId(id);
    }

    @Override
    public Optional<Message> findMessageByToUserId(int id) {
        return messageRepository.findMessageByToUserId(id);
    }
}
