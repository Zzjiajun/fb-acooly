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
import com.acooly.showcase.daliy.entity.Regname;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.acooly.showcase.daliy.service.RegnameService;
import com.acooly.showcase.link.entity.DmCondition;
import com.acooly.showcase.link.entity.DmServer;
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
import com.acooly.showcase.daliy.service.LinkService;
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


	@Override
	protected PageInfo<Link> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_holder", principal.getUsername());
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> mapQuery = Maps.newHashMap();
		ArrayList<String> list1 = new ArrayList<>();
		list1.add("欧洲");
		list1.add("美国");
		list1.add("日本");
		list1.add("韩国");
		list1.add("英国");
		list1.add("印度");
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
		List<String> collect = regnameList.stream().map(Regname::getRegionName).collect(Collectors.toList());
		model.put("collect",collect);
		model.put("list1",list1);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonEntityResult<Link> saveJson(HttpServletRequest request, HttpServletResponse response) {
		JsonEntityResult<Link> result = new JsonEntityResult();
		this.allow(request, response, MappingMethod.create);
		try {
			result.setEntity(this.doSave(request, response, (Model)null, true));
			String domain = result.getEntity().getDomain();
			String regionName = result.getEntity().getRegionName();
			DmServer dmServer = redisUtil.getDmServer();
			boolean directory = RemoteFileOperationsUtil.createDirectory("/www/wwwroot/" + regionName + "/" + domain,dmServer.getUsername(),
					dmServer.getPassword(),dmServer.getIp());
			if (!directory){
				throw new NullPointerException("新建二级域名失败");
			}
//			}
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
		String remoteDir="/"+oldLinkRegionName+"/"+oldLinkDomain;
		try {
			result.setEntity(this.doSave(request, response, (Model)null, false));
			Link entity = (Link) result.getEntity();
			String regionName = entity.getRegionName();
			String domain = entity.getDomain();
//			String newRemoteDir = "/" + regionName + "/" + domain;
			DmServer dmServer = redisUtil.getDmServer();
			boolean b = RemoteFileOperationsUtil.renameDirectory("/www/wwwroot/" + oldLinkRegionName + "/" + oldLinkDomain,
					"/www/wwwroot/" + regionName + "/" + domain,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());

			if (!b){
				throw new NullPointerException("修改二级域名失败");
			}
//			if (!(newRemoteDir.equals(remoteDir))){
//				String fTp = FTPUploader.updateFTp(remoteDir, newRemoteDir);
//				switch (fTp){
//					case "文件夹名称修改成功":
//						result.setSuccess(true);
//						result.setMessage("二级域名修改成功");
//						break;
//					case "文件夹名称修改失败":
//						result.setSuccess(false);
//						result.setMessage("二级域名修改失败");
//						break;
//					case "文件夹不存在":
//						result.setSuccess(false);
//						result.setMessage("服务器上，二级域名不存在");
//					default:
//						result.setSuccess(false);
//						result.setMessage("系统错误，联系管理员");
//				}
//			}
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
		for (Serializable id : ids) {
			Link link = this.getEntityService().get(id);
			Map<String, Object> mapQuery = Maps.newHashMap();
			mapQuery.put("EQ_accessAddress", link.getAccessAddress());
			List<DmCondition> query = dmConditionService.query(mapQuery, null);
			if (query.size() > 0){
				DmCondition dmCondition = query.get(0);
				dmConditionService.removeById(dmCondition.getId());
			}
            String regionName = link.getRegionName();
			String domain = link.getDomain();
			DmServer dmServer = redisUtil.getDmServer();
			boolean b = RemoteFileOperationsUtil.deleteDirectory("/www/wwwroot/" + regionName + "/" + domain
			,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
			if (!b){
				throw new NullPointerException("删除二级域名失败");
			}
		}
		try {
			dmCountryService.dmConditionRedis();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		result = super.deleteJson(request, response);
		return result;
	}


	@Override
	protected Link onSave(HttpServletRequest request, HttpServletResponse response, Model model, Link entity, boolean isCreate) throws Exception {
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		if (isCreate){
			entity.setHolder(principal.getUsername());
			entity.setUserId(Math.toIntExact(principal.getId()));
			entity.setAccessAddress(entity.getRegionName()+"/"+entity.getDomain());

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
}
