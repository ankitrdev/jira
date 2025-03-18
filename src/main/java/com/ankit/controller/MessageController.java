package com.ankit.controller;

import com.ankit.modal.Chat;
import com.ankit.modal.Message;
import com.ankit.modal.User;
import com.ankit.request.CreateMessageRequest;
import com.ankit.service.MessageService;
import com.ankit.service.ProjectService;
import com.ankit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestBody CreateMessageRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        if (user == null) throw new Exception("user not found !!!");
        Chat chats = projectService.getProjectById(request.getProjectId()).getChat();
        if (chats == null) throw new Exception("No chats available");
        Message sentMessage = messageService.sendMessage(request.getSenderId(), request.getProjectId(), request.getContent());
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long projectId)
        throws Exception{
        List<Message> messages = messageService.getMessageByProjectId(projectId);
        return ResponseEntity.ok(messages);
    }
}
