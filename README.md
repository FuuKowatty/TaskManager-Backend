# JobOffers - Backend
The JobOffers backend is a secure platform that allows users to
to access job offers by authenticating themselves.
It also allows users to create their own job offers.
offers. It also contains a scheduler that updates
offers from an external API every 3 hours.
In addition, the application uses Redis caching for
optimised performance and faster data retrieval.

## Architecture
![Architecture](./architecture/architecture-v1.png)


## Endpoints

| Endpoint                                                  |  Method  | Request                                                                                    | Response                          | Function                                                | Authorization                                |
|-----------------------------------------------------------|:--------:|:-------------------------------------------------------------------------------------------|-----------------------------------|---------------------------------------------------------|----------------------------------------------|
| `/accounts/token`                                         |  `GET`   | JSON BODY (email<br/>password)                                                             | JSON BODY (id, email, token)      | authenticate user                                       | *                                            |
| `/accounts/register`                                      |  `POST`  | JSON BODY (<br/>firstName,<br/>LastName<br/>email<br/>password, role)                      | JSON BODY (message)               | create admin                                            | *                                            |
| `/api/tasks`                                              |  `GET`   | -                                                                                          | JSON BODY (List<TaskDto>)         | show all tasks                                          | ADMIN, MANAGER                               |
| `/api/tasks`                                              |  `POST`  | JSON BODY (title, description, endDate, assignedTo                                         | JSON BODY (TaskDto)               | create task                                             | ADMIN, MANAGER                               |
| `/api/tasks/employee/{id}`                                |  `GET`   | -                                                                                          | JSON BODY (List<TaskDto>)         | show all of specified employee task                     | ADMIN, MANAGER, EMPLOYEE(If it is his tasks) |
| `/api/tasks/{id}`                                         |  `GET`   | -                                                                                          | JSON BODY (TaskDto)               | show task by id                                         | ADMIN, MANAGER, EMPLOYEE(If it is his tasks) |
| `/api/tasks/{id}`                                         |  `PUT`   | JSON BODY (title, description, endDate, assignedTo                     JSON BODY (TaskDto) | JSON BODY (TaskDto)               | update task                                             | ADMIN, MANAGER                               |
| `/api/tasks/{id}`                                         | `DELETE` | -                                                                                          | JSON BODY (TaskDto)               | delete  task  by id                                     | ADMIN, MANAGER                               |
| `api/tasks/{id}/complete`                                 | `PATCH`  | -                                                                                          | JSON BODY (message)               | mark task as complete                                   | EMPLOYEE                                     |
| `/api/users`                                              |  `GET`   | -                                                                                          | JSON BODY (List<UserDto>)         | show all users                                          | ADMIN                                        |
| `/api/users`                                              |  `POST`  | JSON BODY (firstName, LastName, email, password, role)                                     | JSON BODY (UserDto)               | create user (manager or employee)                       | ADMIN                                        |
| `/api/users/{id}`                                         |  `PUT`   | JSON BODY (firstName, LastName, email, password, role)                                     | JSON BODY (UserDto)               | update user's data                                      | ADMIN                                        |
| `/api/users/{id}`                                         |  `GET`   | -                                                                                          | JSON BODY (UserDto)               | show user by id                                         | ADMIN                                        |
| `/api/users/{id}`                                         | `DELETE` | -                                                                                          | JSON BODY (UserDto)               | delete user by id                                       | ADMIN                                        |
| `/api/users/stats/sorted-by-completed-tasks?lastMonths=1` |  `GET`   | -                                                                                          | JSON(List<EmployeeStatisticsDto>) | show how many tasks each user completed in `lastMonths` |                                              |


## Technologies

![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java 17](https://img.shields.io/badge/Java_17-007396?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Log4j2](https://img.shields.io/badge/Log4j2-9B7E3E?style=for-the-badge&logo=apache&logoColor=white)
![Testcontainers](https://img.shields.io/badge/Testcontainers-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=java&logoColor=white)
![AssertJ](https://img.shields.io/badge/AssertJ-26A65B?style=for-the-badge&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-007396?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)


## How to build the project on your own
1. Clone this repository
```shell
git clone https://github.com/FuuKowatty/TaskManager-Backend.git
```
2. Go to the folder with cloned repository
3. Run docker compose
```shell
docker compose up
```
4. Run the application
