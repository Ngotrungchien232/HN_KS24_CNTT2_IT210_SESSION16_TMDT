create database session16_tmdt;
use session16_tmdt;

-- Bảng User
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    role VARCHAR(20) DEFAULT 'CUSTOMER'   -- ADMIN hoặc CUSTOMER
);

-- Bảng Category
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- Bảng Product
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    stock INT NOT NULL,
    description TEXT,
    category_id BIGINT,
    image_url VARCHAR(500),
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
);

-- Bảng Order
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date DATETIME NOT NULL,
    shipping_address TEXT NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Tài khoản quản trị mẫu (đăng nhập: admin / admin123)
INSERT INTO user (username, password, full_name, email, role)
VALUES ('admin', 'admin123', N'Quản trị viên', 'admin@shop.local', 'ADMIN');

-- Bảng OrderDetail
CREATE TABLE order_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id)
);