package org.lotzapp.backend.entity.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "price")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private double value;

    @Column
    private int vat;

    @Column(nullable = false)
    private OffsetDateTime createdAt;
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
    )

    @Column(nullable = false)
    private OffsetDateTime validFrom;
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
    )

    @Column(nullable = false)
    private OffsetDateTime lastUpdate;

    @PrePersist
    private void prePersist() {
        if(createdAt == null) createdAt = OffsetDateTime.now();
        if(lastUpdate == null) lastUpdate = OffsetDateTime.now();
    }
}
