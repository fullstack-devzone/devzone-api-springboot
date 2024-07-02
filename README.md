# DevZone API using SpringBoot

An implementation of [DevZone API](https://github.com/fullstack-devzone/fullstack-devzone) using Spring Boot.

## Prerequisites
* JDK 21
* Docker and Docker Compose

Install JDK using [SDKMAN](https://sdkman.io/)

```shell
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
$ sdk install java 21.0.1-tem
$ sdk install gradle
```

## How to?

1. Run tests:

```shell
./gradlew test
```

2. Format code:

```shell
./gradlew spotlessApply
```

3. Run application using Docker Compose:

```shell
./gradlew bootRun
```

4. Run application using Testcontainers:

```shell
./gradlew bootTestRun
```
