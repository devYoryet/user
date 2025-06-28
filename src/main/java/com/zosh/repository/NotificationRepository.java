// NotificationRepository.java
package com.zosh.repository;

import com.zosh.modal.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findBySalonIdOrderByCreatedAtDesc(Long salonId);

    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, Boolean isRead);

    List<Notification> findBySalonIdAndIsReadOrderByCreatedAtDesc(Long salonId, Boolean isRead);
}