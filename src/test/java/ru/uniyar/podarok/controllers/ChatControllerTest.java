package ru.uniyar.podarok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.ChatService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WithMockUser()
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void ChatController_GetSentMessages_ReturnsStatusIsOk() throws Exception {
        List<MessageDto> messages = Arrays.asList(new MessageDto(), new MessageDto());
        when(chatService.getSentChatMessages(any())).thenReturn(messages);

        mockMvc.perform(get("/chat/sentMessages")
                        .param("receiverEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_GetMessages_ReturnsStatusIsOk() throws Exception {
        List<MessageDto> messages = Arrays.asList(new MessageDto(), new MessageDto());
        when(chatService.getChatMessages(any())).thenReturn(messages);

        mockMvc.perform(get("/chat/messages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_GetNewMessages_ReturnsStatusIsOk() throws Exception {
        List<MessageDto> messages = Arrays.asList(new MessageDto(), new MessageDto());
        when(chatService.getNotReadChatMessages(any())).thenReturn(messages);

        mockMvc.perform(get("/chat/newMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_SendMessage_ReturnsStatusIsOk() throws Exception {
        MessageDto messageDto = new MessageDto();
        when(chatService.sendMessage(any())).thenReturn(messageDto);

        mockMvc.perform(post("/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ChatController_GetSentMessages_ReturnsStatusIsNotFound() throws Exception {
        when(chatService.getSentChatMessages(any())).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/sentMessages")
                        .param("receiverEmail", "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_GetSentMessages_ReturnsStatusIsUnauthorized() throws Exception {
        when(chatService.getSentChatMessages(any())).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/sentMessages")
                        .param("receiverEmail", "test@example.com"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void ChatController_GetMessages_ReturnsStatusIsNotFound() throws Exception {
        when(chatService.getChatMessages(any())).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/messages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_GetMessages_ReturnsStatusIsUnauthorized() throws Exception {
        when(chatService.getChatMessages(any())).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/messages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void ChatController_GetNewMessages_ReturnsStatusIsNotFound() throws Exception {
        when(chatService.getNotReadChatMessages(any())).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(get("/chat/newMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_GetNewMessages_ReturnsStatusIsUnauthorized() throws Exception {
        when(chatService.getNotReadChatMessages(any())).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(get("/chat/newMessages")
                        .param("senderEmail", "test@example.com"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }


    @Test
    public void ChatController_SendMessage_ReturnsStatusIsNotFound() throws Exception {
        MessageDto messageDto = new MessageDto();
        when(chatService.sendMessage(any())).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        mockMvc.perform(post("/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void ChatController_SendMessage_ReturnsStatusIsUnauthorized() throws Exception {
        MessageDto messageDto = new MessageDto();
        when(chatService.sendMessage(any())).thenThrow(new UserNotAuthorizedException("Пользователь не авторизован!"));

        mockMvc.perform(post("/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }
}
