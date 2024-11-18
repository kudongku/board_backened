FROM openjdk:21-jdk

# docker compose 용
#COPY wait-for-it.sh wait-for-it.sh
#RUN chmod +x wait-for-it.sh
#ARG JAR_FILE=./build/libs/backend-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["bash", "-c", "./wait-for-it.sh db-mysql:3306 -s -t 100 && java -jar /app.jar"]

# kubernetes 용
ARG JAR_FILE=./build/libs/backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java","-jar","/app.jar"]
