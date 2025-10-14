/*
* acooly.cn Inc.
* Copyright (c) 2023 All Rights Reserved.
* create by acooly
* date:2023-12-14
*/
package com.acooly.showcase.daliy.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.daliy.Utils.RemoteFileOperationsUtil;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.entity.DmRegion;
import com.acooly.showcase.daliy.entity.Regname;
import com.acooly.showcase.daliy.service.*;
import com.acooly.showcase.link.entity.Board;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.link.entity.DmServer;
import com.acooly.showcase.link.service.BoardService;
import com.acooly.showcase.link.service.DmConditionService;
import com.acooly.showcase.link.service.DmCountryService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.daliy.entity.Link;
import com.google.common.collect.Maps;

/**
 * dm_link 管理控制器
 *
 * @author acooly
 * @date 2023-12-14 17:10:52
 */
@Controller
@RequestMapping(value = "/manage/showcase/daily/link")
public class LinkManagerController extends AbstractJsonEntityController<Link, LinkService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private LinkService linkService;
	@Autowired
	private UserService userService;
	@Autowired
	private RegnameService regnameService;
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private RedisUtils redisUtil;
	@Autowired
	private DmConditionService dmConditionService;
	@Autowired
	private DmCountryService dmCountryService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private DmRegionService dmRegionService;
	@Autowired
	private DmCenterService dmCenterService;


	@Override
	protected PageInfo<Link> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_holder", principal.getUsername());
		}
		Map<String, Object> map1Query = Maps.newHashMap();
		map1Query.put("EQ_manageName", principal.getUsername());
		List<Board> boardList = boardService.query(map1Query, null);
		if (boardList.size() > 0){
			String attachedName = boardList.get(0).getAttachedName();
			List<String> gatherList = attachedName != null ? Arrays.asList(attachedName.split(",")) : new ArrayList<>();
			// 删除键为 "EQ_userName" 的条目
			searchParams.remove("EQ_holder");
			searchParams.put("IN_holder", gatherList);
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> mapQuery = Maps.newHashMap();
		List<String> listRegion = dmRegionService.getAll().stream()
				.map(DmRegion::getRegion)
				.collect(Collectors.toList());
		List<Regname> regnameList;
		mapQuery.put("EQ_userType", "2");
		List<User> query = userService.query(mapQuery, null);
		Map<String, String> mapName2 = query.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
		model.put("mapName2", mapName2);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery1= Maps.newHashMap();
		mapQuery1.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery1, null).size() > 0)) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("EQ_name", principal.getUsername());
			map.put("EQ_userId", principal.getId());
			regnameList=regnameService.query(map, null);
		}else {
			regnameList=regnameService.getAll();
		}
		Map<String, Object> map1Query = Maps.newHashMap();
		map1Query.put("EQ_manageName", principal.getUsername());
		List<Board> boardList = boardService.query(map1Query, null);
		if (boardList.size() > 0){
			Map<String, Object> mapTwoQuery = Maps.newHashMap();
			String attachedName = boardList.get(0).getAttachedName();
			List<String> gatherList = attachedName != null ? Arrays.asList(attachedName.split(",")) : new ArrayList<>();
			mapTwoQuery.put("IN_name", gatherList);
			regnameList=regnameService.query(mapTwoQuery, null);
		}
		List<String> collect = regnameList.stream().map(Regname::getRegionName).collect(Collectors.toList());
		model.put("collect",collect);
		model.put("list1",listRegion);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonEntityResult<Link> saveJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<Link> result = new JsonEntityResult();
		this.allow(request, response, MappingMethod.create);
		try {
			// 先获取实体数据
			Link entity = this.loadEntity(request);
			if (entity == null) {
				// 如果是新建，创建一个新的实体
				entity = new Link();
				// 从请求参数中获取数据
				entity.setDomain(request.getParameter("domain"));
				entity.setRegionName(request.getParameter("regionName"));
			}
			
			String domain = entity.getDomain();
			String regionName = entity.getRegionName();
			
			// 先创建远程目录
			DmServer dmServer = redisUtil.getDmServer();
			boolean directory = RemoteFileOperationsUtil.createDirectory("/www/wwwroot/" + regionName + "/" + domain,
					dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
			if (!directory) {
				throw new RuntimeException("新建二级域名目录失败");
			}
			
			// 远程目录创建成功后，再保存到数据库
			result.setEntity(this.doSave(request, response, (Model)null, true));
			
		} catch (Exception var5) {
			this.handleException(result, "新增", var5);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonEntityResult<Link> updateJson(HttpServletRequest request, HttpServletResponse response) {
		this.allow(request, response, MappingMethod.update);
		JsonEntityResult result = new JsonEntityResult();
		String id = request.getParameter("id");
		Link oldLink = this.getEntityService().get(Long.parseLong(id));
		String oldLinkRegionName = oldLink.getRegionName();
		String oldLinkDomain = oldLink.getDomain();
		
		try {
			// 获取更新后的实体数据
			Link entity = this.loadEntity(request);
			if (entity == null) {
				throw new RuntimeException("无法加载实体数据");
			}
			
			String regionName = entity.getRegionName();
			String domain = entity.getDomain();
			
			// 先重命名远程目录
			DmServer dmServer = redisUtil.getDmServer();
			boolean b = RemoteFileOperationsUtil.renameDirectory("/www/wwwroot/" + oldLinkRegionName + "/" + oldLinkDomain,
					"/www/wwwroot/" + regionName + "/" + domain, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());

			if (!b) {
				throw new RuntimeException("修改二级域名目录失败");
			}
			
			// 远程目录重命名成功后，再更新数据库
			result.setEntity(this.doSave(request, response, (Model)null, false));
			
		} catch (Exception var5) {
			this.handleException(result, "更新", var5);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonResult deleteJson(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		Serializable[] ids = this.getRequestIds(request);
		
		try {
			for (Serializable id : ids) {
				Link link = this.getEntityService().get(id);
				String regionName = link.getRegionName();
				String domain = link.getDomain();
				
				// 先删除远程目录
				DmServer dmServer = redisUtil.getDmServer();
				boolean b = RemoteFileOperationsUtil.deleteDirectory("/www/wwwroot/" + regionName + "/" + domain,
						dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
				if (!b) {
					throw new RuntimeException("删除二级域名目录失败");
				}
				//也会删除数据中心Dmcenter的记录
				Map<String, Object> mapCenterMap = Maps.newHashMap();
				mapCenterMap.put("EQ_domain", domain);
				mapCenterMap.put("EQ_secondaryDomain", regionName);
				List<DmCenter> dmCenterList = dmCenterService.query(mapCenterMap, null);
				if (dmCenterList.size() > 0){
					dmCenterService.removeById(dmCenterList.get(0).getId());
				}
				// 远程目录删除成功后，再删除相关的数据库记录
				Map<String, Object> mapQuery = Maps.newHashMap();
				mapQuery.put("EQ_accessAddress", link.getAccessAddress());
				List<DmCondition> query = dmConditionService.query(mapQuery, null);
				if (query.size() > 0) {
					DmCondition dmCondition = query.get(0);
					dmConditionService.removeById(dmCondition.getId());
				}
			}
			
			// 更新Redis缓存
			try {
				dmCountryService.dmConditionRedis();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			// 最后删除主记录
			result = super.deleteJson(request, response);
			
		} catch (Exception e) {
			this.handleException(result, "删除", e);
		}
		
		return result;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Link onSave(HttpServletRequest request, HttpServletResponse response, Model model, Link entity, boolean isCreate) throws Exception {
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		if (isCreate){
			entity.setHolder(principal.getUsername());
			entity.setUserId(Math.toIntExact(principal.getId()));
			entity.setAccessAddress(entity.getRegionName()+"/"+entity.getDomain());

			DmCondition dmCondition = new DmCondition();
			dmCondition.setUserName(principal.getUsername());
			dmCondition.setIsIp(0);
			dmCondition.setIpCountry("US");
			dmCondition.setTimeContinent("America");
			dmCondition.setIsVpn(0);
			dmCondition.setIsFbclid(0);
			dmCondition.setIsChinese(0);
			dmCondition.setIsMobile(0);
			dmCondition.setTimeZone(0);
			dmCondition.setIsSpecificDevice(0);
			dmCondition.setIpLimits(0);
			dmCondition.setIpWhite(0);
			dmCondition.setIsRobot(0);
			dmCondition.setIsIdentify(0);
			dmCondition.setIsBusiness(0);
			dmCondition.setIsVirtual(0);
			dmCondition.setIosVersion("0.0.0");
			dmCondition.setAndVersion("0.0.0");
			dmCondition.setTimeMatch(0);
			dmCondition.setUpdateBy(principal.getUsername());
			dmCondition.setIsParams(0);
			dmCondition.setAccessAddress(entity.getAccessAddress());
			dmConditionService.save(dmCondition);

		}else {
			entity.setAccessAddress(entity.getRegionName()+"/"+entity.getDomain());
			String accessAddress = this.getEntityService().get(entity.getId()).getAccessAddress();
			if (!accessAddress.equals(entity.getAccessAddress())){
				Map<String, Object> mapQuery = Maps.newHashMap();
				mapQuery.put("EQ_accessAddress", accessAddress);
				List<DmCondition> query = dmConditionService.query(mapQuery, null);
				if (query.size() > 0){
					DmCondition dmCondition = query.get(0);
					dmCondition.setAccessAddress(entity.getAccessAddress());
					dmConditionService.update(dmCondition);
				}else {
					DmCondition dmCondition = new DmCondition();
					dmCondition.setUserName(principal.getUsername());
					dmCondition.setIsIp(0);
					dmCondition.setIsVpn(0);
					dmCondition.setIsFbclid(0);
					dmCondition.setIsChinese(0);
					dmCondition.setIsMobile(0);
					dmCondition.setTimeZone(0);
					dmCondition.setIsSpecificDevice(0);
					dmCondition.setAccessAddress(entity.getAccessAddress());
				}
			}

		}
		dmCountryService.dmConditionRedis();
		return super.onSave(request, response, model, entity, isCreate);
	}

	/**
	 * 获取指定目录下的子目录列表
	 * @param clearStr 要清空的目录路径
	 * @param dmServer 服务器信息
	 * @return 子目录列表
	 */
	private List<String> getSubDirectories(String clearStr, DmServer dmServer) {
		List<String> subDirectories = new ArrayList<>();
		try {
			// 查询数据库中该域名下的所有子目录
			String relativePath = clearStr.replace("/www/wwwroot/", "");
			
			// 直接使用相对路径作为查询条件，查询以当前路径开头的子目录
			Map<String, Object> searchParams = Maps.newHashMap();
			searchParams.put("LIKE_accessAddress", relativePath + "/%");

			List<Link> subLinks = linkService.query(searchParams, null);

			for (Link subLink : subLinks) {
				String subPath = subLink.getAccessAddress();
				// 构建完整的子目录路径
				String fullSubPath = "/www/wwwroot/" + subPath;
				subDirectories.add(fullSubPath);
			}
		} catch (Exception e) {
			System.err.println("获取子目录列表失败: " + e.getMessage());
		}
		return subDirectories;
	}


}
