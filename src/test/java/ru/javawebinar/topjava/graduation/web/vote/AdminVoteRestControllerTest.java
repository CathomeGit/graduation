package ru.javawebinar.topjava.graduation.web.vote;

import static ru.javawebinar.topjava.graduation.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER1;

class AdminVoteRestControllerTest extends AbstractVoteRestControllerTest {

    static {
        REST_URL = "/rest/admin/users/" + USER1.getId() + "/votes/";
        USER = ADMIN;
    }
}