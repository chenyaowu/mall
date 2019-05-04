CREATE TABLE `mall_user`(
	`id` BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '用户id 手机号码',
	`nickname` VARCHAR(255) NOT NULL,
	`password` VARCHAR(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt) + salt)',
	`salt` VARCHAR(10) DEFAULT NULL,
	`head` VARCHAR(128) DEFAULT NULL COMMENT '头像，云存储的id',
	`register_date` datetime DEFAULT NULL COMMENT '注册时间',
	`last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
	`login_count` INT(11) DEFAULT '0' COMMENT '登录次数',
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `goods` (
`id` BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
`goods_name` VARCHAR(16) NOT NULL COMMENT '商品名称',
`goods_title` VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
`goods_img` VARCHAR(64) DEFAULT NULL COMMENT '商品图片',
`goods_detail` longtext  COMMENT '商品的详细介绍',
`goods_price` DECIMAL(10, 2) DEFAULT '0.00' COMMENT '商品的单价',
`goods_stock` int(11) DEFAULT '0' COMMENT '商品库存 -1表示没有限制',
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET = utf8mb4;

INSERT INTO `goods` VALUES
(1, 'iphoneX', 'Apple iPhone X(A1865) 64GB 银色 移动联通电信4G手机', '/img/iphonex.png', 'Apple iPhone X(A1865) 64GB 银色 移动联通电信4G手机', 8765.00, 10000),
(2, '华为Meta9', '华为Meta10 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', '/img/meta10.png', '华为Meta10 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', 3212.00, -1);

CREATE TABLE `mall_goods` (
`id` BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品表',
`goods_id` BIGINT(11) NOT NULL COMMENT '商品id',
`mall_price` DECIMAL(10, 2) DEFAULT '0.00' COMMENT '秒杀价格',
`stock_count` int(11) DEFAULT '0' COMMENT '商品库存',
`start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
`end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET = utf8mb4;

INSERT INTO `mall_goods` VALUES
(1, 1, 0.01, 10 , '2019-05-02 15:00:00', '2019-05-03 00:00:00'),
(2, 2, 0.01, 10 , '2019-05-02 15:00:00', '2019-05-03 00:00:00');


CREATE TABLE `order_info`(
`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
`user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
`goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品id',
`delivery_addr_id`  BIGINT(20) DEFAULT NULL COMMENT '收货地址id',
`goods_name` VARCHAR(16) DEFAULT NULL COMMENT '冗余的商品名称',
`goods_count` int(11) DEFAULT '0' COMMENT '商品数量',
`goods_price` DECIMAL(10, 2) DEFAULT '0.00' COMMENT '商品单价',
`order_channel` tinyint(4) DEFAULT '0' COMMENT '1-pc 2-android 3-ios',
`status` tinyint(4) DEFAULT '0' COMMENT '订单状态 0-新建未支付 1-已支付 2-已发货 3-已收货 4-已退款 5-已完成',
`create_date` datetime DEFAULT NULL COMMENT '订单创建时间',
`pay_date` datetime DEFAULT NULL COMMENT '支付时间',
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET = utf8mb4;


CREATE TABLE `mall_order`(
`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
`user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
`order_id` BIGINT(20) DEFAULT NULL COMMENT '订单id',
`goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品id',
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET = utf8mb4;

ALTER TABLE `mall_order`
ADD UNIQUE INDEX `u_uid` (`user_id`, `goods_id`) USING BTREE ;

