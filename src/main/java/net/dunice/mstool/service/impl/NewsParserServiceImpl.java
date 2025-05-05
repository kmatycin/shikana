package net.dunice.mstool.service.impl;

// ... existing imports ...
import net.dunice.mstool.DTO.request.NewsDto;
import net.dunice.mstool.service.NewsParserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsParserServiceImpl implements NewsParserService {

    public List<NewsDto> fetchNewsFromYoklmnracing() {
        List<NewsDto> newsList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://yoklmnracing.ru/").get();
            Elements newsElements = doc.select(".card.mb-3");

            for (Element el : newsElements) {
                String title = el.select(".card-title").text();
                String content = el.select(".card-text").text();
                String date = el.select(".text-muted").text(); // или другой селектор для даты
                String imageUrl = el.select("img").attr("src");

                NewsDto dto = new NewsDto()
                        .setTitle(title)
                        .setContent(content)
                        .setImageUrl(imageUrl);
                // Если нужно, распарсите дату и положите в dto.setDate(...)
                newsList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }
}
