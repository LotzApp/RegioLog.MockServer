package org.lotzapp.backend.service;

import org.lotzapp.backend.entity.client.ClientEntity;
import org.lotzapp.backend.repository.client.ClientRepository;
import org.lotzapp.backend.converter.client.ClientEntityConverter;
import org.lotzapp.regiologapi.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientEntityConverter clientEntityConverter;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientEntityConverter clientEntityConverter) {
        this.clientRepository = clientRepository;
        this.clientEntityConverter = clientEntityConverter;
    }

    public Client addClient(Client client) {
        if(client.getId().isPresent()) throw new IllegalArgumentException("Client id must not be set");
        var clientEntity = clientEntityConverter.toEntity(client);
        var savedEntity = clientRepository.save(clientEntity);
        return clientEntityConverter.toRest(savedEntity);
    }

    public ClientEntity getClientEntityById(UUID id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client getClientById(UUID id) {
        return clientEntityConverter.toRest(getClientEntityById(id));
    }
}
