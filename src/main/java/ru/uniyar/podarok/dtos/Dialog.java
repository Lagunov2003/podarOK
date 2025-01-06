package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dialog {
    private MessageDto lastMessage;
    private String username;
    private Long userId;
    private String senderEmail;
}
