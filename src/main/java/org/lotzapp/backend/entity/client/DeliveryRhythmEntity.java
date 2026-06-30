package org.lotzapp.backend.entity.client;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery_rhythm")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@ToString
public class DeliveryRhythmEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column private String description;

  @Column(nullable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime validFrom;

  @Column
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime validTo;

  @Column private Integer orderDay;
  @Column private String orderTime;

  @Column
  @Min(1) @Max(7)
  private Integer deliveryDay;

  @Column private String deliveryTime;

  @Column
  @Min(1) @Max(53)
  private Integer deliveryWeek;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastUpdate;

  @PrePersist
  private void prePersist() {
    if(createdAt == null) createdAt = OffsetDateTime.now();
    if(validFrom == null) validFrom = OffsetDateTime.now();
    if(lastUpdate == null) lastUpdate = OffsetDateTime.now();
  }
}
