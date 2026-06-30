package org.lotzapp.backend.entity.client;

import jakarta.persistence.*;
import lombok.*;
import org.lotzapp.regiologapi.model.LocationStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "location")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class LocationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(length = 60)
  private String name;

  @Column(length = 60)
  private String number;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String zip;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  @Builder.Default
  @Enumerated(EnumType.STRING)
  private LocationStatus status = LocationStatus.ACTIVE;

  @Column(length = 60)
  private String firstName;

  @Column(length = 60)
  private String lastName;

  @Column(length = 60)
  private String phone;

  @Column(length = 60)
  private String mail;

  @Column(length = 60)
  private String geoLocation;

  @Column private boolean invoice = true;
  @Column private boolean delivery = true;
  @Column private boolean collection = false;

  @Column private Integer countryId;

  @Column
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @Column
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastUpdate;

  @PrePersist
  private void prePersist() {
    if(createdAt == null) createdAt = OffsetDateTime.now();
    if(lastUpdate == null) lastUpdate = OffsetDateTime.now();
  }
}
