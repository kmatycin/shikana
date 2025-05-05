package net.dunice.mstool.service;

import net.dunice.mstool.DTO.request.NewsDto;

import java.util.List;

public interface NewsParserService {
    List<NewsDto> fetchNewsFromYoklmnracing();
}
