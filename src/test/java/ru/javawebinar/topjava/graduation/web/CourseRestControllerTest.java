package ru.javawebinar.topjava.graduation.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Course;
import ru.javawebinar.topjava.graduation.service.CourseService;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.graduation.TestUtil.readFromJson;
import static ru.javawebinar.topjava.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.graduation.testdata.CourseTestData.*;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.PRIME;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER1;
import static ru.javawebinar.topjava.graduation.web.json.JsonUtil.writeValue;

class CourseRestControllerTest extends AbstractControllerTest {

    private static final String ADMIN_URL = "/rest/admin/restaurants/" + PRIME.getId() + "/courses/";
    private static final String USER_URL = "/rest/profile/restaurants/" + PRIME.getId() + "/courses/";

    @Autowired
    CourseService service;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL + PRIME_01_DRINK.getId())
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
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_URL + PRIME_01_DRINK.getId())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void delete() throws Exception {
        int id = service.create(getCreated(), PRIME.getId()).getId();
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
        Course updated = getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(service.get(updated.getId(), PRIME.getId()), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Course expected = getCreated();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andExpect(status().isCreated());

        Course returned = readFromJson(action, Course.class);
        expected.setId(returned.getId());
        assertMatch(returned, expected);
        List<Course> expectedList = new LinkedList<>(PRIME_COURSES);
        expectedList.add(expected);
        assertMatch(service.getAll(PRIME.getId()), expectedList);
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
        Course expected = new Course("", PRIME);
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        Course updated = getUpdated();
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
        Course updated = new Course(PRIME_01_DRINK);
        updated.setName(PRIME_02_DRINK.getName());
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_URL + PRIME_01_DRINK.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(updated)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Course expected = new Course(PRIME_01_DRINK);
        expected.setId(null);
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andExpect(status().isConflict());
    }
}