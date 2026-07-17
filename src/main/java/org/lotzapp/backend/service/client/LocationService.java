package org.lotzapp.backend.service.client;

import lombok.extern.slf4j.Slf4j;
import org.lotzapp.backend.ReflectionUpdater;
import org.lotzapp.backend.entity.client.LocationEntity;
import org.lotzapp.backend.repository.client.LocationRepository;
import org.lotzapp.backend.converter.client.LocationEntityConverter;
import org.lotzapp.regiologapi.model.Location;
import org.lotzapp.regiologapi.model.LocationStatus;
import org.lotzapp.regiologapi.model.LocationUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;
import java.util.*;

import static org.lotzapp.util.DataUtils.updateIfPresent;

@Service
@Slf4j
public class LocationService {
  private final LocationRepository locationRepository;
  private final LocationEntityConverter locationEntityConverter;

  @Autowired
  public LocationService(
      LocationRepository locationRepository, LocationEntityConverter locationEntityConverter) {
    this.locationRepository = locationRepository;
    this.locationEntityConverter = locationEntityConverter;
  }

  public List<Location> getLocations() {
    return locationEntityConverter.toRest(locationRepository.findAll());
  }

  public Location addLocation(Location location) {
    var targetEntity = locationEntityConverter.toEntity(location);
    targetEntity.setLastUpdate(OffsetDateTime.now());
    targetEntity.setCreatedAt(OffsetDateTime.now());
    return locationEntityConverter.toRest(locationRepository.save(targetEntity));
  }

  public Optional<LocationEntity> getLocationEntity(UUID id) {
    return locationRepository.findById(id);
  }

  public Optional<Location> getLocation(UUID id) {
    return getLocationEntity(id).map(locationEntityConverter::toRest);
  }

  public void deleteLocation(UUID id) {
    locationRepository.deleteById(id);
  }

  private void updateLocation(LocationEntity entity, Location target) {
    ReflectionUpdater.updateFields(entity, target);
  }

  public Location updateLocation(Location location) {
    Assert.isTrue(location.getId().isPresent(), "Location id must be present for update");
    var locationEntity = locationRepository.findById(location.getId().get());

    Assert.isTrue(
        locationEntity.isPresent(), "Location with id " + location.getId() + " not found");
    updateLocation(locationEntity.get(), location);
    locationEntity.get().setLastUpdate(OffsetDateTime.now());

    var result = locationRepository.save(locationEntity.get());
    return locationEntityConverter.toRest(result);
  }

