package com.example.studentlessonspring.service;


import com.example.studentlessonspring.entity.Message;

import java.util.List;

public interface MessageService {
    Message save(Message message);
    List<Message> findMessageByToUserIdAndFromUserId(int fromUserId, int toUserId);
}
