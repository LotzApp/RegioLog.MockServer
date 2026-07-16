package org.lotzapp.backend.controller.product;

import lombok.extern.slf4j.Slf4j;
import org.lotzapp.backend.service.product.ProductService;
import org.lotzapp.regiologapi.api.ProductApi;
import org.lotzapp.regiologapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping({"${openapi.regioLogData.base-path:/regiolog}"})
@Slf4j
public class RegioLogProductController implements ProductApi {
  private final ProductService productService;

  @Autowired
  public RegioLogProductController(ProductService productService) {
    this.productService = productService;
  }

  @Override
  public ResponseEntity<ProductPage> getProducts(
      OffsetDateTime lastUpdate,
      PossessionType type,
      UUID clientId,
      String search,
      Integer offset,
      Integer limit,
      String xRequestId) {
    var partnerProducts = productService.getPartnerProducts();
    var page = new ProductPage(0, partnerProducts.size(), partnerProducts.size(), partnerProducts);
    return ResponseEntity.ok(page);
  }

  @Override
  public ResponseEntity<Product> getProductById(UUID productId, String xRequestId) {
    var product = productService.getProductById(productId);
      return product.map(ResponseEntity::ok)
              .orElseGet(() -> ResponseEntity.notFound().build());

  }

  @Override
  public ResponseEntity<Product> addProduct(ProductUpsert productUpsert, String xRequestId) {
    return ProductApi.super.addProduct(productUpsert, xRequestId);
  }

  @Override
  public ResponseEntity<Product> updatePartnerProduct(
      UUID productId, ProductPartner productPartner, String xRequestId) {
    return ProductApi.super.updatePartnerProduct(productId, productPartner, xRequestId);
  }

  @Override
  public ResponseEntity<Product> updateProduct(
      UUID productId, ProductUpsert productUpsert, String xRequestId) {
    return ProductApi.super.updateProduct(productId, productUpsert, xRequestId);
  }
}
