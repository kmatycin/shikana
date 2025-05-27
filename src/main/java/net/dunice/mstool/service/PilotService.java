package net.dunice.mstool.service;

import net.dunice.mstool.DTO.response.PilotResponse;
import java.util.List;

public interface PilotService {
    List<PilotResponse> getAllPilots();

    PilotResponse getPilotByNickname(String nickname);

    List<PilotResponse> searchPilots(String query);

    List<PilotResponse> getPilotsByTeam(String team);

    List<PilotResponse> getPilotsByCountry(String country);

    List<PilotResponse> getActivePilots();

    PilotResponse createPilot(PilotResponse pilot);

    PilotResponse updatePilot(String nickname, PilotResponse pilot);

    void deletePilot(String nickname);
} 