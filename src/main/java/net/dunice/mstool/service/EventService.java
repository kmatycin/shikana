package net.dunice.mstool.service;

import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.response.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    EventResponseDto createEvent(EventDto eventDto, String userEmail);
    
    Page<EventResponseDto> getAllEvents(Pageable pageable);
    
    EventResponseDto getEventById(UUID id);
    
    EventResponseDto updateEvent(UUID id, EventDto eventDto, String userEmail);
    
    void deleteEvent(UUID id, String userEmail);
}
