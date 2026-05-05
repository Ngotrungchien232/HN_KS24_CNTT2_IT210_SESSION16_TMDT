package com.session16_tmdt.controller.user;

import com.session16_tmdt.model.Product;
import com.session16_tmdt.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @SuppressWarnings("unchecked")
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();
        Map<Product, Integer> cartItems = new HashMap<>();
        double total = 0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product p = productService.findById(entry.getKey());
            if (p != null) {
                cartItems.put(p, entry.getValue());
                total += p.getPrice() * entry.getValue();
            }
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "user/cart/cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();
        cart.put(id, cart.getOrDefault(id, 0) + quantity);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null) cart.remove(id);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id, @RequestParam int quantity, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null && quantity > 0) {
            cart.put(id, quantity);
        } else if (cart != null && quantity <= 0) {
            cart.remove(id);
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }
}