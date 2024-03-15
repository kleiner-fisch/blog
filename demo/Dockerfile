FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./blog.db blog.db
COPY target/*.jar blog.jar
EXPOSE 8080
CMD ["java","-jar","blog.jar"]
