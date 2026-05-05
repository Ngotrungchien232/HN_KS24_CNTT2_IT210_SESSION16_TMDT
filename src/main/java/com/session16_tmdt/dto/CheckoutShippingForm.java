package com.session16_tmdt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckoutShippingForm {

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
    private String fullName;

    @Pattern(regexp = "^$|^[\\w-.]+@[\\w-]+(\\.[\\w-]+)+$", message = "Email không hợp lệ")
    private String email;

    @Pattern(regexp = "^$|^[0-9]{10,11}$", message = "Số điện thoại phải là 10-11 chữ số")
    private String phone;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Size(min = 5, max = 500, message = "Địa chỉ phải từ 5 đến 500 ký tự")
    private String address;
}
