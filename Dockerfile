FROM openjdk:19-jdk-alpine
MAINTAINER JoviSimons
COPY target/APIGateway-0.0.1-SNAPSHOT.jar APIGateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/APIGateway-0.0.1-SNAPSHOT.jar"]FROM openjdk:19-jdk-alpine