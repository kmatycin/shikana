package net.dunice.mstool.service;

import net.dunice.mstool.DTO.request.NewsDto;
import java.util.List;

public interface NewsService {
    List<NewsDto> getAllNews(int limit, int offset);

    NewsDto createNews(NewsDto newsDto, String token);
}
