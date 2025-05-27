package net.dunice.mstool.service.impl;

import net.dunice.mstool.DTO.request.NewsDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsParserServiceImplTest {

    @InjectMocks
    private NewsParserServiceImpl newsParserService;

    private Document mockDocument;
    private Elements mockNewsElements;
    private Element mockNewsElement;
    private static final String TEST_TITLE = "Test News Title";
    private static final String TEST_CONTENT = "Test News Content";
    private static final String TEST_DATE = "2024-03-20";
    private static final String TEST_IMAGE_URL = "https://example.com/image.jpg";
    private static final String TEST_URL = "https://yoklmnracing.ru/";

    @BeforeEach
    void setUp() {
        // Setup mock elements
        mockNewsElement = mock(Element.class);
        mockNewsElements = mock(Elements.class);
        mockDocument = mock(Document.class);

        // Setup mock selectors
        Element titleElement = mock(Element.class);
        Element contentElement = mock(Element.class);
        Element dateElement = mock(Element.class);
        Element imageElement = mock(Element.class);

        when(mockNewsElement.select(".card-title")).thenReturn(new Elements(titleElement));
        when(mockNewsElement.select(".card-text")).thenReturn(new Elements(contentElement));
        when(mockNewsElement.select(".text-muted")).thenReturn(new Elements(dateElement));
        when(mockNewsElement.select("img")).thenReturn(new Elements(imageElement));

        when(titleElement.text()).thenReturn(TEST_TITLE);
        when(contentElement.text()).thenReturn(TEST_CONTENT);
        when(dateElement.text()).thenReturn(TEST_DATE);
        when(imageElement.attr("src")).thenReturn(TEST_IMAGE_URL);

        when(mockNewsElements.iterator()).thenReturn(List.of(mockNewsElement).iterator());
        when(mockDocument.select(".card.mb-3")).thenReturn(mockNewsElements);
    }

    @Test
    void fetchNewsFromYoklmnracing_Success() {
        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            // Arrange
            jsoupMockedStatic.when(() -> Jsoup.connect(anyString()).get())
                    .thenReturn(mockDocument);

            // Act
            List<NewsDto> result = newsParserService.fetchNewsFromYoklmnracing();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            NewsDto news = result.get(0);
            assertEquals(TEST_TITLE, news.getTitle());
            assertEquals(TEST_CONTENT, news.getContent());
            assertEquals(TEST_IMAGE_URL, news.getImageUrl());

            // Verify Jsoup connection
            jsoupMockedStatic.verify(() -> Jsoup.connect(TEST_URL).get());
        }
    }

    @Test
    void fetchNewsFromYoklmnracing_EmptyResponse() {
        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            // Arrange
            when(mockDocument.select(".card.mb-3")).thenReturn(new Elements());
            jsoupMockedStatic.when(() -> Jsoup.connect(anyString()).get())
                    .thenReturn(mockDocument);

            // Act
            List<NewsDto> result = newsParserService.fetchNewsFromYoklmnracing();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void fetchNewsFromYoklmnracing_ConnectionError() {
        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            // Arrange
            jsoupMockedStatic.when(() -> Jsoup.connect(anyString()).get())
                    .thenThrow(new IOException("Connection error"));

            // Act
            List<NewsDto> result = newsParserService.fetchNewsFromYoklmnracing();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void fetchNewsFromYoklmnracing_MissingElements() {
        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            // Arrange
            Element emptyElement = mock(Element.class);
            when(emptyElement.select(anyString())).thenReturn(new Elements());
            when(mockNewsElements.iterator()).thenReturn(List.of(emptyElement).iterator());
            jsoupMockedStatic.when(() -> Jsoup.connect(anyString()).get())
                    .thenReturn(mockDocument);

            // Act
            List<NewsDto> result = newsParserService.fetchNewsFromYoklmnracing();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            NewsDto news = result.get(0);
            assertNull(news.getTitle());
            assertNull(news.getContent());
            assertNull(news.getImageUrl());
        }
    }

    @Test
    void fetchNewsFromYoklmnracing_MultipleNews() {
        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            // Arrange
            Element secondNewsElement = mock(Element.class);
            Element titleElement2 = mock(Element.class);
            Element contentElement2 = mock(Element.class);
            Element dateElement2 = mock(Element.class);
            Element imageElement2 = mock(Element.class);

            when(secondNewsElement.select(".card-title")).thenReturn(new Elements(titleElement2));
            when(secondNewsElement.select(".card-text")).thenReturn(new Elements(contentElement2));
            when(secondNewsElement.select(".text-muted")).thenReturn(new Elements(dateElement2));
            when(secondNewsElement.select("img")).thenReturn(new Elements(imageElement2));

            when(titleElement2.text()).thenReturn("Second News Title");
            when(contentElement2.text()).thenReturn("Second News Content");
            when(dateElement2.text()).thenReturn("2024-03-21");
            when(imageElement2.attr("src")).thenReturn("https://example.com/image2.jpg");

            when(mockNewsElements.iterator()).thenReturn(List.of(mockNewsElement, secondNewsElement).iterator());
            jsoupMockedStatic.when(() -> Jsoup.connect(anyString()).get())
                    .thenReturn(mockDocument);

            // Act
            List<NewsDto> result = newsParserService.fetchNewsFromYoklmnracing();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());

            NewsDto firstNews = result.get(0);
            assertEquals(TEST_TITLE, firstNews.getTitle());
            assertEquals(TEST_CONTENT, firstNews.getContent());
            assertEquals(TEST_IMAGE_URL, firstNews.getImageUrl());

            NewsDto secondNews = result.get(1);
            assertEquals("Second News Title", secondNews.getTitle());
            assertEquals("Second News Content", secondNews.getContent());
            assertEquals("https://example.com/image2.jpg", secondNews.getImageUrl());
        }
    }
} 