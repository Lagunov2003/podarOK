package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import ru.uniyar.podarok.dtos.Dialog;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.ChatService;

import java.util.List;

/**
 * Контроллер для работы с сообщениями в чате.
 * Все методы доступны только пользователям с ролью администратора (`ROLE_ADMIN`) или пользователя (`ROLE_USER`).
 */
@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {
    private ChatService chatService;

    /**
     * Получить отправленные сообщения.
     *
     * @param receiverEmail email получателя для фильтрации сообщений.
     * @return HTTP-ответ с отправленными сообщениями.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если текущий пользователь или получатель не найден.
     */
    @GetMapping("/sentMessages")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> getSentMessages(@RequestParam(required = false) String receiverEmail)
            throws UserNotAuthorizedException, UserNotFoundException {
        List<MessageDto> messages = chatService.getSentChatMessages(receiverEmail);
        return ResponseEntity.ok(messages);
    }

    /**
     * Получить сообщения от определённого отправителя.
     *
     * @param senderEmail email отправителя для фильтрации сообщений.
     * @return HTTP-ответ с сообщениями.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если текущий пользователь или отправитель не найден.
     */
    @GetMapping("/receivedMessages")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> getReceivedMessages(@RequestParam(required = false) String senderEmail)
            throws UserNotAuthorizedException, UserNotFoundException {
        List<MessageDto> messages = chatService.getReceivedChatMessages(senderEmail);
        return ResponseEntity.ok(messages);
    }

    /**
     * Получить диалог с определённым отправителем.
     *
     * @param userEmail email отправителя для фильтрации сообщений.
     * @return HTTP-ответ с сообщениями.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если текущий пользователь или отправитель не найден.
     */
    @GetMapping("/messages")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> getMessages(@RequestParam String userEmail)
            throws UserNotAuthorizedException, UserNotFoundException {
        List<MessageDto> messages = chatService.getSentAndReceivedChatMessages(userEmail);
        return ResponseEntity.ok(messages);
    }

    /**
     * Получить новые (непрочитанные) сообщения от определённого отправителя.
     *
     * @param senderEmail (опционально) Email отправителя для фильтрации новых сообщений.
     * @return HTTP-ответ с новыми сообщениями.
     * @throws UserNotFoundException если текущий пользователь или отправитель не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    @GetMapping("/newMessages")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> getNewMessages(@RequestParam(required = false) String senderEmail)
            throws UserNotAuthorizedException, UserNotFoundException {
        List<MessageDto> messages = chatService.getNotReadChatMessages(senderEmail);
        return ResponseEntity.ok(messages);
    }

    /**
     * Отправить сообщение.
     *
     * @param messageDto объект сообщения для отправки.
     * @return HTTP-ответ с отправляемым сообщением.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если текущий пользователь или получатель не найден.
     */
    @PostMapping("/send")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_USER')")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto)
            throws UserNotAuthorizedException, UserNotFoundException {
        MessageDto savedMessage = chatService.sendMessage(messageDto);
        return ResponseEntity.ok(savedMessage);
    }

    /**
     * Получить список всех диалогов.
     *
     * @return HTTP-ответ с диалогами всех пользователей с администратором.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если текущий пользователь не найден.
     */
    @GetMapping("/allDialogs")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> showAllDialogs() throws UserNotFoundException, UserNotAuthorizedException {
        List<Dialog> dialogs = chatService.getAllDialogs();
        return ResponseEntity.ok(dialogs);
    }
}
