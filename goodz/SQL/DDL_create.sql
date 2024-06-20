-- Active: 1716800736662@@127.0.0.1@3306@goodz

-- User 테이블  / 📁 user
CREATE TABLE `User` (
    `user_id` VARCHAR(100) NOT NULL,
    `no` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `nickname` VARCHAR(100),
    `password` VARCHAR(100) NOT NULL,
    `birth` VARCHAR(50) NULL,
    `phone_number` VARCHAR(20) NULL,
    `account` VARCHAR(255) NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY (`no`)
) COMMENT='유저';

CREATE TABLE `user_auth` (
	`auth_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`AUTH` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`auth_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='사용자 권한';


CREATE TABLE `Persistent_Login` (
	`series` VARCHAR(64) NOT NULL,
	`user_name` VARCHAR(64) NOT NULL,
	`token` VARCHAR(64) NOT NULL,
	`Field` TIMESTAMP NOT NULL,
	PRIMARY KEY (`series`),
    FOREIGN KEY (`user_name`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='자동 로그인';


CREATE TABLE `Social_Login` (
	`social_login_id` VARCHAR(100) NOT NULL,
	`user_id` VARCHAR(100) NOT NULL,
	`provider` VARCHAR(50) NOT NULL,
	`provider_user_id` VARCHAR(100) NOT NULL,
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`social_login_id`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='소셜로그인';

CREATE TABLE `Shippingaddress` (
	`address_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`recipient_name` VARCHAR(100) NOT NULL,
	`address` VARCHAR(255) NOT NULL,
	`zip_code` VARCHAR(20) NOT NULL,
	`phone_number` VARCHAR(20) NOT NULL,
	`is_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '기본 배송지 설정 여부',
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`address_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='배송주소';

CREATE TABLE `Follow` (
	`follower_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`follower_id` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`follower_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (`follower_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='팔로우';


-- Brand 테이블 / 📁 product
CREATE TABLE `Brand` (
	`b_no` INT NOT NULL AUTO_INCREMENT COMMENT '브랜드번호',
	`b_name` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`b_no`),
    UNIQUE KEY (`b_name`)
) COMMENT='브랜드';

CREATE TABLE `Product` (
	`p_no` INT NOT NULL AUTO_INCREMENT,
	`b_name` VARCHAR(100) NOT NULL COMMENT '브랜드번호',
	`product_name` VARCHAR(100) NOT NULL,
	`initial_price` INT NOT NULL,
	`category` VARCHAR(100) NOT NULL,
	`views` INT NOT NULL DEFAULT 0,
	`created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`p_no`)
) COMMENT='상품';

CREATE TABLE `product_option` (
	`option_id` INT NOT NULL AUTO_INCREMENT,
	`p_no` INT NOT NULL,
	`size` VARCHAR(50) NOT NULL,
	`option_price` INT NOT NULL,
	`stock_quantity` INT NOT NULL,
	`status` ENUM('on', 'off') NOT NULL,
	PRIMARY KEY (`option_id`),
    FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='상품 옵션';


CREATE TABLE `Pricehistory` (
	`price_history_no` INT NOT NULL AUTO_INCREMENT,
	`p_no` INT NOT NULL,
	`size` VARCHAR(50) NOT NULL,
	`fluctuated_price` INT NOT NULL,
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`price_history_no`),
    FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='가격변동';

CREATE TABLE `Purchase` (
	`purchase_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`p_no` INT NOT NULL,
	`option_id` INT NOT NULL,
	`order_id` VARCHAR(255) NULL,
	`purchase_price` INT NOT NULL,
	`payment_method` VARCHAR(50) NOT NULL,
	`address` VARCHAR(255) NULL,
	`tracking_no` VARCHAR(255) NULL,
	`purchase_state` ENUM('pending', 'paid', 'ready_to_ship', 'shipping', 'delivered', 'cancelled') NOT NULL,
	`ordered_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`purchase_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (`option_id`) REFERENCES `Product_option`(`option_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='구매';

CREATE TABLE `Sales` (
	`s_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`p_no` INT NOT NULL,
	`sales_tracking_no` VARCHAR(50) NOT NULL,
	`sale_price` INT NOT NULL,
	`size` VARCHAR(50) NOT NULL,
	`address` VARCHAR(255) NOT NULL,
	`account` VARCHAR(255) NOT NULL,
	`sale_state` ENUM('pending', 'reception', 'checking', 'completed', 'cancelled') NOT NULL DEFAULT 'pending',
	`sale_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`s_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='판매';

CREATE TABLE `Post` (
	`post_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`content` TEXT NULL,
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`post_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='게시글';

CREATE TABLE `Comment` (
	`c_no` INT NOT NULL AUTO_INCREMENT,
	`post_no` INT NOT NULL,
	`user_id` VARCHAR(100) NOT NULL,
	`comment` TEXT NOT NULL,
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`c_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (`post_no`) REFERENCES `Post`(`post_no`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='댓글';

CREATE TABLE `Wishlist` (
	`w_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`parent_table` VARCHAR(100) NOT NULL,
	`parent_no` INT NOT NULL,
	`created_at` DATE NULL,
	PRIMARY KEY (`w_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='관심';

CREATE TABLE `Like` (
	`like_no` INT NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(100) NOT NULL,
	`post_no` INT NOT NULL,
	`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`like_no`),
	FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (`post_no`) REFERENCES `Post`(`post_no`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='좋아요';

CREATE TABLE `Tag` (
	`t_no` INT NOT NULL AUTO_INCREMENT,
	`p_no` INT NOT NULL,
	`post_no` INT NOT NULL,
	PRIMARY KEY (`t_no`),
    FOREIGN KEY (p_no) REFERENCES Product(p_no) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (post_no) REFERENCES Post(post_no) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='상품 태그';

CREATE TABLE `File` (
	`no` INT NOT NULL AUTO_INCREMENT COMMENT '파일번호',
	`parent_table` VARCHAR(45) NOT NULL COMMENT '유저프로필사진 => "user" 상품이미지 => 카테고리명 게시글 => "post"',
	`parent_no` INT NOT NULL COMMENT '유저프로필사진 => 유저번호 상품이미지 => 상품번호 게시글 => 게시글번호',
	`file_name` TEXT NOT NULL,
	`origin_name` TEXT NOT NULL,
	`file_path` TEXT NOT NULL,
	`file_size` INT NOT NULL DEFAULT 0,
	`reg_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`file_code` INT NOT NULL DEFAULT 0 COMMENT '일반 -> 0 대표이미지 -> 1',
	PRIMARY KEY (`no`)
) COMMENT='파일';


