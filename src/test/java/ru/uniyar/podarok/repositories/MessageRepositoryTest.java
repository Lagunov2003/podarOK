package ru.uniyar.podarok.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private EntityManager entityManager;
    private User sender;
    private User receiver;
    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        sender = new User();
        sender.setId(1L);
        receiver = new User();
        receiver.setId(2L);

        message1 = new Message();
        message1.setSender(sender);
        message1.setReceiver(receiver);
        message1.setContent("Hello");
        message1.setRead(false);
        message1.setTimestamp(LocalDateTime.now());

        message2 = new Message();
        message2.setSender(sender);
        message2.setReceiver(receiver);
        message2.setContent("World");
        message2.setRead(true);
        message2.setTimestamp(LocalDateTime.now());

        messageRepository.save(message1);
        messageRepository.save(message2);
    }

    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE messages RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    public void MessageRepository_FindBySenderId_ReturnsMessageList() {
        List<Message> messages = messageRepository.findBySenderId(
                sender.getId(),
                Sort.by("timestamp").ascending());

        assertThat(messages).hasSize(2);
        assertThat(messages).containsExactlyInAnyOrder(message1, message2);
    }

    @Test
    public void MessageRepository_FindByReceiverId_ReturnsMessageList() {
        List<Message> messages = messageRepository.findByReceiverId(
                receiver.getId(),
                Sort.by("timestamp").ascending());

        assertThat(messages).hasSize(2);
        assertThat(messages).containsExactlyInAnyOrder(message1, message2);
    }

    @Test
    public void MessageRepository_FindBySenderIdAndReceiverId_ReturnsMessageList() {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverId(
                sender.getId(),
                receiver.getId(),
                Sort.by("timestamp").ascending());

        assertThat(messages).hasSize(2);
        assertThat(messages).containsExactlyInAnyOrder(message1, message2);
    }

    @Test
    public void MessageRepository_FindByReceiverIdAndRead_ReturnsMessageList() {
        List<Message> messages = messageRepository.findByReceiverIdAndRead(
                receiver.getId(),
                false,
                Sort.by("timestamp").ascending());

        assertThat(messages).hasSize(1);
        assertThat(messages).containsExactly(message1);
    }

    @Test
    public void MessageRepository_FindBySenderIdAndReceiverIdAndRead_ReturnsMessageList() {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdAndRead(
                sender.getId(),
                receiver.getId(),
                true,
                Sort.by("timestamp").ascending());

        assertThat(messages).hasSize(1);
        assertThat(messages).containsExactly(message2);
    }
}
