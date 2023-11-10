CREATE TABLE `ac_showcase_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_no` varchar(32) NOT NULL COMMENT '会员编码',
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_no_UNIQUE` (`member_no`),
  UNIQUE KEY `uk_member_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='会员信息';

CREATE TABLE `ac_showcase_member_contact` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `member_no` varchar(64) NOT NULL COMMENT '用户编码',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `mobile_no` varchar(16) DEFAULT NULL COMMENT '{title:’手机号码’, type:’mobile’}',
  `phone_no` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(128) DEFAULT NULL COMMENT '{title:’邮箱’, type:’email’}',
  `qq` varchar(16) DEFAULT NULL COMMENT 'QQ账号',
  `wangwang` varchar(32) DEFAULT NULL COMMENT '旺旺账号',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信账号',
  `facebeek` varchar(32) DEFAULT NULL COMMENT '脸书账号',
  `google` varchar(32) DEFAULT NULL COMMENT '谷歌账号',
  `province` varchar(32) DEFAULT NULL COMMENT '省',
  `city` varchar(32) DEFAULT NULL COMMENT '市',
  `district` varchar(32) DEFAULT NULL COMMENT '区',
  `zip` varchar(16) DEFAULT NULL COMMENT '邮政编码',
  `address` varchar(128) DEFAULT NULL COMMENT '地址',
  `company_name` varchar(32) DEFAULT NULL COMMENT '公司名',
  `career` varchar(16) DEFAULT NULL COMMENT '职业',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员联系信息';

CREATE TABLE `ac_showcase_member_extend` (
                                             `id` bigint(20) NOT NULL,
                                             `context` text,
                                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                             `comments` varchar(255) DEFAULT NULL,
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ac_showcase_member_file` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
   `object_id` varchar(64) NOT NULL COMMENT '文件ID',
   `member_no` varchar(64) NOT NULL COMMENT '用户编码',
   `username` varchar(32) NOT NULL COMMENT '用户名',
   `file_title` varchar(45) DEFAULT NULL COMMENT '文件标题',
   `file_name` varchar(45) NOT NULL COMMENT '文件名',
   `file_path` varchar(128) NOT NULL COMMENT '{title:’文件路径’,type:’file’}',
   `file_ext` varchar(16) DEFAULT NULL COMMENT '文件扩展名',
   `file_type` varchar(16) NOT NULL COMMENT '文件类型{doc:文档,pic:图片,zip:归档,other:其他}',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `comments` varchar(128) DEFAULT NULL COMMENT '备注',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='会员附件';

CREATE TABLE `ac_showcase_member_profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_no` varchar(64) NOT NULL COMMENT '会员编码',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `daily_words` varchar(64) DEFAULT NULL COMMENT '个性签名',
  `profile_photo` varchar(128) DEFAULT NULL COMMENT '{title:’头像’, type:’file’}',
  `email_status` varchar(16) DEFAULT NULL COMMENT '邮箱认证 {yes:已认证,no:未认证}',
  `mobile_no_status` varchar(16) DEFAULT NULL COMMENT '手机认证 {yes:已认证,no:未认证}',
  `real_name_status` varchar(16) DEFAULT NULL COMMENT '实名认证 {yes:已认证,no:未认证}',
  `secret_qa_status` varchar(16) DEFAULT NULL COMMENT '安全问题设置状态',
  `sms_send_status` varchar(16) DEFAULT NULL COMMENT '发送短信 {yes:发送,no:不发送}',
  `manager` varchar(16) DEFAULT NULL COMMENT '客户经理',
  `broker` varchar(16) DEFAULT NULL COMMENT '经纪人',
  `inviter` varchar(16) DEFAULT NULL COMMENT '邀请人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='会员配置信息';


CREATE TABLE `ac_showcase_roweditor` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
     `member_no` varchar(32) NOT NULL COMMENT '会员编码',
     `username` varchar(32) NOT NULL COMMENT '{title:’用户名’,type:’account’}',
     `real_name` varchar(16) NOT NULL COMMENT '{title:’姓名’,type:’chinese’}',
     `idcard_no` varchar(48) NOT NULL COMMENT '{title:’身份证号码’,type:’idcard’}',
     `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
     `birthday` date NOT NULL COMMENT '生日',
     `gender` varchar(16) NOT NULL COMMENT '{title:''性别’,alias: ‘gender’}',
     `animal` varchar(16) DEFAULT NULL COMMENT '{title:’生肖’, alias: ‘animal’}',
     `mobile_no` varchar(11) DEFAULT NULL COMMENT '{title:’手机号码’,type:’mobile’}',
     `mail` varchar(64) DEFAULT NULL COMMENT '{title:’邮件’,type:’email’}',
     `website` varchar(128) DEFAULT NULL COMMENT '{title:’网址’,type:’url’}',
     `done_ratio` int(11) DEFAULT NULL COMMENT '{title:’完成度‘,type:’percent’}',
     `salary` int(11) DEFAULT NULL COMMENT '{title:’薪水’,type:’money’}',
     `registry_channel` varchar(16) DEFAULT NULL COMMENT '{title:’注册渠道’, alias: ‘channel’}',
     `status` varchar(16) NOT NULL DEFAULT '1' COMMENT '{title:’状态’, alias:’simple’}',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_time` datetime NOT NULL COMMENT '更新时间',
     `comments` varchar(255) DEFAULT NULL COMMENT '备注',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_roweditor_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='行编辑实例';


CREATE TABLE `ac_showcase_user_group` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(45) NOT NULL COMMENT '名称',
    `descn` varchar(225) DEFAULT NULL COMMENT '描述',
    `users` varchar(512) DEFAULT NULL COMMENT '用户名',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `comments` varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='用户动态分组';
