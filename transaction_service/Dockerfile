FROM openjdk:17-jdk-slim

#Information on who maintains the image
LABEL maintainer="imbuka.com"
LABEL version="1.0"
LABEL description="Docker container for Transaction service application"

#Add the applications jar to the image
COPY target/transaction_service-0.0.1-SNAPSHOT.jar transaction_service-0.0.1-SNAPSHOT.jar

#Execute the application.
ENTRYPOINT ["java", "-jar", "transaction_service-0.0.1-SNAPSHOT.jar"]