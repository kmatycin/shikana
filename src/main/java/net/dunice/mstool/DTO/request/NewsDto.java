package net.dunice.mstool.DTO.request;

import java.time.Instant;
import java.util.UUID;

public class NewsDto {
    private UUID id;
    private String title;
    private String content;
    private UUID authorId;
    private String authorName;
    private Instant date;
    private String imageUrl;

    public UUID getId() {
        return id;
    }

    public NewsDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NewsDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NewsDto setContent(String content) {
        this.content = content;
        return this;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public NewsDto setAuthorId(UUID authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public NewsDto setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public Instant getDate() {
        return date;
    }

    public NewsDto setDate(Instant date) {
        this.date = date;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public NewsDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
