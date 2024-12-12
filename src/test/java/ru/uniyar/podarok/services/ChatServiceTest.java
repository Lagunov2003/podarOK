package ru.uniyar.podarok.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.MessageRepository;
import ru.uniyar.podarok.utils.MessageDtoConverter;


@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @Mock
    private MessageDtoConverter messageDtoConverter;

    @InjectMocks
    private ChatService chatService;


    @Test
    public void ChatService_GetSentChatMessages_ReturnsMessageList() throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(currentUser);
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findBySenderId(any(), any())).thenReturn(messages);
        MessageDto messageDto1 = new MessageDto();
        MessageDto messageDto2 = new MessageDto();
        when(messageDtoConverter.convertToMessageDto(any())).thenReturn(messageDto1, messageDto2);

        List<MessageDto> result = chatService.getSentChatMessages(null);

        assertEquals(2, result.size());
        assertEquals(messageDto1, result.get(0));
        assertEquals(messageDto2, result.get(1));
    }

    @Test
    public void ChatService_GetChatMessages_ReturnsMessageList() throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(currentUser);
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findByReceiverId(any(), any())).thenReturn(messages);
        MessageDto messageDto1 = new MessageDto();
        MessageDto messageDto2 = new MessageDto();
        when(messageDtoConverter.convertToMessageDto(any())).thenReturn(messageDto1, messageDto2);

        List<MessageDto> result = chatService.getChatMessages(null);

        assertEquals(2, result.size());
        assertEquals(messageDto1, result.get(0));
        assertEquals(messageDto2, result.get(1));
    }

    @Test
    public void ChatService_SendMessage_ReturnsMessageList() throws UserNotFoundException, UserNotAuthorizedException {
        User sender = new User();
        sender.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(sender);
        User receiver = new User();
        receiver.setId(2L);
        when(userService.findByEmail(any())).thenReturn(receiver);
        MessageDto messageDto = new MessageDto();
        messageDto.setReceiverEmail("receiver@example.com");
        messageDto.setContent("Hello");
        Message message = new Message();
        when(messageDtoConverter.convertToMessage(any(), any(), any())).thenReturn(message);
        when(messageRepository.save(any())).thenReturn(message);
        MessageDto savedMessageDto = new MessageDto();
        when(messageDtoConverter.convertToMessageDto(any())).thenReturn(savedMessageDto);

        MessageDto result = chatService.sendMessage(messageDto);

        assertEquals(savedMessageDto, result);
    }

    @Test
    public void ChatService_GetNotReadChatMessages_ReturnsMessageList() throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(currentUser);
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findByReceiverIdAndIsRead(any(), any(), any())).thenReturn(messages);
        MessageDto messageDto1 = new MessageDto();
        MessageDto messageDto2 = new MessageDto();
        when(messageDtoConverter.convertToMessageDto(any())).thenReturn(messageDto1, messageDto2);

        List<MessageDto> result = chatService.getNotReadChatMessages(null);

        assertEquals(2, result.size());
        assertEquals(messageDto1, result.get(0));
        assertEquals(messageDto2, result.get(1));
    }

    @Test
    public void ChatService_GetSentChatMessages_ThrowsUserNotFoundException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.getSentChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetSentChatMessages_ThrowsUserNotAuthorizedException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.getSentChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetChatMessages_ThrowsUserNotFoundException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.getChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetChatMessages_ThrowsUserNotAuthorizedException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.getChatMessages(null);
        });
    }

    @Test
    public void ChatService_SendMessage_ThrowsUserNotFoundException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.sendMessage(new MessageDto());
        });
    }

    @Test
    public void ChatService_SendMessage_ThrowsUserNotAuthorizedException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.sendMessage(new MessageDto());
        });
    }

    @Test
    public void ChatService_GetNotReadChatMessages_ThrowsUserNotFoundException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.getNotReadChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetNotReadChatMessages_ThrowsUserNotAuthorizedException() throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.getNotReadChatMessages(null);
        });
    }
}
