FROM openjdk:8-jre-alpine
MAINTAINER trihm21
ADD ./target/media.jar /app/
CMD ["java", "-Xmx4096m", "-jar", "/app/media.jar"]
EXPOSE 443