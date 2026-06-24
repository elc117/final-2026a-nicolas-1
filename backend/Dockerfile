# Stage 1: Build the application
FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew installDist --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy
EXPOSE 8080
COPY --from=build /home/gradle/src/app/build/install/app /app
WORKDIR /app
CMD ["./bin/app"]
