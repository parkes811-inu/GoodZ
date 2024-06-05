-- Active: 1716800736662@@127.0.0.1@3306@goodz
-- Drop existing tables if they exist
DROP TABLE IF EXISTS `user`, `user_auth`, `persistent_logins`, `Social_Login`, `Following`, `Follower`, `Post`, `Comment`, `Like`, `Tag`, `Product`, `Product_image`, `Product_option`, `Brand`, `Pricehistory`, `Wishlist`, `Sales`, `Inspection`, `Purchase`, `Shipment`, `Shippingaddress`, `file`;

-- Brand 테이블 / 📁 product
CREATE TABLE `Brand`(
    `b_no` INT NOT NULL AUTO_INCREMENT,
    `b_name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`b_no`),
    UNIQUE KEY `unique_b_name` (`b_name`)
) COMMENT='브랜드';

-- User 테이블  / 📁 user
CREATE TABLE `user` (
    `user_id` VARCHAR(100) NOT NULL, -- 유저 아이디
    `username` VARCHAR(50) NOT NULL, -- 유저 이름
    `nickname` VARCHAR(100) NOT NULL, -- 유저 닉네임
    `password` VARCHAR(100) NOT NULL,
    `birth` VARCHAR(50) NOT NULL, -- 2024/01/01 형식으로 안넣으면 뒤진다.
    `phone_number` VARCHAR(20) NOT NULL, -- 010-1234-1234
    `profile_picture_url` VARCHAR(255),
    `account` VARCHAR(255),
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`)
) COMMENT='유저';




DROP TABLE IF EXISTS persistent_logins;
-- Persistent_Login 테이블 / 📁 user
create table persistent_logins (
	username varchar(64) not null
	, series varchar(64) primary key
	, token varchar(64) not null
	, last_used timestamp not null
);
-- CREATE TABLE `Persistent_Login` (
-- 	`persistent_no`		INT				NOT NULL AUTO_INCREMENT,
-- 	`user_id`			VARCHAR(100)	NOT NULL,
-- 	`token`				VARCHAR(255)	NOT NULL,
-- 	`expiration_date`	DATE			NOT NULL,
-- 	`created_at`		timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
-- 	`state`	ENUM('remember', 'auto', 'all')	NOT NULL,
--     PRIMARY KEY (persistent_no),
--     FOREIGN KEY (user_id) REFERENCES User(user_id)
-- ) COMMENT='자동로그인';


