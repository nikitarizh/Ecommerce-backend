FROM openjdk:11
COPY ./target/testtask-1.0.jar /
WORKDIR /
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "testtask-1.0.jar"]