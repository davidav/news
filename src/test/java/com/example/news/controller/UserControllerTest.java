package com.example.news.controller;

import com.example.news.StringTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractTest {

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenFindAllRequestFromAdmin_thenReturnAllUsers() throws Exception {

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        StringTestUtils.readStringFromResource("response/find_all_users_response.json")));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "moderator",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenFindAllRequestFromModerator_thenReturnForbidden() throws Exception {

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "user",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenFindAllRequestFromUser_thenReturnForbidden() throws Exception {

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenFindAllRequestFromNoLogin_thenReturnUnauthorized() throws Exception {

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isUnauthorized());
    }

}
