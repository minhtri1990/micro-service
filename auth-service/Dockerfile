FROM 10.60.156.72/its/openjdk:8

EXPOSE 9712

ADD target/its-auth-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]