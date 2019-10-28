## TopJava graduation project
Author - Tatiana Danilina
##Running the application
 * build package with Maven `mvn package`
 * Deploy `graduation.war` with Tomcat at `localhost:8080/graduation`
 * Run REST requests listed below
### Application description
A voting system for deciding where to have lunch with REST API.\
Admins manage restaurants and upload its' menus daily. Users vote for a restaurant to have lunch at until 11:00.\
Voting results can be viewed after the voting process is finished. Historical results can be requested on date.
 * There are 2 user roles: admins and regular users. Unauthorized access is prohibited. 
 * Admin section is accessible by `/rest/admin` path and user section under `/rest/profile` path. Mappings under `/rest` are accessible with any role.
 * The following resources are accessible:
    - restaurants;
    - restaurant dishes;
    - restaurant daily offers;
    - users;
    - user votes
 * Admins have the following functions:
    - viewing, adding, updating, enabling/disabling and deleting users.
    - viewing, adding, updating, deleting restaurants. If restaurant has votes it can't be deleted.
    - viewing, adding, updating, deleting dishes. If a course is included in an offer it can't be deleted.
    - viewing and uploading restaurant daily offers. Offers are bulk uploaded per restaurant. Non-existent dishes are created or existing ones are retrieved by name and restaurant id otherwise. In case of repeated post action previously uploaded offers are replaced with new ones.
    - viewing votes of any user.
    - viewing vote results after 11:00.
 * Users have the following abilities:
    - viewing restaurants, its' dishes and offers.
    - voting for a restaurant before 11:00. If vote is posted repeatedly previous vote is replaced.
    - viewing own votes.
    - viewing vote results after 11:00.
Time is defined by default server system timezone.
### Curl commands to REST API
(application deployed in application context `graduation`)
#### AdminUserRestController

| Role | Path                 |  Method | Description   |
|------|----------------------|---------|---------------|
| Admin| [`/rest/admin/users`]|   `GET` | Get all users |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/admin/users --user admin@gmail.com:admin`

| Role | Path                      |  Method | Description    |
|------|---------------------------|---------|----------------|
| Admin| [`/rest/admin/users/{id}`]|   `GET` | Get user by id |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/admin/users/100000 --user admin@gmail.com:admin`

| Role | Path                 |  Method  | Description |
|------|----------------------|----------|-------------|
| Admin| [`/rest/admin/users`]|   `POST` | Create user |
* **Example:**

`curl -s -X POST -d '{"name":"New user","email":"yan@ya.ru","password":"12345","roles":["ROLE_USER"]}' -H 'Content-Type:application/json' http://localhost:8080/graduation/rest/admin/users --user admin@gmail.com:admin`\
Create with error: `curl -s -X POST -d '{"name":"User1","email":"admin1@gmail.com","password":"","roles":["ROLE_USER"]}' -H 'Content-type: application/json' http://localhost:8080/graduation/rest/admin/users --user admin@gmail.com:admin`

| Role | Path                      |  Method | Description |
|------|---------------------------|---------|-------------|
| Admin| [`/rest/admin/users/{id}`]|   `PUT` | Update user |
* **Example:**

`curl -s -X PUT -d '{"id":100002,"name":"Updated","email":"admin@gmail.com","roles":["ROLE_USER","ROLE_ADMIN"]}' -H 'Content-type: application/json' http://localhost:8080/graduation/rest/admin/users/100002 --user admin@gmail.com:admin`

| Role | Path                      |  Method   | Description        |
|------|---------------------------|-----------|--------------------|
| Admin| [`/rest/admin/users/{id}`]|   `PATCH` | En-/disable user   |

* **URL Params**\
    enabled=boolean
* **Example:**

`curl -s -X PATCH http://localhost:8080/graduation/rest/admin/users/100000?enabled=false --user admin@gmail.com:admin`

| Role | Path                      |  Method | Description |
|------|---------------------------|---------|-------------|
| Admin| [`/rest/admin/users/{id}`]|`DELETE` | Delete user |
* **Example:**

`curl -s -X DELETE http://localhost:8080/graduation/rest/admin/users/100000 --user admin@gmail.com:admin`

#### RestaurantRestController

