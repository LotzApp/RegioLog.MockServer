package org.lotzapp.backend.converter.product;

import org.lotzapp.backend.converter.IConverter;
import org.lotzapp.backend.entity.product.AssortmentEntity;
import org.lotzapp.regiologapi.model.Assortment;
import org.springframework.stereotype.Component;

import static org.lotzapp.util.DataUtils.addIfPresent;

@Component
public class AssortmentEntityConverter implements IConverter<Assortment, AssortmentEntity> {
    @Override
    public Assortment toRest(AssortmentEntity assortmentEntity) {
        var assortment = new Assortment();
        assortment.id(assortmentEntity.getId());
        assortment.name(assortmentEntity.getName());
        assortment.createdAt(assortmentEntity.getCreatedAt());
        assortment.lastUpdate(assortmentEntity.getLastUpdate());
        return assortment;
    }

    @Override
    public AssortmentEntity toEntity(Assortment assortment) {
        var builder = AssortmentEntity.builder();
        addIfPresent(assortment.getId(), builder::id);
        addIfPresent(assortment.getCreatedAt(), builder::createdAt);
        addIfPresent(assortment.getLastUpdate(), builder::lastUpdate);
        addIfPresent(assortment.getName(), builder::name);
        return builder.build();
    }
}
