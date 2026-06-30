package org.lotzapp.backend.converter.client;

import org.lotzapp.backend.entity.client.ClientEntity;
import org.lotzapp.backend.converter.IConverter;
import org.lotzapp.regiologapi.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClientEntityConverter implements IConverter<Client, ClientEntity> {
    private final LocationEntityConverter locationEntityConverter;
    private final DeliveryRhythmEntityConverter deliveryRhythmEntityConverter;

    @Autowired
    public ClientEntityConverter(LocationEntityConverter locationEntityConverter, DeliveryRhythmEntityConverter deliveryRhythmEntityConverter) {
        this.locationEntityConverter = locationEntityConverter;
        this.deliveryRhythmEntityConverter = deliveryRhythmEntityConverter;
    }

    @Override
    public Client toRest(ClientEntity clientEntity) {
        var result = new Client();

        if(clientEntity.getLocations() != null) {
            clientEntity.getLocations().stream()
                    .map(locationEntityConverter::toRest)
                    .forEach(result::addLocationsItem);
        }

        if(clientEntity.getDeliveryRhythms() != null) {
            clientEntity.getDeliveryRhythms().stream()
                    .map(deliveryRhythmEntityConverter::toRest)
                    .forEach(result::addDeliveryRhythmsItem);
        }
        result.id(clientEntity.getId());
        return result;
    }

    @Override
    public ClientEntity toEntity(Client client) {
        var result = ClientEntity.builder();
        if(client.getDeliveryRhythms().isPresent()) {
            result.deliveryRhythms(client.getDeliveryRhythms().get().stream()
                    .map(deliveryRhythmEntityConverter::toEntity)
                    .collect(Collectors.toList()));
        }

        if(client.getLocations().isPresent()) {
            result.locations(client.getLocations().get().stream()
                    .map(locationEntityConverter::toEntity)
                    .collect(Collectors.toList()));
        }


        return result.build();
    }
}
