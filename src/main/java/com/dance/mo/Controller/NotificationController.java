package com.dance.mo.Controller;

import com.dance.mo.Entities.Notification;
import com.dance.mo.Entities.User;
import com.dance.mo.Services.NotificationService;
import com.dance.mo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @MessageMapping("/subscribe")
    @SendTo("/topic/notifications")
    public List<Notification> subscribe(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return notificationService.getLatestNotifications(user);
    }

}

