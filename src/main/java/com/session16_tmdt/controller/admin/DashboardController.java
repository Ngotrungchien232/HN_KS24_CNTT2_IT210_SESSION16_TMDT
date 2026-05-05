package com.session16_tmdt.controller.admin;


import com.session16_tmdt.repository.OrderDetailRepository;
import com.session16_tmdt.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public DashboardController(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        double totalRevenue = orderRepository.findAll().stream().mapToDouble(o -> o.getTotalAmount()).sum();
        model.addAttribute("totalRevenue", totalRevenue);
        List<Object[]> topProducts = orderDetailRepository.findTopSellingProducts();
        model.addAttribute("topProducts", topProducts);
        return "admin/dashboard";
    }
}