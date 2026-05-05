package com.session16_tmdt.controller.user;

import com.session16_tmdt.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Hiển thị 8 sản phẩm đầu trang chủ
        model.addAttribute("products", productService.findAll(PageRequest.of(0, 8)));
        return "user/home";
    }
}