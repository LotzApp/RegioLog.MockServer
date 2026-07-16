package org.lotzapp.backend.service.product;

import org.lotzapp.backend.converter.product.RegioLogProductEntityConverter;
import org.lotzapp.backend.entity.product.PriceEntity;
import org.lotzapp.backend.entity.product.ProductEntity;
import org.lotzapp.backend.repository.client.ClientRepository;
import org.lotzapp.backend.repository.product.ProductRepository;
import org.lotzapp.backend.service.client.ClientService;
import org.lotzapp.regiologapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final ClientService clientService;
  private final ClientRepository clientRepository;
  private final RegioLogProductEntityConverter productEntityConverter;

  @Autowired
  public ProductService(
      ProductRepository productRepository,
      ClientService clientService,
      ClientRepository clientRepository,
      RegioLogProductEntityConverter productEntityConverter) {
    this.productRepository = productRepository;
    this.clientService = clientService;
    this.clientRepository = clientRepository;
    this.productEntityConverter = productEntityConverter;
  }

  private PriceEntity buildPriceEntity(double net, int vat) {
    return PriceEntity.builder()
        .value(net)
        .vat(vat)
        .validFrom(OffsetDateTime.now())
        .createdAt(OffsetDateTime.now())
        .lastUpdate(OffsetDateTime.now())
        .build();
  }

  @Transactional
  public void initializeData() {
    var partners = clientRepository.findAll();
    var products =
        List.of(
            ProductEntity.builder()
                .name("Bio Vollmilch 1L")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.5, 19), buildPriceEntity(1.4, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Freilandeier 10er")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(2)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(2.8, 19), buildPriceEntity(2.6, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Roggenbrot 500g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(276)
                .prices(List.of(buildPriceEntity(1.8, 19), buildPriceEntity(1.65, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Bergkäse gereift 200g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(3)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(3.2, 19), buildPriceEntity(2.95, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Kartoffeln 2.5kg")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(4)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.9, 19), buildPriceEntity(1.75, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Apfelsaft naturtrüb 1L")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.6, 19), buildPriceEntity(1.45, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Honig Wildblüte 500g")
                .status(ProductStatus.INACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(3)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(5.5, 19), buildPriceEntity(5.0, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Rindfleisch Faschiert 500g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(2)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(4.8, 19), buildPriceEntity(4.4, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Marmelade Erdbeere 250g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(2.2, 19), buildPriceEntity(2.0, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Nudeln Dinkel 400g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.7, 19), buildPriceEntity(1.55, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Karotten Bund")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(4)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(0.9, 19), buildPriceEntity(0.8, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Joghurt Natur 500g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.1, 19), buildPriceEntity(0.99, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Speck geräuchert 200g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(3)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(3.5, 19), buildPriceEntity(3.2, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Zwiebeln 1kg")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(4)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(0.75, 19), buildPriceEntity(0.65, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Butter gesalzen 250g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.8, 19), buildPriceEntity(1.65, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Weißwein Grüner Veltliner 0.75L")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(6.5, 19), buildPriceEntity(5.9, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Äpfel Gala 1kg")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(4)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.2, 19), buildPriceEntity(1.05, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Forelle geräuchert 300g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(2)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(5.8, 19), buildPriceEntity(5.3, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Linsen getrocknet 500g")
                .status(ProductStatus.ACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(1)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(1.4, 19), buildPriceEntity(1.25, 7)))
                .assortments(List.of())
                .build(),
            ProductEntity.builder()
                .name("Ziegenkäse Rolle 150g")
                .status(ProductStatus.INACTIVE)
                .productVisibility(ProductVisibility.VISIBLE)
                .salesUnitId(3)
                .originCountryId(40)
                .prices(List.of(buildPriceEntity(3.8, 19), buildPriceEntity(3.4, 7)))
                .assortments(List.of())
                .build());

    IntStream.range(0, products.size())
        .forEach(
            i -> {
              var targetProduct = products.get(i);
              var targetPartner = partners.get(i % partners.size());
              targetProduct.setClient(targetPartner);
              targetPartner.getProducts().add(targetProduct);
            });

    productRepository.saveAll(products);
  }

  @Transactional(readOnly = true)
  public List<Product> getPartnerProducts() {
    var ownClientId = clientService.getOwnClientId();
    var partnerProducts = new ArrayList<ProductEntity>();

    for (var p : productRepository.findAll()) {
      if (!p.getClient().getId().equals(ownClientId)) {
        partnerProducts.add(p);
      }
    }
    return productEntityConverter.toRest(partnerProducts);
  }

  /**
   * Retrieves a product by its unique identifier.
   *
   * @param productId the unique identifier of the product to retrieve
   * @return an {@code Optional} containing the product if found, or an empty {@code Optional} if
   *     not found
   */
  @Transactional(readOnly = true)
  public Optional<Product> getProductById(UUID productId) {
    return productRepository.findById(productId).map(productEntityConverter::toRest);
  }

  @Transactional
  public Product upsertProduct(UUID productId, ProductUpsert productUpsert) {
    var optionalProduct = productRepository.findById(productId);
    if (optionalProduct.isEmpty()) return null;

    var productEntity = optionalProduct.get();
    if (productUpsert.getData() != null) updateProductData(productEntity, productUpsert.getData());
    productRepository.save(productEntity);
    return productEntityConverter.toRest(productEntity);
  }
  private void updateProductData(ProductEntity productEntity, ProductData data) {
    productEntity.setName(data.getName());
    productEntity.setStatus(data.getStatus());
    productEntity.setProductVisibility(data.getVisibility());
    // data.getContent()
    //        data.getDeposit()
    //        data.getNumber()
    //        data.getSeasonal()
    //        data.getShortName();
  }
}
