FROM eclipse-temurin:24-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY *.jar EntreBicis-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/EntreBicis-0.0.1-SNAPSHOT.jar"]