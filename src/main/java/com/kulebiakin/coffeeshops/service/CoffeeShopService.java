package com.kulebiakin.coffeeshops.service;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import com.kulebiakin.coffeeshops.repository.CoffeeShopRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CoffeeShop save(CoffeeShop coffeeShop) {
        return coffeeShopRepository.save(coffeeShop);
    }

    public void deleteById(Long id) {
        coffeeShopRepository.deleteById(id);
    }

    @Transactional
    public CoffeeShop updateCoffeeShop(Long id, @Valid CoffeeShop coffeeShop) {
        CoffeeShop existingCoffeeShop = coffeeShopRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Coffee shop not found with id: " + id));
        mapToExistingCoffeeShop(coffeeShop, existingCoffeeShop);
        return coffeeShopRepository.save(existingCoffeeShop);
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

    public List<CoffeeShop> findAllSorted(String sortBy, String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return switch (sortBy) {
                case "name" -> coffeeShopRepository.findAllByOrderByNameDesc();
                case "rating" -> coffeeShopRepository.findAllByOrderByRatingDesc();
                default -> // sortBy = "id"
                        coffeeShopRepository.findAllByOrderByIdDesc();
            };
        } else {
            return switch (sortBy) {
                case "name" -> coffeeShopRepository.findAllByOrderByNameAsc();
                case "rating" -> coffeeShopRepository.findAllByOrderByRatingAsc();
                default -> // sortBy = "id"
                        coffeeShopRepository.findAllByOrderByIdAsc();
            };
        }
    }

}

