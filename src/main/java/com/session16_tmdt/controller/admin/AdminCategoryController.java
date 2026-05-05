package com.session16_tmdt.controller.admin;

import com.session16_tmdt.model.Category;
import com.session16_tmdt.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/category/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/form";
    }

    @PostMapping("/add")
    public String add(@Valid Category category, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/category/form";
        }
        categoryService.save(category);
        ra.addFlashAttribute("message", "Thêm danh mục thành công");
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "admin/category/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid Category category, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/category/form";
        }
        category.setId(id);
        categoryService.save(category);
        ra.addFlashAttribute("message", "Cập nhật danh mục thành công");
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (categoryService.hasProducts(id)) {
            ra.addFlashAttribute("error", "Không thể xóa danh mục vì còn sản phẩm");
        } else {
            categoryService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa danh mục thành công");
        }
        return "redirect:/admin/categories";
    }
}
