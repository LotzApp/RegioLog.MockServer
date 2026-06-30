package org.lotzapp.backend.service;

import java.time.OffsetDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.lotzapp.backend.ReflectionUpdater;
import org.lotzapp.backend.converter.client.DeliveryRhythmEntityConverter;
import org.lotzapp.backend.entity.client.DeliveryRhythmEntity;
import org.lotzapp.backend.repository.client.DeliveryRhythmRepository;
import org.lotzapp.regiologapi.model.DeliveryRhythm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class DeliveryRhythmService {
  private final DeliveryRhythmRepository deliveryRhythmRepository;
  private final DeliveryRhythmEntityConverter deliveryRhythmEntityConverter;

  @Autowired
  public DeliveryRhythmService(
      DeliveryRhythmRepository deliveryRhythmRepository,
      DeliveryRhythmEntityConverter deliveryRhythmEntityConverter) {
    this.deliveryRhythmRepository = deliveryRhythmRepository;
    this.deliveryRhythmEntityConverter = deliveryRhythmEntityConverter;
  }

  public List<DeliveryRhythm> getDeliveryRhythms() {
    return deliveryRhythmEntityConverter.toRest(deliveryRhythmRepository.findAll());
  }

  public DeliveryRhythm addDeliveryRhythm(DeliveryRhythm deliveryRhythm) {
    var targetEntity = deliveryRhythmEntityConverter.toEntity(deliveryRhythm);
    targetEntity.setLastUpdate(OffsetDateTime.now());
    targetEntity.setCreatedAt(OffsetDateTime.now());
    return deliveryRhythmEntityConverter.toRest(deliveryRhythmRepository.save(targetEntity));
  }

  public Optional<DeliveryRhythmEntity> getDeliveryRhythmEntity(UUID id) {
    return deliveryRhythmRepository.findById(id);
  }

  public Optional<DeliveryRhythm> getDeliveryRhythm(UUID id) {
    return getDeliveryRhythmEntity(id).map(deliveryRhythmEntityConverter::toRest);
  }

  public void deleteDeliveryRhythm(UUID id) {
    deliveryRhythmRepository.deleteById(id);
  }

  private void updateDeliveryRhythm(DeliveryRhythmEntity entity, DeliveryRhythm target) {
    ReflectionUpdater.updateFields(entity, target);
  }

  public DeliveryRhythm updateDeliveryRhythm(DeliveryRhythm location) {
    Assert.isTrue(location.getId().isPresent(), "Delivery rhythm id must be present for update");
    var deliveryRhythmEntity = deliveryRhythmRepository.findById(location.getId().get());

    Assert.isTrue(
        deliveryRhythmEntity.isPresent(),
        "Delivery rhythm with id " + location.getId() + " not found");
    updateDeliveryRhythm(deliveryRhythmEntity.get(), location);
    deliveryRhythmEntity.get().setLastUpdate(OffsetDateTime.now());

    return deliveryRhythmEntityConverter.toRest(
        deliveryRhythmRepository.save(deliveryRhythmEntity.get()));
  }

  public List<DeliveryRhythm> getDefaultDeliveryRhythms() {
    List<DeliveryRhythmEntity> deliveryRhythms =
        List.of(
            DeliveryRhythmEntity.builder()
                .name("Weekly Monday Delivery")
                .description("Standard weekly delivery every Monday")
                .orderDay(5)
                .orderTime("17:00")
                .deliveryDay(1)
                .deliveryTime("09:00")
                .deliveryWeek(null)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Bi-weekly Wednesday")
                .description("Every other Wednesday delivery")
                .orderDay(3)
                .orderTime("14:30")
                .deliveryDay(3)
                .deliveryTime("10:30")
                .deliveryWeek(2)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Daily Fresh Goods")
                .description("Daily delivery for perishables")
                .orderDay(null)
                .orderTime("08:00")
                .deliveryDay(null)
                .deliveryTime("14:00")
                .deliveryWeek(null)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Friday Premium")
                .description("Premium Friday delivery service")
                .orderDay(4)
                .orderTime("16:00")
                .deliveryDay(5)
                .deliveryTime("17:00")
                .deliveryWeek(null)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Monthly Bulk Order")
                .description("Large bulk delivery once per month")
                .orderDay(25)
                .orderTime("10:00")
                .deliveryDay(2)
                .deliveryTime("08:00")
                .deliveryWeek(4)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Tuesday Evening")
                .description("Tuesday late afternoon delivery")
                .orderDay(1)
                .orderTime("12:00")
                .deliveryDay(2)
                .deliveryTime("18:30")
                .deliveryWeek(null)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Quarterly Stock Up")
                .description("Quarterly large stock delivery")
                .orderDay(10)
                .orderTime("09:00")
                .deliveryDay(4)
                .deliveryTime("06:00")
                .deliveryWeek(13)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Weekend Service")
                .description("Saturday morning delivery")
                .orderDay(6)
                .orderTime("19:00")
                .deliveryDay(6)
                .deliveryTime("08:00")
                .deliveryWeek(null)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Emergency Rush")
                .description("Same-day or next-day emergency delivery")
                .orderDay(null)
                .orderTime("11:00")
                .deliveryDay(null)
                .deliveryTime("16:00")
                .deliveryWeek(1)
                .build(),
            DeliveryRhythmEntity.builder()
                .name("Thursday Supplies")
                .description("Regular Thursday supply delivery")
                .orderDay(2)
                .orderTime("13:00")
                .deliveryDay(4)
                .deliveryTime("11:00")
                .deliveryWeek(null)
                .build());
    return deliveryRhythmEntityConverter.toRest(deliveryRhythms);
  }
}
