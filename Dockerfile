FROM gradle:8.6-jdk11-alpine AS BUILD
WORKDIR /app
COPY . .
RUN ./gradlew clean test
RUN ./gradlew buildFatJar

FROM openjdk:11-jre-slim
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /app/build/libs/*-all.jar /app/nfg-demo.jar
ENTRYPOINT ["java","-jar","/app/nfg-demo.jar"]