package net.dunice.mstool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "news")
@Getter
@Setter
public class NewsEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "image_url")
    private String imageUrl;

    // Конструкторы
    public NewsEntity() {}

    public NewsEntity(String title, String content, UUID authorId, Instant createdAt, String imageUrl) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    // Переопределение toString для отладки
    @Override
    public String toString() {
        return "NewsEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", createdAt=" + createdAt +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public NewsEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NewsEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NewsEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public NewsEntity setAuthorId(UUID authorId) {
        this.authorId = authorId;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public NewsEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public NewsEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}