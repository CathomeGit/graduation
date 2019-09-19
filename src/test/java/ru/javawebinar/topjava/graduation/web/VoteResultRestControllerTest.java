package ru.javawebinar.topjava.graduation.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER1;
import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.RESTRICTION_HOUR;
import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.TIMEZONE;

class VoteResultRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/rest/vote-results/";

    @Test
    void getCurrent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andExpect(ZonedDateTime.now(TIMEZONE).getHour() >= RESTRICTION_HOUR ?
                        status().isOk() : status().isLocked())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getHistorical() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "history?date=2019-08-02")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}