FROM maven:3-openjdk-17-slim AS BUILD
COPY . /home/maven/src
WORKDIR /home/maven/src
RUN mvn package

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/maven/src/target/*-with-dependencies.jar /app/nfg-test-ktor.jar
ENTRYPOINT ["java","-jar","/app/nfg-test-ktor.jar"]