package net.dunice.mstool.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.NewsEntity;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.NewsRepository;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.security.JwtService;
import net.dunice.mstool.service.NewsService;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final Logger log = Logger.getLogger(NewsServiceImpl.class.getName());

    @Override
    public List<NewsDto> getAllNews(int limit, int offset) {
        // Логика получения новостей с пагинацией
        return List.of(); // Заглушка
    }

    @Override
    public NewsDto createNews(NewsDto newsDto, String token) {
        String email = jwtService.getEmailFromToken(token);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        
        NewsEntity news = new NewsEntity();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setImageUrl(newsDto.getImageUrl());
        news.setAuthorId(user.getId());
        news.setCreatedAt(Instant.now());
        
        NewsEntity savedNews = newsRepository.save(news);
        return convertToDto(savedNews);
    }

    private void updateNewsJson(NewsEntity news) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            LocalDate date = news.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
            String fileName = String.format("news/%d/%02d_%s.json",
                    date.getYear(), date.getMonthValue(), getMonthName(date.getMonthValue()));

            File newsFile = new File(fileName);
            newsFile.getParentFile().mkdirs();

            NewsContainer container = newsFile.exists() ?
                    mapper.readValue(newsFile, NewsContainer.class) :
                    new NewsContainer(new ArrayList<>());

            container.getNews().add(convertToDto(news));
            mapper.writeValue(newsFile, container);
        } catch (IOException e) {
            throw new CustomException(ErrorCodes.UNKNOWN);
        }
    }

    private NewsDto convertToDto(NewsEntity entity) {
        NewsDto dto = new NewsDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setAuthorId(entity.getAuthorId());
        dto.setAuthorName(getAuthorName(entity.getAuthorId()));
        dto.setDate(entity.getCreatedAt());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }

    private String getMonthName(int month) {
        return Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
    }

    private String getAuthorName(UUID authorId) {
        // Логика получения имени автора
        return "Author"; // Заглушка
    }

    private static class NewsContainer {
        private List<NewsDto> news;

        public NewsContainer(List<NewsDto> news) {
            this.news = news;
        }

        public List<NewsDto> getNews() {
            return news;
        }

        public void setNews(List<NewsDto> news) {
            this.news = news;
        }
    }
}
