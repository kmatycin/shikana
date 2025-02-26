package net.dunice.mstool.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class NewsEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String content;
    @ManyToOne
    private UserEntity author;
    private Instant createdAt;

    // Геттеры, сеттеры
}
