package ru.uniyar.podarok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.uniyar.podarok.entities.Message;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Message}.
 * Предоставляет методы для выполнения операций с сообщениями.
 * Наследуется от {@link JpaRepository}, что обеспечивает базовые CRUD-операции.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Находит все сообщения, отправленные пользователем с указанным идентификатором.
     *
     * @param senderId идентификатор отправителя.
     * @param sort сортировка результатов.
     * @return список сообщений, отправленных пользователем с указанным идентификатором.
     */
    List<Message> findBySenderId(Long senderId, Sort sort);

    /**
     * Находит все сообщения, полученные пользователем с указанным идентификатором.
     *
     * @param receiverId идентификатор получателя.
     * @param sort сортировка результатов.
     * @return список сообщений, полученных пользователем с указанным идентификатором.
     */
    List<Message> findByReceiverId(Long receiverId, Sort sort);

    /**
     * Находит все сообщения, отправленные и полученные между двумя пользователями.
     *
     * @param senderId идентификатор отправителя.
     * @param receiverId идентификатор получателя.
     * @param sort сортировка результатов.
     * @return список сообщений между двумя пользователями.
     */
    @Query("SELECT m FROM Message m WHERE m.sender = :senderId AND m.receiver = :receiverId")
    List<Message> findBySenderIdAndReceiverId(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId,
            Sort sort
    );

    /**
     * Находит все сообщения, полученные пользователем с указанным идентификатором и статусом прочтения.
     *
     * @param receiverId идентификатор получателя.
     * @param read статус прочтения сообщений.
     * @param sort сортировка результатов.
     * @return список сообщений с указанным статусом прочтения.
     */
    List<Message> findByReceiverIdAndRead(Long receiverId, Boolean read, Sort sort);

    /**
     * Находит все сообщения, отправленные и полученные между двумя пользователями с указанным статусом прочтения.
     *
     * @param senderId идентификатор отправителя.
     * @param receiverId идентификатор получателя.
     * @param read статус прочтения сообщений.
     * @param sort сортировка результатов.
     * @return список сообщений с указанным статусом прочтения.
     */
    List<Message> findBySenderIdAndReceiverIdAndRead(Long senderId, Long receiverId, Boolean read, Sort sort);
}
