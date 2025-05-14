package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.response.EventResponseDto;
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
    public ResponseEntity<EventResponseDto> createEvent(
            @RequestBody EventDto eventDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(eventService.createEvent(eventDto, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(Pageable pageable) {
        return ResponseEntity.ok(eventService.getAllEvents(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable UUID id,
            @RequestBody EventDto eventDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDto, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        eventService.deleteEvent(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
