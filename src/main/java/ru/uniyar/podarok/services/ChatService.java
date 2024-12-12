package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.MessageRepository;
import ru.uniyar.podarok.utils.MessageDtoConverter;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private UserService userService;
    private MessageDtoConverter messageDtoConverter;
    private final Sort sort = Sort.by("timestamp").ascending();

    public List<MessageDto> getSentChatMessages(String receiverEmail) throws UserNotFoundException, UserNotAuthorizedException {
        Long senderId = userService.getCurrentAuthenticationUser().getId();
        if (receiverEmail == null) {
            List<Message> foundedMessages =   messageRepository.findBySenderId(senderId, sort);
            return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
        }
        Long receiverId = userService.findByEmail(receiverEmail).getId();
        List<Message> foundedMessages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, sort);
        return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
    }

    public List<MessageDto> getChatMessages(String senderEmail) throws UserNotFoundException, UserNotAuthorizedException {
        Long receiverId = userService.getCurrentAuthenticationUser().getId();
        if (senderEmail == null) {
            List<Message> foundedMessages = messageRepository.findByReceiverId(receiverId, sort);
            return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
        }
        Long senderId = userService.findByEmail(senderEmail).getId();
        List<Message> foundedMessages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, sort);
        return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
    }

    public MessageDto sendMessage(MessageDto messageDto) throws UserNotFoundException, UserNotAuthorizedException {
        User sender = userService.getCurrentAuthenticationUser();
        User receiver = userService.findByEmail(messageDto.getReceiverEmail());
        Message message = messageDtoConverter.convertToMessage(messageDto.getContent(), sender, receiver);
        messageRepository.save(message);
        return messageDtoConverter.convertToMessageDto(message);
    }

    public List<MessageDto> getNotReadChatMessages(String senderEmail) throws UserNotFoundException, UserNotAuthorizedException {
        Long receiverId = userService.getCurrentAuthenticationUser().getId();
        if (senderEmail == null) {
            List<Message> foundedMessages = messageRepository.findByReceiverIdAndIsRead(receiverId, false, sort);
            return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
        }
        Long senderId = userService.findByEmail(senderEmail).getId();
        List<Message> foundedMessages = messageRepository.findBySenderIdAndReceiverIdAndIsRead(senderId, receiverId, false, sort);
        return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
    }
}
