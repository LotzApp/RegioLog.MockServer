package org.lotzapp.backend.service.client;

import lombok.extern.slf4j.Slf4j;
import org.lotzapp.backend.entity.client.ClientEntity;
import org.lotzapp.backend.exception.InvalidNumberOfOwnClientsException;
import org.lotzapp.backend.repository.client.ClientRepository;
import org.lotzapp.backend.converter.client.ClientEntityConverter;
import org.lotzapp.regiologapi.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ClientService {
  private final ClientRepository clientRepository;
  private final ClientEntityConverter clientEntityConverter;

  @Autowired
  public ClientService(
      ClientRepository clientRepository, ClientEntityConverter clientEntityConverter) {
    this.clientRepository = clientRepository;
    this.clientEntityConverter = clientEntityConverter;
  }

  public Client addClient(Client client) {
    if (client.getId().isPresent()) throw new IllegalArgumentException("Client id must not be set");
    var clientEntity = clientEntityConverter.toEntity(client);
    var savedEntity = clientRepository.save(clientEntity);
    return clientEntityConverter.toRest(savedEntity);
  }

  /**
   * Get the client id of the owning client.
   * @return the owner client id
   */
  public UUID getOwnClientId() {
    var ownerList = clientRepository.getClientEntityByIsPartner(false);
    if (ownerList.size() != 1) {
      log.error("Found {} own clients but only a single one is allowed!", ownerList);
      throw new InvalidNumberOfOwnClientsException(
          "Found %s own clients but only a single one is allowed!".formatted(ownerList));
    }
    return ownerList.getFirst().getId();
  }

  public ClientEntity getClientEntityById(UUID id) {
    return clientRepository.findById(id).orElse(null);
  }

  public Client getClientById(UUID id) {
    return clientEntityConverter.toRest(getClientEntityById(id));
  }

  public List<Client> getPartnerClients() {
    var partners = clientRepository.getClientEntityByIsPartner(true);
    return clientEntityConverter.toRest(partners);
  }
}
