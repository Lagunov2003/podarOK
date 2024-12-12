package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.uniyar.podarok.entities.Message;
import org.springframework.data.domain.Sort;
import ru.uniyar.podarok.entities.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId, Sort sort);
    List<Message> findByReceiverId(Long receiverId, Sort sort);
    @Query("SELECT m FROM Message m WHERE m.sender = :sender AND m.receiver = :receiver")
    List<Message> findBySenderIdAndReceiverId(@Param("sender") User sender, @Param("receiver") User receiver, Sort sort);

    List<Message> findByReceiverIdAndIsRead(Long receiverId, Boolean isRead, Sort sort);
    List<Message> findBySenderIdAndReceiverIdAndIsRead(Long senderId, Long receiverId, Boolean isRead, Sort sort);
}
