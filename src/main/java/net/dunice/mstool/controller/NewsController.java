package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<NewsDto>> createNews(
            @RequestBody NewsDto newsDto,
            @RequestHeader("Authorization") String token) {
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
        NewsDto createdNews = newsService.createNews(newsDto, cleanToken);
        CustomSuccessResponse<NewsDto> response = new CustomSuccessResponse<>(createdNews);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<NewsDto>>> getAllNews(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<NewsDto> news = newsService.getAllNews(limit, offset);
        CustomSuccessResponse<List<NewsDto>> response = new CustomSuccessResponse<>(news);
        return ResponseEntity.ok(response);
    }
}
