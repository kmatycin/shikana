package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.response.UserProfileResponse;
import net.dunice.mstool.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    @PutMapping("/{username}/profile")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @PathVariable String username,
            @RequestBody UserProfileResponse profile) {
        return ResponseEntity.ok(userService.updateUserProfile(username, profile));
    }
} 