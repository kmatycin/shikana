package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.response.UserSearchResponse;
import net.dunice.mstool.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(userService.searchUsers(query.trim()));
    }
} 