FROM 10.60.156.72/its/openjdk:8

EXPOSE 9713

ADD target/its-highway-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]