  public List<Location> getDefaultLocations() {
    List<LocationEntity> locations =
        List.of(
            LocationEntity.builder()
                .name("Main Warehouse")
                .number("LOC-001")
                .address("Industriestraße 12")
                .zip("4020")
                .city("Linz")
                .status(LocationStatus.ACTIVE)
                .firstName("Thomas")
                .lastName("Müller")
                .phone("+43 732 123456")
                .mail("t.mueller@lotzapp.org")
                .geoLocation("48.3069,14.2858")
                .invoice(true)
                .delivery(true)
                .collection(false)
                .countryId(40)
                .createdAt(OffsetDateTime.now())
                .lastUpdate(OffsetDateTime.now())
                .build(),
            LocationEntity.builder()
                .name("Vienna Distribution Center")
                .number("LOC-002")
                .address("Mariahilfer Straße 88")
                .zip("1070")
                .city("Vienna")
                .status(LocationStatus.ACTIVE)
                .firstName("Anna")
                .lastName("Berger")
                .phone("+43 1 9876543")
                .mail("a.berger@lotzapp.org")
                .geoLocation("48.1972,16.3437")
                .invoice(true)
                .delivery(true)
                .collection(true)
                .countryId(40)
                .createdAt(OffsetDateTime.now())
                .lastUpdate(OffsetDateTime.now())
                .build(),
            LocationEntity.builder()
                .name("Graz South Depot")
                .number("LOC-003")
                .address("Südtiroler Platz 5")
                .zip("8020")
                .city("Graz")
                .status(LocationStatus.ACTIVE)
                .firstName("Klaus")
                .lastName("Huber")
                .phone("+43 316 654321")
                .mail("k.huber@lotzapp.org")
                .geoLocation("47.0707,15.4395")
                .invoice(true)
                .delivery(false)
                .collection(true)
                .countryId(40)
                .createdAt(OffsetDateTime.now())
                .lastUpdate(OffsetDateTime.now())
                .build(),
            LocationEntity.builder()
                .name("Salzburg Pickup Point")
                .number("LOC-004")
                .address("Getreidegasse 22")
                .zip("5020")
                .city("Salzburg")
                .status(LocationStatus.ACTIVE)
                .firstName("Maria")
                .lastName("Hofer")
                .phone("+43 662 112233")
                .mail("m.hofer@lotzapp.org")
                .geoLocation("47.8003,13.0443")
                .invoice(false)
                .delivery(false)
                .collection(true)
                .countryId(40)
                .createdAt(OffsetDateTime.now())
                .lastUpdate(OffsetDateTime.now())
                .build(),
            LocationEntity.builder()
                .name("Innsbruck Alpine Hub")
                .number("LOC-005")
                .address("Maria-Theresien-Straße 7")
                .zip("6020")
                .city("Innsbruck")
                .status(LocationStatus.INACTIVE)
                .firstName("Stefan")
                .lastName("Wolf")
                .phone("+43 512 445566")
                .mail("s.wolf@lotzapp.org")
                .geoLocation("47.2692,11.4041")
                .invoice(true)
                .delivery(true)
                .collection(false)
                .countryId(40)
                .createdAt(OffsetDateTime.now().minusDays(30))
                .lastUpdate(OffsetDateTime.now().minusDays(5))
                .build(),
            LocationEntity.builder()
                .name("Berlin Central Store")
                .number("LOC-006")
                .address("Unter den Linden 45")
                .zip("10117")
                .city("Berlin")
                .status(LocationStatus.ACTIVE)
                .firstName("Laura")
                .lastName("Schmidt")
                .phone("+49 30 9988776")
                .mail("l.schmidt@lotzapp.org")
                .geoLocation("52.5166,13.3806")
                .invoice(true)
                .delivery(true)
                .collection(true)
                .countryId(49)
                .createdAt(OffsetDateTime.now().minusDays(60))
                .lastUpdate(OffsetDateTime.now().minusDays(2))
                .build(),
            LocationEntity.builder()
                .name("Munich Logistics Node")
                .number("LOC-007")
                .address("Leopoldstraße 100")
                .zip("80802")
                .city("Munich")
                .status(LocationStatus.ACTIVE)
                .firstName("Max")
                .lastName("Fischer")
                .phone("+49 89 3344556")
                .mail("m.fischer@lotzapp.org")
                .geoLocation("48.1614,11.5839")
                .invoice(true)
                .delivery(true)
                .collection(false)
                .countryId(49)
                .createdAt(OffsetDateTime.now().minusDays(45))
                .lastUpdate(OffsetDateTime.now().minusDays(1))
                .build(),
            LocationEntity.builder()
                .name("Zurich Express Point")
                .number("LOC-008")
                .address("Bahnhofstrasse 31")
                .zip("8001")
                .city("Zurich")
                .status(LocationStatus.ACTIVE)
                .firstName("Sophie")
                .lastName("Keller")
                .phone("+41 44 2233445")
                .mail("s.keller@lotzapp.org")
                .geoLocation("47.3769,8.5417")
                .invoice(true)
                .delivery(false)
                .collection(true)
                .countryId(41)
                .createdAt(OffsetDateTime.now().minusDays(90))
                .lastUpdate(OffsetDateTime.now().minusDays(10))
                .build(),
            LocationEntity.builder()
                .name("Prague East Facility")
                .number("LOC-009")
                .address("Wenceslas Square 14")
                .zip("11000")
                .city("Prague")
                .status(LocationStatus.INACTIVE)
                .firstName("Jan")
                .lastName("Novak")
                .phone("+420 2 55667788")
                .mail("j.novak@lotzapp.org")
                .geoLocation("50.0755,14.4378")
                .invoice(false)
                .delivery(true)
                .collection(false)
                .countryId(420)
                .createdAt(OffsetDateTime.now().minusDays(120))
                .lastUpdate(OffsetDateTime.now().minusDays(20))
                .build(),
            LocationEntity.builder()
                .name("Budapest West Terminal")
                .number("LOC-010")
                .address("Andrássy Avenue 60")
                .zip("1062")
                .city("Budapest")
                .status(LocationStatus.ACTIVE)
                .firstName("Eva")
                .lastName("Nagy")
                .phone("+36 1 7788990")
                .mail("e.nagy@lotzapp.org")
                .geoLocation("47.4979,19.0402")
                .invoice(true)
                .delivery(true)
                .collection(true)
                .countryId(36)
                .createdAt(OffsetDateTime.now().minusDays(15))
                .lastUpdate(OffsetDateTime.now())
                .build());
    return locationEntityConverter.toRest(locations);
  }

  public Location updateLocation(LocationUpsert upsert) {
    LocationEntity targetLocation;
    if(upsert.getId().isPresent()) targetLocation = getLocationEntity(upsert.getId().get()).orElseThrow(() -> new IllegalArgumentException("Location not found"));
    else {
      targetLocation = LocationEntity.builder()
              .status(LocationStatus.ACTIVE)
              .build();
    }

    var locationBuilder = targetLocation.toBuilder();
    updateIfPresent(upsert.getGeoLocation(), locationBuilder::geoLocation, targetLocation.getGeoLocation());
    updateIfPresent(upsert.getCountryId(), locationBuilder::countryId, targetLocation.getCountryId());
    updateIfPresent(upsert.getCity(), locationBuilder::city, targetLocation.getCity());
    updateIfPresent(upsert.getAddress(), locationBuilder::address, targetLocation.getAddress());
    updateIfPresent(upsert.getCollection(), locationBuilder::collection, targetLocation.isCollection());
    updateIfPresent(upsert.getDelivery(), locationBuilder::delivery, targetLocation.isDelivery());
    updateIfPresent(upsert.getZip(), locationBuilder::zip, targetLocation.getZip());
    updateIfPresent(upsert.getFirstName(), locationBuilder::firstName, targetLocation.getFirstName());
    updateIfPresent(upsert.getLastName(), locationBuilder::lastName, targetLocation.getLastName());
    updateIfPresent(upsert.getEmail(), locationBuilder::mail, targetLocation.getMail());
    updateIfPresent(upsert.getInvoice(), locationBuilder::invoice, targetLocation.isInvoice());
    locationRepository.save(locationBuilder.build());
    return locationEntityConverter.toRest(targetLocation);
  }
}
