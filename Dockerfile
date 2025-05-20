#build
FROM maven:3.9.9-amazoncorretto-21-al2023 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

#run
FROM amazoncorretto:21.0.5
WORKDIR /app
COPY --from=build ./build/target/*.jar /app.jar
EXPOSE 8080
EXPOSE 9090
ENV DB_URL=''
ENV DB_USER=''
ENV DB_PASSWORD=''
ENV GOOGLE_CLIENT_ID=''
ENV GOOGLE_CLIENT_SECRET=''
ENV TZ='America/Sao_Paulo'
ENTRYPOINT ["java", "-jar", "/app.jar"]