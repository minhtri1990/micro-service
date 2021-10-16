FROM 10.60.156.72/its/openjdk:8

EXPOSE 9735

ADD target/its-violation-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]