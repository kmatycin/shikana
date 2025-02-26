package net.dunice.mstool.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class NewsDto {
    private UUID id;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private UUID authorId;
    private String authorName;
    private Instant createdAt;
}
