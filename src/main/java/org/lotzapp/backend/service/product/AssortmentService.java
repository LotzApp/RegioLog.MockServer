package org.lotzapp.backend.service.product;

import org.lotzapp.backend.converter.product.AssortmentEntityConverter;
import org.lotzapp.backend.repository.product.AssortmentRepository;
import org.lotzapp.regiologapi.model.Assortment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssortmentService {
  private final AssortmentRepository assortmentRepository;
  private final AssortmentEntityConverter assortmentEntityConverter;

  @Autowired
  public AssortmentService(
      AssortmentRepository assortmentRepository,
      AssortmentEntityConverter assortmentEntityConverter) {
    this.assortmentRepository = assortmentRepository;
    this.assortmentEntityConverter = assortmentEntityConverter;
  }

  public List<Assortment> getAssortments() {
    return assortmentEntityConverter.toRest(assortmentRepository.findAll());
  }

  public Assortment addAssortment(Assortment assortment) {
    if (assortment.getId().isPresent())
      throw new IllegalArgumentException("Assortment id must not be set");
    var targetEntity = assortmentEntityConverter.toEntity(assortment);
    return assortmentEntityConverter.toRest(assortmentRepository.save(targetEntity));
  }

  public Assortment getAssortmentById(UUID id) {
    var result = assortmentRepository.findById(id);
    return result.map(assortmentEntityConverter::toRest).orElse(null);
  }
}
