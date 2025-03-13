# IM Banking Application

## Description
This is a banking application containing four microservices (api-gateway, customer_service, transaction_service, notification_service) to allow creating of users and accounts, carrying out transactions (deposit, withdrawal, transfer of funds) and also send notifications. The customer_service contains both the implemntation for registering & authenticating users and also creating accounts.

![HLD](https://github.com/Alvinimbua/IM_assesment/blob/0dbc1292b1dd0810bffcb008d21004f2dd17e816/IM%20Banking%20HLD.drawio.png)

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Alvinimbua/IM_assesment/tree/master
2. Build the docker images with the following command for each of the service:
    ```bash
   mvn compile jib:dockerBuild
3.Under the customer_service directory run the following command to start all the services:
 ```bash
   docker compose up -d



   



