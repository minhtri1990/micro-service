FROM 10.60.156.72/its/openjdk:8

EXPOSE 9734

ADD target/its-device-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]