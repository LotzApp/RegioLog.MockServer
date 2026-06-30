package org.lotzapp.component;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.lotzapp.backend.service.ClientService;
import org.lotzapp.backend.service.DeliveryRhythmService;
import org.lotzapp.backend.service.LocationService;
import org.lotzapp.regiologapi.model.*;
import org.lotzapp.util.TimeUtils;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClientComponent {
  @Getter private final List<Client> clientList = new ArrayList<>();

  private final LocationService locationService;
  private final ClientService clientService;

  private final DeliveryRhythmService deliveryRhythmService;

  @Autowired
  public ClientComponent(LocationService locationService, ClientService clientService, DeliveryRhythmService deliveryRhythmService) {
    this.locationService = locationService;
      this.clientService = clientService;
      this.deliveryRhythmService = deliveryRhythmService;
  }

  public void initializeData() {
    clientList.add(buildClient("Cl1.lotzapp.org", true, true));
    clientList.add(buildClient("Cl2.org", true, false));
    clientList.add(buildClient("Cl3.at", false, false));
    clientList.add(buildClient(TimeUtils.LONG_LOAD_KEY, false, false));
    clientList.add(buildClient(TimeUtils.LOADING_TIMEOUT_KEY, false, false));
  }

  public ClientData buildClientData(String domain) {
    var data = new ClientData();
    data.name("Name - " + domain);
    data.website("www." + domain + ".xxyz");
    data.email("Mail@Mail.org");
    data.company("### Company ###");
    return data;
  }

  public Category buildProductCategory(int index) {
    return new Category(
        UUID.randomUUID(),
        "### Category %d ###".formatted(index),
        CategoryStatus.ACTIVE,
        1,
        OffsetDateTime.now(),
        OffsetDateTime.now());
  }

  public List<Category> buildProductCategoryList() {
    var list = new ArrayList<Category>();
    list.add(buildProductCategory(1));
    list.add(buildProductCategory(2));
    return list;
  }

  public Client buildClient(String domain, boolean addCategories, boolean addDeliveryRhythm) {
    var client = new Client();

    var defaultLocations = locationService.getDefaultLocations();
    var deliveryRhythms = deliveryRhythmService.getDefaultDeliveryRhythms();
    var baseIndex = 2 * clientList.size();
    client.addLocationsItem(getItem(baseIndex, defaultLocations));
    client.addLocationsItem(getItem(baseIndex + 1, defaultLocations));

    if (addDeliveryRhythm) {
      client.addDeliveryRhythmsItem(getItem(baseIndex, deliveryRhythms));
      client.addDeliveryRhythmsItem(getItem(baseIndex + 1, deliveryRhythms));
    }

    //UUID mapping
    client = clientService.addClient(client);
    client.setIsPartner(!clientList.isEmpty());
    client.setDomain(domain);
    client.setCreatedAt(OffsetDateTime.now());
    client.setLastUpdate(OffsetDateTime.now());

    client.data(buildClientData(domain));

    if (addCategories) client.categories(buildProductCategoryList());
    return client;
  }

  private <T> @NonNull T getItem(int index, List<T> availableItems) {
    if (index < 0 || index > availableItems.size())
      throw new IllegalArgumentException("Index out of bounds");
    return availableItems.get(index);
  }

  public ResponseEntity<Client> updatePartnerClient(UUID uuid, ClientPartner partner) {
    var clientToUpdate =
        clientList.stream()
            .filter(c -> c.getId().isPresent() && c.getId().get().equals(uuid))
            .findFirst();
    if (clientToUpdate.isPresent()) {
      var client = clientToUpdate.get();
      client.setLastUpdate(OffsetDateTime.now());
      client.setStatus(partner.getStatus().get());
      return ResponseEntity.ok(client);
    }

    return ResponseEntity.notFound().build();
  }

  public ResponseEntity<Client> updateClient(ClientUpdate update) {
    if(update.getLocations().isPresent()) updateLocations(update.getLocations().get());
    return ResponseEntity.ok(clientList.stream()
            .filter(cl -> !cl.getIsPartner())
            .findFirst()
            .orElseThrow()
    );
  }

  private void updateLocations(List<LocationUpsert> locationUpserts) {
    locationUpserts.forEach(locationService::updateLocation);
  }

  public Client getClient(UUID uuid) {
    var optionalClient =
        clientList.stream()
            .filter(c -> c.getId().isPresent() && c.getId().get().equals(uuid))
            .findFirst();
    if (optionalClient.isEmpty()) return null;

    var client = optionalClient.get();

    // request all locations by the database again to react to changes
    client.setLocations(
        JsonNullable.of(
            client.getLocations().get().stream()
                .map(l -> l.getId().get())
                .map(locationService::getLocation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList()));

    //request all delivery rhythms by the database again to react to changes
    if(client.getDeliveryRhythms().isPresent()) {
      client.setDeliveryRhythms(
              JsonNullable.of(
                      client.getDeliveryRhythms().get().stream()
                              .map(l -> l.getId().get())
                              .map(deliveryRhythmService::getDeliveryRhythm)
                              .filter(Optional::isPresent)
                              .map(Optional::get)
                              .toList()));
    }

    return client;
  }
}
