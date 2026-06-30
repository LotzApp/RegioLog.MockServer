package org.lotzapp.backend.converter.client;

import org.lotzapp.backend.entity.client.LocationEntity;
import org.lotzapp.backend.converter.IConverter;
import org.lotzapp.regiologapi.model.Location;
import org.springframework.stereotype.Component;

import static org.lotzapp.util.DataUtils.addIfPresent;

@Component
public class LocationEntityConverter implements IConverter<Location, LocationEntity> {
  @Override
  public Location toRest(LocationEntity locationEntity) {
    var result =
        new Location(
            locationEntity.getAddress(),
            locationEntity.getZip(),
            locationEntity.getCity(),
            locationEntity.getStatus(),
            locationEntity.getCreatedAt(),
            locationEntity.getLastUpdate());
    result.countryId(locationEntity.getCountryId());
    result.id(locationEntity.getId());
    result.name(locationEntity.getName());
    return result;
  }

  @Override
  public LocationEntity toEntity(Location location) {
    var result =
        LocationEntity.builder()
                        .address(location.getAddress())
                        .zip(location.getZip())
                        .city(location.getCity())
                        .status(location.getStatus())
                        .address(location.getAddress())
                        .address(location.getAddress());

    addIfPresent(location.getGeoLocation(), result::geoLocation);
    addIfPresent(location.getCountryId(), result::countryId);
    addIfPresent(location.getId(), result::id);
    addIfPresent(location.getName(), result::name);

    return result.build();
  }
}
