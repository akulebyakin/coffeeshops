package com.kulebiakin.coffeeshops.service;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import com.kulebiakin.coffeeshops.repository.CoffeeShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoffeeShopService {

    private final CoffeeShopRepository coffeeShopRepository;

    @Autowired
    public CoffeeShopService(CoffeeShopRepository coffeeShopRepository) {
        this.coffeeShopRepository = coffeeShopRepository;
    }

    public List<CoffeeShop> findAll() {
        return coffeeShopRepository.findAll();
    }

    public List<CoffeeShop> findAllOrderByRatingDesc() {
        return coffeeShopRepository.findAllOrderByRatingDesc();
    }

    public CoffeeShop findById(Long id) {
        return coffeeShopRepository.findById(id).orElse(null);
    }

    public CoffeeShop save(CoffeeShop coffeeShop) {
        return coffeeShopRepository.save(coffeeShop);
    }

    public void delete(Long id) {
        coffeeShopRepository.deleteById(id);
    }
}

