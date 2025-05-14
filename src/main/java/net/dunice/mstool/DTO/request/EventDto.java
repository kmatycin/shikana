package net.dunice.mstool.DTO.request;

import lombok.Data;
import java.time.Instant;

@Data
public class EventDto {
    private String title;
    private String description;
    private String date;
    private String location;
    private String imageUrl;
    private boolean isExternal;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // Дополнительные поля для внешних событий
    private String status;
    private String game;
    private String stages;
    private String organizer;
}
