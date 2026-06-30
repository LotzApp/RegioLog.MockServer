package org.lotzapp.backend.service;

import org.lotzapp.backend.entity.Price;
import org.lotzapp.backend.repository.PriceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceService {
    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;

        var defaultPrice = new Price(null, 14.2);
        priceRepository.save(defaultPrice);
    }

    public Price getPriceById(long id) {
        var price = priceRepository.findById(id);
        if(price.isPresent()) {
            return price.get();
        }
        throw new IllegalArgumentException("Price with id " + id + " not found");
    }

    public Price addPrice(Price price) {
        return priceRepository.save(price);
    }

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    public void updatePrice(Price price) {
        var entity = priceRepository.findById(price.getId());
        if(entity.isPresent()) {
            entity.get().setValue(price.getValue());
            priceRepository.save(entity.get());
        } else {
            throw new IllegalArgumentException("Price with id " + price.getId() + " not found");
        }
    }
}
