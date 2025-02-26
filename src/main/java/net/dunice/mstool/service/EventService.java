package net.dunice.mstool.service;

import net.dunice.mstool.DTO.request.EventDto;
import java.util.List;

public interface EventService {
    List<EventDto> getAllEvents(int limit, int offset);
    EventDto createEvent(EventDto eventDto, String token);
}
