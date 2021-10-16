FROM 10.60.156.72/its/openjdk:8

EXPOSE 9711

ADD target/its-gateway.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]