package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import com.kulebiakin.coffeeshops.service.CoffeeShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coffee-shops")
public class CoffeeShopController {

    private final CoffeeShopService coffeeShopService;

    @Autowired
    public CoffeeShopController(CoffeeShopService coffeeShopService) {
        this.coffeeShopService = coffeeShopService;
    }

    // Show list of coffee shops
    @GetMapping
    public String listCoffeeShops(@RequestParam(value = "sortByRating", required = false) String sortByRating, Model model) {
        List<CoffeeShop> coffeeShops;
        if ("desc".equals(sortByRating)) {
            coffeeShops = coffeeShopService.findAllOrderByRatingDesc();
        } else {
            coffeeShops = coffeeShopService.findAll();
        }
        model.addAttribute("coffeeShops", coffeeShops);
        return "coffee-list";
    }

    // Add new coffee shop (only available for MANAGER or ADMIN)
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/new")
    public String createCoffeeShopForm(Model model) {
        model.addAttribute("coffeeShop", new CoffeeShop());
        return "coffee-form";
    }

    // Save new coffeeshop
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping
    public String saveCoffeeShop(@ModelAttribute("coffeeShop") @Valid CoffeeShop coffeeShop, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "coffee-form";
        }
        coffeeShopService.save(coffeeShop);
        return "redirect:/coffee-shops";
    }

    // Edit coffeeshop
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editCoffeeShopForm(@PathVariable Long id, Model model) {
        CoffeeShop coffeeShop = coffeeShopService.findById(id);
        if (coffeeShop == null) {
            return "redirect:/coffee-shops";
        }
        model.addAttribute("coffeeShop", coffeeShop);
        return "coffee-form";
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateCoffeeShop(@PathVariable Long id, @ModelAttribute("coffeeShop") @Valid CoffeeShop coffeeShop, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "coffee-form";
        }
        coffeeShop.setId(id);
        coffeeShopService.save(coffeeShop);
        return "redirect:/coffee-shops";
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteCoffeeShop(@PathVariable Long id) {
        coffeeShopService.deleteById(id);
        return "redirect:/coffee-shops";
    }

}
