package net.dunice.mstool.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.EventEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.EventRepository;
import net.dunice.mstool.security.JwtService;
import net.dunice.mstool.service.EventService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final JwtService jwtUtil;

    @Override
    public List<EventDto> getAllEvents(int limit, int offset) {
        // Здесь будет логика получения событий с пагинацией из репозитория
        // Например: eventRepository.findAll(PageRequest.of(offset / limit, limit))
        // Маппинг в EventDto опущен для краткости
        return List.of(); // Заглушка
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
                }

                // Таблица
                Elements rows = el.select("table tbody tr");
                if (rows.size() > 0) {
                    dto.setGame(rows.get(0).select("td").get(1).text()); // Нужно добавить поле game в EventDto
                }
                if (rows.size() > 1) {
                    dto.setStages(rows.get(1).text().trim()); // Нужно добавить поле stages в EventDto
                }
                if (rows.size() > 2) {
                    dto.setOrganizer(rows.get(2).text().trim()); // Нужно добавить поле organizer в EventDto
                }
                if (rows.size() > 3) {
                    dto.setDate(rows.get(3).text().trim()); // Здесь текстовый формат дат, потом можно преобразовать
                }

                eventList.add(dto);
            }

            return eventList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Ошибка при парсинге внешних событий", e);
        }
    }

    public EventDto createEvent(EventDto eventDto, String token) {
        // Проверка роли пользователя
        String role = jwtUtil.extractRole(token);
        if (!"ORGANIZER".equals(role)) {
            throw new CustomException(ErrorCodes.CODE_NOT_NULL);
        }

        // Маппинг DTO в сущность
        EventEntity event = new EventEntity();
        event.setTitle(eventDto.getTitle());
        event.setDate(eventDto.getDate());
        event.setLocation(eventDto.getLocation());
        event.setStatus("UPCOMING");

        // Сохранение в БД
        EventEntity savedEvent = eventRepository.save(event);

        // Маппинг обратно в DTO
        EventDto savedEventDto = new EventDto();
        savedEventDto.setId(savedEvent.getId());
        savedEventDto.setTitle(savedEvent.getTitle());
        savedEventDto.setDate(savedEvent.getDate());
        savedEventDto.setLocation(savedEvent.getLocation());
        savedEventDto.setStatus(savedEvent.getStatus());

        return savedEventDto;
    }

    public List<NewsDto> loadNews(String year, String month) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File newsFile = new File("news/" + year + "/" + month + ".json");
        NewsContainer container = objectMapper.readValue(newsFile, NewsContainer.class);
        return container.getNews();
    }

    @Data
    private static class NewsContainer {
        private List<NewsDto> news;
    }
}
