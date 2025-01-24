package com.kulebiakin.coffeeshops.controller;

import com.kulebiakin.coffeeshops.entity.CoffeeShop;
import com.kulebiakin.coffeeshops.entity.User;
import com.kulebiakin.coffeeshops.service.CoffeeShopService;
import com.kulebiakin.coffeeshops.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/coffee-shops")
public class CoffeeShopController {

    private final CoffeeShopService coffeeShopService;
    private final UserService userService;

    @Autowired
    public CoffeeShopController(CoffeeShopService coffeeShopService, UserService userService) {
        this.coffeeShopService = coffeeShopService;
        this.userService = userService;
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
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public String saveCoffeeShop(@ModelAttribute("coffeeShop") @Valid CoffeeShop coffeeShop,
                                 BindingResult bindingResult,
                                 Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "coffee-form";
        }

        User currentUser = userService.findByLogin(authentication.getName());
        coffeeShop.setAddedBy(currentUser); // Set current user as the one who added the coffee shop
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
        coffeeShopService.updateCoffeeShop(id, coffeeShop);
        return "redirect:/coffee-shops";
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteCoffeeShop(@PathVariable Long id) {
        coffeeShopService.deleteById(id);
        return "redirect:/coffee-shops";
    }

}
