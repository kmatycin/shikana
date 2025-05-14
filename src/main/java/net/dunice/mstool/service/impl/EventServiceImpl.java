package net.dunice.mstool.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.EventEntity;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mappers.EventMapper;
import net.dunice.mstool.repository.EventRepository;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.security.JwtTokenProvider;
import net.dunice.mstool.service.EventService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public List<EventDto> getAllEvents(int limit, int offset) {
        return eventRepository.findAll(PageRequest.of(offset, limit))
                .stream()
                .filter(event -> !event.isExternal())
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getExternalEvents() {
        return eventRepository.findAll()
                .stream()
                .filter(EventEntity::isExternal)
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto, String token) {
        UserEntity user = getUserFromToken(token);
        
        EventEntity event = eventMapper.toEntity(eventDto);
        event.setCreatedBy(user);
        
        // Set default status if not provided
        if (event.getStatus() == null) {
            event.setStatus("UPCOMING");
        }
        
        EventEntity savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional
    public EventDto updateEvent(UUID id, EventDto eventDto, String token) {
        UserEntity user = getUserFromToken(token);
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.EVENT_NOT_FOUND));

        // Проверяем, является ли пользователь создателем события
        if (!event.getCreatedBy().getId().equals(user.getId())) {
            throw new CustomException(ErrorCodes.FORBIDDEN);
        }

        eventMapper.updateEntityFromDto(eventDto, event);
        event.setUpdatedBy(user);

        EventEntity updatedEvent = eventRepository.save(event);
        return eventMapper.toDto(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id, String token) {
        UserEntity user = getUserFromToken(token);
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.EVENT_NOT_FOUND));

        // Проверяем, является ли пользователь создателем события
        if (!event.getCreatedBy().getId().equals(user.getId())) {
            throw new CustomException(ErrorCodes.FORBIDDEN);
        }

        eventRepository.delete(event);
    }

    @Override
    public List<EventDto> parseExternalEvents() throws IOException {
        try {
            Document doc = Jsoup.connect("https://yoklmnracing.ru/championships").get();
            Elements cardElements = doc.select(".card-body");

            List<EventDto> eventList = new java.util.ArrayList<>();

            for (Element el : cardElements) {
                EventDto dto = new EventDto();

                // Заголовок и ссылка
                Element titleElement = el.selectFirst("h1.card-title a");
                if (titleElement != null) {
                    dto.setTitle(titleElement.text());
                    dto.setLocation(titleElement.attr("href")); // Можно сохранить ссылку как location
                }

                // Статус регистрации
                Element registrationBadge = el.selectFirst(".badge.text-bg-primary");
                if (registrationBadge != null) {
                    dto.setStatus(registrationBadge.text());
                } else {
                    dto.setStatus("UPCOMING"); // Default status
                }

                // Таблица
                Elements rows = el.select("table tbody tr");
                if (rows.size() > 0) {
                    dto.setGame(rows.get(0).select("td").get(1).text());
                }
                if (rows.size() > 1) {
                    dto.setStages(rows.get(1).text().trim());
                }
                if (rows.size() > 2) {
                    dto.setOrganizer(rows.get(2).text().trim());
                }
                if (rows.size() > 3) {
                    dto.setDate(rows.get(3).text().trim());
                }

                dto.setExternal(true); // Mark as external event
                eventList.add(dto);
            }

            return eventList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Ошибка при парсинге внешних событий", e);
        }
    }

    public List<NewsDto> loadNews(String year, String month) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File newsFile = new File("news/" + year + "/" + month + ".json");
        NewsContainer container = objectMapper.readValue(newsFile, NewsContainer.class);
        return container.getNews();
    }

    private UserEntity getUserFromToken(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
    }

    @Data
    private static class NewsContainer {
        private List<NewsDto> news;
    }
}
