# work-calendar-java
A simple exercise exposed in [Hello World youtube channel](https://www.youtube.com/watch?v=zAOOR-2RYMY&t=25s) make with my knowned technologies 

"Build a calendar where you can see the current full year, separated by months in columns of three but that these are collapsible to be able to see it from a phone, there must be a list of categories, these must initially be vacations and work, you must be able to add categories to this list and also to be able to eliminate them, the categories must be selectable and when selecting it and clicking on a day of this calendar, that day must be marked with the indicated category, in the case that the day already has a category it must indicate which has both categories, if I click again one day I must remove the category that is selected."
## Requirements
For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
## Running the application locally

- Execute the `main` method in the `com.fgonzalez.categorycalendar.CategoryCalendarApp` class from your favorite IDE.

- Also you can use mvn spring boot run command
```
mvn spring-boot:run
```

### A little web page be deploy [locally](http://localhost:8080/) and [swagger ui](http://localhost:8080/swagger-ui/) 
## Tecnologies
- Java
    - Spring Boot
    - Lombok
    - Spring Web
    - H2 DB
    - Spring Data JPA
    - MapStruct
    - Swagger
    - JUnit (test)
    - Mockito (test)
- Javascript
    - JQuery 3.3.1
    - Bootstrap 4
        - jquery bootstrap year calendar
        - bootstrap colorpicker
