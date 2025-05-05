package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.constants.ConstantFields;
import net.dunice.mstool.service.NewsParserService;
import net.dunice.mstool.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final  NewsService newsService;
    private final  NewsParserService newsParserService;

    @PostMapping
    public ResponseEntity<CustomSuccessResponse<NewsDto>> createNews(
            @RequestBody NewsDto newsDto, // @Valid для валидации
            @RequestHeader("Authorization") String token) {
        System.out.println("Токен в NewsController (до очистки): " + token);
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
        System.out.println("Очищенный токен в NewsController: " + cleanToken);
        System.out.println("Полученный NewsDto: " + newsDto); // Отладка
        NewsDto createdNews = newsService.createNews(newsDto, cleanToken);
        CustomSuccessResponse<NewsDto> response = new CustomSuccessResponse<>(createdNews);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @GetMapping
//    public ResponseEntity<CustomSuccessResponse<List<NewsDto>>> getNews(
//            @RequestParam(defaultValue = "10") int limit,
//            @RequestParam(defaultValue = "0") int offset,
//            @RequestHeader("Authorization") String token) {
//        String cleanToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token.trim();
//        List<NewsDto> news = newsService.getAllNews(limit, offset);
//        CustomSuccessResponse<List<NewsDto>> response = new CustomSuccessResponse<>(news);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping
    public List<NewsDto> getAllNews() {
        return newsParserService.fetchNewsFromYoklmnracing();
    }
}
