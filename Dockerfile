FROM openjdk:17-jdk-slim

WORKDIR /app

EXPOSE 3080

COPY target/endpoint-with-sql-0.0.1-SNAPSHOT.jar /app/endpoint-with-sql.jar

ENTRYPOINT ["java", "-jar", "endpoint-with-sql.jar"]