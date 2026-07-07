package org.lotzapp.backend.repository.client;

import org.lotzapp.backend.entity.client.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {
    List<ClientEntity> getClientEntityByIsPartner(boolean isPartner);
}
