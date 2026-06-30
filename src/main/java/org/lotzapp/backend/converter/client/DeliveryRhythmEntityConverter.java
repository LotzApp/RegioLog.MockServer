package org.lotzapp.backend.converter.client;

import org.lotzapp.backend.entity.client.DeliveryRhythmEntity;
import org.lotzapp.backend.converter.IConverter;
import org.lotzapp.regiologapi.model.DeliveryRhythm;
import org.springframework.stereotype.Component;

import static org.lotzapp.util.DataUtils.addIfPresent;

@Component
public class DeliveryRhythmEntityConverter
    implements IConverter<DeliveryRhythm, DeliveryRhythmEntity> {

  @Override
  public DeliveryRhythm toRest(DeliveryRhythmEntity deliveryRhythmEntity) {
    var result =
        new DeliveryRhythm(
            deliveryRhythmEntity.getName(),
            deliveryRhythmEntity.getDescription(),
            deliveryRhythmEntity.getValidFrom());
    result.id(deliveryRhythmEntity.getId());
    return result;
  }

  @Override
  public DeliveryRhythmEntity toEntity(DeliveryRhythm deliveryRhythm) {
    var result = DeliveryRhythmEntity.builder()
            .name(deliveryRhythm.getName())
            .description(deliveryRhythm.getDescription())
            .validFrom(deliveryRhythm.getValidFrom());
    addIfPresent(deliveryRhythm.getId(), result::id);
    return result.build();
  }
}
