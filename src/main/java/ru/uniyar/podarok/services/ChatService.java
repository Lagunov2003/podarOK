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
import ru.uniyar.podarok.utils.Converters.MessageDtoConverter;

import java.util.List;

/**
 * Сервис для управления чатами между пользователями.
 */
@Service
@AllArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private UserService userService;
    private MessageDtoConverter messageDtoConverter;
    private final Sort sort = Sort.by("timestamp").ascending();

    /**
     * Получает список сообщений, отправленных текущим пользователем.
     * Если указан email получателя, возвращаются только сообщения для данного получателя.
     *
     * @param receiverEmail email получателя (может быть {@code null}).
     * @return список DTO сообщений.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     */
    public List<MessageDto> getSentChatMessages(String receiverEmail)
            throws UserNotFoundException, UserNotAuthorizedException {
        Long senderId = userService.getCurrentAuthenticationUser().getId();

        if (receiverEmail == null) {
            List<Message> foundedMessages =   messageRepository.findBySenderId(senderId, sort);
            return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
        }

        Long receiverId = userService.findByEmail(receiverEmail).getId();
        List<Message> foundedMessages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, sort);

        return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
    }

    /**
     * Получает список сообщений, отправленных указанным отправителем.
     * Если email отправителя не указан, возвращаются все сообщения, адресованные текущему пользователю.
     *
     * @param senderEmail email отправителя (может быть {@code null}).
     * @return список DTO сообщений.
     * @throws UserNotFoundException      если пользователь с указанным email не найден.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     */
    public List<MessageDto> getChatMessages(String senderEmail)
            throws UserNotFoundException, UserNotAuthorizedException {
        Long receiverId = userService.getCurrentAuthenticationUser().getId();

        if (senderEmail == null) {
            List<Message> foundedMessages = messageRepository.findByReceiverId(receiverId, sort);
            return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
        }

        Long senderId = userService.findByEmail(senderEmail).getId();
        List<Message> foundedMessages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, sort);

        return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
    }

    /**
     * Отправляет сообщение от текущего пользователя указанному получателю.
     *
     * @param messageDto DTO сообщения с информацией о содержимом и получателе.
     * @return DTO отправленного сообщения.
     * @throws UserNotFoundException если получатель с указанным email не найден.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     */
    public MessageDto sendMessage(MessageDto messageDto) throws UserNotFoundException, UserNotAuthorizedException {
        User sender = userService.getCurrentAuthenticationUser();
        User receiver = userService.findByEmail(messageDto.getReceiverEmail());
        Message message = messageDtoConverter.convertToMessage(messageDto.getContent(), sender, receiver);
        messageRepository.save(message);
        return messageDtoConverter.convertToMessageDto(message);
    }

    /**
     * Получает список непрочитанных сообщений от указанного отправителя.
     * Если email отправителя не указан, возвращаются все непрочитанные сообщения текущего пользователя.
     *
     * @param senderEmail email отправителя (может быть {@code null}).
     * @return список DTO сообщений.
     * @throws UserNotFoundException если отправитель с указанным email не найден.
     * @throws UserNotAuthorizedException если текущий пользователь не авторизован.
     */
    public List<MessageDto> getNotReadChatMessages(String senderEmail)
            throws UserNotFoundException, UserNotAuthorizedException {
        Long receiverId = userService.getCurrentAuthenticationUser().getId();

        if (senderEmail == null) {
            List<Message> foundedMessages = messageRepository.findByReceiverIdAndRead(receiverId, false, sort);
            return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
        }

        Long senderId = userService.findByEmail(senderEmail).getId();
        List<Message> foundedMessages = messageRepository.findBySenderIdAndReceiverIdAndRead(
                senderId, receiverId, false, sort
        );

        return foundedMessages.stream().map(messageDtoConverter::convertToMessageDto).toList();
    }
}
