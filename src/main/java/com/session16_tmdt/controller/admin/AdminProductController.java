package com.session16_tmdt.controller.admin;


import com.session16_tmdt.model.Product;
import com.session16_tmdt.service.CategoryService;
import com.session16_tmdt.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("products", productService.findAll(PageRequest.of(page, 10)));
        model.addAttribute("currentPage", page);
        return "admin/product/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product/form";
    }

    @PostMapping("/add")
    public String add(@Valid Product product, BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product/form";
        }
        productService.save(product);
        ra.addFlashAttribute("message", "Thêm sản phẩm thành công");
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid Product product, BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product/form";
        }
        product.setId(id);
        productService.save(product);
        ra.addFlashAttribute("message", "Cập nhật sản phẩm thành công");
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        productService.deleteById(id);
        ra.addFlashAttribute("message", "Xóa sản phẩm thành công");
        return "redirect:/admin/products";
    }
}
