package org.lotzapp.component;

import org.lotzapp.regiologapi.model.*;
import org.lotzapp.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductComponent {
    private final List<ProductPage> PRODUCT_PAGES = new ArrayList<>();
    private final List<Product> ALL_PRODUCTS = new ArrayList<>();

    private final ClientComponent clientComponent;

    @Autowired
    public ProductComponent(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    public void initializeData() {
        PRODUCT_PAGES.add(getProductPage(0, 3));
        PRODUCT_PAGES.add(getProductPage(3, 1));
        PRODUCT_PAGES.add(getProductPage(4, 7));
    }

    public List<Product> getProducts() {
        return ALL_PRODUCTS;
    }

    private Product getProduct(int id, ProductData data) {
        if(data == null) {
        data = new ProductData(
                "### Name %d #### THIs is a very long name".formatted(id),
                ProductStatus.ACTIVE,
                ProductVisibility.VISIBLE
            );
        }

        var clients = clientComponent.getClientList();
        var clientId = id % clients.size();

        var result = new Product(
                data,
                UUID.randomUUID(),
                1,
                1,
                CategoryComponent.getCategories().getFirst().getId(),
                clients.get(clientId).getId().get(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        var price = new Price(
                OffsetDateTime.now(),
                UUID.randomUUID(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        price.net(new BigDecimal("12.2"));
        price.vat(new BigDecimal("20"));
        result.salesUnitId(id);
        result.addPricesItem(price);
        return result;
    }

    private void addProductsToList(List<Product> listToAdd, Product... products) {
        for(var p : products) {
            ALL_PRODUCTS.add(p);
            listToAdd.add(p);
        }
    }

    private ProductPage getProductPage(int start, int items) {
        var products = new ArrayList<Product>();
        for(int i = start; i < start + items; i++) {
            addProductsToList(products,
                    getProduct(i, null),
                    getProduct(i, new ProductData(
                            TimeUtils.LOADING_TIMEOUT_KEY,
                            ProductStatus.ACTIVE,
                            ProductVisibility.VISIBLE
                    )),
                    getProduct(i, new ProductData(
                            TimeUtils.LONG_LOAD_KEY,
                            ProductStatus.ACTIVE,
                            ProductVisibility.VISIBLE
                    ))
            );
        }
        var page = new ProductPage(
                start, start + items, items,
                products
        );
        return page;
    }

    public ResponseEntity<Product> updateProduct(Object[] args) {
        var uuid = (UUID) args[0];
        var partnerProduct = (ProductPartner) args[1];

        var productToUpdate = ALL_PRODUCTS.stream().filter(p -> p.getId().equals(uuid))
                .findFirst();

        if(productToUpdate.isPresent())  {
            var product = productToUpdate.get();
            product.getData().setVisibility(partnerProduct.getVisibility().get());
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<ProductPage> getProducts(Object[] args) {
        return ResponseEntity.ok(PRODUCT_PAGES.getFirst());
    }

    public Product getProduct(Object[] args) {
        var uuid = (UUID) args[0];
        var product = ALL_PRODUCTS.stream()
                .filter(p -> p.getId().equals(uuid))
                .findFirst()
                .orElse(null);
        return product;
    }
}
