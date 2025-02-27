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

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<EventDto>>> getEvents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        List<EventDto> events = eventService.getAllEvents(limit, offset);
        CustomSuccessResponse<List<EventDto>> response = new CustomSuccessResponse<>(events);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<EventDto>> createEvent(
            @RequestBody EventDto eventDto,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        EventDto createdEvent = eventService.createEvent(eventDto, token);
        CustomSuccessResponse<EventDto> response = new CustomSuccessResponse<>(createdEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
