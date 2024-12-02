package com.example.news.service;

import com.example.news.aop.NewsDeleteAvailable;
import com.example.news.aop.NewsEditAvailable;
import com.example.news.dto.PagesRequest;
import com.example.news.model.News;
import com.example.news.repository.NewsRepository;
import com.example.news.util.AppHelperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Component
@RequiredArgsConstructor
@Slf4j
 public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final UserService userService;
    private final CategoryService categoryService;



    @Override
    public List<News> findAll(PagesRequest request) {
        return newsRepository.findAll(
                PageRequest.of(request.getPageNumber(), request.getPageSize())).getContent();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormatter.format("Новость с id {} не найдена", id).getMessage()));
    }

    @Override
    @Transactional
    public News save(News news, UserDetails userDetails) {
        News savedNews = newsRepository.save(news);
        userService.update(news.getUser(), userDetails);
        categoryService.update(news.getCategory());

        return savedNews;
    }

    @Override
    @NewsEditAvailable
    public News update(News requestNews) {
        News existedNews = findById(requestNews.getId());
        AppHelperUtils.copyNonNullProperties(requestNews, existedNews);

        return newsRepository.save(existedNews);
    }

    @Override
    @NewsDeleteAvailable
    public void deleteById(Long id, UserDetails userDetails) {
        newsRepository.deleteById(id);
    }
}
