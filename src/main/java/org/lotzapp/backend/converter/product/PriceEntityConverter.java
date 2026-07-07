package org.lotzapp.backend.converter.product;

import org.lotzapp.backend.converter.IConverter;
import org.lotzapp.backend.entity.product.PriceEntity;
import org.lotzapp.regiologapi.model.Price;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceEntityConverter implements IConverter<Price, PriceEntity> {
  @Override
  public Price toRest(PriceEntity priceEntity) {
    var price =
        new Price(
            priceEntity.getValidFrom(),
            priceEntity.getId(),
            priceEntity.getCreatedAt(),
            priceEntity.getLastUpdate());
    price.net(BigDecimal.valueOf(priceEntity.getValue()));
    price.vat(BigDecimal.valueOf(priceEntity.getVat()));
    return price;
  }
}
