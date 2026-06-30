package org.lotzapp.backend.controller;

import org.lotzapp.component.ClientComponent;
import org.lotzapp.regiologapi.api.ClientApi;
import org.lotzapp.regiologapi.model.Client;
import org.lotzapp.regiologapi.model.ClientPartner;
import org.lotzapp.regiologapi.model.ClientUpdate;
import org.lotzapp.regiologapi.model.PossessionType;
import org.lotzapp.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"${openapi.regioLogData.base-path:/regiolog}"})
public class ClientController implements ClientApi {
    private final ClientComponent clientComponent;

    @Autowired
    public ClientController(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    @Override
    public ResponseEntity<Client> updateClient(ClientUpdate clientUpdate, String xRequestId) {
        return clientComponent.updateClient(clientUpdate);
    }

    @Override
    public ResponseEntity<List<Client>> getClients(OffsetDateTime lastUpdate, PossessionType type, String xRequestId) {
        return ResponseEntity.ok(clientComponent.getClientList().stream()
                .map(cl -> clientComponent.getClient(cl.getId().get()))
                .toList());
    }

    @Override
    public ResponseEntity<Client> getClientById(UUID clientId, String xRequestId) {
        var client = clientComponent.getClient(clientId);
        if (client == null) return ResponseEntity.notFound().build();
        TimeUtils.handleSpecialNames(client.getDomain());
        return ResponseEntity.ok(client);
    }

    @Override
    public ResponseEntity<Client> updatePartnerClient(UUID clientId, ClientPartner clientPartner, String xRequestId) {
        return clientComponent.updatePartnerClient(clientId, clientPartner);
    }
}
