package net.dunice.mstool.service.impl;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.NewsEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.NewsRepository;
import net.dunice.mstool.security.JwtService;
import net.dunice.mstool.service.NewsService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final JwtService jwtService;

    @Override
    public List<NewsDto> getAllNews(int limit, int offset) {
        // Логика получения новостей с пагинацией

        return List.of(); // Заглушка
    }

    @Override
    public NewsDto createNews(NewsDto newsDto, String token) {
        System.out.println("Токен в createNews: " + token);
        String role = jwtService.extractRole(token);
        System.out.println("Извлечённая роль: " + role);
        if (!"ORGANIZER".equals(role) && !"PILOT".equals(role)) {
            throw new CustomException(ErrorCodes.NO_ACCESS_TO_USER_DATA);
        }
        NewsEntity news = new NewsEntity();
        news.setContent(newsDto.getContent());
        UUID authorId = jwtService.extractUserId(token);
        System.out.println("Извлечённый authorId: " + authorId);
        news.setAuthorId(authorId);
        news.setCreatedAt(Instant.now());
        System.out.println("Сохраняем новость: " + news);
        NewsEntity savedNews = newsRepository.save(news);
        NewsDto savedNewsDto = new NewsDto();
        savedNewsDto.setId(savedNews.getId());
        savedNewsDto.setContent(savedNews.getContent());
        savedNewsDto.setAuthorId(savedNews.getAuthorId());
        savedNewsDto.setAuthorName(getAuthorName(savedNews.getAuthorId()));
        savedNewsDto.setCreatedAt(savedNews.getCreatedAt());
        return savedNewsDto;
    }

    private String getAuthorName(UUID authorId) {
        // Логика получения имени автора
        return "Author"; // Заглушка
    }
}
