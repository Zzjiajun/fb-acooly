/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-26
 */
package com.acooly.showcase.daliy.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.event.EventBus;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.base.CustomerEventHandler;
import com.acooly.showcase.daliy.Utils.ModifyIndexHtml;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.daliy.Utils.RemoteFileOperationsUtil;
import com.acooly.showcase.daliy.entity.*;
import com.acooly.showcase.daliy.service.*;
import com.acooly.showcase.event.CreateCustomerEvent;
import com.acooly.showcase.event.CreateCustomerTwoEvent;
import com.acooly.showcase.link.entity.*;
import com.acooly.showcase.link.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * dm_center 管理控制器
 *
 * @author acooly
 * @date 2024-03-26 12:23:34
 */
@Controller
@RequestMapping(value = "/manage/showcase/daily/dmCenter")
public class DmCenterManagerController extends AbstractJsonEntityController<DmCenter, DmCenterService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmCenterService dmCenterService;


	@Autowired
	private DmRegionService dmRegionService;
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private DmShowService dmShowService;
	@Autowired
	private DmPixelService dmPixelService;
	@Autowired
	private DmDomainService dmDomainService;
	@Autowired
	private UserService userService;
	@Autowired
	private LinkIntService linkIntService;
	@Autowired
	private LinkSrcsService linkSrcsService;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private DmServerService dmServerService;
	@Autowired
	private DmStencilService dmStencilService;
	@Autowired
	private RedisUtils redisUtil;
	@Autowired
	private DmAccessService dmAccessService;
	@Autowired
	private DmClickService dmClickService;




	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<DmRegion> list = dmRegionService.getAll();
		List<String> strings = list.stream().map(DmRegion::getRegion).collect(Collectors.toList());
		List<Link> query;
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery1 = Maps.newHashMap();
		mapQuery1.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery1, null).size() > 0)) {
			Map<String, Object> map1 = Maps.newHashMap();
			map1.put("EQ_userId", principal.getId());
			map1.put("EQ_holder", principal.getUsername());
			query = linkService.query(map1, null);
		} else {
			query = linkService.getAll();
		}
		List<String> listRegionName = query.stream().map(Link::getRegionName).distinct().collect(Collectors.toList());
		Map<String, List<String>> mapDomain = query.stream()
				.collect(Collectors.groupingBy(Link::getRegionName,
						Collectors.mapping(Link::getDomain, Collectors.toList())));
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userType", "2");
		List<User> query3 = userService.query(mapQuery, null);
		Map<String, String> mapName = query3.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));

		model.put("mapName", mapName);
		List<DmShow> showList = dmShowService.getAll();
		Map<String, String> collected = showList.stream().collect(Collectors.toMap(user -> user.getRegion() + "  :  " + user.getSerialNumber()+"("+user.getRemark()+")", DmShow::getDomain));
		model.put("collected" ,collected);
		model.put("regionList",strings);
		model.put("domainList", listRegionName);
		model.put("mapDomain", mapDomain);
	}

	@Override
	protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, DmCenter entity) {
		String domain = entity.getDomain();
		Map<String, Object> map = Maps.newHashMap();
		map.put("EQ_regionName",domain);
		List<Link> query = linkService.query(map, null);
		List<String> collected = query.stream().map(Link::getDomain).distinct().collect(Collectors.toList());
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("collected1",collected);
		model.addAllAttributes(map1);
		super.onEdit(request, response, model, entity);
	}

	public String redisString(String key,Long value){
		if (!redisUtil.exist(key)){
			DmStencil dmStencil = dmStencilService.get(value);
			redisUtil.set(key, dmStencil.getPathName());
			return  dmStencil.getPathName();
		}else {
			return redisUtil.get(key);
		}

	}

	public void linuxCopyFile(DmCenter entity,String str,String pathNameCil,String string){
		DmServer dmServer = redisUtil.getDmServer();
		if (entity.getDisplayOption()==1) {
			Preconditions.checkNotNull(entity.getSerialNumber(), "落地页模版不能为空"); // 检查落地页模版是否为空
			Preconditions.checkNotNull(entity.getPixel(), "像素Id不能为空"); // 检查像素id是否为空
			// 先清除落地页文件，并复制新的文件
			boolean b = RemoteFileOperationsUtil.copyFiles("/www/wwwroot/" + string, "/www/wwwroot/" + str
			,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
			if (!b) {
				throw new NullPointerException("模版复制到" + str + "失败");
			}
		}else {
			boolean b=RemoteFileOperationsUtil.copyFiles("/www/wwwroot/"+pathNameCil,"/www/wwwroot/" + str,
			dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
			if (!b){
				throw new NullPointerException("模版复制到"+ str+"失败");
			}
		}
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	@Async
	protected DmCenter onSave(HttpServletRequest request, HttpServletResponse response, Model model, DmCenter entity, boolean isCreate) throws Exception {
		List<String> list = Arrays.asList(entity.getPixel().split("\n")); // 将像素id拆分为列表
		User principal = (User) SecurityUtils.getSubject().getPrincipal(); // 获取当前用户
		String str=entity.getDomain() + "/" + entity.getSecondaryDomain(); // 拼接域名
		entity.setUserName(principal.getUsername()); // 设置实体的用户名为当前用户的用户名
		String clearStr = "/www/wwwroot/"+str;
		String filePath = "/www/wwwroot/"+str+"/index.html";
		String builtKey = redisUtil.buildKey("acooly","pathNameCil");
		String builtKey1 = redisUtil.buildKey("acooly","pathNameCil1");
		String builtKeyIp = redisUtil.buildKey("acooly","Ip");
		String pathNameCil=redisString(builtKey,1l);
		String pathNameCil1=redisString(builtKey1,2l);
		DmServer dmServer;
		if (!redisUtil.exist(builtKeyIp)){
			dmServer = dmServerService.get(1l);
			LinkedList<DmServer> dmServers = new LinkedList<>();
			dmServers.add(dmServer);
			redisUtil.set(builtKeyIp,new Gson().toJson(dmServers));
		}else {
			String string = redisUtil.get(builtKeyIp);
			List<DmServer> dmServers =new Gson().fromJson(string, new TypeToken<List<DmServer>>(){}.getType());
			dmServer=dmServers.get(0);
		}
		if(isCreate){
			entity.setClicksNumber(0); // 设置点击数初始值为0
			entity.setVisitsNumber(0); // 设置访问数初始值为0
			if (entity.getDiversion() == 1) {
				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
				//引流的落地页和表单模版不一样，需要复制新的模版
//				if (entity.getDisplayOption()==1) {
//					Preconditions.checkNotNull(entity.getSerialNumber(), "落地页模版不能为空"); // 检查落地页模版是否为空
//					Preconditions.checkNotNull(entity.getPixel(), "像素Id不能为空"); // 检查像素id是否为空
//					// 先清除落地页文件，并复制新的文件
//					String string = entity.getSerialNumber() + "D";
//					boolean b = RemoteFileOperationsUtil.copyFiles("/www/wwwroot/" + string, "/www/wwwroot/" + str);
//					if (!b) {
//						throw new NullPointerException("模版复制到" + str + "失败");
//					}
//				}else {
//					boolean b=RemoteFileOperationsUtil.copyFiles("/www/wwwroot/"+pathNameCil1,"/www/wwwroot/" + str);
//					if (!b){
//						throw new NullPointerException("模版复制到"+ str+"失败");
//					}
//				}
				linuxCopyFile(entity,str,pathNameCil1,entity.getSerialNumber() + "D");
				// 创建引流链接对象
				//查看是否已经有了相同的域名
				Map<String, Object> map = Maps.newHashMap();
				map.put("EQ_domain",str);
				List<LinkInt> query = linkIntService.query(map, null);
				if (query.isEmpty()){
					LinkInt linkInt = new LinkInt();
					linkInt.setUserName(principal.getUsername());
					linkInt.setDomain(str);
					linkInt.setCountLink(1);
					linkIntService.save(linkInt);
				}else {
					LinkInt linkInt = query.get(0);
					linkInt.setCountLink(1);
					linkIntService.update(linkInt);
				}
				List<LinkSrcs> linkSrcsList = linkSrcsService.query(map, null);
				if (!linkSrcsList.isEmpty()){
					linkSrcsList.forEach(s->{
						linkSrcsService.removeById(s.getId());
					});
				}
				LinkSrcs linkSrcs = new LinkSrcs();
				linkSrcs.setLinkSrc(entity.getLink());
				linkSrcs.setUserName(principal.getUsername());
				linkSrcs.setDomain(str);
				linkSrcsService.save(linkSrcs); // 保存新的链接信息

			}else {
				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
//				if (entity.getDisplayOption()==1) {
//					Preconditions.checkNotNull(entity.getSerialNumber(), "落地页模版不能为空"); // 检查落地页模版是否为空
//					Preconditions.checkNotNull(entity.getPixel(), "像素Id不能为空"); // 检查像素id是否为空
//					// 先清除落地页文件，并复制新的文件
//					boolean b = RemoteFileOperationsUtil.copyFiles("/www/wwwroot/" + entity.getSerialNumber(), "/www/wwwroot/" + str);
//					if (!b) {
//						throw new NullPointerException("模版复制到" + str + "失败");
//					}
//				}else {
//					boolean b=RemoteFileOperationsUtil.copyFiles("/www/wwwroot/"+pathNameCil,"/www/wwwroot/" + str);
//					if (!b){
//						throw new NullPointerException("模版复制到"+ str+"失败");
//					}
//				}
				linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
				CreateCustomerEvent event=new CreateCustomerEvent();
				event.setHost(dmServer.getIp());
				event.setUsername(dmServer.getUsername());
				event.setPassword(dmServer.getPassword());
				event.setFilePath(filePath);
				event.setNewLink(entity.getLink());
				eventBus.publish(event);
			}
			//如果是落地页
			if (entity.getDisplayOption()==1) {
				// 保存像素id信息
				Map<String, Object> map2 = Maps.newHashMap();
				map2.put("EQ_domain",str);
				List<DmPixel> query3 = dmPixelService.query(map2, null);
				if (!query3.isEmpty()){
					query3.forEach(s->{
						dmPixelService.removeById(s.getId());
					});
				}
				LinkedList<DmPixel> dmPixelList = new LinkedList<>();
				list.forEach(s->{
					DmPixel dmPixel = new DmPixel();
					dmPixel.setPixelId(s);
					dmPixel.setDomain(str);
					dmPixel.setUserName(principal.getUsername());
					dmPixelList.add(dmPixel);
				});
				dmPixelService.saves(dmPixelList);
			}
		}else {




			// 修改时 当修改链接发送改变时
			DmCenter dmCenterOld = this.getEntityService().get(entity.getId());
			Map<String, Object> map = Maps.newHashMap();
			String oldStr = dmCenterOld.getDomain() + "/" + dmCenterOld.getSecondaryDomain();
			map.put("EQ_domainName",oldStr);


			//像素id修改
			if (!entity.getPixel().equals(dmCenterOld.getPixel()) ||!oldStr.equals(str)){
				//先删除原有的像素id
				Map<String, Object> map2 = Maps.newHashMap();
				map2.put("EQ_domain",entity.getDomain() + "/" + entity.getSecondaryDomain());
				List<DmPixel> dmPixels = dmPixelService.query(map2, null);
				if (!dmPixels.isEmpty()){
					dmPixels.forEach(s->{
						dmPixelService.removeById(s.getId());
					});
				}
				list.forEach(s->{
					DmPixel dmPixel = new DmPixel();
					dmPixel.setPixelId(s);
					dmPixel.setDomain(str);
					dmPixel.setUserName(principal.getUsername());
					dmPixelService.save(dmPixel);
				});
			}

			//分流
			if (entity.getDiversion() == 1 && !entity.getDiversion().equals(dmCenterOld.getDiversion())) {
				//并且修改了域名
				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
				linuxCopyFile(entity,str,pathNameCil1,entity.getSerialNumber() + "D");

				// 创建引流链接对象
				//查看是否已经有了相同的域名
				Map<String, Object> map1 = Maps.newHashMap();
				map1.put("EQ_domain",str);
				List<LinkInt> queryt = linkIntService.query(map1, null);
				if (queryt.isEmpty()){
					LinkInt linkInt = new LinkInt();
					linkInt.setUserName(principal.getUsername());
					linkInt.setDomain(str);
					linkInt.setCountLink(1);
					linkIntService.save(linkInt);
				}else {
					LinkInt linkInt = queryt.get(0);
					linkInt.setCountLink(1);
					linkIntService.update(linkInt);
				}
				List<LinkSrcs> linkSrcsList = linkSrcsService.query(map1, null);
				if (!linkSrcsList.isEmpty()){
					linkSrcsList.forEach(s->{
						linkSrcsService.removeById(s.getId());
					});
				}
				LinkSrcs linkSrcs = new LinkSrcs();
				linkSrcs.setLinkSrc(entity.getLink());
				linkSrcs.setUserName(principal.getUsername());
				linkSrcs.setDomain(str);
				linkSrcsService.save(linkSrcs); // 保存新的链接信息


			}
			if (entity.getDiversion() == 0 && !entity.getDiversion().equals(dmCenterOld.getDiversion())){
				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
				linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
				CreateCustomerEvent event=new CreateCustomerEvent();
				event.setHost(dmServer.getIp());
				event.setUsername(dmServer.getUsername());
				event.setPassword(dmServer.getPassword());
				event.setFilePath(filePath);
				event.setNewLink(entity.getLink());
				eventBus.publish(event);
			}


			//如果分流类型不变
			if (entity.getDiversion().equals(dmCenterOld.getDiversion())) {
				//广告类型改变
				if (entity.getDisplayOption().equals(dmCenterOld.getDisplayOption())){
					//是分流
					if (entity.getDiversion() == 1){
							RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
							linuxCopyFile(entity,str,pathNameCil1,entity.getSerialNumber() + "D");

							// 创建引流链接对象
							//查看是否已经有了相同的域名
							Map<String, Object> map1 = Maps.newHashMap();
							map.put("EQ_domain",str);
							List<LinkInt> queryt = linkIntService.query(map1, null);
							if (queryt.isEmpty()){
								LinkInt linkInt = new LinkInt();
								linkInt.setUserName(principal.getUsername());
								linkInt.setDomain(str);
								linkInt.setCountLink(1);
								linkIntService.save(linkInt);
							}else {
								LinkInt linkInt = queryt.get(0);
								linkInt.setCountLink(1);
								linkIntService.update(linkInt);
							}
							List<LinkSrcs> linkSrcsList = linkSrcsService.query(map1, null);
							if (!linkSrcsList.isEmpty()){
								linkSrcsList.forEach(s->{
									linkSrcsService.removeById(s.getId());
								});
							}
							LinkSrcs linkSrcs = new LinkSrcs();
							linkSrcs.setLinkSrc(entity.getLink());
							linkSrcs.setUserName(principal.getUsername());
							linkSrcs.setDomain(str);
							linkSrcsService.save(linkSrcs); // 保存新的链接信息
					}else {
						RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
						linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
						CreateCustomerEvent event=new CreateCustomerEvent();
						event.setHost(dmServer.getIp());
						event.setUsername(dmServer.getUsername());
						event.setPassword(dmServer.getPassword());
						event.setFilePath(filePath);
						event.setNewLink(entity.getLink());
						eventBus.publish(event);
					}
				}else {
					if (entity.getDiversion() != 1){
						RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
						linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
						CreateCustomerEvent event=new CreateCustomerEvent();
						event.setHost(dmServer.getIp());
						event.setUsername(dmServer.getUsername());
						event.setPassword(dmServer.getPassword());
						event.setFilePath(filePath);
						event.setNewLink(entity.getLink());
						eventBus.publish(event);
					}
				}
			}

		}

		// 在程序退出时关闭线程池

		return super.onSave(request, response, model, entity, isCreate);
	}


	@Override
	protected void doRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
		DmServer dmServer = redisUtil.getDmServer();

		if (ids != null && ids.length != 0) {
			if (ids.length == 1) {
				DmCenter dmCenter = this.getEntityService().get(ids[0]);
				String str=dmCenter.getDomain() + "/" + dmCenter.getSecondaryDomain();

				RemoteFileOperationsUtil.clearDirectory("/www/wwwroot/" + str,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());

				Map<String, Object> map1 = Maps.newHashMap();
				map1.put("EQ_domain",str);
//				List<DmPixel> query1 = dmPixelService.query(map1, null);
//				if (!query1.isEmpty()){
//					query1.forEach(s->{
//						dmPixelService.remove(s);
//					});
//				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("EQ_domainName",str);
				List<DmDomain> query = dmDomainService.query(map, null);
				if (!query.isEmpty()){
					query.forEach(s->{
						s.setLink("");
						dmDomainService.update(s);
					});
				}

				List<LinkInt> linkIntList = linkIntService.query(map1, null);
				if (!linkIntList.isEmpty()){
					LinkInt linkInt = linkIntList.get(0);
					linkInt.setCountLink(1);
					linkIntService.update(linkInt);
				}

				List<LinkSrcs> linkSrcsList = linkSrcsService.query(map1, null);
				if (!linkSrcsList.isEmpty()){
					linkSrcsList.forEach(s->{
						linkSrcsService.removeById(s.getId());
					});
				}
				//轮询数变为1    所有的轮询链接删除

				this.getEntityService().removeById(ids[0]);
			} else {
				this.getEntityService().removes(ids);
			}

		} else {
			throw new IllegalArgumentException("请求参数中没有指定需要删除的实体Id");
		}
	}





	//清除浏览记录

	@RequestMapping(value = "eliminate")
	@ResponseBody
	public JsonResult eliminate(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		String id = request.getParameter("id");
		DmCenter dmCenter = this.getEntityService().get(Long.valueOf(id));
		dmCenter.setVisitsNumber(0);
		dmCenter.setClicksNumber(0);
		this.getEntityService().update(dmCenter);
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_centerId", id);
		List<DmAccess> accessList = dmAccessService.query(mapQuery, null);
		accessList.forEach(s->{
			dmAccessService.removeById(s.getId());
		});
		List<DmClick> clickList = dmClickService.query(mapQuery, null);
		clickList.forEach(s->{
			dmClickService.removeById(s.getId());
		});
		jsonResult.setMessage("清除浏览记录成功");
		return jsonResult;
	}



	@RequestMapping(value = "buildCanonicalUrl")
	public String buildCanonicalUrl(HttpServletRequest request, HttpServletResponse response ,Model model) {
		String displayOption = request.getParameter("displayOption");
		model.addAttribute("k",displayOption);
		return "manage/showcase/daily/displayOption";
	}

	@RequestMapping({"listDisplayOption"})
	@ResponseBody
	public JsonListResult<DmCenter> listDisplayOption(HttpServletRequest request, HttpServletResponse response, @RequestParam String type) throws Exception {
		JsonListResult<DmCenter> result = new JsonListResult();
		Map<String, Object> searchParams = this.getSearchParams(request);
		if (type.equals("1")){
			searchParams.put("EQ_displayOption","1");
		} else if (type.equals("2")){
			searchParams.put("EQ_displayOption","2");
		}else {
			searchParams.put("EQ_diversion","1");
		}
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_userName",principal.getUsername());
		}
		PageInfo<DmCenter> pageInfo = this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
		result.setTotal(pageInfo.getTotalCount());
		List<DmCenter> rowList = pageInfo.getPageResults();
		List<User> userList = userService.getAll();
		Map<String, String> userMap = userList.stream()
				.collect(Collectors.toMap(User::getUsername, User::getRealName));
		rowList.forEach(s->{
			s.setUserName(userMap.get(s.getUserName()));
		});
		result.setRows(rowList);
		result.setHasNext(pageInfo.hasNext());
		result.setPageNo(pageInfo.getCurrentPage());
		result.setPageSize(pageInfo.getCountOfCurrentPage());
		return result;
	}


	@Override
	protected PageInfo<DmCenter> doList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> searchParams = this.getSearchParams(request);
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userName", principal.getUsername());
		if (!(permissionsService.query(mapQuery, null).size() > 0)) {
			searchParams.put("EQ_userName",principal.getUsername());
		}
		return this.getEntityService().query(this.getPageInfo(request), searchParams, this.getSortMap(request));
	}


	@Override
	public JsonListResult<DmCenter> listJson(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<DmCenter> dmCenterJsonListResult = super.listJson(request, response);
		List<DmCenter> rows = dmCenterJsonListResult.getRows();
		List<User> userList = userService.getAll();
		Map<String, String> userMap = userList.stream()
				.collect(Collectors.toMap(User::getUsername, User::getRealName));
		rows.forEach(s->{
			s.setUserName(userMap.get(s.getUserName()));
		});
		dmCenterJsonListResult.setRows(rows);
		return dmCenterJsonListResult;
	}
}
