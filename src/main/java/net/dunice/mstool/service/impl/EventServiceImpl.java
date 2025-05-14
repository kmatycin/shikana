package net.dunice.mstool.service.impl;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.response.EventResponseDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.EventEntity;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mappers.EventMapper;
import net.dunice.mstool.repository.EventRepository;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public EventResponseDto createEvent(EventDto eventDto, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        EventEntity event = eventMapper.toEntity(eventDto);
        event.setCreatedBy(user);
        event.setUpdatedBy(user);

        EventEntity savedEvent = eventRepository.save(event);
        return eventMapper.toResponseDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(eventMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEventById(UUID id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.EVENT_NOT_FOUND));
        return eventMapper.toResponseDto(event);
    }

    @Override
    @Transactional
    public EventResponseDto updateEvent(UUID id, EventDto eventDto, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.EVENT_NOT_FOUND));

        if (!event.getCreatedBy().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new CustomException(ErrorCodes.FORBIDDEN);
        }

        eventMapper.updateEntityFromDto(eventDto, event);
        event.setUpdatedBy(user);

        EventEntity updatedEvent = eventRepository.save(event);
        return eventMapper.toResponseDto(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.EVENT_NOT_FOUND));

        if (!event.getCreatedBy().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new CustomException(ErrorCodes.FORBIDDEN);
        }

        eventRepository.delete(event);
    }
}
