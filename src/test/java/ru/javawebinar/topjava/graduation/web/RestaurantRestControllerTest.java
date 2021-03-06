package ru.javawebinar.topjava.graduation.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.service.RestaurantService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.graduation.TestUtil.readFromJson;
import static ru.javawebinar.topjava.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER1;
import static ru.javawebinar.topjava.graduation.web.json.JsonUtil.writeValue;

class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String ADMIN_URL = RestaurantRestController.ADMIN_URL + '/';
    private static final String USER_URL = RestaurantRestController.USER_URL + '/';

    @Autowired
    RestaurantService service;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("current-offers").clear();
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL + KFC.getId())
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
    void deleteRestricted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_URL + KFC.getId())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void delete() throws Exception {
        int id = service.create(getCreated()).getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_URL + id)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(service.get(updated.getId()), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant expected = getCreated();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andExpect(status().isCreated());

        Restaurant returned = readFromJson(action, Restaurant.class);
        expected.setId(returned.getId());
        assertMatch(returned, expected);
        assertMatch(service.getAll(), KFC, PRIME, MUMU, expected);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant expected = new Restaurant("");
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant updated = getUpdated();
        updated.setName("");
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Restaurant updated = new Restaurant(PRIME);
        updated.setName(KFC.getName());
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_URL + PRIME.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(updated)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Restaurant expected = new Restaurant(PRIME);
        expected.setId(null);
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andExpect(status().isConflict());
    }
}