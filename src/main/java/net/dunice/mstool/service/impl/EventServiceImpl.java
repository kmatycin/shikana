package net.dunice.mstool.service.impl;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.EventEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.EventRepository;
import net.dunice.mstool.security.JwtService;
import net.dunice.mstool.service.EventService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final JwtService jwtUtil;

    @Override
    public List<EventDto> getAllEvents(int limit, int offset) {
        // Здесь будет логика получения событий с пагинацией из репозитория
        // Например: eventRepository.findAll(PageRequest.of(offset / limit, limit))
        // Маппинг в EventDto опущен для краткости
        return List.of(); // Заглушка
    }

    @Override
    public EventDto createEvent(EventDto eventDto, String token) {
        // Проверка роли пользователя
        String role = jwtUtil.extractRole(token);
        if (!"ORGANIZER".equals(role)) {
            throw new CustomException(ErrorCodes.CODE_NOT_NULL);
        }

        // Маппинг DTO в сущность
        EventEntity event = new EventEntity();
        event.setTitle(eventDto.getTitle());
        event.setDate(eventDto.getDate());
        event.setLocation(eventDto.getLocation());
        event.setStatus("UPCOMING");

        // Сохранение в БД
        EventEntity savedEvent = eventRepository.save(event);

        // Маппинг обратно в DTO
        EventDto savedEventDto = new EventDto();
        savedEventDto.setId(savedEvent.getId());
        savedEventDto.setTitle(savedEvent.getTitle());
        savedEventDto.setDate(savedEvent.getDate());
        savedEventDto.setLocation(savedEvent.getLocation());
        savedEventDto.setStatus(savedEvent.getStatus());

        return savedEventDto;
    }
}
