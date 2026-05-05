package com.session16_tmdt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User không được để trống")
    private User user;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "shipping_address", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Size(min = 5, max = 500, message = "Địa chỉ phải từ 5 đến 500 ký tự")
    private String shippingAddress;

    @Column(name = "total_amount", nullable = false)
    @NotNull(message = "Tổng tiền không được để trống")
    @DecimalMin(value = "0.01", message = "Tổng tiền phải lớn hơn 0")
    private Double totalAmount;

    private String status;  // "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
