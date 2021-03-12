# work-calendar-java 
## STEPS

Creation of java project structure through maven.

```
mvn archetype:generate 
    -DgroupId=com.fgonzalez.categorycalendar 
    -DartifactId=java-category-calendar 
    -DarchetypeArtifactId=maven-archetype-quickstart 
    -DinteractiveMode=false
```

Add java version

```
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>  
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
```

Add Spring boot dependences

```
    <parent>
	  <groupId>org.springframework.boot</groupId>
	  <artifactId>spring-boot-starter-parent</artifactId>
	  <version>2.3.9.RELEASE</version>
	</parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-java8time</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- TEST dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        ...
```

Add build spring boot maven plugin

```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

Add the configuration for H2 on application.properties

```
# App conf
spring.application.name='Category Calendar'

# datasource conf
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# H2 conf
spring.h2.console.enabled=true
spring.h2.console.path=/h2
```

create `data.sql` and `schema.sql` for definiton of H2 DB 

schema.sql
```
DROP TABLE IF EXISTS TBL_CATEGORIES;

CREATE TABLE TBL_CATEGORIES (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(70) NOT NULL
);

DROP TABLE IF EXISTS TBL_CATEGORY_SCHEDULE;

CREATE TABLE TBL_CATEGORY_SCHEDULE (
  id INT AUTO_INCREMENT PRIMARY KEY,
  schedule_date DATE NOT NULL,
  fk_categories_id INT NOT NULL
);

ALTER TABLE TBL_CATEGORY_SCHEDULE ADD FOREIGN KEY ( fk_categories_id ) REFERENCES TBL_CATEGORIES( id );
```

data.sql
```
INSERT INTO TBL_CATEGORIES (category_name) VALUES
  ('vacations'),
  ('work');
```


Add thymeleaft folders for templates and static resources

```
mkdir resources/templates
mkdir resources/static/css
```

Add the notation `@SpringBootApplication` of main class and call to run method on main `SpringApplication.run(CategoryCalendarApp.class, args);`

Add index 'Hello world' to templates folder, and a empty style.css to css folder
```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8" />
        <link rel="stylesheet" type="text/css" th:href="@{/css/style.css}"/>
    </head>
    <body>
        <br/><br/>
        <div>Hello World!</div>
    </body>
</html>
```

Now we can see the hello world message on [http://localhost:8080/](http://localhost:8080/)
and h2-console [http://localhost:8080/h2](http://localhost:8080/h2) make sure 'JDBC URL' is the defined on applications. properties to view the tables on console

add vscode files to .gitignore

```
# vscode launch file
launch.json
.vscode/
```

Update initial changes to git (thymeleaft and H2)

```
git stash
git branch initial_conf
git checkout initial_conf
git stash pop
git add *
git commit -m "initial conf of thymeleaft and H2 DB"
```