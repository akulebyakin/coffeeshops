package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.CoffeeShopService;
import com.kulebiakin.coffeeshops.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CoffeeShopControllerTest {

    @Mock
    private CoffeeShopService coffeeShopService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CoffeeShopController coffeeShopController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listCoffeeShopsWithSorting() {
        List<CoffeeShop> coffeeShops = Collections.singletonList(new CoffeeShop());
        when(coffeeShopService.findAllSorted("name", "asc")).thenReturn(coffeeShops);

        String viewName = coffeeShopController.listCoffeeShops("name", "asc", model);

        assertEquals("coffee-list", viewName);
        verify(model).addAttribute("coffeeShops", coffeeShops);
        verify(model).addAttribute("currentSortBy", "name");
        verify(model).addAttribute("currentOrder", "asc");
    }

    @Test
    void listCoffeeShopsWithoutSorting() {
        List<CoffeeShop> coffeeShops = Collections.singletonList(new CoffeeShop());
        when(coffeeShopService.findAll()).thenReturn(coffeeShops);

        String viewName = coffeeShopController.listCoffeeShops(null, null, model);

        assertEquals("coffee-list", viewName);
        verify(model).addAttribute("coffeeShops", coffeeShops);
    }

    @Test
    void createCoffeeShopForm() {
        String viewName = coffeeShopController.createCoffeeShopForm(model);

        assertEquals("coffee-form", viewName);
        verify(model).addAttribute(eq("coffeeShop"), any(CoffeeShop.class));
    }

    @Test
    void saveCoffeeShopWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = coffeeShopController.saveCoffeeShop(new CoffeeShop(), bindingResult, authentication, redirectAttributes);

        assertEquals("coffee-form", viewName);
    }

    @Test
    void saveCoffeeShopSuccessfully() {
        CoffeeShop coffeeShop = new CoffeeShop();
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authentication.getName()).thenReturn("user");
        when(userService.findByLogin("user")).thenReturn(user);
        when(coffeeShopService.save(coffeeShop)).thenReturn(coffeeShop);

        String viewName = coffeeShopController.saveCoffeeShop(coffeeShop, bindingResult, authentication, redirectAttributes);

        assertEquals("redirect:/coffee-shops", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Кофейня успешно добавлена. ID = " + coffeeShop.getId());
    }

    @Test
    void editCoffeeShopFormWithValidId() {
        CoffeeShop coffeeShop = new CoffeeShop();
        when(coffeeShopService.findById(1L)).thenReturn(coffeeShop);

        String viewName = coffeeShopController.editCoffeeShopForm(1L, model);

        assertEquals("coffee-form", viewName);
        verify(model).addAttribute("coffeeShop", coffeeShop);
    }

    @Test
    void editCoffeeShopFormWithInvalidId() {
        when(coffeeShopService.findById(1L)).thenReturn(null);

        String viewName = coffeeShopController.editCoffeeShopForm(1L, model);

        assertEquals("redirect:/coffee-shops", viewName);
    }

    @Test
    void updateCoffeeShopWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = coffeeShopController.updateCoffeeShop(1L, new CoffeeShop(), bindingResult, redirectAttributes);

        assertEquals("coffee-form", viewName);
    }

    @Test
    void updateCoffeeShopSuccessfully() {
        CoffeeShop coffeeShop = new CoffeeShop();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = coffeeShopController.updateCoffeeShop(1L, coffeeShop, bindingResult, redirectAttributes);

        assertEquals("redirect:/coffee-shops", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Кофейня успешно обновлена. ID = 1");
    }

    @Test
    void deleteCoffeeShopSuccessfully() {
        String viewName = coffeeShopController.deleteCoffeeShop(1L, redirectAttributes);

        assertEquals("redirect:/coffee-shops", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Кофейня успешно удалена. ID = 1");
    }
}
