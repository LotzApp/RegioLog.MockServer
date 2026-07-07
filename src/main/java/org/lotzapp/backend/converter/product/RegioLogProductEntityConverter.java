package org.lotzapp.backend.converter.product;

import org.lotzapp.backend.converter.IConverter;
import org.lotzapp.backend.entity.product.ProductEntity;
import org.lotzapp.regiologapi.model.Product;
import org.lotzapp.regiologapi.model.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegioLogProductEntityConverter implements IConverter<Product, ProductEntity> {
    private final PriceEntityConverter priceEntityConverter;

    @Autowired
    public RegioLogProductEntityConverter(PriceEntityConverter priceEntityConverter) {
        this.priceEntityConverter = priceEntityConverter;
    }

    private ProductData getProductData(ProductEntity productEntity) {
        return new ProductData(
                productEntity.getName(),
                productEntity.getStatus(),
                productEntity.getProductVisibility()
        );
    }

    @Override
    public Product toRest(ProductEntity productEntity) {
        var product = new Product(
                getProductData(productEntity),
                productEntity.getId(),
                productEntity.getSalesUnitId(),
                productEntity.getOriginCountryId(),
                null,
                productEntity.getClient().getId(),
                productEntity.getCreatedAt(),
                productEntity.getLastUpdate());
        product.salesUnitId(productEntity.getSalesUnitId());
        product.prices(priceEntityConverter.toRest(productEntity.getPrices()));
        //ToDo
        product.customCategoryId(UUID.randomUUID());
        return product;
    }
}
