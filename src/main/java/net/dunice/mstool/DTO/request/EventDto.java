package net.dunice.mstool.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class EventDto {
    private UUID id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Date cannot be null")
    private Instant date;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    private String status;
}
