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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final JwtService jwtUtil;

    @Override
    public List<NewsDto> getAllNews(int limit, int offset) {
        // Логика получения новостей с пагинацией

        return List.of(); // Заглушка
    }

    @Override
    public NewsDto createNews(NewsDto newsDto, String token) {
        // Проверка роли
        String role = jwtUtil.extractRole(token);
        if (!"ORGANIZER".equals(role) && !"PILOT".equals(role)) {
            throw new CustomException(ErrorCodes.CODE_NOT_NULL);
        }

        // Маппинг в сущность
        NewsEntity news = new NewsEntity();
        news.setContent(newsDto.getContent());
        //news.setAuthorId(jwtUtil.extractUserId(token));
        news.setCreatedAt(Instant.now());

        // Сохранение
        NewsEntity savedNews = newsRepository.save(news);

        // Маппинг обратно в DTO
        NewsDto savedNewsDto = new NewsDto();
        savedNewsDto.setId(savedNews.getId());
        savedNewsDto.setContent(savedNews.getContent());
        //savedNewsDto.setAuthorId(savedNews.getAuthorId());
        //savedNewsDto.setAuthorName(getAuthorName(savedNews.getAuthorId())); // Предполагается метод получения имени
        savedNewsDto.setCreatedAt(savedNews.getCreatedAt());

        return savedNewsDto;
    }

    private String getAuthorName(UUID authorId) {
        // Логика получения имени автора
        return "Author"; // Заглушка
    }
}
