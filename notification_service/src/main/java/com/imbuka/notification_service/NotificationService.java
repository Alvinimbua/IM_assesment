package com.imbuka.notification_service;

import com.imbuka.notification_service.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "transaction-events", groupId = "notification-group")
    public void listenToTransactionEvents(String message) {
        // Simulate sending a notification
        System.out.println("Received transaction event: " + message);
        sendNotification(message);
    }

    private void sendNotification(String message) {
        // Simulate sending an email
        String to = "imbank@groups.com";
        String subject = "Transaction Notification";
        String body = "Transaction details: " + message;
        emailService.sendEmail(to, subject, body);
    }
}
