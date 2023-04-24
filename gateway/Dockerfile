FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
ARG JAR_FILE=D:\Users\Vanya\ip\java-shareit\gateway\target
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAR_FILE} -jar /app.jar"]