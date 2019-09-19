package ru.javawebinar.topjava.graduation.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.graduation.model.Offer;
import ru.javawebinar.topjava.graduation.service.OfferService;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.graduation.testdata.OfferTestData.PRIME_01;
import static ru.javawebinar.topjava.graduation.testdata.OfferTestData.PRIME_01_01;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.PRIME;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER1;
import static ru.javawebinar.topjava.graduation.web.json.JsonUtil.writeValue;

class OfferRestControllerTest extends AbstractControllerTest {

    private static final String ADMIN_URL = "/rest/admin/restaurants/" + PRIME.getId() + "/offers/";
    private static final String USER_URL = "/rest/profile/restaurants/" + PRIME.getId() + "/offers/";

    @Autowired
    OfferService service;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL + PRIME_01_01.getId())
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL + 1)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create() throws Exception {
        List<Offer> offers = new LinkedList<>();
        PRIME_01.forEach(o -> offers.add(new Offer(null, o.getCourse(), o.getRestaurant(), o.getDate(), o.getPrice())));

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(offers)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(offers)))
                .andExpect(status().isCreated());

        assert service.getBetweenDates(PRIME.getId(), LocalDate.now(), LocalDate.now()).size() == PRIME_01.size();
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBetween() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL + "/filter?startDate=2019-08-02&endDate=")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}