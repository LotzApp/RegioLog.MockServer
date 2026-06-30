package org.lotzapp.backend.controller;

import org.lotzapp.backend.entity.Price;
import org.lotzapp.backend.service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("prices")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping()
    public ResponseEntity<List<Price>> getPrice(@RequestParam Long id) {
        if(id == null) {
            return ResponseEntity.ok(priceService.getAllPrices());
        }
        return ResponseEntity.ok(List.of(priceService.getPriceById(id)));
    }

    @PutMapping
    public ResponseEntity<?> updatePrice(@RequestBody Price price) {
        priceService.updatePrice(price);
        return ResponseEntity.ok().build();
    }
}
