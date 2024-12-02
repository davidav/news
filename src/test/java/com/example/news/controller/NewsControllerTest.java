package com.example.news.controller;

import com.example.news.StringTestUtils;
import com.example.news.dto.news.UpsertNewsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NewsControllerTest extends AbstractTest {

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUpdateNewsRequestFromAdmin_thenReturnUnauthorized()throws Exception {

        UpsertNewsRequest request = new UpsertNewsRequest();
        request.setCategoryId(1L);
        request.setTitle("Update title news by admin");
        request.setText("Update text news by admin. Author news is user. Update text");

        mockMvc.perform(put("/api/news/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "moderator",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUpdateNewsRequestFromModerator_thenReturnUnauthorized()throws Exception {

        UpsertNewsRequest request = new UpsertNewsRequest();
        request.setCategoryId(1L);
        request.setTitle("Update title news by moderator");
        request.setText("Update text news by moderator. Author news is user. Update text");

        mockMvc.perform(put("/api/news/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "user",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUpdateNewsRequestFromAuthor_ReturnUpdatedNews() throws Exception {

        UpsertNewsRequest request = new UpsertNewsRequest();
        request.setCategoryId(1L);
        request.setTitle("Update title news by user");
        request.setText("Update text news by user. Author news is user. Update text");

        mockMvc.perform(put("/api/news/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(StringTestUtils.readStringFromResource(
                        "response/updateNewsRequestFromAuthor.json")));

    }


}