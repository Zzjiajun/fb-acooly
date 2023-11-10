INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`, `create_time`, `update_time`)
VALUES
	(201510276, NULL, '开发实例', 'MENU', 0, '2017-09-18 02:51:03', '', 1, 'fa fa-coffee', NULL, '2018-09-13 02:30:58', '2017-09-18 03:17:14'),
  (2016093015, 201510276, '客户管理[父子]', 'URL', 0, '2019-03-13 03:02:10', '/manage/showcase/customer/index.html', 1, 'fa fa-users', NULL, '2017-09-10 01:21:28', '2017-09-14 11:23:58'),
	(201510277, 2016093015, '客户基本信息', 'URL', 1, '2017-09-10 01:21:27', '/manage/showcase/customerBasic/index.html', 1, 'fa fa-circle-o', NULL, '2018-09-13 02:30:58', '2017-09-12 02:17:01'),
	(201510278, 2016093015, '客户扩展信息', 'URL', 1, '2016-10-12 17:23:49', '/manage/showcase/customerExtend/index.html', 1, 'fa fa-circle-o',NULL, '2018-09-13 02:30:58', '2017-09-12 02:17:04'),
	(2016093016, 2016093015, '添加客户', 'FUNCTION', 1, '2017-09-12 02:17:44', 'customer:create', 1, 'fa fa-circle-o', NULL, '2017-09-12 02:17:44', '2017-09-12 15:50:21'),
	(2016093017, 2016093015, '客户导出', 'FUNCTION', 1, '2017-09-12 02:18:22', 'customer:export', 1, 'fa fa-circle-o', NULL, '2017-09-12 02:18:22', '2017-09-12 15:53:59'),
	(2016093018, 201510276, '商品管理[树型]', 'URL', 0, '2019-04-13 11:09:22', '/manage/showcase/goods/index.html', 1, 'fa fa-shopping-bag', NULL, '2017-09-13 01:09:22', '2017-09-14 11:23:43'),
	(2016093019, 2016093018, '商品分类', 'URL', 1, '2017-09-13 01:10:03', '/manage/showcase/goodsCategory/index.html', 1, 'fa fa-circle-o', NULL, '2017-09-13 01:10:03', '2017-09-13 01:10:03');

INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`)
VALUES
	(1, 201510276),
	(1, 2016093015),
	(1, 201510277),
	(1, 201510278),
	(1, 2016093016),
	(1, 2016093017),
	(1, 2016093018),
	(1, 2016093019);
