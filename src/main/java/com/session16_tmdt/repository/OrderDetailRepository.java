package com.session16_tmdt.repository;


import com.session16_tmdt.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od.product.name, SUM(od.quantity) FROM OrderDetail od GROUP BY od.product ORDER BY SUM(od.quantity) DESC")
    List<Object[]> findTopSellingProducts();
}