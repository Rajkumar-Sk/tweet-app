FROM openjdk:11
EXPOSE 9090
ADD target/tweet-app.jar tweet-app.jar
ENTRYPOINT ["java","-jar","/tweet-app.jar"]