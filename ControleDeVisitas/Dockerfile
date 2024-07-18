FROM maven:3.8.4-openjdk-17 AS build
RUN mkdir usr/src/app

WORKDIR usr/src/app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY . .

RUN mvn package -DskipTests

FROM eclipse-temurin:latest
RUN mkdir -p usr/src/app

WORKDIR usr/src/app
COPY --from=build usr/src/app/target/ControleDeVisitas-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]