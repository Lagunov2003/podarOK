package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/sentMessages")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> getSentMessages(@RequestParam(required = false) String receiverEmail) {
        try {
            List<MessageDto> messages = chatService.getSentChatMessages(receiverEmail);
            return ResponseEntity.ok(messages);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> getMessages(@RequestParam(required = false) String senderEmail) {
        try {
            List<MessageDto> messages = chatService.getChatMessages(senderEmail);
            return ResponseEntity.ok(messages);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/newMessages")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> getNewMessages(@RequestParam(required = false) String senderEmail) {
        try {
            List<MessageDto> messages = chatService.getNotReadChatMessages(senderEmail);
            return ResponseEntity.ok(messages);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        try {
            MessageDto savedMessage = chatService.sendMessage(messageDto);
            return ResponseEntity.ok(savedMessage);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