-- Social_Login 테이블 / 📁 user
CREATE TABLE `Social_Login` (
	`social_login_id`	VARCHAR(100)	NOT NULL,
	`user_id`			VARCHAR(100)	NOT NULL,
	`provider`			VARCHAR(50)		NOT NULL,
	`provider_user_id`	VARCHAR(100)	NOT NULL,
	`created_at` 		timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (social_login_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='소셜로그인';

-- Following 테이블 / 📁 user
CREATE TABLE `Following` (
	`following_no`	INT				NOT NULL AUTO_INCREMENT,
	`user_id`		VARCHAR(100)	NOT NULL,
	`following_id`	VARCHAR(100),
    PRIMARY KEY (following_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='팔로잉';


-- Follower 테이블 / 📁 user
CREATE TABLE `Follower` (
	`follower_no`	INT				NOT NULL AUTO_INCREMENT,
	`user_id`		VARCHAR(100)	NOT NULL,
	`follower_id`	VARCHAR(100),
    PRIMARY KEY (follower_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='팔로워';



-- Post 테이블 / 📁 post
CREATE TABLE `Post` (
	`post_no`	INT				NOT NULL AUTO_INCREMENT,
	`user_id`	VARCHAR(100)	NOT NULL,
	`content`	TEXT,
	-- `image_url`	VARCHAR(255)	NOT NULL,
	`created_at` timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='게시글';



-- Comment 테이블 / 📁 post
CREATE TABLE `Comment` (
	`c_no`			INT				NOT NULL AUTO_INCREMENT,
	`post_no`		INT				NOT NULL,
	`user_id`		VARCHAR(100)	NOT NULL,
	`comment`		TEXT,
	`created_at`	 timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`	 timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (c_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (post_no) REFERENCES Post(post_no)
) COMMENT='댓글';


-- Like 테이블 / 📁 post
CREATE TABLE `Like` (
	`like_no`	INT				NOT NULL AUTO_INCREMENT,
	`user_id`	VARCHAR(100)	NOT NULL,
	`post_no`	INT				NOT NULL,
	`created_at`	 timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- `updated_at`	 timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 6/5 like해제하면 삭제되므로 필요X
    PRIMARY KEY (like_no),
    -- FOREIGN KEY (c_no) REFERENCES Comment(c_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (post_no) REFERENCES Post(post_no)
);


-- Tag 테이블 / 📁 post
CREATE TABLE `Tag` (
	`t_no`		INT	NOT NULL AUTO_INCREMENT,
	`p_no`		INT	NOT NULL,
	`post_no`	INT	NOT NULL,
    PRIMARY KEY (t_no),
    FOREIGN KEY (p_no) REFERENCES Product(p_no),
    FOREIGN KEY (post_no) REFERENCES Post(post_no)
) COMMENT='상품 태그';




-- 2024.05.31 박은서 product 테이블 분할
-- Product 테이블 / 📁 product
-- CREATE TABLE `Product` (
-- 	`p_no`				INT				NOT NULL AUTO_INCREMENT,
-- 	`product_name`		VARCHAR(100)	NOT NULL,
-- 	`price`				INT				NOT NULL,
-- 	`b_name`			VARCHAR(100)	NOT NULL,
-- 	`category`			VARCHAR(50)		NOT NULL,
-- 	`size`				VARCHAR(100)	NOT NULL,
-- 	`views`				INT				NOT NULL DEFAULT '0',
-- 	`stock_quantity`	INT				NOT NULL,
-- 	`image_url`			VARCHAR(1000)	NOT NULL,
-- 	`created_at`	    timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     `updated_at`	 	timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
--      PRIMARY KEY (p_no),
-- 	 FOREIGN KEY (b_name) REFERENCES Brand(b_name)
-- ) COMMENT='상품';

-- Product 테이블 / 📁 product
CREATE TABLE `Product` (
    `p_no` INT NOT NULL AUTO_INCREMENT,
    `product_name` VARCHAR(100) NOT NULL,
    `initial_price` INT NOT NULL,  -- 기본 가격
    `b_name` VARCHAR(100) NOT NULL,
    `category` VARCHAR(50) NOT NULL,
    `views` INT NOT NULL DEFAULT '0',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`p_no`),
    FOREIGN KEY (`b_name`) REFERENCES `Brand`(`b_name`) ON DELETE CASCADE
) COMMENT='상품';

-- Product 이미지 테이블 / 📁 product -> 이미지 테이블 없애도 될듯 ?
-- CREATE TABLE `Product_image` (
--     `img_id` INT NOT NULL AUTO_INCREMENT,
--     `p_no` INT NOT NULL,
--     `image_url` VARCHAR(1000) NOT NULL,
--     PRIMARY KEY (`img_id`),
--     FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`) ON DELETE CASCADE
-- ) COMMENT='상품 이미지';
-- / 쓸모없을거같다해서 주석해놓음!!! -6/3 도희-


-- Product 옵션 테이블 / 📁 product
CREATE TABLE `Product_option` (
    `option_id` INT NOT NULL AUTO_INCREMENT,
    `p_no` INT NOT NULL,
    `size` VARCHAR(50) NOT NULL,
    `option_price` INT NOT NULL,  -- 사이즈별 추가 금액
    `stock_quantity` INT NOT NULL,
    `status` ENUM('on', 'off') NOT NULL,
    PRIMARY KEY (`option_id`),
    FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`) ON DELETE CASCADE
) COMMENT='상품 옵션';

-- PriceHistory 테이블 / 📁 product
CREATE TABLE `Pricehistory` (
    `price_history_no` INT NOT NULL AUTO_INCREMENT,
    `p_no` INT NOT NULL,
    `size` VARCHAR(50) NOT NULL,
    `fluctuated_price` INT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`price_history_no`),
    FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`)
) COMMENT='가격변동';

-- Wishlist 테이블 / 📁 user
CREATE TABLE `Wishlist` (
    `w_no` INT NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(100) NOT NULL,
    `parent_no` INT NOT NULL,
    `parent_table` VARCHAR(100) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 6/4 필요 없어서 주석처리함 -도희-
    PRIMARY KEY (`w_no`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`),
) COMMENT='관심 목록';

-- Sales 테이블 / 📁 pay
CREATE TABLE `Sales` (
    `s_no` INT NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(100) NOT NULL,
    `p_no` INT NOT NULL,
    `sale_tracking_no` VARCHAR(50) NOT NULL,
    `sale_price` INT NOT NULL,
    `size` VARCHAR(50) NOT NULL,
    `sale_state` ENUM('pending', 'checking' 'completed', 'cancelled') NOT NULL,
    `sale_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`s_no`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`),
    FOREIGN KEY (`p_no`) REFERENCES `Product`(`p_no`)
) COMMENT='판매';

-- Inspection 테이블 / 📁 pay
CREATE TABLE `Inspection` (
    `i_no` INT NOT NULL AUTO_INCREMENT,
    `s_no` INT NOT NULL,
    `ins_state` ENUM('pending', 'verified', 'rejected') NOT NULL,
    `ins_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`i_no`),
    FOREIGN KEY (`s_no`) REFERENCES `Sales`(`s_no`)
) COMMENT='검수';

-- Purchase 테이블 / 📁 pay
CREATE TABLE `Purchase` (
    `purchase_no`       INT             NOT NULL AUTO_INCREMENT,
    `user_id`           VARCHAR(100)    NOT NULL,
    `p_no`              INT             NOT NULL,
    `purchase_price`    INT             NOT NULL,
    `payment_method`    VARCHAR(50)     NOT NULL,
    `purchase_state`    ENUM('pending', 'paid', 'shipping', 'delivered', 'cancelled') NOT NULL DEFAULT 'pending',
    -- 미결제, 결제된, 배송중, 배송완료, 취소(환불)
    `ordered_at`        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (purchase_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (p_no) REFERENCES Product(p_no)
) COMMENT='구매';

-- purchase_date 컬럼 삭제
ALTER TABLE Purchase DROP COLUMN purchase_date;

-- ordered_at, updated_at 컬럼 추가
ALTER TABLE Purchase
    ADD COLUMN order_id varchar(100),
    ADD COLUMN ordered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- purchase_state 컬럼 변경
ALTER TABLE Purchase MODIFY COLUMN purchase_state ENUM('pending', 'paid', 'shipping', 'delivered', 'cancelled') NOT NULL DEFAULT 'pending';


-- Shipment 테이블 / 📁 pay
CREATE TABLE `Shipment` (
    `shipment_no` INT NOT NULL AUTO_INCREMENT,
    `purchase_no` INT NOT NULL,
    `user_id` VARCHAR(100) NOT NULL,
    `tracking_no` VARCHAR(50) NOT NULL,
    `shipment_state` ENUM('pending', 'shipped', 'in_transit', 'delivered', 'returned') NOT NULL,
    `shipped_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `delivered_date` DATE,
    PRIMARY KEY (`shipment_no`),
    FOREIGN KEY (`purchase_no`) REFERENCES `Purchase`(`purchase_no`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) COMMENT='배송';


-- Shippingaddress 테이블 / 📁 user
CREATE TABLE `Shippingaddress` (
    `address_no` INT NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(100) NOT NULL,
    `recipient_name` VARCHAR(100) NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `zip_code` VARCHAR(20) NOT NULL,
    `phone_number` VARCHAR(20) NOT NULL,
    `is_default` BOOLEAN NOT NULL DEFAULT FALSE, 
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`address_no`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) COMMENT='배송주소';

-- file 테이블 / 📁 utils
CREATE TABLE `file` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `parent_table` VARCHAR(45) NOT NULL,
  `parent_no` INT NOT NULL,
  `file_name` TEXT NOT NULL,
  `origin_name` TEXT,
  `file_path` TEXT NOT NULL,
  `file_size` INT NOT NULL DEFAULT '0',
  `reg_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `upd_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `file_code` INT NOT NULL DEFAULT '0', -- 파일종류 코드 => 1:썸네일, 2: 일반첨부파일...
  PRIMARY KEY (`no`)
) COMMENT='파일';

DROP TABLE IF EXISTS user_auth;
-- user_auth 테이블 / 📁 user
CREATE TABLE `user_auth` (
    `auth_no` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` VARCHAR(100) NOT NULL, -- 회원 아이디
    `AUTH` VARCHAR(100) NOT NULL, -- 권한 (ROLE_USER, ROLE_ADMIN, ...)
    FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) COMMENT='사용자 권한';

-- persistent_logins 테이블 / 📁 user
CREATE TABLE `persistent_logins` (
    `username` VARCHAR(64) NOT NULL,
    `series` VARCHAR(64) PRIMARY KEY,
    `token` VARCHAR(64) NOT NULL,
    `last_used` TIMESTAMP NOT NULL
) COMMENT='자동 로그인';
