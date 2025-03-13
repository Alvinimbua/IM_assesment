FROM openjdk:17-jdk-slim

#Information on who maintains the image
LABEL maintainer="imbuka.com"
LABEL version="1.0"
LABEL description="Docker container for Notification service application"

#Add the applications jar to the image
COPY target/notification_service-0.0.1-SNAPSHOT.jar notification_service-0.0.1-SNAPSHOT.jar

#Execute the application.
ENTRYPOINT ["java", "-jar", "notification_service-0.0.1-SNAPSHOT.jar"]