| Role | Path                         |  Method | Description         |
|------|------------------------------|---------|---------------------|
| Admin| [`/rest/admin/restaurants`]  |   `GET` | Get all restaurants |
| User | [`/rest/profile/restaurants`]|   `GET` | Get all restaurants |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/restaurants --user ivan-i@ya.ru:qwerty`

| Role | Path                              |  Method | Description          |
|------|-----------------------------------|---------|----------------------|
| Admin| [`/rest/admin/restaurants/{id}`]  |   `GET` | Get restaurant by id |
| User | [`/rest/profile/restaurants/{id}`]|   `GET` | Get restaurant by id |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/restaurants/100003 --user ivan-i@ya.ru:qwerty`

| Role | Path                                        |  Method | Description            |
|------|---------------------------------------------|---------|------------------------|
| Admin| [`/rest/admin/restaurants/current-offers`]  |   `GET` | Get all current offers |
| User | [`/rest/profile/restaurants/current-offers`]|   `GET` | Get all current offers |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/restaurants/current-offers --user ivan-i@ya.ru:qwerty`

| Role | Path                       |  Method  | Description       |
|------|----------------------------|----------|-------------------|
| Admin| [`/rest/admin/restaurants`]|   `POST` | Create restaurant |
* **Example:**

`curl -s -X POST -d '{"name":"New restaurant"}' -H 'Content-Type:application/json' http://localhost:8080/graduation/rest/admin/restaurants --user admin@gmail.com:admin`\
Create with error: `curl -s -X POST -d '{"name":""}' -H 'Content-Type:application/json' http://localhost:8080/graduation/rest/admin/restaurants --user admin@gmail.com:admin`

| Role | Path                            |  Method | Description       |
|------|---------------------------------|---------|-------------------|
| Admin| [`/rest/admin/restaurants/{id}`]|   `PUT` | Update restaurant |
* **Example:**

`curl -s -X PUT -d '{"id":100003,"name":"Updated"}' -H 'Content-type: application/json' http://localhost:8080/graduation/rest/admin/restaurants/100003 --user admin@gmail.com:admin`

| Role | Path                            |  Method | Description       |
|------|---------------------------------|---------|-------------------|
| Admin| [`/rest/admin/restaurants/{id}`]|`DELETE` | Delete restaurant |
* **Example:**

`curl -s -X DELETE http://localhost:8080/graduation/rest/admin/restaurants/100044 --user admin@gmail.com:admin`
#### CourseRestController

| Role | Path                                                |  Method | Description     |
|------|-----------------------------------------------------|---------|-----------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/dishes`]  |   `GET` | Get all dishes |
| User | [`/rest/profile/restaurants/{restaurantId}/dishes`]|   `GET` | Get all dishes |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/restaurants/100003/dishes --user ivan-i@ya.ru:qwerty`

| Role | Path                                                     |  Method | Description      |
|------|-----------------------------------|----------------------|----------------------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/dishes/{id}`]  |   `GET` | Get course by id |
| User | [`/rest/profile/restaurants/{restaurantId}/dishes/{id}`]|   `GET` | Get course by id |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/restaurants/100003/dishes/100013 --user ivan-i@ya.ru:qwerty`

| Role | Path                                              |  Method  | Description   |
|------|---------------------------------------------------|----------|---------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/dishes`]|   `POST` | Create course |
* **Example:**

`curl -s -X POST -d '{"name":"New position"}' -H 'Content-Type:application/json' http://localhost:8080/graduation/rest/admin/restaurants/100003/dishes --user admin@gmail.com:admin`\
Create with error: `curl -s -X POST -d '{"name":" "}' -H 'Content-Type:application/json' http://localhost:8080/graduation/rest/admin/restaurants/100003/dishes --user admin@gmail.com:admin`

| Role | Path                                                   |  Method | Description   |
|------|--------------------------------------------------------|---------|---------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/dishes/{id}`]|   `PUT` | Update course |
* **Example:**

`curl -s -X PUT -d '{"id":100013,"name":"Updated"}' -H 'Content-type: application/json' http://localhost:8080/graduation/rest/admin/restaurants/100003/dishes/100013 --user admin@gmail.com:admin`

| Role | Path                                                   |  Method | Description   |
|------|--------------------------------------------------------|---------|---------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/dishes/{id}`]|`DELETE` | Delete course |
* **Example:**

`curl -s -X DELETE http://localhost:8080/graduation/rest/admin/restaurants/100003/dishes/100044 --user admin@gmail.com:admin`
#### OfferRestController

| Role | Path                                               |  Method | Description    |
|------|----------------------------------------------------|---------|----------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/offers`]  |   `GET` | Get all offers |
| User | [`/rest/profile/restaurants/{restaurantId}/offers`]|   `GET` | Get all offers |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/restaurants/100003/offers --user ivan-i@ya.ru:qwerty`

