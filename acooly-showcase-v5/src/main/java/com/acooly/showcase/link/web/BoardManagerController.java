/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-19
*/
package com.acooly.showcase.link.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.dto.UserRole;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.stemp.entity.EmFileds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.link.entity.Board;
import com.acooly.showcase.link.service.BoardService;

import com.google.common.collect.Maps;

/**
 * dm_board 管理控制器
 *
 * @author acooly
 * @date 2025-02-19 18:45:30
 */
@Controller
@RequestMapping(value = "/manage/link/board")
public class BoardManagerController extends AbstractJsonEntityController<Board, BoardService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private BoardService boardService;
	@Autowired
	private UserService userService;


	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<Board> boardList = this.getEntityService().getAll();
		List<String> stringList = boardList.stream().map(Board::getManageName).collect(Collectors.toList());
		List<User> userList = userService.getAll();
		List<User> usernameList = userList.stream()
				.filter(user -> user.getUserType()==1) // 过滤掉 userType 不是 2 的用户
				.filter(username -> !stringList.contains(username)) // 过滤掉在 stringList 中的用户名
				.collect(Collectors.toList());
		ArrayList<Long> userIds = new ArrayList<>();
		usernameList.forEach(user -> {
			List<UserRole> userRoles = userService.getRoleIdsByUserId(user.getId());
			userRoles.forEach(userRole -> {
				if (userRole.getRoleId()!=3){
					userIds.add(userRole.getUserId());
				}
			});
		});
		//取出usernameList中的userIds用户名
		List<String> list = usernameList.stream().filter(user -> userIds.contains(user.getId()))
				.map(User::getUsername)
				.filter(username -> !stringList.contains(username)) // 过滤掉在 stringList 中的用户名
				.collect(Collectors.toList());
		List<String> usernameList1= userList.stream()
				.filter(user -> user.getUserType()==2) // 过滤掉 userType 不是 2 的用户
				.map(User::getUsername)
				.filter(username -> !stringList.contains(username)) // 过滤掉在 stringList 中的用户名
				.collect(Collectors.toList());
		model.put("usernameList", list);
		model.put("usernameList1", usernameList1);
	}

	@Override
	public JsonListResult<Board> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<Board> result = new JsonListResult();
		this.allow(request, response, MappingMethod.list);

		try {
			result.appendData(this.referenceData(request));
			PageInfo<Board> pageInfo = this.doList(request, response);
			result.setTotal(pageInfo.getTotalCount());
			List<Board> pageResults = pageInfo.getPageResults();
//			List<User> userList = userService.getAll();
//			Map<Long, String> nameByIdMap = userList.stream().collect(Collectors.toMap(User:: getId, User:: getUsername));
//			pageResults.forEach(board -> {
//				StringBuilder attachedNames = new StringBuilder();
//				String attachedName = board.getAttachedName();
//				List<String> gatherList = attachedName != null ? Arrays.asList(attachedName.split(",")) : new ArrayList<>();
//				gatherList.forEach(attached -> {
//					try {
//						Long userId = Long.parseLong(attached);
//						String name = nameByIdMap.get(userId);
//						if (name != null) {
//							if (attachedNames.length() > 0) {
//								attachedNames.append(", ");
//							}
//							attachedNames.append(name);
//						} else {
//							System.out.println("User ID " + userId + " not found in nameByIdMap");
//						}
//					} catch (NumberFormatException e) {
//						System.out.println("Attached name contains non-numeric value: " + attached);
//					}
//				});
//				board.setAttachedName(attachedNames.toString());
//			});
			result.setRows(pageResults);
			result.setHasNext(pageInfo.hasNext());
			result.setPageNo(pageInfo.getCurrentPage());
			result.setPageSize(pageInfo.getCountOfCurrentPage());
		} catch (Exception var5) {
			this.handleException(result, "分页查询", var5);
		}

		return result;
	}
}
