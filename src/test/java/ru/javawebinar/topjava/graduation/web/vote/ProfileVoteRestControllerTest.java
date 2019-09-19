package ru.javawebinar.topjava.graduation.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.PRIME;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER1;
import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.RESTRICTION_HOUR;
import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.TIMEZONE;

class ProfileVoteRestControllerTest extends AbstractVoteRestControllerTest {

    static {
        REST_URL = "/rest/profile/votes/";
        USER = USER1;
    }

    @Test
    void vote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL).param("restaurantId", PRIME.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(ZonedDateTime.now(TIMEZONE).getHour() >= RESTRICTION_HOUR ?
                        status().isUnavailableForLegalReasons() : status().isCreated());
    }
}