package net.dunice.mstool.controller;

import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.constants.ConstantFields;
import net.dunice.mstool.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<NewsDto>>> getNews(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        List<NewsDto> news = newsService.getAllNews(limit, offset);
        CustomSuccessResponse<List<NewsDto>> response = new CustomSuccessResponse<>(news);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<NewsDto>> createNews(
            @RequestBody NewsDto newsDto,
            @RequestHeader(ConstantFields.AUTHORIZATION) String token) {
        NewsDto createdNews = newsService.createNews(newsDto, token);
        CustomSuccessResponse<NewsDto> response = new CustomSuccessResponse<>(createdNews);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
