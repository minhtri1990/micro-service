FROM 10.60.156.72/its/openjdk:8

EXPOSE 9710

ADD target/its-registry-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]