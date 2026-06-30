# Setup:

Add a sample gradle.properties in your user home directory

* Linux: `~/.gradle/gradle.properties`
* Windows: `%USERPROFILE%\.gradle\gradle.properties`

This file should include your user and host to connect to NexusOSS

```properties
# ~/.gradle/gradle.properties
nexusUrl=https://repository.lotzapp.work/repository/maven-hosted/
nexusUsername=your_username
nexusPassword=your_password
```

# Technologies

## Database

If this would be a production system a database setup tool/library would be needed.
(e.g. [Flyway](https://documentation.red-gate.com/flyway/reference/usage/flyway-open-source),
or [Liquibase](https://www.liquibase.org/))

If a test database is needed a database library like [H2](https://www.h2database.com/html/main.html) could be used.
This is an in-memory database and does not require a database server.

If transaction handling should be defined in the application code, you have to disable `spring.jpa.open-in-view=true` in
`application.properties`. This property is set to `true` by default. With this property, a transaction is opened when
a new request is received.

## Logging

Lombok in combination with slf4j is used for logging. More information can be found here:
[Spring boot logging](https://medium.com/@AlexanderObregon/enhancing-logging-with-log-and-slf4j-in-spring-boot-applications-f7e70c6e4cc7).

## Clean-Architecture
To enforce a clean architecture, it is possiblet to make tests with `arch-unit`. The project is checked
package-wise with `arch-unit`. So you can define, which classes of a specific package are allowed to be used in
other classes of other packages.
So for example, you can define a package for services and restrict the usage only to other services, controllers and 
components. If you would use a service within a repository the tests would fail!
[ArchUnit-Documentation](https://www.archunit.org/).

## Attention

Don't try to mix `jakarta.persistence` and `javax.persistence` in the same project. This leads to
problems during startup and database creation. Just use `javax.persistence`. This is the new
`javax.persistence`
package. [Stackoverflow article about jakarta.persistence](https://stackoverflow.com/questions/60021815/why-has-javax-persistence-api-been-replaced-by-jakarta-persistence-api-in-spring)



