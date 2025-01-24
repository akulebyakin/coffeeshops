package com.kulebiakin.coffeeshops.service;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import com.kulebiakin.coffeeshops.repository.CoffeeShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoffeeShopServiceTest {

    @Mock
    private CoffeeShopRepository coffeeShopRepository;

    @InjectMocks
    private CoffeeShopService coffeeShopService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsListOfCoffeeShops() {
        List<CoffeeShop> coffeeShops = List.of(new CoffeeShop(), new CoffeeShop());
        when(coffeeShopRepository.findAll()).thenReturn(coffeeShops);

        List<CoffeeShop> result = coffeeShopService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findAllOrderByRatingDesc_ReturnsListOfCoffeeShopsOrderedByRating() {
        List<CoffeeShop> coffeeShops = List.of(new CoffeeShop(), new CoffeeShop());
        when(coffeeShopRepository.findAllOrderByRatingDesc()).thenReturn(coffeeShops);

        List<CoffeeShop> result = coffeeShopService.findAllOrderByRatingDesc();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findById_CoffeeShopExists_ReturnsCoffeeShop() {
        CoffeeShop coffeeShop = new CoffeeShop();
        coffeeShop.setId(1L);
        when(coffeeShopRepository.findById(1L)).thenReturn(Optional.of(coffeeShop));

        CoffeeShop result = coffeeShopService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_CoffeeShopDoesNotExist_ReturnsNull() {
        when(coffeeShopRepository.findById(1L)).thenReturn(Optional.empty());

        CoffeeShop result = coffeeShopService.findById(1L);

        assertNull(result);
    }

    @Test
    void save_SavesAndReturnsCoffeeShop() {
        CoffeeShop coffeeShop = new CoffeeShop();
        when(coffeeShopRepository.save(coffeeShop)).thenReturn(coffeeShop);

        CoffeeShop result = coffeeShopService.save(coffeeShop);

        assertNotNull(result);
        verify(coffeeShopRepository, times(1)).save(coffeeShop);
    }

    @Test
    void deleteById_DeletesCoffeeShop() {
        doNothing().when(coffeeShopRepository).deleteById(1L);

        coffeeShopService.deleteById(1L);

        verify(coffeeShopRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateCoffeeShop_CoffeeShopExists_UpdatesAndReturnsCoffeeShop() {
        CoffeeShop existingCoffeeShop = new CoffeeShop();
        existingCoffeeShop.setId(1L);
        existingCoffeeShop.setName("Old Name");

        CoffeeShop updatedCoffeeShop = new CoffeeShop();
        updatedCoffeeShop.setName("New Name");

        when(coffeeShopRepository.findById(1L)).thenReturn(Optional.of(existingCoffeeShop));
        when(coffeeShopRepository.save(existingCoffeeShop)).thenReturn(existingCoffeeShop);

        coffeeShopService.updateCoffeeShop(1L, updatedCoffeeShop);

        assertEquals("New Name", existingCoffeeShop.getName());
        verify(coffeeShopRepository, times(1)).save(existingCoffeeShop);
    }

    @Test
    void updateCoffeeShop_CoffeeShopDoesNotExist_DoesNothing() {
        CoffeeShop updatedCoffeeShop = new CoffeeShop();
        updatedCoffeeShop.setName("New Name");

        when(coffeeShopRepository.findById(1L)).thenReturn(Optional.empty());

        coffeeShopService.updateCoffeeShop(1L, updatedCoffeeShop);

        verify(coffeeShopRepository, never()).save(any(CoffeeShop.class));
    }
}