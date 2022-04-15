# Github - REST service


The application enable to retrieve basic information about particular user from Github API and save them in database.

API:
* GET/users/login
* {

    id:

    login:

    name:

    avatarUrl:

    createdAT:

    calculations: (result of 6 / users no of followers * (2 + users no of public repos))

    }
* in database there are saved: user login and number of requests to API

### Technologies used

* Java 17
* Spring Boot
* H2 Database
* Spring Data
* Spring Web MVC
* Spring Test
* Lombok
* Okhttp
* Maven
