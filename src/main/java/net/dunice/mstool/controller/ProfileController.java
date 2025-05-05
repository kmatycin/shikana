package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.ProfileDto;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.constants.ConstantFields;
import net.dunice.mstool.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<CustomSuccessResponse<ProfileDto>> getCurrentProfile(
            @RequestHeader("Authorization") String token) {
        System.out.println("Токен в контроллере (до очистки): " + token);
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
        System.out.println("Очищенный токен в контроллере: " + cleanToken);
        ProfileDto profile = profileService.getProfileByToken(cleanToken);
        CustomSuccessResponse<ProfileDto> response = new CustomSuccessResponse<>(profile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CustomSuccessResponse<ProfileDto>> getProfile(@PathVariable("uuid") UUID id) {
        ProfileDto profile = profileService.getProfile(id);
        CustomSuccessResponse<ProfileDto> response = new CustomSuccessResponse<>(profile);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<CustomSuccessResponse<ProfileDto>> updateProfile(
            @RequestBody ProfileDto profileDto,
            @RequestHeader("Authorization") String token) {
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
        ProfileDto updatedProfile = profileService.updateProfile(profileDto, cleanToken);
        CustomSuccessResponse<ProfileDto> response = new CustomSuccessResponse<>(updatedProfile);
        return ResponseEntity.ok(response);
    }
}
