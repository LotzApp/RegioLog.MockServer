package org.lotzapp.component;

import org.lotzapp.adminapi.model.InstanceBase;

import java.time.OffsetDateTime;
import java.util.UUID;

public class InstanceComponent {
    public static InstanceBase createInstance(String name) {
        return new InstanceBase(
                UUID.randomUUID(),
                name,
                name + ".org",
                name + "@mail.org",
                OffsetDateTime.now()
        );
    }
}
