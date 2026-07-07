package org.lotzapp.backend.entity.product;

import jakarta.persistence.*;
import lombok.*;
import org.lotzapp.backend.entity.client.ClientEntity;
import org.lotzapp.regiologapi.model.ProductStatus;
import org.lotzapp.regiologapi.model.ProductVisibility;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class ProductEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private ProductStatus status;

  @Column(nullable = false)
  private ProductVisibility productVisibility;

  @Column(nullable = false)
  private Integer salesUnitId;

  @Column(nullable = false)
  private Integer originCountryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id")
  @ToString.Exclude
  private ClientEntity client;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "product_id")
  @ToString.Exclude
  @Builder.Default
  private List<PriceEntity> prices = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(nullable = false)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(nullable = false)
  private OffsetDateTime lastUpdate;

  @ManyToMany(mappedBy = "products")
  @ToString.Exclude
  @Builder.Default
  private List<AssortmentEntity> assortments = new ArrayList<>();

  // @Column
  // private Integer baseUnitId;
  // @Column
  // private UUID depositProductId;

  // @Column
  // private UUID categoryId;
  // @Column(nullable = false)
  // private UUID customCategoryId;
  // @Column
  // private List<Integer> labelIds;
  // @Column
  // private List<Availability> availabilities;
  // private ProductData data;
  // private ProductInfo info;
  // private List<ProductPhoto> photos = new ArrayList();
  // @Column
  // private List<Identification> identifications;
  // @Column
  // private List<Dimension> dimensions;
  // @Column
  // private List<Conversion> conversions;

  @PrePersist
  private void prePersist() {
    if (createdAt == null) createdAt = OffsetDateTime.now();
    if (lastUpdate == null) lastUpdate = OffsetDateTime.now();
  }
}
