package net.dunice.mstool.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.dunice.mstool.constants.EventStatus;

@Data
public class EventDto {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Date is required")
    private String date;

    private String location;

    private String imageUrl;

    @NotNull(message = "External flag is required")
    private Boolean isExternal;

    private EventStatus status;

    private String game;

    private String stages;

    private String organizer;
}
