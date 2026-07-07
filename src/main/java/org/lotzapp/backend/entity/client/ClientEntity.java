package org.lotzapp.backend.entity.client;

import jakarta.persistence.*;
import lombok.*;
import org.lotzapp.backend.entity.product.ProductEntity;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "regio_client")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class ClientEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_id")
  @ToString.Exclude
  private List<DeliveryRhythmEntity> deliveryRhythms;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_id")
  @ToString.Exclude
  private List<LocationEntity> locations;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<ProductEntity> products;

  @Column(nullable = false)
  private boolean isPartner;
}
