// NotificationServiceImpl.java
package com.zosh.service.impl;

import com.zosh.modal.Notification;
import com.zosh.repository.NotificationRepository;
import com.zosh.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getSalonNotifications(Long salonId) {
        return notificationRepository.findBySalonIdOrderByCreatedAtDesc(salonId);
    }

    @Override
    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notification.setUpdatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public void sendBookingNotification(Long userId, Long salonId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSalonId(salonId);
        notification.setDescription(message);
        notification.setType("BOOKING");
        notification.setIsRead(false);

        Notification saved = createNotification(notification);

        // Enviar en tiempo real
        if (userId != null) {
            messagingTemplate.convertAndSend("/topic/user/" + userId, saved);
        }
        if (salonId != null) {
            messagingTemplate.convertAndSend("/topic/salon/" + salonId, saved);
        }
    }
}