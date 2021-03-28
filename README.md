# Spring Boot Restful API

## Objective
RESTful service built using Spring Boot :beers::beers:

## Development

To start your application in the dev profile, run:

```
./gradlew
```


### Doing API-First development using openapi-generator

[OpenAPI-Generator]() is configured for this application. You can generate API code from the `src/main/resources/swagger/api.yml` definition file by running:

```bash
./gradlew openApiGenerate
```

Then implements the generated delegate classes with `@Service` classes.

To edit the `api.yml` definition file, you can use a tool such as [Swagger-Editor](). Start a local instance of the swagger-editor using docker by running: `docker-compose -f src/main/docker/swagger-editor.yml up -d`. The editor will then be reachable at [http://localhost:7742](http://localhost:7742).

Refer to [Doing API-First development][] for more details.

## Building for production

### Packaging as jar

To build the final jar and optimize the sabinoLabs application for production, run:

```


./gradlew -Pprod clean bootJar

```

To ensure everything worked, run:

```


java -jar build/libs/*.jar

```

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```


./gradlew -Pprod -Pwar clean bootWar

```

## Testing

To launch your application's tests, run:

```
./gradlew test integrationTest jacocoTestReport
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the gradle plugin.

Then, run a Sonar analysis:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your  development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./gradlew bootJar -Pprod jibDockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (` docker-compose`), which is able to generate docker configurations for one or several  applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (` ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[ homepage and latest documentation]: https://www..tech
[ 6.10.1 archive]: https://www..tech/documentation-archive/v6.10.1
[doing microservices with ]: https://www..tech/documentation-archive/v6.10.1/microservices-architecture/
[using  in development]: https://www..tech/documentation-archive/v6.10.1/development/
[using docker and docker-compose]: https://www..tech/documentation-archive/v6.10.1/docker-compose
[using  in production]: https://www..tech/documentation-archive/v6.10.1/production/
[running tests page]: https://www..tech/documentation-archive/v6.10.1/running-tests/
[code quality page]: https://www..tech/documentation-archive/v6.10.1/code-quality/
[setting up continuous integration]: https://www..tech/documentation-archive/v6.10.1/setting-up-ci/
[openapi-generator]: https://openapi-generator.tech
[swagger-editor]: https://editor.swagger.io
[doing api-first development]: https://www..tech/documentation-archive/v6.10.1/doing-api-first-development/
