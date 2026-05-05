package com.session16_tmdt.controller.user;

import com.session16_tmdt.dto.CheckoutShippingForm;
import com.session16_tmdt.model.User;
import com.session16_tmdt.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final OrderService orderService;

    public CheckoutController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String checkoutForm(HttpSession session, Model model, RedirectAttributes ra) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || "ADMIN".equals(sessionUser.getRole())) {
            ra.addFlashAttribute("error", "Vui lòng đăng nhập tài khoản khách hàng để thanh toán");
            return "redirect:/auth/login";
        }

        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (!model.containsAttribute("shipping")) {
            CheckoutShippingForm form = new CheckoutShippingForm();
            form.setFullName(sessionUser.getFullName());
            form.setEmail(sessionUser.getEmail());
            form.setPhone(sessionUser.getPhone());
            form.setAddress(sessionUser.getAddress());
            model.addAttribute("shipping", form);
        }
        return "user/checkout/checkout_form";
    }

    @PostMapping("/place")
    public String placeOrder(@Valid @ModelAttribute("shipping") CheckoutShippingForm shipping,
                             BindingResult result,
                             HttpSession session, Model model, RedirectAttributes ra) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || "ADMIN".equals(sessionUser.getRole())) {
            ra.addFlashAttribute("error", "Vui lòng đăng nhập tài khoản khách hàng để thanh toán");
            return "redirect:/auth/login";
        }

        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "Giỏ hàng trống");
            return "redirect:/cart";
        }

        if (result.hasErrors()) {
            model.addAttribute("shipping", shipping);
            return "user/checkout/checkout_form";
        }

        try {
            orderService.createOrder(sessionUser, shipping.getAddress(), cart);
            session.removeAttribute("cart");
            ra.addFlashAttribute("message", "Đặt hàng thành công!");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }
}
