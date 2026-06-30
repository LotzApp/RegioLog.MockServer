package org.lotzapp.component;

import org.lotzapp.adminapi.model.Role;
import org.lotzapp.adminapi.model.User;
import org.lotzapp.adminapi.model.UserStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserComponent {
    private static List<User> USERS = new ArrayList<>();

    static  {
        USERS.add(getUser("User01"));
        USERS.add(getUser("User02"));
        USERS.add(getUser("User03"));
    }

    private static User getUser(String name) {
        var user = new User(
                UUID.randomUUID(),
                name,
                OffsetDateTime.now(),
                "Firstname: %s".formatted(name),
                "Lastname: %s".formatted(name),
                "RestCode",
                UserStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                List.of(new Role("ROle01", "App01", List.of("asdf"))),
                "Instance",
                "Mail",
                "InstanceDomain",
                "ApiKey",
                "PartnerApp",
                false,
                List.of()
        );
        return user;
    }

    public static ResponseEntity<List<User>> getUsersResponse(Object[] args) {
        System.out.println(args);
        return ResponseEntity.ok(USERS);
    }
}
