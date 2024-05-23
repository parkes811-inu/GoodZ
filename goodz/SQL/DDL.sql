-- User 테이블
CREATE TABLE `User` (
	`user_id`				VARCHAR(100)	NOT NULL,
	`username`				VARCHAR(50)		NOT NULL,
	`password`				VARCHAR(100)	NOT NULL,
	`birth`					VARCHAR(50)		NOT NULL,
	`phone_number`			VARCHAR(20)		NOT NULL,
	`role`					ENUM('admin', 'user')	NOT NULL,
	`profile_picture_url`	VARCHAR(255),
	`account`				VARCHAR(255),
	`created_at`			timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` 			timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) COMMENT='유저';



-- Persistent_Loing 테이블
CREATE TABLE `Persistent_Login` (
	`persistent_no`		INT				NOT NULL AUTO_INCREMENT,
	`user_id`			VARCHAR(100)	NOT NULL,
	`token`				VARCHAR(255)	NOT NULL,
	`expiration_date`	DATE			NOT NULL,
	`created_at`		timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`state`	ENUM('remember', 'auto', 'all')	NOT NULL,
    PRIMARY KEY (persistent_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='자동로그인';


-- Social_Login 테이블
CREATE TABLE `Social_Login` (
	`social_login_id`	VARCHAR(100)	NOT NULL,
	`user_id`			VARCHAR(100)	NOT NULL,
	`provider`			VARCHAR(50)		NOT NULL,
	`provider_user_id`	VARCHAR(100)	NOT NULL,
	`created_at` 		timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (social_login_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='소셜로그인';

-- Following 테이블
CREATE TABLE `Following` (
	`following_no`	INT				NOT NULL AUTO_INCREMENT,
	`user_id`		VARCHAR(100)	NOT NULL,
	`following_id`	VARCHAR(100),
    PRIMARY KEY (following_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='팔로잉';


-- Follower 테이블
CREATE TABLE `Follower` (
	`follower_no`	INT				NOT NULL AUTO_INCREMENT,
	`user_id`		VARCHAR(100)	NOT NULL,
	`follower_id`	VARCHAR(100),
    PRIMARY KEY (follower_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='팔로워';



-- Post 테이블
CREATE TABLE `Post` (
	`post_no`	INT				NOT NULL AUTO_INCREMENT,
	`user_id`	VARCHAR(100)	NOT NULL,
	`title`		VARCHAR(100) 	NOT NULL,
	`content`	TEXT,
	`image_url`	VARCHAR(255)	NOT NULL,
	`created_at` timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='게시글';



-- Comment 테이블
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


-- Like 테이블
CREATE TABLE `Like` (
	`like_no`	INT				NOT NULL AUTO_INCREMENT,
	`c_no`		INT				NOT NULL,
	`user_id`	VARCHAR(100)	NOT NULL,
	`post_no`	INT				NOT NULL,
	`created_at`	 timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`	 timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (like_no),
    FOREIGN KEY (c_no) REFERENCES Comment(c_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (post_no) REFERENCES Post(post_no)
);


-- Tag 테이블
CREATE TABLE `Tag` (
	`t_no`		INT	NOT NULL AUTO_INCREMENT,
	`p_no`		INT	NOT NULL,
	`post_no`	INT	NOT NULL,
    PRIMARY KEY (t_no),
    FOREIGN KEY (p_no) REFERENCES Product(p_no),
    FOREIGN KEY (post_no) REFERENCES Post(post_no)
) COMMENT='상품 태그';


-- Product 테이블
CREATE TABLE `Product` (
	`p_no`				INT				NOT NULL AUTO_INCREMENT,
	`product_name`		VARCHAR(100)	NOT NULL,
	`description`		TEXT,
	`price`				INT				NOT NULL,
	`brand`				VARCHAR(50)		NOT NULL,
	`category`			VARCHAR(50)		NOT NULL,
	`size`				VARCHAR(100)	NOT NULL,
	`views`				INT				NOT NULL DEFAULT '0',
	`stock_quantity`	INT				NOT NULL,
	`image_url`			VARCHAR(255)	NOT NULL,
	`created_at`	    timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`	 	timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
     PRIMARY KEY (p_no)
) COMMENT='상품';




-- Pricehistory 테이블
CREATE TABLE `Pricehistory` (
	`price_history_no`	INT				NOT NULL AUTO_INCREMENT,
	`p_no`				INT				NOT NULL,
	`fluctuated_price`	INT				NOT NULL,
	`created_at`	    timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`	 	timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (price_history_no),
    FOREIGN KEY (p_no) REFERENCES Product(p_no)
) COMMENT='가격변동';




-- Wishlist 테이블
CREATE TABLE `Wishlist` (
	`w_no`		INT				NOT NULL AUTO_INCREMENT,
	`user_id`	VARCHAR(100)	NOT NULL,
	`p_no`		INT				NOT NULL,
	`created_at` timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (w_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (p_no) REFERENCES Product(p_no)
) COMMENT='관심';


-- Sales 테이블
CREATE TABLE `Sales` (
	`s_no`			INT				NOT NULL AUTO_INCREMENT,
	`user_id`		VARCHAR(100)	NOT NULL,
	`p_no`			INT				NOT NULL,
    `sale_tracking_no` VARCHAR(50)	NOT NULL,
	`sale_price`	INT				NOT NULL,
	`sale_state`	ENUM('pending', 'completed', 'cancelled')	NOT NULL,
	`sale_date`		timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (s_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (p_no) REFERENCES Product(p_no)
) COMMENT='판매';


-- Inspection 테이블
CREATE TABLE `Inspection` (
	`i_no`		INT	NOT NULL AUTO_INCREMENT,
	`s_no`		INT	NOT NULL,
	`ins_state`	ENUM('pending', 'verified', 'rejected')	NOT NULL,
	`ins_date`	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (i_no),
    FOREIGN KEY (s_no) REFERENCES Sales(s_no)
)COMMENT='검수';



-- Pusrchase 테이블
CREATE TABLE `Purchase` (
	`purchase_no`		INT				NOT NULL AUTO_INCREMENT,
	`user_id`			VARCHAR(100)	NOT NULL,
	`p_no`				INT				NOT NULL,
	`purcahse_pirce`	INT				NOT NULL,
	`payment_method`	VARCHAR(50)		NOT NULL,
	`purchase_state`	ENUM('pending', 'shipped', 'delivered', 'cancelled')	NOT NULL,
	`purchase_date`		timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (purchase_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (p_no) REFERENCES Product(p_no)
)COMMENT='구매';




-- Shipment 테이블
CREATE TABLE `Shipment` (
	`shipment_no`	INT				NOT NULL AUTO_INCREMENT,
	`purchase_no`	INT	 			NOT NULL,
	`user_id`		VARCHAR(100)	NOT NULL,
	`p_no`			INT				NOT NULL,
	`tracking_no`	VARCHAR(50)		NOT NULL,
	`shipment_state`	ENUM('pending', 'shipped', 'in_transit', 'delivered', 'returned')	NOT NULL,
	`shipped_date`		timestamp	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`delivered_date`	DATE,
    PRIMARY KEY (shipment_no),
    FOREIGN KEY (purchase_no) REFERENCES Purchase(purchase_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (p_no) REFERENCES Product(p_no)
) COMMENT='배송';


-- Shippingaddress
CREATE TABLE `Shippingaddress` (
	`address_no`		INT				NOT NULL AUTO_INCREMENT,
	`user_id`			VARCHAR(100)	NOT NULL,
	`recipient_name`	VARCHAR(100)	NOT NULL,
	`address`			VARCHAR(255)	NOT NULL,
	`zip_code`			VARCHAR(20)		NOT NULL,
	`phone_number`		VARCHAR(20)		NOT NULL,
	`is_default`		BOOLEAN			NOT NULL DEFAULT TRUE,	
	`type`				ENUM('return','buy')	NOT NULL,
	`created_at`	  	timestamp 		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`	 	timestamp		NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (address_no),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
) COMMENT='배송주소';

