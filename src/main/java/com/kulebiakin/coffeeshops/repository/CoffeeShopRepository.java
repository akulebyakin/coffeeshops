package com.kulebiakin.coffeeshops.repository;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoffeeShopRepository extends JpaRepository<CoffeeShop, Long> {

    // Пример метода для сортировки кофеен по рейтингу
    @Query("SELECT c FROM CoffeeShop c ORDER BY c.rating DESC")
    List<CoffeeShop> findAllOrderByRatingDesc();
}

