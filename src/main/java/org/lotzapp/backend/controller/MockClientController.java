package org.lotzapp.backend.controller;

import org.lotzapp.backend.service.DeliveryRhythmService;
import org.lotzapp.backend.service.LocationService;
import org.lotzapp.regiologapi.model.DeliveryRhythm;
import org.lotzapp.regiologapi.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("client")
public class MockClientController {

    private final LocationService locationService;
    private final DeliveryRhythmService deliveryRhythmService;

    @Autowired
    public MockClientController(LocationService locationService, DeliveryRhythmService deliveryRhythmService) {
        this.locationService = locationService;
        this.deliveryRhythmService = deliveryRhythmService;
    }

    @GetMapping("locations")
    public ResponseEntity<Collection<Location>> getLocations() {
        return ResponseEntity.ok(locationService.getLocations());
    }

    @PostMapping("locations")
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.addLocation(location));
    }

    @DeleteMapping("locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("locations")
    public ResponseEntity<Location> updateLocation(@RequestBody Location location) {
        location.setLastUpdate(OffsetDateTime.now());
        return ResponseEntity.ok(locationService.updateLocation(location));
    }


    @GetMapping("deliveryRhythms")
    public ResponseEntity<Collection<DeliveryRhythm>> getDeliveryRhythms() {
        return ResponseEntity.ok(deliveryRhythmService.getDeliveryRhythms());
    }

    @PostMapping("deliveryRhythms")
    public ResponseEntity<DeliveryRhythm> addDeliveryRhythm(@RequestBody DeliveryRhythm deliveryRhythm) {
        return ResponseEntity.ok(deliveryRhythmService.addDeliveryRhythm(deliveryRhythm));
    }

    @DeleteMapping("deliveryRhythms/{id}")
    public ResponseEntity<Void> deleteDeliveryRhythm(@PathVariable UUID id) {
        deliveryRhythmService.deleteDeliveryRhythm(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("deliveryRhythms")
    public ResponseEntity<DeliveryRhythm> updateDeliveryRhythm(@RequestBody DeliveryRhythm deliveryRhythm) {
        deliveryRhythm.lastUpdate(OffsetDateTime.now());
        return ResponseEntity.ok(deliveryRhythmService.updateDeliveryRhythm(deliveryRhythm));
    }
}