| Role | Path                                                    |  Method | Description     |
|------|---------------------------------------------------------|---------|-----------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/offers/{id}`]  |   `GET` | Get offer by id |
| User | [`/rest/profile/restaurants/{restaurantId}/offers/{id}`]|   `GET` | Get offer by id |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/restaurants/100003/offers/100031 --user ivan-i@ya.ru:qwerty`

| Role | Path                                                      |  Method | Description                  |
|------|-----------------------------------------------------------|---------|------------------------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/offers/filter`]  |   `GET` | Get offers for a date period |
| User | [`/rest/profile/restaurants/{restaurantId}/offers/filter`]|   `GET` | Get offers for a date period |

* **URL Params**\
    startDate=yyyy-mm-dd\
    endDate=yyyy-mm-dd
* **Example:**

`curl -s 'http://localhost:8080/graduation/rest/profile/restaurants/100003/offers/filter?startDate=2019-09-01&endDate=2019-09-30' --user ivan-i@ya.ru:qwerty`

| Role | Path                                              |  Method  | Description             |
|------|---------------------------------------------------|----------|-------------------------|
| Admin| [`/rest/admin/restaurants/{restaurantId}/offers`] |   `POST` | Upload restaurant offers |
* **Example:**

`curl -s -X POST -d '[{"course":{"name":"New soup"},"price":100.80},{"course":{"name":"New main course"},"price":151.50},{"course":{"name":"New drink"},"price":120.20}]' -H 'Content-Type:application/json' http://localhost:8080/graduation/rest/admin/restaurants/100003/offers --user admin@gmail.com:admin`

#### ProfileVoteRestController, AdminVoteRestController

| Role | Path                                  |  Method | Description             |
|------|---------------------------------------|---------|-------------------------|
| Admin| [`/rest/admin/users/{userId}/votes`]  |   `GET` | Get all votes of a user |
| User | [`/rest/profile/votes`]               |   `GET` | Get all votes of a user |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/votes --user ivan-i@ya.ru:qwerty`

| Role | Path                                       |  Method | Description            |
|------|--------------------------------------------|---------|------------------------|
| Admin| [`/rest/admin/users/{userId}/votes/{id}`]  |   `GET` | Get one vote of a user |
| User | [`/rest/profile/votes/{id}`]               |   `GET` | Get one vote of a user |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/profile/votes/100009 --user ivan-i@ya.ru:qwerty`

| Role | Path                                         |  Method | Description                 |
|------|----------------------------------------------|---------|-----------------------------|
| Admin| [`/rest/admin/users/{userId}/votes/filter`]  |   `GET` | Get votes for a date period |
| User | [`/rest/profile/votes/filter`]               |   `GET` | Get votes for a date period |

* **URL Params**\
    startDate=yyyy-mm-dd\
    endDate=yyyy-mm-dd
* **Example:**

`curl -s 'http://localhost:8080/graduation/rest/profile/votes/filter?startDate=2019-08-02&endDate=2019-08-31' --user ivan-i@ya.ru:qwerty`

| Role | Path                   |  Method  | Description           |
|------|------------------------|----------|-----------------------|
| User | [`/rest/profile/votes`]|   `POST` | Vote for a restaurant |
* **Example:**

`curl -s -X POST http://localhost:8080/graduation/rest/profile/votes?restaurantId=100003 --user ivan-i@ya.ru:qwerty`

#### VoteResultRestController

| Role       | Path                    |  Method | Description               |
|------------|-------------------------|---------|---------------------------|
| Admin, User| [`/rest/vote-results`]  |   `GET` | Get current voting result |
* **Example:**

`curl -s http://localhost:8080/graduation/rest/vote-results --user ivan-i@ya.ru:qwerty`

| Role       | Path                            |  Method | Description                  |
|------------|---------------------------------|---------|------------------------------|
| Admin, User| [`/rest/vote-results/history`]  |   `GET` | Get historical voting result |

* **URL Params**\
    date=yyyy-mm-dd
* **Example:**

`curl -s http://localhost:8080/graduation/rest/vote-results/history?date=2019-08-02 --user ivan-i@ya.ru:qwerty`

### Ideas for further development
 * Add restaurant security role. Let restaurants automatically post their offers with REST API. Admins should view and approve them before voting process. 
 * Restrict posting offers after voting has begun.
 * Restrict voting for a restaurant that has no current offers.
 * Notify users by email/sms of their profiles' changes.
 * Add message bundle.
 * Improve exception messages.
 * Validate nested entities when posting offers.