package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Dto для отправки сообщения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String content;
    private String receiverEmail;
    private LocalDateTime timestamp;
}
