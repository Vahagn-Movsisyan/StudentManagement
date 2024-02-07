package com.example.studentlessonspring.controller;

import com.example.studentlessonspring.entity.Message;
import com.example.studentlessonspring.entity.User;
import com.example.studentlessonspring.security.SpringUser;
import com.example.studentlessonspring.service.MessageService;
import com.example.studentlessonspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/chat/page")
    public String chatPage() {
        return "redirect:/receive";
    }

    @GetMapping("/chat/{id}")
    public String chat(@PathVariable("id") int id, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        User fromUser = springUser.getUser();
        Optional<User> byId = userService.findById(id);
        if (byId.isPresent()) {
            User toUser = byId.get();
            modelMap.addAttribute("fromUser", fromUser);
            modelMap.addAttribute("toUser", toUser);
            return "chat";
        }
        return "classmates";
    }

    @GetMapping("/receive")
    public String receive(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        List<Message> sendMessages = user.getReceiveMessages();
        modelMap.addAttribute("receiveMessages",sendMessages);
        return "chat";
    }

    @PostMapping("/send")
    public String send(@RequestParam(name = "fromUser") int fromUserId,
                       @RequestParam(name = "toUser") int toUserId,
                       @RequestParam(name = "message") String messageText) {
        Optional<User> fromUser = userService.findById(fromUserId);
        Optional<User> toUser = userService.findById(toUserId);

        if (fromUser.isPresent() && toUser.isPresent()) {
            Message message = new Message();
            message.setFromUser(fromUser.get());
            message.setToUser(toUser.get());
            message.setMessage(messageText);

            messageService.save(message);

            fromUser.get().getSendMessages().add(message);
            toUser.get().getReceiveMessages().add(message);
            userService.save(fromUser.get());
            userService.save(toUser.get());
        }
        return "redirect:/chat/page";
    }
}
