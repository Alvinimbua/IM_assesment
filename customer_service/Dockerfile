FROM openjdk:17-jdk-slim

#Information on who maintains the image
LABEL maintainer="imbuka.com"
LABEL version="1.0"
LABEL description="Docker container for Customer service application"

#Add the applications jar to the image
COPY target/customer_service-0.0.1-SNAPSHOT.jar customer_service-0.0.1-SNAPSHOT.jar

#Execute the application.
ENTRYPOINT ["java", "-jar", "customer_service-0.0.1-SNAPSHOT.jar"]