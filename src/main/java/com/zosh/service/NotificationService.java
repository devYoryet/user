// NotificationService.java
package com.zosh.service;

import com.zosh.modal.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(Long userId);

    List<Notification> getSalonNotifications(Long salonId);

    Notification createNotification(Notification notification);

    Notification markAsRead(Long notificationId);

    void deleteNotification(Long notificationId);

    void sendBookingNotification(Long userId, Long salonId, String message);
}