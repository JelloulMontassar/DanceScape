package com.dance.mo.Repositories;
import com.dance.mo.Entities.Notification;
import com.dance.mo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop10ByOrderBySendDateDesc();
    List<Notification> findBySeenFalseAndReceiverOrderBySendDateDesc(User receiver);

}
