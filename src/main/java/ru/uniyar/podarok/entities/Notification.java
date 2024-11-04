package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String itemValue;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
