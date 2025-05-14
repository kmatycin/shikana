package net.dunice.mstool.service.impl;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.response.PilotResponse;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.PilotEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mapper.PilotMapper;
import net.dunice.mstool.repository.PilotRepository;
import net.dunice.mstool.service.PilotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PilotServiceImpl implements PilotService {
    private final PilotRepository pilotRepository;
    private final PilotMapper pilotMapper;

    @Override
    public List<PilotResponse> getAllPilots() {
        return pilotRepository.findAll()
                .stream()
                .map(pilotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PilotResponse getPilotByNickname(String nickname) {
        PilotEntity pilot = pilotRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCodes.PILOT_NOT_FOUND));
        return pilotMapper.toResponse(pilot);
    }

    @Override
    public List<PilotResponse> searchPilots(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        return pilotRepository.findByNicknameContainingIgnoreCase(query)
                .stream()
                .map(pilotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PilotResponse> getPilotsByTeam(String team) {
        return pilotRepository.findByTeam(team)
                .stream()
                .map(pilotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PilotResponse> getPilotsByCountry(String country) {
        return pilotRepository.findByCountry(country)
                .stream()
                .map(pilotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PilotResponse> getActivePilots() {
        return pilotRepository.findByIsActive(true)
                .stream()
                .map(pilotMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PilotResponse createPilot(PilotResponse pilot) {
        if (pilotRepository.existsByNickname(pilot.getNickname())) {
            throw new CustomException(ErrorCodes.PILOT_ALREADY_EXISTS);
        }
        PilotEntity pilotEntity = new PilotEntity();
        pilotMapper.updateEntityFromResponse(pilot, pilotEntity);
        return pilotMapper.toResponse(pilotRepository.save(pilotEntity));
    }

    @Override
    @Transactional
    public PilotResponse updatePilot(String nickname, PilotResponse pilot) {
        PilotEntity pilotEntity = pilotRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCodes.PILOT_NOT_FOUND));
        pilotMapper.updateEntityFromResponse(pilot, pilotEntity);
        return pilotMapper.toResponse(pilotRepository.save(pilotEntity));
    }

    @Override
    @Transactional
    public void deletePilot(String nickname) {
        if (!pilotRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCodes.PILOT_NOT_FOUND);
        }
        pilotRepository.deleteByNickname(nickname);
    }
} 