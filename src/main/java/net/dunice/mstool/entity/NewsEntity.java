package net.dunice.mstool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "news")
@Getter
@Setter
public class NewsEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Конструкторы
    public NewsEntity() {}

    public NewsEntity(String content, UUID authorId, Instant createdAt) {
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
    }

    // Переопределение toString для отладки
    @Override
    public String toString() {
        return "NewsEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", createdAt=" + createdAt +
                '}';
    }
}