package net.dunice.mstool.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.NewsEntity;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.NewsRepository;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private NewsDto newsDto;
    private NewsEntity newsEntity;
    private UserEntity userEntity;
    private UUID userId;
    private UUID newsId;
    private String token;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USERNAME = "Test User";
    private static final String TEST_TITLE = "Test News";
    private static final String TEST_CONTENT = "Test Content";
    private static final String TEST_IMAGE_URL = "https://example.com/image.jpg";
    private static final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        newsId = UUID.randomUUID();
        token = TEST_TOKEN;

        // Setup user
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setUsername(TEST_USERNAME);

        // Setup news DTO
        newsDto = new NewsDto();
        newsDto.setTitle(TEST_TITLE);
        newsDto.setContent(TEST_CONTENT);
        newsDto.setImageUrl(TEST_IMAGE_URL);

        // Setup news entity
        newsEntity = new NewsEntity();
        newsEntity.setId(newsId);
        newsEntity.setTitle(TEST_TITLE);
        newsEntity.setContent(TEST_CONTENT);
        newsEntity.setImageUrl(TEST_IMAGE_URL);
        newsEntity.setAuthorId(userId);
        newsEntity.setCreatedAt(Instant.now());
    }

    @Test
    void getAllNews_Success() {
        // Arrange
        int limit = 10;
        int offset = 0;
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<NewsEntity> newsPage = new PageImpl<>(List.of(newsEntity), pageable, 1);
        when(newsRepository.findAll(pageable)).thenReturn(newsPage);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        List<NewsDto> result = newsService.getAllNews(limit, offset);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        NewsDto resultNews = result.get(0);
        assertEquals(TEST_TITLE, resultNews.getTitle());
        assertEquals(TEST_CONTENT, resultNews.getContent());
        assertEquals(TEST_IMAGE_URL, resultNews.getImageUrl());
        assertEquals(TEST_USERNAME, resultNews.getAuthorName());
    }

    @Test
    void getAllNews_EmptyResult() {
        // Arrange
        int limit = 10;
        int offset = 0;
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<NewsEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(newsRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        List<NewsDto> result = newsService.getAllNews(limit, offset);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createNews_Success() {
        // Arrange
        when(jwtService.getEmailFromToken(token)).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(userEntity));
        when(newsRepository.save(any(NewsEntity.class))).thenReturn(newsEntity);

        // Act
        NewsDto result = newsService.createNews(newsDto, token);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_CONTENT, result.getContent());
        assertEquals(TEST_IMAGE_URL, result.getImageUrl());
        verify(newsRepository).save(any(NewsEntity.class));
    }

    @Test
    void createNews_UserNotFound() {
        // Arrange
        when(jwtService.getEmailFromToken(token)).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> newsService.createNews(newsDto, token));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(newsRepository, never()).save(any());
    }

    @Test
    void convertToDto_Success() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        NewsDto result = newsService.convertToDto(newsEntity);

        // Assert
        assertNotNull(result);
        assertEquals(newsId, result.getId());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_CONTENT, result.getContent());
        assertEquals(TEST_IMAGE_URL, result.getImageUrl());
        assertEquals(userId, result.getAuthorId());
        assertEquals(TEST_USERNAME, result.getAuthorName());
        assertEquals(newsEntity.getCreatedAt(), result.getDate());
    }

    @Test
    void convertToDto_UnknownAuthor() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        NewsDto result = newsService.convertToDto(newsEntity);

        // Assert
        assertNotNull(result);
        assertEquals("Unknown Author", result.getAuthorName());
    }

    @Test
    void getMonthName_Success() {
        // Test for different months
        assertEquals("январь", newsService.getMonthName(1));
        assertEquals("февраль", newsService.getMonthName(2));
        assertEquals("март", newsService.getMonthName(3));
        assertEquals("апрель", newsService.getMonthName(4));
        assertEquals("май", newsService.getMonthName(5));
        assertEquals("июнь", newsService.getMonthName(6));
        assertEquals("июль", newsService.getMonthName(7));
        assertEquals("август", newsService.getMonthName(8));
        assertEquals("сентябрь", newsService.getMonthName(9));
        assertEquals("октябрь", newsService.getMonthName(10));
        assertEquals("ноябрь", newsService.getMonthName(11));
        assertEquals("декабрь", newsService.getMonthName(12));
    }

    @Test
    void getAuthorName_Success() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        String result = newsService.getAuthorName(userId);

        // Assert
        assertEquals(TEST_USERNAME, result);
    }

    @Test
    void getAuthorName_UserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        String result = newsService.getAuthorName(userId);

        // Assert
        assertEquals("Unknown Author", result);
    }
} 