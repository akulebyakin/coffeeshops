package com.kulebiakin.coffeeshops.service;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import com.kulebiakin.coffeeshops.repository.CoffeeShopRepository;
import jakarta.validation.Valid;
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

    public void deleteById(Long id) {
        coffeeShopRepository.deleteById(id);
    }

    public void updateCoffeeShop(Long id, @Valid CoffeeShop coffeeShop) {
        CoffeeShop existingCoffeeShop = coffeeShopRepository.findById(id).orElse(null);
        if (existingCoffeeShop != null) {
            mapToExistingCoffeeShop(coffeeShop, existingCoffeeShop);
            coffeeShopRepository.save(existingCoffeeShop);
        }
    }

    private static void mapToExistingCoffeeShop(CoffeeShop coffeeShop, CoffeeShop existingCoffeeShop) {
        existingCoffeeShop.setName(coffeeShop.getName());
        existingCoffeeShop.setAddress(coffeeShop.getAddress());
        existingCoffeeShop.setPhone(coffeeShop.getPhone());
        existingCoffeeShop.setEmail(coffeeShop.getEmail());
        existingCoffeeShop.setWebsite(coffeeShop.getWebsite());
        existingCoffeeShop.setDescription(coffeeShop.getDescription());
        existingCoffeeShop.setImage(coffeeShop.getImage());
        existingCoffeeShop.setRating(coffeeShop.getRating());
    }
}

