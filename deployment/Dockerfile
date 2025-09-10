FROM eclipse-temurin:21-jdk-alpine AS build

RUN apk add --no-cache gradle

COPY modules/crediya-common /crediya-common
WORKDIR /crediya-common
RUN gradle publishToMavenLocal

COPY modules/crediya-applications /app
WORKDIR /app
RUN gradle clean build -x validateStructure --no-daemon

FROM eclipse-temurin:21-jre-alpine AS release

COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
