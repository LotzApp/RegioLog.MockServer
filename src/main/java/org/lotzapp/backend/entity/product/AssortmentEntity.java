package org.lotzapp.backend.entity.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "price")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class AssortmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    @Max(60)
    private String name;

    @Column(nullable = false)
    private OffsetDateTime createdAt;
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
    )
    @Column(nullable = false)
    private OffsetDateTime lastUpdate;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name="assortment_products")
    private List<ProductEntity> products;

    @PrePersist
    private void prePersist() {
        if(createdAt == null) createdAt = OffsetDateTime.now();
        if(lastUpdate == null) lastUpdate = OffsetDateTime.now();
    }
}
