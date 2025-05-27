package net.dunice.mstool.service.impl;

import net.dunice.mstool.DTO.response.PilotResponse;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.PilotEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mapper.PilotMapper;
import net.dunice.mstool.repository.PilotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PilotServiceImplTest {

    @Mock
    private PilotRepository pilotRepository;

    @Mock
    private PilotMapper pilotMapper;

    @InjectMocks
    private PilotServiceImpl pilotService;

    private PilotEntity pilotEntity;
    private PilotResponse pilotResponse;
    private static final String TEST_NICKNAME = "testPilot";
    private static final String TEST_TEAM = "Test Team";
    private static final String TEST_COUNTRY = "Test Country";

    @BeforeEach
    void setUp() {
        // Setup pilot entity
        pilotEntity = new PilotEntity();
        pilotEntity.setNickname(TEST_NICKNAME);
        pilotEntity.setTeam(TEST_TEAM);
        pilotEntity.setCountry(TEST_COUNTRY);
        pilotEntity.setIsActive(true);

        // Setup pilot response
        pilotResponse = new PilotResponse();
        pilotResponse.setNickname(TEST_NICKNAME);
        pilotResponse.setTeam(TEST_TEAM);
        pilotResponse.setCountry(TEST_COUNTRY);
        pilotResponse.setIsActive(true);
    }

    @Test
    void getAllPilots_Success() {
        // Arrange
        when(pilotRepository.findAll()).thenReturn(List.of(pilotEntity));
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        List<PilotResponse> result = pilotService.getAllPilots();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pilotResponse, result.get(0));
        verify(pilotRepository).findAll();
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void getPilotByNickname_Success() {
        // Arrange
        when(pilotRepository.findByNickname(TEST_NICKNAME)).thenReturn(Optional.of(pilotEntity));
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        PilotResponse result = pilotService.getPilotByNickname(TEST_NICKNAME);

        // Assert
        assertNotNull(result);
        assertEquals(pilotResponse, result);
        verify(pilotRepository).findByNickname(TEST_NICKNAME);
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void getPilotByNickname_NotFound() {
        // Arrange
        when(pilotRepository.findByNickname(TEST_NICKNAME)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> pilotService.getPilotByNickname(TEST_NICKNAME));
        assertEquals(ErrorCodes.PILOT_NOT_FOUND, exception.getErrorCodes());
        verify(pilotRepository).findByNickname(TEST_NICKNAME);
        verify(pilotMapper, never()).toResponse(any());
    }

    @Test
    void searchPilots_Success() {
        // Arrange
        String query = "test";
        when(pilotRepository.findByNicknameContainingIgnoreCase(query)).thenReturn(List.of(pilotEntity));
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        List<PilotResponse> result = pilotService.searchPilots(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pilotResponse, result.get(0));
        verify(pilotRepository).findByNicknameContainingIgnoreCase(query);
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void searchPilots_EmptyQuery() {
        // Act
        List<PilotResponse> result = pilotService.searchPilots(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pilotRepository, never()).findByNicknameContainingIgnoreCase(any());
        verify(pilotMapper, never()).toResponse(any());
    }

    @Test
    void getPilotsByTeam_Success() {
        // Arrange
        when(pilotRepository.findByTeam(TEST_TEAM)).thenReturn(List.of(pilotEntity));
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        List<PilotResponse> result = pilotService.getPilotsByTeam(TEST_TEAM);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pilotResponse, result.get(0));
        verify(pilotRepository).findByTeam(TEST_TEAM);
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void getPilotsByCountry_Success() {
        // Arrange
        when(pilotRepository.findByCountry(TEST_COUNTRY)).thenReturn(List.of(pilotEntity));
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        List<PilotResponse> result = pilotService.getPilotsByCountry(TEST_COUNTRY);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pilotResponse, result.get(0));
        verify(pilotRepository).findByCountry(TEST_COUNTRY);
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void getActivePilots_Success() {
        // Arrange
        when(pilotRepository.findByIsActive(true)).thenReturn(List.of(pilotEntity));
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        List<PilotResponse> result = pilotService.getActivePilots();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pilotResponse, result.get(0));
        verify(pilotRepository).findByIsActive(true);
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void createPilot_Success() {
        // Arrange
        when(pilotRepository.existsByNickname(TEST_NICKNAME)).thenReturn(false);
        when(pilotRepository.save(any(PilotEntity.class))).thenReturn(pilotEntity);
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        PilotResponse result = pilotService.createPilot(pilotResponse);

        // Assert
        assertNotNull(result);
        assertEquals(pilotResponse, result);
        verify(pilotRepository).existsByNickname(TEST_NICKNAME);
        verify(pilotRepository).save(any(PilotEntity.class));
        verify(pilotMapper).updateEntityFromResponse(pilotResponse, any(PilotEntity.class));
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void createPilot_AlreadyExists() {
        // Arrange
        when(pilotRepository.existsByNickname(TEST_NICKNAME)).thenReturn(true);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> pilotService.createPilot(pilotResponse));
        assertEquals(ErrorCodes.PILOT_ALREADY_EXISTS, exception.getErrorCodes());
        verify(pilotRepository).existsByNickname(TEST_NICKNAME);
        verify(pilotRepository, never()).save(any());
        verify(pilotMapper, never()).updateEntityFromResponse(any(), any());
    }

    @Test
    void updatePilot_Success() {
        // Arrange
        when(pilotRepository.findByNickname(TEST_NICKNAME)).thenReturn(Optional.of(pilotEntity));
        when(pilotRepository.save(pilotEntity)).thenReturn(pilotEntity);
        when(pilotMapper.toResponse(pilotEntity)).thenReturn(pilotResponse);

        // Act
        PilotResponse result = pilotService.updatePilot(TEST_NICKNAME, pilotResponse);

        // Assert
        assertNotNull(result);
        assertEquals(pilotResponse, result);
        verify(pilotRepository).findByNickname(TEST_NICKNAME);
        verify(pilotRepository).save(pilotEntity);
        verify(pilotMapper).updateEntityFromResponse(pilotResponse, pilotEntity);
        verify(pilotMapper).toResponse(pilotEntity);
    }

    @Test
    void updatePilot_NotFound() {
        // Arrange
        when(pilotRepository.findByNickname(TEST_NICKNAME)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> pilotService.updatePilot(TEST_NICKNAME, pilotResponse));
        assertEquals(ErrorCodes.PILOT_NOT_FOUND, exception.getErrorCodes());
        verify(pilotRepository).findByNickname(TEST_NICKNAME);
        verify(pilotRepository, never()).save(any());
        verify(pilotMapper, never()).updateEntityFromResponse(any(), any());
    }

    @Test
    void deletePilot_Success() {
        // Arrange
        when(pilotRepository.existsByNickname(TEST_NICKNAME)).thenReturn(true);

        // Act
        pilotService.deletePilot(TEST_NICKNAME);

        // Assert
        verify(pilotRepository).existsByNickname(TEST_NICKNAME);
        verify(pilotRepository).deleteByNickname(TEST_NICKNAME);
    }

    @Test
    void deletePilot_NotFound() {
        // Arrange
        when(pilotRepository.existsByNickname(TEST_NICKNAME)).thenReturn(false);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> pilotService.deletePilot(TEST_NICKNAME));
        assertEquals(ErrorCodes.PILOT_NOT_FOUND, exception.getErrorCodes());
        verify(pilotRepository).existsByNickname(TEST_NICKNAME);
        verify(pilotRepository, never()).deleteByNickname(any());
    }
} 