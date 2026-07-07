package org.lotzapp.backend.repository.product;

import org.lotzapp.backend.entity.product.AssortmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssortmentRepository extends JpaRepository<AssortmentEntity, UUID> {}
