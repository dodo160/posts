FROM maven:3.8.1-openjdk-17 AS build
COPY src /home/posts/src
COPY pom.xml /home/posts
RUN mvn -f /home/posts/pom.xml clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/posts/target/posts-0.0.1-SNAPSHOT.jar"]