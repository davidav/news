package com.example.news.service;

import com.example.news.dto.PagesRequest;
import com.example.news.model.News;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsService {
    List<News> findAll(PagesRequest request);

    News findById(Long id);

    @Transactional
    News save(News news, UserDetails userDetails);

    News update(News news);

    void deleteById(Long id, UserDetails userDetails);

}
