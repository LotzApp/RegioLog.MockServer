package org.lotzapp.backend.repository.client;

import org.lotzapp.backend.entity.client.DeliveryRhythmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRhythmRepository extends JpaRepository<DeliveryRhythmEntity, UUID> {}
