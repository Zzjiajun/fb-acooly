INSERT INTO `sys_resource` (`id`, `parentid`, `name`, `type`, `show_state`, `order_time`, `value`, `show_mode`, `icon`, `descn`, `create_time`, `update_time`)
VALUES
(201808211426, NULL, '开发实例V5', 'MENU', 0, '2020-09-20 23:13:42', 'XXX', 1, 'fa-circle-o', NULL, '2021-10-09 16:51:19', '2021-10-09 16:51:25'),
(201808211427, 201808211426, '会员管理', 'URL', 0, '2021-10-09 16:53:13', '/manage/showcase/member/showcaseMember/index.html', 1, 'fa-circle-o', NULL, '2021-10-09 16:53:13', '2021-10-19 14:25:02'),
(201808211428, 201808211426, '会员配置', 'URL', 0, '2021-10-09 16:53:12', '/manage/showcase/member/showcaseMemberProfile/index.html', 1, 'fa-circle-o', NULL, '2021-10-26 22:19:30', '2021-11-09 17:53:04'),
(201808211429, 201808211427, '会员联系信息', 'URL', 1, '2021-10-26 22:20:02', '/manage/showcase/member/showcaseMemberContact/index.html', 1, 'fa-circle-o', NULL, '2021-10-26 22:20:02', '2021-11-09 17:53:16'),
(201808211430, 201808211427, '会员附件', 'URL', 1, '2021-10-26 22:20:35', '/manage/showcase/member/showcaseMemberFile/index.html', 1, 'fa-circle-o', NULL, '2021-10-26 22:20:35', '2021-11-07 21:23:58'),
(201808211431, 201808211427, '动态Tabs', 'URL', 1, '2021-11-07 22:16:13', '/manage/showcase/demo/dynamicTab/index.html', 1, 'fa-circle-o', NULL, '2021-11-07 22:16:13', '2021-11-09 17:52:59'),
(201808211432, 201808211426, '行编辑', 'URL', 0, '2021-10-09 16:53:11', '/manage/demo/roweditor/roweditor/index.html', 1, 'fa-circle-o', NULL, '2021-11-14 16:17:49', '2021-11-14 16:18:05'),
(201808211433, 201808211426, '批量管理', 'URL', 0, '2021-10-09 16:53:10', '/manage/demo/usergroup/userGroup/index.html', 1, 'fa-circle-o', NULL, '2021-12-19 17:12:01', '2021-12-19 21:34:33');

INSERT INTO `sys_role_resc` (`role_id`, `resc_id`)
VALUES
(1, 201808211426),
(1, 201808211427),
(1, 201808211428),
(1, 201808211429),
(1, 201808211430),
(1, 201808211431),
(1, 201808211432),
(1, 201808211433);
