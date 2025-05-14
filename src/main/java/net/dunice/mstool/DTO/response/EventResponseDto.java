package net.dunice.mstool.DTO.response;

import lombok.Data;
import net.dunice.mstool.constants.EventStatus;
import java.time.Instant;
import java.util.UUID;

@Data
public class EventResponseDto {
    private UUID id;
    private String title;
    private String description;
    private String date;
    private String location;
    private String imageUrl;
    private boolean isExternal;
    private EventStatus status;
    private String game;
    private String stages;
    private String organizer;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
} 