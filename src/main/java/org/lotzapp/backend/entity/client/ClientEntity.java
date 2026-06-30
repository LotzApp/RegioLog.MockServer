package org.lotzapp.backend.entity.client;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<DeliveryRhythmEntity> deliveryRhythms;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<LocationEntity> locations;

}
