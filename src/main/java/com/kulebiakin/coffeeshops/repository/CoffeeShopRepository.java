package com.kulebiakin.coffeeshops.repository;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoffeeShopRepository extends JpaRepository<CoffeeShop, Long> {

    // Sort coffee shops by rating
    @Query("SELECT c FROM CoffeeShop c ORDER BY c.rating DESC")
    List<CoffeeShop> findAllOrderByRatingDesc();

    // Sort coffee shops by id
    List<CoffeeShop> findAllByOrderByIdAsc();
    List<CoffeeShop> findAllByOrderByIdDesc();

    // Sort coffee shops by name
    List<CoffeeShop> findAllByOrderByNameAsc();
    List<CoffeeShop> findAllByOrderByNameDesc();

    // Sort coffee shops by address
    List<CoffeeShop> findAllByOrderByRatingAsc();
    List<CoffeeShop> findAllByOrderByRatingDesc();
}

