package net.dunice.mstool.service.impl;

import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.response.EventResponseDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.EventEntity;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mappers.EventMapper;
import net.dunice.mstool.repository.EventRepository;
import net.dunice.mstool.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventDto eventDto;
    private EventEntity eventEntity;
    private EventResponseDto eventResponseDto;
    private UserEntity userEntity;
    private UUID eventId;
    private String userEmail;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_TITLE = "Test Event";
    private static final String TEST_DESCRIPTION = "Test Description";

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        userEmail = TEST_EMAIL;

        // Setup user
        userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setRole("USER");

        // Setup event DTO
        eventDto = new EventDto();
        eventDto.setTitle(TEST_TITLE);
        eventDto.setDescription(TEST_DESCRIPTION);

        // Setup event entity
        eventEntity = new EventEntity();
        eventEntity.setId(eventId);
        eventEntity.setTitle(TEST_TITLE);
        eventEntity.setDescription(TEST_DESCRIPTION);
        eventEntity.setCreatedBy(userEntity);
        eventEntity.setUpdatedBy(userEntity);

        // Setup event response DTO
        eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(eventId);
        eventResponseDto.setTitle(TEST_TITLE);
        eventResponseDto.setDescription(TEST_DESCRIPTION);
    }

    @Test
    void createEvent_Success() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(eventMapper.toEntity(eventDto)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(eventMapper.toResponseDto(eventEntity)).thenReturn(eventResponseDto);

        // Act
        EventResponseDto response = eventService.createEvent(eventDto, userEmail);

        // Assert
        assertNotNull(response);
        assertEquals(eventId, response.getId());
        assertEquals(TEST_TITLE, response.getTitle());
        assertEquals(TEST_DESCRIPTION, response.getDescription());
        verify(eventRepository).save(any(EventEntity.class));
    }

    @Test
    void createEvent_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.createEvent(eventDto, userEmail));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void getAllEvents_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<EventEntity> events = List.of(eventEntity);
        Page<EventEntity> eventPage = new PageImpl<>(events, pageable, events.size());
        when(eventRepository.findAll(pageable)).thenReturn(eventPage);
        when(eventMapper.toResponseDto(eventEntity)).thenReturn(eventResponseDto);

        // Act
        Page<EventResponseDto> response = eventService.getAllEvents(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(TEST_TITLE, response.getContent().get(0).getTitle());
    }

    @Test
    void getEventById_Success() {
        // Arrange
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventMapper.toResponseDto(eventEntity)).thenReturn(eventResponseDto);

        // Act
        EventResponseDto response = eventService.getEventById(eventId);

        // Assert
        assertNotNull(response);
        assertEquals(eventId, response.getId());
        assertEquals(TEST_TITLE, response.getTitle());
    }

    @Test
    void getEventById_NotFound() {
        // Arrange
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.getEventById(eventId));
        assertEquals(ErrorCodes.EVENT_NOT_FOUND, exception.getErrorCodes());
    }

    @Test
    void updateEvent_Success_ByCreator() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(eventMapper.toResponseDto(eventEntity)).thenReturn(eventResponseDto);

        // Act
        EventResponseDto response = eventService.updateEvent(eventId, eventDto, userEmail);

        // Assert
        assertNotNull(response);
        assertEquals(eventId, response.getId());
        verify(eventRepository).save(any(EventEntity.class));
    }

    @Test
    void updateEvent_Success_ByAdmin() {
        // Arrange
        UserEntity adminUser = new UserEntity();
        adminUser.setId(UUID.randomUUID());
        adminUser.setEmail("admin@example.com");
        adminUser.setRole("ADMIN");

        when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(eventMapper.toResponseDto(eventEntity)).thenReturn(eventResponseDto);

        // Act
        EventResponseDto response = eventService.updateEvent(eventId, eventDto, adminUser.getEmail());

        // Assert
        assertNotNull(response);
        assertEquals(eventId, response.getId());
        verify(eventRepository).save(any(EventEntity.class));
    }

    @Test
    void updateEvent_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.updateEvent(eventId, eventDto, userEmail));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void updateEvent_EventNotFound() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.updateEvent(eventId, eventDto, userEmail));
        assertEquals(ErrorCodes.EVENT_NOT_FOUND, exception.getErrorCodes());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void updateEvent_Forbidden() {
        // Arrange
        UserEntity otherUser = new UserEntity();
        otherUser.setId(UUID.randomUUID());
        otherUser.setEmail("other@example.com");
        otherUser.setRole("USER");

        when(userRepository.findByEmail(otherUser.getEmail())).thenReturn(Optional.of(otherUser));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.updateEvent(eventId, eventDto, otherUser.getEmail()));
        assertEquals(ErrorCodes.FORBIDDEN, exception.getErrorCodes());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void deleteEvent_Success_ByCreator() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));

        // Act
        eventService.deleteEvent(eventId, userEmail);

        // Assert
        verify(eventRepository).delete(eventEntity);
    }

    @Test
    void deleteEvent_Success_ByAdmin() {
        // Arrange
        UserEntity adminUser = new UserEntity();
        adminUser.setId(UUID.randomUUID());
        adminUser.setEmail("admin@example.com");
        adminUser.setRole("ADMIN");

        when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));

        // Act
        eventService.deleteEvent(eventId, adminUser.getEmail());

        // Assert
        verify(eventRepository).delete(eventEntity);
    }

    @Test
    void deleteEvent_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.deleteEvent(eventId, userEmail));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(eventRepository, never()).delete(any());
    }

    @Test
    void deleteEvent_EventNotFound() {
        // Arrange
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userEntity));
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.deleteEvent(eventId, userEmail));
        assertEquals(ErrorCodes.EVENT_NOT_FOUND, exception.getErrorCodes());
        verify(eventRepository, never()).delete(any());
    }

    @Test
    void deleteEvent_Forbidden() {
        // Arrange
        UserEntity otherUser = new UserEntity();
        otherUser.setId(UUID.randomUUID());
        otherUser.setEmail("other@example.com");
        otherUser.setRole("USER");

        when(userRepository.findByEmail(otherUser.getEmail())).thenReturn(Optional.of(otherUser));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> eventService.deleteEvent(eventId, otherUser.getEmail()));
        assertEquals(ErrorCodes.FORBIDDEN, exception.getErrorCodes());
        verify(eventRepository, never()).delete(any());
    }
} 