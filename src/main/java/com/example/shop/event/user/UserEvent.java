package com.example.shop.event.user;

import com.example.shop.domian.User;
import com.example.shop.enums.UserEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UserEvent {

    private UUID eventId;
    private UserEventType userEventType;
    private UUID userId;
    private String userName;
    private String firstName;
    private String lastName;
    private Date timestamp;
    private String source;

    public static UserEvent created(User user) {
        return UserEvent.builder()
                .eventId(UUID.randomUUID())
                .userEventType(UserEventType.USER_CREATED)
                .userId(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .timestamp(new Date())
                .source("/api")
                .build();
    }

    public static UserEvent updated(User user) {
        return UserEvent.builder()
                .eventId(UUID.randomUUID())
                .userEventType(UserEventType.USER_UPDATED)
                .userId(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .timestamp(new Date())
                .source("/api")
                .build();
    }

    public static UserEvent deleted(User user) {
        return UserEvent.builder()
                .eventId(UUID.randomUUID())
                .userEventType(UserEventType.USER_DELETED)
                .userId(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .timestamp(new Date())
                .source("/api")
                .build();
    }

    public static UserEvent viewed(User user) {
        return UserEvent.builder()
                .eventId(UUID.randomUUID())
                .userEventType(UserEventType.USER_VIEWED)
                .userId(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .timestamp(new Date())
                .source("/api")
                .build();
    }
}
