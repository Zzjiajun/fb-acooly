CREATE TABLE `acooly_coder_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(32) NOT NULL COMMENT '{title:’用户名’,type:’account’}',
  `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
  `birthday` date NOT NULL COMMENT '生日',
  `gender` varchar(16) NOT NULL COMMENT '{title:''性别’,alias: ‘gender’}',
  `animal` varchar(16) DEFAULT NULL COMMENT '{title:’生肖’, alias: ‘animal’}',
  `real_name` varchar(16) NOT NULL COMMENT '{title:’姓名’,type:’chinese’}',
  `idcard_type` varchar(18) NOT NULL COMMENT '{title:’证件类型’, type:’option’,options:{cert:’身份证‘,pass:’护照‘,other:’其他‘}}',
  `idcard_no` varchar(48) NOT NULL COMMENT '{title:’身份证号码’,type:’idcard’}',
  `mobile_no` varchar(11) DEFAULT NULL COMMENT '{title:’手机号码’,type:’mobile’}',
  `mail` varchar(64) DEFAULT NULL COMMENT '{title:’邮件’,type:’email’}',
  `customer_type` varchar(16) DEFAULT NULL COMMENT '{title:’客户类型’, type:’option’,options:{normal:’普通‘,vip:’重要‘,sepc:’特别‘}}',
  `subject` varchar(128) DEFAULT NULL COMMENT '摘要',
  `content` text COMMENT '详情',
  `done_ratio` int(11) DEFAULT NULL COMMENT '{title:’完成度‘,type:’percent’}',
  `salary` int(11) DEFAULT NULL COMMENT '{title:’薪水’,type:’money’}',
  `registry_channel` varchar(16) DEFAULT NULL COMMENT '{title:’注册渠道’, alias: ‘channel’}',
  `push_adv` varchar(16) DEFAULT NULL COMMENT '{title:’推送广告’, alias:’whether’}',
  `num_status` tinyint(4) DEFAULT NULL COMMENT '数字类型{1:A,2:B,3:C类型}',
  `status` varchar(16) NOT NULL DEFAULT '1' COMMENT '{title:’状态’, alias:’simple’}',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `website` varchar(128) DEFAULT NULL COMMENT '{title:’网址’,type:’url’}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='acoolycoder测试';


CREATE TABLE `dm_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `age` int NOT NULL COMMENT '年龄',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `customer_type` varchar(16) DEFAULT NULL COMMENT '客户类型 {normal:普通,vip:重要,sepc:特别}',
  `fee` double DEFAULT NULL COMMENT '手续费',
  `gender` int NOT NULL COMMENT '性别 {1:男,2:女,3:人妖}',
  `idcard_no` varchar(32) DEFAULT NULL COMMENT '证件号码',
  `idcard_type` varchar(16) DEFAULT NULL COMMENT '证件类型 {cert:身份证,passport:驾驶证}',
  `mail` varchar(128) DEFAULT NULL COMMENT '邮件',
  `mobile_no` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `real_name` varchar(16) DEFAULT NULL COMMENT '姓名',
  `salary` bigint(20) DEFAULT NULL COMMENT '薪水',
  `subject` varchar(255) DEFAULT NULL COMMENT '简介',
  `status` int NOT NULL  COMMENT '状态 {0:无效,1:有效}',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息';

CREATE TABLE `dm_customer_basic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `car_status` varchar(255) DEFAULT NULL,
  `children_count` int(11) NOT NULL,
  `education_level` varchar(255) DEFAULT NULL,
  `house_fund` varchar(255) DEFAULT NULL,
  `house_statue` varchar(255) DEFAULT NULL,
  `income_month` varchar(255) DEFAULT NULL,
  `marital_status` varchar(255) DEFAULT NULL,
  `social_insurance` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `dm_customer_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `context` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `dm_goods_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `sort_time` bigint(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `dm_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) DEFAULT NULL,
  `category_name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `supplier` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `descn` varchar(255) DEFAULT NULL,
  `detail_url` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='市级信息';
