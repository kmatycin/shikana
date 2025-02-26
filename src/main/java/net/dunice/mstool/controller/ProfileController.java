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

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<ProfileDto>> getProfile(@PathVariable UUID id) {
        ProfileDto profile = profileService.getProfile(id);
        CustomSuccessResponse<ProfileDto> response = new CustomSuccessResponse<>(profile);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<CustomSuccessResponse<ProfileDto>> updateProfile(
            @RequestBody ProfileDto profileDto,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        ProfileDto updatedProfile = profileService.updateProfile(profileDto, token);
        CustomSuccessResponse<ProfileDto> response = new CustomSuccessResponse<>(updatedProfile);
        return ResponseEntity.ok(response);
    }
}
