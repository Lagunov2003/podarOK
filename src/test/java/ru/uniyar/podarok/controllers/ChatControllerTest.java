package ru.uniyar.podarok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.uniyar.podarok.dtos.Dialog;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.ChatService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WithMockUser
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatService chatService;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @InjectMocks
    private ChatController chatController;

    @Test
    public void ChatController_GetSentMessages_ReturnsStatusIsOk()
            throws Exception {
        List<MessageDto> messages = Arrays.asList(new MessageDto(), new MessageDto());
        when(chatService.getSentChatMessages(any())).thenReturn(messages);

        mockMvc.perform(get("/chat/sentMessages")
                        .param("receiverEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_GetSentMessages_ReturnsStatusIsNotFound()
            throws Exception {
        when(chatService.getSentChatMessages(any())).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/sentMessages")
                        .param("receiverEmail", "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_GetSentMessages_ReturnsStatusIsUnauthorized()
            throws Exception {
        when(chatService.getSentChatMessages(any())).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/sentMessages")
                        .param("receiverEmail", "test@example.com"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void ChatController_GetReceivedMessages_ReturnsStatusIsOk()
            throws Exception {
        List<MessageDto> messages = Arrays.asList(new MessageDto(), new MessageDto());
        when(chatService.getReceivedChatMessages(any())).thenReturn(messages);

        mockMvc.perform(get("/chat/receivedMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_GetReceivedMessages_ReturnsStatusIsNotFound()
            throws Exception {
        when(chatService.getReceivedChatMessages(any())).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/receivedMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_GetReceivedMessages_ReturnsStatusIsUnauthorized()
            throws Exception {
        when(chatService.getReceivedChatMessages(any())).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/receivedMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void ChatController_GetMessages_ReturnsStatusIsOk()
            throws Exception {
        List<MessageDto> messages = Arrays.asList(new MessageDto(), new MessageDto());
        when(chatService.getSentAndReceivedChatMessages(any())).thenReturn(messages);

        mockMvc.perform(get("/chat/messages")
                        .param("userEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_GetMessages_ReturnsStatusIsNotFound()
            throws Exception {
        when(chatService.getSentAndReceivedChatMessages(any())).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/messages")
                        .param("userEmail", "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_GetMessages_ReturnsStatusIsUnauthorized()
            throws Exception {
        when(chatService.getSentAndReceivedChatMessages(any())).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/messages")
                        .param("userEmail", "test@example.com"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void ChatController_GetNewMessages_ReturnsStatusIsOk()
            throws Exception {
        List<MessageDto> messages = Arrays.asList(new MessageDto(), new MessageDto());
        when(chatService.getNotReadChatMessages(any())).thenReturn(messages);

        mockMvc.perform(get("/chat/newMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_GetNewMessages_ReturnsStatusIsNotFound()
            throws Exception {
        when(chatService.getNotReadChatMessages(any())).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/newMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_GetNewMessages_ReturnsStatusIsUnauthorized()
            throws Exception {
        when(chatService.getNotReadChatMessages(any())).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/newMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void ChatController_SendMessage_VerifiesMessageIsSent()
            throws Exception {
        MessageDto messageDto = new MessageDto();
        messageDto.setReceiverEmail("user@example.com");
        when(chatService.sendMessage(any(MessageDto.class))).thenReturn(messageDto);

        chatController.sendMessage(messageDto);

        mockMvc.perform(post("/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"test\"" +
                                ", \"receiverEmail\":\"user@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ChatController_SendMessage_ThrowsUserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException {
        MessageDto messageDto = new MessageDto();
        messageDto.setReceiverEmail("user@example.com");
        when(chatService.sendMessage(any(MessageDto.class))).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> chatController.sendMessage(messageDto));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ChatController_SendMessage_ThrowsUserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException {
        MessageDto messageDto = new MessageDto();
        messageDto.setReceiverEmail("user@example.com");
        when(chatService.sendMessage(any(MessageDto.class))).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        assertThrows(UserNotAuthorizedException.class, () -> chatController.sendMessage(messageDto));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void ChatController_ShowAllDialogs_ReturnsStatusIsOk()
            throws Exception {
        Dialog dialog = new Dialog(
                new MessageDto(
                        "message",
                        "receiver",
                        LocalDateTime.now()),
                "test",
                1L,
                "sender@email.com");
        when(chatService.getAllDialogs()).thenReturn(List.of(dialog));

        mockMvc.perform(get("/chat/allDialogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dialog)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void ChatController_ShowAllDialogs_ReturnsStatusIsUnauthorized()
            throws Exception {
        Dialog dialog = new Dialog(
                new MessageDto(
                        "message",
                        "receiver",
                        LocalDateTime.now()),
                "test",
                1L,
                "sender@email.com");
        when(chatService.getAllDialogs()).thenThrow(
                new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/allDialogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dialog)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void ChatController_ShowAllDialogs_ReturnsStatusIsNotFound()
            throws Exception {
        Dialog dialog = new Dialog(
                new MessageDto(
                        "message",
                        "receiver",
                        LocalDateTime.now()),
                "test",
                1L,
                "sender@email.com");
        when(chatService.getAllDialogs()).thenThrow(
                new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/allDialogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dialog)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }
}
