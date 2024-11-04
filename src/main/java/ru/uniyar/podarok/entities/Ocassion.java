package ru.uniyar.podarok.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ocassion")
public class Ocassion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
}
