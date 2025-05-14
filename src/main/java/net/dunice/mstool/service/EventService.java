package net.dunice.mstool.service;

import net.dunice.mstool.DTO.request.EventDto;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface EventService {
    List<EventDto> getAllEvents(int limit, int offset);
    List<EventDto> getExternalEvents();
    EventDto createEvent(EventDto eventDto, String token);
    EventDto updateEvent(UUID id, EventDto eventDto, String token);
    void deleteEvent(UUID id, String token);
    List<EventDto> parseExternalEvents()throws IOException;
}
