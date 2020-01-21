FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/all-in-one*.jar app.jar
ENTRYPOINT ["java", "-server", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]