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
3. Under the customer_service directory run the following command to start all the services:
     ```bash
   docker compose up -d
4. Also ensure Kafka is started while running the serice. use the following command to start instance of kafka. 
   Ensure Docker is running:
     ```bash
     sudo docker run -p 9092:9092 apache/kafka:3.9.0
5. To connect and have an instance of keycloack running for authentication of the server, Run the following command      on the api-gateway directory folder:
      ```bash
      docker compose up -d
## Choice Architecure
Microservice Architecture
Why Microservices?
- Scalability: Each service can be scaled independently based on demand.
- Flexibility: Services can be developed, deployed, and maintained independently.
- Fault Isolation: Failure in one service does not affect others.
- Technology Diversity: Each service can use the most appropriate technology stack.

## Security Considerations
1. Authentication -> Impleneted JWT(JSON Web Tokens) for stateles authentication
2. Authorization -> implemented role based access control(RBAC) to restrict access resources

## Inter-Service Communication
Implemented :
Synchronous Communication: REST APIs for real-time requests (e.g. deposit to account).
Asynchronous Communication: Use Kafka or RabbitMQ for event-driven communication (e.g., notifications).
   
## Data Consistency Across Microservices
To handle atomicity and race conditions when doing transactions on the transaction service the implemntation and use of @Transactional has been used to ensure all operations succeed or fail together.
To handle race conditions and support high concurrency an implemntation of @Version has been done as it avoids blocking and scales as well.

## Scalability, High Availability and Disaster Recovery
Scaling
1. Horizontal Scaling:
   Deploy multiple instances of each service behind a load balancer.
   Use Kubernetes or Docker Swarm for container orchestration.

2. Auto-Scaling:
   Use Kubernetes Horizontal Pod Autoscaler (HPA) or cloud-based auto-scaling (e.g., AWS Auto Scaling).

3. Database Scaling:
   Use read replicas for read-heavy workloads.
   IHave mplement sharding for large datasets.

High Availability
1. Load Balancing:
   Use of HAProxy for load balancing at the API Gateway level.

2. Redundancy:
   Deploy services across multiple availability zones (AZs) or regions.
   Use active-active or active-passive configurations.

3. Health Checks:
   Implement health checks and automatic failover using Kubernetes or cloud-native tools.

Disaster Recovery
1. Backups:
   Regularly back up databases and critical data.
   Use AWS S3, Google Cloud Storage, or similar services for backups.

2. Replication:
   Use database replication (e.g., PostgreSQL streaming replication) for real-time data redundancy.

3. Failover:
   Implement automated failover for databases and services.
   Use DNS failover or cloud-based failover solutions.

4. Disaster Recovery Plan:
   Define a Recovery Time Objective (RTO) and Recovery Point Objective (RPO).
   Regularly test the disaster recovery plan to ensure readiness.



   



