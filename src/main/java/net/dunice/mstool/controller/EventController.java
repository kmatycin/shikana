package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.response.EventResponseDto;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<EventResponseDto>> createEvent(
            @RequestBody EventDto eventDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        EventResponseDto createdEvent = eventService.createEvent(eventDto, userDetails.getUsername());
        return ResponseEntity.ok(new CustomSuccessResponse<>(createdEvent));
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<Page<EventResponseDto>>> getAllEvents(Pageable pageable) {
        Page<EventResponseDto> events = eventService.getAllEvents(pageable);
        return ResponseEntity.ok(new CustomSuccessResponse<>(events));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<EventResponseDto>> getEventById(@PathVariable UUID id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(new CustomSuccessResponse<>(event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<EventResponseDto>> updateEvent(
            @PathVariable UUID id,
            @RequestBody EventDto eventDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventDto, userDetails.getUsername());
        return ResponseEntity.ok(new CustomSuccessResponse<>(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        eventService.deleteEvent(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
