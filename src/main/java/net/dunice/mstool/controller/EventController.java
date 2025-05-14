package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.constants.ConstantFields;
import net.dunice.mstool.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<EventDto>>> getEvents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        List<EventDto> events = eventService.getAllEvents(limit, offset);
        return ResponseEntity.ok(new CustomSuccessResponse<>(events));
    }

    @GetMapping("/external")
    public ResponseEntity<CustomSuccessResponse<List<EventDto>>> getExternalEvents(
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        List<EventDto> externalEvents = eventService.getExternalEvents();
        return ResponseEntity.ok(new CustomSuccessResponse<>(externalEvents));
    }

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<EventDto>> createEvent(
            @RequestBody EventDto eventDto,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        EventDto createdEvent = eventService.createEvent(eventDto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomSuccessResponse<>(createdEvent));
    }

    @PostMapping("/external")
    public ResponseEntity<CustomSuccessResponse<EventDto>> createExternalEvent(
            @RequestBody EventDto eventDto,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        eventDto.setExternal(true);
        EventDto createdEvent = eventService.createEvent(eventDto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomSuccessResponse<>(createdEvent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<EventDto>> updateEvent(
            @PathVariable UUID id,
            @RequestBody EventDto eventDto,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        EventDto updatedEvent = eventService.updateEvent(id, eventDto, token);
        return ResponseEntity.ok(new CustomSuccessResponse<>(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<Void>> deleteEvent(
            @PathVariable UUID id,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        eventService.deleteEvent(id, token);
        return ResponseEntity.ok(new CustomSuccessResponse<>(null));
    }
}
