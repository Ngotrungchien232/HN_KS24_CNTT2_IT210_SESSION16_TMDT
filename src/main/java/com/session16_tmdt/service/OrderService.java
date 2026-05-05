package com.session16_tmdt.service;


import com.session16_tmdt.model.Order;
import com.session16_tmdt.model.OrderDetail;
import com.session16_tmdt.model.Product;
import com.session16_tmdt.model.User;
import com.session16_tmdt.repository.OrderDetailRepository;
import com.session16_tmdt.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
    }

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(User user, String shippingAddress, Map<Long, Integer> cartItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING");
        order.setTotalAmount(0.0);
        Order savedOrder = orderRepository.save(order);

        double total = 0;
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = productService.findById(productId);
            if (product.getStock() < quantity) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng");
            }
            productService.updateStock(productId, quantity);

            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setUnitPrice(product.getPrice());
            orderDetailRepository.save(detail);
            total += product.getPrice() * quantity;
        }
        savedOrder.setTotalAmount(total);
        return orderRepository.save(savedOrder);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }
}
