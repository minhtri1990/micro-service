FROM 10.60.156.72/its/openjdk:8

EXPOSE 9716

ADD target/its-media-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]