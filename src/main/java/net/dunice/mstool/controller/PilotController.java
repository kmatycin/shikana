package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.response.PilotResponse;
import net.dunice.mstool.service.PilotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pilots")
@RequiredArgsConstructor
public class PilotController {
    private final PilotService pilotService;

    @GetMapping("/{nickname}")
    public ResponseEntity<PilotResponse> getPilotByNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(pilotService.getPilotByNickname(nickname));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PilotResponse>> searchPilots(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(pilotService.searchPilots(query));
    }

    @GetMapping("/team/{team}")
    public ResponseEntity<List<PilotResponse>> getPilotsByTeam(@PathVariable String team) {
        return ResponseEntity.ok(pilotService.getPilotsByTeam(team));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<PilotResponse>> getPilotsByCountry(@PathVariable String country) {
        return ResponseEntity.ok(pilotService.getPilotsByCountry(country));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PilotResponse>> getActivePilots() {
        return ResponseEntity.ok(pilotService.getActivePilots());
    }

    @GetMapping
    public ResponseEntity<List<PilotResponse>> getAllPilots() {
        return ResponseEntity.ok(pilotService.getAllPilots());
    }

    @PostMapping
    public ResponseEntity<PilotResponse> createPilot(@RequestBody PilotResponse pilot) {
        return ResponseEntity.ok(pilotService.createPilot(pilot));
    }

    @PutMapping("/{nickname}")
    public ResponseEntity<PilotResponse> updatePilot(@PathVariable String nickname, @RequestBody PilotResponse pilot) {
        return ResponseEntity.ok(pilotService.updatePilot(nickname, pilot));
    }

    @DeleteMapping("/{nickname}")
    public ResponseEntity<Void> deletePilot(@PathVariable String nickname) {
        pilotService.deletePilot(nickname);
        return ResponseEntity.ok().build();
    }
} 