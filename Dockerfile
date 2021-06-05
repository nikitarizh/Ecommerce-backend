FROM openjdk:11
COPY ./target/testtask-1.0.war /
WORKDIR /
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "testtask-1.0.war"]