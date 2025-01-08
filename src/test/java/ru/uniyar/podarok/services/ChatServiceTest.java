package ru.uniyar.podarok.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.MessageRepository;
import ru.uniyar.podarok.utils.converters.MessageDtoConverter;


@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @Mock
    private MessageDtoConverter messageDtoConverter;

    @InjectMocks
    @Spy
    private ChatService chatService;


    @Test
    public void ChatService_GetSentChatMessages_ReturnsMessageList()
            throws UserNotFoundException, UserNotAuthorizedException {
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
    public void ChatService_GetSentChatMessages_ThrowsUserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.getSentChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetSentChatMessages_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.getSentChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetReceivedChatMessages_ReturnsMessageList()
            throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(currentUser);
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findByReceiverId(any(), any())).thenReturn(messages);
        MessageDto messageDto1 = new MessageDto();
        MessageDto messageDto2 = new MessageDto();
        when(messageDtoConverter.convertToMessageDto(any())).thenReturn(messageDto1, messageDto2);

        List<MessageDto> result = chatService.getReceivedChatMessages(null);

        assertEquals(2, result.size());
        assertEquals(messageDto1, result.get(0));
        assertEquals(messageDto2, result.get(1));
    }

    @Test
    public void ChatService_GetReceivedChatMessages_ThrowsUserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.getReceivedChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetReceivedChatMessages_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.getReceivedChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetSentAndReceivedChatMessages_ReturnsMessageList_WhenListIsNotEmpty()
            throws UserNotFoundException, UserNotAuthorizedException {
        User otherUser = new User();
        String userEmail = "test@example.com";
        otherUser.setId(2L);
        otherUser.setEmail(userEmail);
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(currentUser);
        List<MessageDto> sentMessages = new ArrayList<>();
        sentMessages.add(new MessageDto(
                "Message 1",
                "receiver@example.com",
                LocalDateTime.of(2025, 1, 1, 10, 0)));
        sentMessages.add(new MessageDto(
                "Message 2",
                "receiver@example.com",
                LocalDateTime.of(2025, 1, 2, 10, 0)));
        List<MessageDto> receivedMessages = new ArrayList<>();
        receivedMessages.add(new MessageDto(
                "Message 3",
                "receiver@example.com",
                LocalDateTime.of(2025, 1, 3, 10, 0)));
        when(userService.findByEmail(userEmail)).thenReturn(otherUser);
        when(chatService.getSentChatMessages(userEmail)).thenReturn(sentMessages);
        when(chatService.getReceivedChatMessages(userEmail)).thenReturn(receivedMessages);

        List<MessageDto> result = chatService.getSentAndReceivedChatMessages(userEmail);

        assertEquals(3, result.size());
        assertEquals("Message 1", result.get(0).getContent());
        assertEquals("Message 2", result.get(1).getContent());
        assertEquals("Message 3", result.get(2).getContent());
    }

    @Test
    public void ChatService_GetSentAndReceivedChatMessages_ReturnsMessageList_WhenListIsEmpty()
            throws UserNotFoundException, UserNotAuthorizedException {
        User otherUser = new User();
        String userEmail = "test@example.com";
        otherUser.setId(2L);
        otherUser.setEmail(userEmail);
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(currentUser);
        List<MessageDto> sentMessages = new ArrayList<>();
        List<MessageDto> receivedMessages = new ArrayList<>();
        when(userService.findByEmail(userEmail)).thenReturn(otherUser);
        when(chatService.getSentChatMessages(userEmail)).thenReturn(sentMessages);
        when(chatService.getReceivedChatMessages(userEmail)).thenReturn(receivedMessages);

        List<MessageDto> result = chatService.getSentAndReceivedChatMessages(userEmail);

        assertEquals(0, result.size());
    }

    @Test
    public void ChatService_GetSentAndReceivedChatMessages_ThrowsUserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.getSentAndReceivedChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetSentAndReceivedChatMessages_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.getSentAndReceivedChatMessages(null);
        });
    }

    @Test
    public void ChatService_SendMessage_ReturnsMessageList()
            throws UserNotFoundException, UserNotAuthorizedException {
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
    public void ChatService_SendMessage_ThrowsUserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.sendMessage(new MessageDto());
        });
    }

    @Test
    public void ChatService_SendMessage_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.sendMessage(new MessageDto());
        });
    }

    @Test
    public void ChatService_GetNotReadChatMessages_ReturnsMessageList()
            throws UserNotFoundException, UserNotAuthorizedException {
        User currentUser = new User();
        currentUser.setId(1L);
        when(userService.getCurrentAuthenticationUser()).thenReturn(currentUser);
        List<Message> messages = Arrays.asList(new Message(), new Message());
        when(messageRepository.findByReceiverIdAndRead(any(), any(), any())).thenReturn(messages);
        MessageDto messageDto1 = new MessageDto();
        MessageDto messageDto2 = new MessageDto();
        when(messageDtoConverter.convertToMessageDto(any())).thenReturn(messageDto1, messageDto2);

        List<MessageDto> result = chatService.getNotReadChatMessages(null);

        assertEquals(2, result.size());
        assertEquals(messageDto1, result.get(0));
        assertEquals(messageDto2, result.get(1));
    }

    @Test
    public void ChatService_GetNotReadChatMessages_ThrowsUserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> {
            chatService.getNotReadChatMessages(null);
        });
    }

    @Test
    public void ChatService_GetNotReadChatMessages_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException {
        when(userService.getCurrentAuthenticationUser()).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> {
            chatService.getNotReadChatMessages(null);
        });
    }
}
