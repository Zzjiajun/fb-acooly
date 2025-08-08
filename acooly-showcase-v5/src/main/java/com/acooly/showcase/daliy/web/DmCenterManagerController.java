/*
 * acooly.cn Inc.
 * Copyright (c) 2024 All Rights Reserved.
 * create by acooly
 * date:2024-03-26
 */
package com.acooly.showcase.daliy.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.event.EventBus;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;

import com.acooly.showcase.daliy.Utils.EncryptionUtil;
import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.daliy.Utils.RemoteFileOperationsUtil;
import com.acooly.showcase.daliy.Utils.SSHConnectionManager;
import com.acooly.showcase.daliy.entity.*;
import com.acooly.showcase.daliy.service.*;
import com.acooly.showcase.event.CreateCustomerEvent;

import com.acooly.showcase.event.CreateCustomerThreeEvent;
import com.acooly.showcase.link.entity.*;
import com.acooly.showcase.link.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * dm_center 管理控制器
 * 
 * 风险提示：
 * 1. 大量@Autowired注入可能导致循环依赖风险
 * 2. @Async和@Transactional同时使用可能导致事务失效
 * 3. 异步操作可能导致资源竞争
 * 4. 异常处理不够完善，建议使用统一的异常处理机制
 * 
 * 建议改进：
 * 1. 使用@Lazy注解延迟加载非必需依赖
 * 2. 将异步操作和事务操作分离
 * 3. 添加统一的异常处理机制
 * 4. 考虑使用事件驱动架构减少直接依赖
 *
 * @author acooly
 * @date 2024-03-26 12:23:34
 */
@Slf4j
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
	@Autowired
	private DmCountryService dmCountryService;
	@Autowired
	private DmConditionService dmConditionService;
	@Autowired
	private DmTrollsService dmTrollsService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private DmObserverPermissionService dmObserverPermissionService;


	@Override
	public JsonEntityResult<DmCenter> updateJson(HttpServletRequest request, HttpServletResponse response) {

		JsonEntityResult<DmCenter> result = super.updateJson(request, response);
		//更新redis缓存
		try {
			dmCountryService.dmCenterRedis();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			dmCountryService.dmConditionRedis();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public JsonEntityResult<DmCenter> saveJson(HttpServletRequest request, HttpServletResponse response) {

		JsonEntityResult<DmCenter> result = super.saveJson(request, response);
		//创建观察者记录
		DmObserverPermission dmObserverPermission = new DmObserverPermission();
		dmObserverPermission.setDmCenterId(result.getEntity().getId());
		dmObserverPermission.setGrantTime(new Date());
		dmObserverPermission.setStatus(1);
		dmObserverPermission.setGrantBy("admin");
		dmObserverPermissionService.save(dmObserverPermission);
		//更新redis缓存
		try {
			dmCountryService.dmCenterRedis();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			dmCountryService.dmConditionRedis();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		List<DmRegion> list = dmRegionService.getAll();
		List<String> strings = list.stream().map(DmRegion::getRegion).collect(Collectors.toList());
		List<Link> query;
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		boolean isCurrentUserObserver = dmObserverPermissionService.isObserver(principal.getId());
		model.put("isCurrentUserObserver", isCurrentUserObserver);
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
		Map<String, Object> map1Query = Maps.newHashMap();
		map1Query.put("EQ_manageName", principal.getUsername());
		List<Board> boardList = boardService.query(map1Query, null);
		if (boardList.size() > 0){
			Map<String, Object> map2 = Maps.newHashMap();
			String attachedName = boardList.get(0).getAttachedName();
			List<String> gatherList = attachedName != null ? Arrays.asList(attachedName.split(",")) : new ArrayList<>();
			map2.put("IN_holder", gatherList);
			query=linkService.query(map2, null);
		}
		List<String> listRegionName = query.stream().map(Link::getRegionName).distinct().collect(Collectors.toList());
		Map<String, List<String>> mapDomain = query.stream()
				.collect(Collectors.groupingBy(Link::getRegionName,
						Collectors.mapping(Link::getDomain, Collectors.toList())));
		Map<String, Object> mapQuery = Maps.newHashMap();
		mapQuery.put("EQ_userType", "2");
		List<User> query3 = userService.query(mapQuery, null);
		Map<String, String> mapName = query3.stream().collect(Collectors.toMap(User::getUsername, User::getRealName));
		List<DmCenter> all = this.getEntityService().getAll();
		Map<String, List<String>> map = getMap(all, mapDomain);
		model.put("mapName", mapName);
		List<DmShow> showList = dmShowService.getAll();
		Map<String, String> collected = showList.stream().collect(Collectors.toMap(user -> user.getRegion() + "  :  " + user.getSerialNumber()+"("+user.getRemark()+")", DmShow::getDomain));
		model.put("collected" ,collected);
		model.put("regionList",strings);
		model.put("domainList", listRegionName);
//		model.put("mapDomain", mapDomain);
		model.put("mapDomain", map);
	}
	private Map<String, List<String>> getMap(List<DmCenter> dmCenterList, Map<String, List<String>> mapDomain) {
		Map<String, List<String>> filteredMapDomain = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : mapDomain.entrySet()) {
			String key = entry.getKey(); // 主域名
			List<String> filteredValues = entry.getValue().stream()
					.filter(value -> dmCenterList.stream().noneMatch(dm -> dm.getDomain().equals(key) && dm.getSecondaryDomain().equals(value)))
					.collect(Collectors.toList());
			filteredMapDomain.put(key, filteredValues); // 直接放入 List<String>
		}
		return filteredMapDomain;
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
		if(entity.getProtect()==0){
			SecretKey secretKey = EncryptionUtil.stringToSecretKey(entity.getKeyy());
            String decrypt = null;
            try {
                decrypt = EncryptionUtil.decrypt(entity.getLink(), secretKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            entity.setLink(decrypt);
		}
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

	@Override
	protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            dmCountryService.dmCenterRedis();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onCreate(request, response, model);
	}

	public void linuxCopyFile(DmCenter entity, String str, String pathNameCil, String string){
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
	protected DmCenter onSave(HttpServletRequest request, HttpServletResponse response, Model model, DmCenter entity, boolean isCreate) throws Exception {
		List<String> list = Arrays.asList(entity.getPixel().split("\n")); // 将像素id拆分为列表
		User principal = (User) SecurityUtils.getSubject().getPrincipal(); // 获取当前用户
		String str=entity.getDomain() + "/" + entity.getSecondaryDomain(); // 拼接域名
//		entity.setUserName(principal.getUsername()); // 设置实体的用户名为当前用户的用户名
		String clearStr = "/www/wwwroot/"+str;
		String filePath = "/www/wwwroot/"+str+"/index.html";
		String builtKey = redisUtil.buildKey("acooly","pathNameCil");
		String builtKey1 = redisUtil.buildKey("acooly","pathNameCil1");
		String builtProtectKey = redisUtil.buildKey("acooly","protectCil");
		String builtProtectKey1 = redisUtil.buildKey("acooly","protectCil1");
		String builtFastKey = redisUtil.buildKey("acooly","fastCil");
		String builtFastKey1 = redisUtil.buildKey("acooly","fastCil1");
		String builtKeyIp = redisUtil.buildKey("acooly","Ip");
		String pathNameCil;
		String pathNameCil1;
		Map<String, Object> mapCondition = Maps.newHashMap();
		mapCondition.put("EQ_accessAddress",str);
		DmCondition dmCondition = dmConditionService.query(mapCondition, null).get(0);
		entity.setUserName(dmCondition.getUserName()); // 设置实体的用户名为当前用户的用户名
		//绑定限制条件的id
		entity.setConditionId(dmCondition.getId());
		if (entity.getProtect() == 0) {
			pathNameCil = redisString(builtKey, 1L);
			pathNameCil1 = redisString(builtKey1, 2L);
//			if ((dmCondition.getIsVpn() == 0 || dmCondition.getIsVpn() == null) &&
//					(dmCondition.getIpLimits() == 0 || dmCondition.getIpLimits() == null)) {
//				pathNameCil = redisString(builtFastKey, 5L);
//				pathNameCil1 = redisString(builtKey1, 2L);
//			} else {
//				pathNameCil = redisString(builtKey, 1L);
//				pathNameCil1 = redisString(builtKey1, 2L);
//			}
		} else {
			pathNameCil = redisString(builtProtectKey, 3L);
			pathNameCil1 = redisString(builtProtectKey1, 4L);
		}
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
			String encrypt="";
			String encodedKey="";
			entity.setTrolls(0); // 设置初始的骚扰数为0
			entity.setClicksNumber(0); // 设置点击数初始值为0
			entity.setVisitsNumber(0); // 设置访问数初始值为0
			if (entity.getProtect()==0){
				SecretKey secretKey = EncryptionUtil.generateKey();
				encrypt = EncryptionUtil.encrypt(entity.getLink(), secretKey);
				entity.setLink(encrypt);
				encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
				entity.setKeyy(encodedKey);
			}else {
				SecretKey secretKey = EncryptionUtil.generateKey();
				encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
				entity.setKeyy(encodedKey);
			}
			if (entity.getDiversion() == 1) {
				safeClearDirectory(clearStr, dmServer);
//				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
				//引流的落地页和表单模版不一样，需要复制新的模版
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
				linkSrcs.setKeyy(entity.getKeyy());
				linkSrcs.setProtect(entity.getProtect());
				linkSrcs.setUserName(principal.getUsername());
				linkSrcs.setDomain(str);
				linkSrcsService.save(linkSrcs); // 保存新的链接信息

			}else {
				safeClearDirectory(clearStr, dmServer);
//				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
				linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
				eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
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
			DmCenter dmCenterOld = this.getEntityService().get(entity.getId());
			Map<String, Object> map = Maps.newHashMap();
			String oldStr = dmCenterOld.getDomain() + "/" + dmCenterOld.getSecondaryDomain();
			map.put("EQ_domainName",oldStr);
			// 修改时 当修改链接发送改变时
			if (entity.getProtect()==0) {
				SecretKey secretKey = EncryptionUtil.generateKey();
				String encrypt = EncryptionUtil.encrypt(entity.getLink(), secretKey);
				String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
				entity.setKeyy(encodedKey);
				entity.setLink(encrypt);
			}

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
				safeClearDirectory(clearStr, dmServer);
//				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
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
				linkSrcs.setProtect(entity.getProtect());
				linkSrcs.setKeyy(entity.getKeyy());
				linkSrcs.setUserName(principal.getUsername());
				linkSrcs.setDomain(str);
				linkSrcsService.save(linkSrcs); // 保存新的链接信息


			}
			if (entity.getDiversion() == 0 && !entity.getDiversion().equals(dmCenterOld.getDiversion())){
				safeClearDirectory(clearStr, dmServer);
//				RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
				linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
				eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
			}


			//如果分流类型不变
			if (entity.getDiversion().equals(dmCenterOld.getDiversion())) {
				//广告类型改变
				if (entity.getDisplayOption().equals(dmCenterOld.getDisplayOption())){
					//是分流
					if (entity.getDiversion() == 1){
							safeClearDirectory(clearStr, dmServer);
//							RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
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
							linkSrcs.setProtect(entity.getProtect());
							linkSrcs.setKeyy(entity.getKeyy());
							linkSrcs.setUserName(principal.getUsername());
							linkSrcs.setDomain(str);
							linkSrcsService.save(linkSrcs); // 保存新的链接信息
					}else {
						safeClearDirectory(clearStr, dmServer);
//						RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
						linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
						eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
					}
				}else {
					if (entity.getDiversion() != 1){
						safeClearDirectory(clearStr, dmServer);
//						RemoteFileOperationsUtil.clearDirectory(clearStr,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
						linuxCopyFile(entity,str,pathNameCil,entity.getSerialNumber());
						eventTwoChooseCreate(dmServer,filePath,entity,dmCondition);
					}
				}
			}

		}

		// 在程序退出时关闭线程池
		return super.onSave(request, response, model, entity, isCreate);
	}


	public void eventChooseCreate(DmServer dmServer,String filePath,DmCenter entity,DmCondition dmCondition){
		if (dmCondition.getIsVpn()==0 || dmCondition.getIsVpn()==null || entity.getDisplayOption()==2){
			CreateCustomerThreeEvent event = new CreateCustomerThreeEvent();
			event.setHost(dmServer.getIp());
			event.setUsername(dmServer.getUsername());
			event.setPassword(dmServer.getPassword());
			event.setFilePath(filePath);
			event.setNewLink(entity.getLink());
			if (StringUtils.isEmpty(dmCondition.getIpCountry())){
				event.setIpCountry("US");
			}else {
				event.setIpCountry(dmCondition.getIpCountry());
			}
			event.setTimeZone(dmCondition.getTimeZone());
			event.setTimeContinent(dmCondition.getTimeContinent());
			event.setIsChinese(dmCondition.getIsChinese());
			event.setIsMobile(dmCondition.getIsMobile());
			event.setIsSpecificDevice(dmCondition.getIsSpecificDevice());
			event.setIsFbclid(dmCondition.getIsFbclid());
			event.setIsIp(dmCondition.getIsIp());
			event.setIsVpn(dmCondition.getIsVpn());
			event.setVpnCode(0);
			event.setIpWhite(dmCondition.getIpWhite());
			event.setWhiteList(dmCondition.getWhiteList());
			eventBus.publish(event);
		}else {
			CreateCustomerEvent event=new CreateCustomerEvent();
			event.setHost(dmServer.getIp());
			event.setUsername(dmServer.getUsername());
			event.setPassword(dmServer.getPassword());
			event.setFilePath(filePath);
			event.setNewLink(entity.getLink());
			eventBus.publish(event);
		}
	}


	public void eventTwoChooseCreate(DmServer dmServer,String filePath,DmCenter entity,DmCondition dmCondition){
		CreateCustomerEvent event=new CreateCustomerEvent();
		event.setHost(dmServer.getIp());
		event.setUsername(dmServer.getUsername());
		event.setPassword(dmServer.getPassword());
		event.setFilePath(filePath);
		event.setNewLink(entity.getLink());
		eventBus.publish(event);
	}




	@Override
	protected void doRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
		DmServer dmServer = redisUtil.getDmServer();
		//批量删除时，不会清理文件，只会修改数据库记录。
		if (ids != null && ids.length != 0) {
			if (ids.length == 1) {
				DmCenter dmCenter = this.getEntityService().get(ids[0]);
				String str=dmCenter.getDomain() + "/" + dmCenter.getSecondaryDomain();

				RemoteFileOperationsUtil.clearDirectory("/www/wwwroot/" + str,dmServer.getUsername(),dmServer.getPassword(),dmServer.getIp());
				safeClearDirectory("/www/wwwroot/" + str, dmServer);
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
		dmCenter.setTrolls(0);
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
		List<DmTrolls> trollsList = dmTrollsService.query(mapQuery, null);
		trollsList.forEach(s->{
			dmTrollsService.removeById(s.getId());
		});
		String str=dmCenter.getDomain() + "/" + dmCenter.getSecondaryDomain();
		String buildKey = redisUtil.buildKey("AoollyNumberIp", str);
		redisUtil.del(buildKey);
		jsonResult.setMessage("清除浏览记录成功");
		return jsonResult;
	}

	@RequestMapping(value = "eliminateAll")
	@ResponseBody
	public JsonResult eliminateAll(HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
//		dmAccessService.getAll().forEach(s->{
//			dmAccessService.removeById(s.getId());
//		});
//		dmClickService.getAll().forEach(s->{
//			dmClickService.removeById(s.getId());
//		});
//		dmTrollsService.getAll().forEach(s->{
//			dmTrollsService.removeById(s.getId());
//		});
		dmAccessService.deleteAll();
		dmClickService.deleteAll();
		dmTrollsService.deleteAll();
		dmCenterService.getAll().forEach(s->{
			s.setVisitsNumber(0);
			s.setClicksNumber(0);
			s.setTrolls(0);
			this.getEntityService().update(s);
			String str=s.getDomain() + "/" + s.getSecondaryDomain();
			String buildKey = redisUtil.buildKey("AoollyNumberIp", str);
			redisUtil.del(buildKey);
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

		boolean isObserver = dmObserverPermissionService.isObserver(principal.getId());
		if (isObserver) {
			// 观察者用户：只查询有权限的记录
			Long currentUserId = principal.getId();
			List<Long> accessibleIds = dmObserverPermissionService.getAccessibleDmCenterIds(currentUserId);

			if (accessibleIds != null && !accessibleIds.isEmpty()) {
				searchParams.put("IN_id", accessibleIds);
			} else {
				// 如果没有权限记录，返回空结果
				searchParams.put("EQ_id", -1L); // 使用不存在的ID确保返回空结果
			}
		}else {
			// 非观察者用户：按原有权限逻辑处理
			Map<String, Object> mapQuery = Maps.newHashMap();
			mapQuery.put("EQ_userName", principal.getUsername());

			if (!(permissionsService.query(mapQuery, null).size() > 0)) {
				// 用户没有特殊权限，只能查看自己的记录
				searchParams.put("EQ_userName", principal.getUsername());
			}

			// 检查用户是否有管理权限（Board权限）
			Map<String, Object> map1Query = Maps.newHashMap();
			map1Query.put("EQ_manageName", principal.getUsername());
			List<Board> boardList = boardService.query(map1Query, null);

			if (boardList.size() > 0) {
				String attachedName = boardList.get(0).getAttachedName();
				List<String> gatherList = attachedName != null ? Arrays.asList(attachedName.split(",")) : new ArrayList<>();

				// 删除键为 "EQ_userName" 的条目
				searchParams.remove("EQ_userName");
				searchParams.put("IN_userName", gatherList);
			}
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
			SecretKey secretKey = EncryptionUtil.stringToSecretKey(s.getKeyy());
            try {
				if(s.getProtect()==0){
					String decrypt = EncryptionUtil.decrypt(s.getLink(), secretKey);
					s.setLink(decrypt);
				}
			} catch (Exception e) {
                throw new RuntimeException("解密失败");
            }
            s.setUserName(userMap.get(s.getUserName()));
		});
		dmCenterJsonListResult.setRows(rows);
        try {
            dmCountryService.dmCenterRedis();
			dmCountryService.dmConditionRedis();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dmCenterJsonListResult;
	}

	/**
	 * 安全清空目录，保留子目录
	 * @param clearStr 要清空的目录路径
	 * @param dmServer 服务器信息
	 */
	private void safeClearDirectory(String clearStr, DmServer dmServer) {
		try {
			// 解析域名信息
			String relativePath = clearStr.replace("/www/wwwroot/", "");
			String[] pathParts = relativePath.split("/");

			if (pathParts.length >= 2) {
				String domain = pathParts[0];
				String secondaryDomain = pathParts[1];
				// 检查是否存在子目录
				if (hasSubDirectories(domain, secondaryDomain)) {
					// 存在子目录，使用安全清空策略
					safeClearDirectoryWithSubDirs(clearStr, dmServer);
				} else {
					// 不存在子目录，使用原有的清空策略
					RemoteFileOperationsUtil.clearDirectory(clearStr, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
				}
			}else {
				// 路径格式不正确，使用原有策略
				RemoteFileOperationsUtil.clearDirectory(clearStr, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
			}
		}catch (Exception e){
			log.error("安全清空目录失败",e);
			throw new RuntimeException("安全清空目录失败,请检查服务器配置检查二级域名下是否有三级域名，或联系管理员");
			// 降级到原有策略
			// RemoteFileOperationsUtil.clearDirectory(clearStr, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
		}
	}
	/**
	 * 检查目标路径是否包含子目录
	 * @param domain 主域名
	 * @param secondaryDomain 二级域名
	 * @return 如果存在子目录返回true，否则返回false
	 */
	private boolean hasSubDirectories(String domain, String secondaryDomain) {
		try {
			// 构建完整的域名路径
			String fullDomain = domain + "/" + secondaryDomain;

			// 查询数据库中是否存在以当前域名开头的子目录
			Map<String, Object> searchParams = Maps.newHashMap();
			searchParams.put("LIKE_accessAddress", fullDomain + "/%");

			List<Link> subLinks = linkService.query(searchParams, null);

			// 过滤出真正的子目录（排除当前目录本身）
			boolean hasSubDirs = subLinks.stream()
					.anyMatch(link -> {
						String accessAddress = link.getAccessAddress();
						// 检查是否是直接子目录（只有一层额外的路径）
						if (accessAddress.startsWith(fullDomain + "/")) {
							String relativePath = accessAddress.substring(fullDomain.length() + 1);
							// 不包含额外的斜杠，说明是直接子目录
							return !relativePath.contains("/");
						}
						return false;
					});

			return hasSubDirs;
		} catch (Exception e) {
			log.error("检查子目录失败: domain={}, secondaryDomain={}", domain, secondaryDomain, e);
			return false;
		}
	}
	/**
	 * 获取子目录列表
	 * @param clearStr 主目录路径
	 * @param dmServer 服务器信息
	 * @return 子目录路径列表
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
//	private List<String> getSubDirectories(String clearStr, DmServer dmServer) {
//		List<String> subDirectories = new ArrayList<>();
//		try {
//			// 查询数据库中该域名下的所有子目录
//			String relativePath = clearStr.replace("/www/wwwroot/", "");
//			String[] pathParts = relativePath.split("/");
//
//			if (pathParts.length >= 2) {
//				String domain = pathParts[0];
//				String secondaryDomain = pathParts[1];
//				String fullDomain = domain + "/" + secondaryDomain;
//
//				// 查询数据库中的子目录
//				Map<String, Object> searchParams = Maps.newHashMap();
//				searchParams.put("LIKE_accessAddress", fullDomain + "/%");
//
//				List<Link> subLinks = linkService.query(searchParams, null);
//
//				for (Link subLink : subLinks) {
//					String subPath = subLink.getAccessAddress();
//					// 构建完整的子目录路径
//					String fullSubPath = "/www/wwwroot/" + subPath;
//					subDirectories.add(fullSubPath);
//				}
//			}
//		} catch (Exception e) {
//			log.error("获取子目录列表失败", e);
//		}
//		return subDirectories;
//	}
	/**
	 * 生成唯一的备份路径（改进版本）
	 * @param originalPath 原始路径
	 * @return 备份路径
	 */
	private String generateBackupPath(String originalPath) {
		// 生成时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());

		// 生成随机字符串
		String randomStr = UUID.randomUUID().toString().substring(0, 8);

		// 构建备份路径，使用更安全的路径格式
		String safePath = originalPath.replace("/", "_").replace(" ", "_");
		String backupDir = "/www/wwwroot/acooly/" + timestamp + "_" + randomStr;
		String backupPath = backupDir + "/" + safePath;

		return backupPath;
	}

	/**
	 * 备份子目录内容（使用cp命令）
	 * @param subDirectories 子目录路径列表
	 * @param dmServer 服务器信息
	 * @return 备份路径映射（原路径 -> 备份路径）
	 */
	private Map<String, String> backupSubDirectories(List<String> subDirectories, DmServer dmServer) {
		Map<String, String> backupPaths = new HashMap<>();

		try {
			for (String subDir : subDirectories) {
				// 生成唯一的备份路径
				String backupPath = generateBackupPath(subDir);

				// 先创建备份目录
				String createBackupDirCommand = "mkdir -p " + backupPath;
				RemoteFileOperationsUtil.executeCommand(createBackupDirCommand, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
				RemoteFileOperationsUtil.copyFiles(subDir,backupPath, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());

//				// 使用cp命令完整复制子目录
//				String backupCommand = String.format(
//						"cp -r %s/* %s/",
//						subDir,
//						backupPath
//				);
//
//				RemoteFileOperationsUtil.executeCommand(backupCommand, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());

				backupPaths.put(subDir, backupPath);
				log.info("备份子目录成功: {} -> {}", subDir, backupPath);
			}
		} catch (Exception e) {
			log.error("备份子目录失败", e);
			throw new RuntimeException("备份子目录失败: " + e.getMessage());
		}

		return backupPaths;
	}

	/**
	 * 完整恢复子目录内容（使用cp命令）
	 * @param backupPaths 备份路径映射（原路径 -> 备份路径）
	 * @param dmServer 服务器信息
	 */
	private void restoreSubDirectories(Map<String, String> backupPaths, DmServer dmServer) {
		try {
			for (Map.Entry<String, String> entry : backupPaths.entrySet()) {
				String originalPath = entry.getKey();
				String backupPath = entry.getValue();

				// 先确保目标目录存在
				String createTargetDirCommand = "mkdir -p " + originalPath;
				RemoteFileOperationsUtil.executeCommand(createTargetDirCommand, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());

				// 使用cp命令完整恢复子目录内容
				String restoreCommand = String.format(
						"cp -r %s/* %s/",
						backupPath,
						originalPath
				);

				RemoteFileOperationsUtil.executeCommand(restoreCommand, dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());

				log.info("恢复子目录成功: {} <- {}", originalPath, backupPath);
			}
		} catch (Exception e) {
			log.error("恢复子目录失败", e);
			throw new RuntimeException("恢复子目录失败: " + e.getMessage());
		}
	}
	/**
	 * 异步删除备份内容（改进版本）
	 * @param backupPaths 备份路径映射（原路径 -> 备份路径）
	 * @param dmServer 服务器信息
	 */
	@Async
	public void asyncDeleteBackupContent(Map<String, String> backupPaths, DmServer dmServer) {
		SSHConnectionManager sshManager = null;
		try {
			// 建立 SSH 连接
			sshManager = new SSHConnectionManager(dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
			sshManager.connect();
			
			for (Map.Entry<String, String> entry : backupPaths.entrySet()) {
				String backupPath = entry.getValue();

				// 先验证备份目录存在
				String checkResult = sshManager.executeCommand("[ -d \"" + backupPath + "\" ] && echo \"exists\" || echo \"not exists\"");
				
				if (checkResult.contains("exists")) {
					// 删除备份目录及其所有内容
					sshManager.executeCommand("rm -rf " + backupPath);
					log.info("异步删除备份内容成功: {}", backupPath);
				} else {
					log.warn("备份目录不存在，跳过删除: {}", backupPath);
				}
			}
		} catch (Exception e) {
			log.error("异步删除备份内容失败", e);
			// 异步删除失败不影响主流程，只记录日志
		} finally {
			// 关闭 SSH 连接
			if (sshManager != null) {
				sshManager.disconnect();
			}
		}
	}
	/**
	 * 安全清空目录，保留子目录结构但清空内容（连接复用版本）
	 * @param clearStr 要清空的目录路径
	 * @param dmServer 服务器信息
	 */
	private void safeClearDirectoryWithSubDirs(String clearStr, DmServer dmServer) {
		Map<String, String> backupPaths = new HashMap<>();
		SSHConnectionManager sshManager = null;
		
		try {
			// 建立 SSH 连接
			sshManager = new SSHConnectionManager(dmServer.getUsername(), dmServer.getPassword(), dmServer.getIp());
			sshManager.connect();
			
			// 第一步：获取所有子目录的路径
			List<String> subDirectories = getSubDirectories(clearStr, dmServer);
			
			if (subDirectories.isEmpty()) {
				// 没有子目录，直接清空
				sshManager.executeCommand("rm -rf " + clearStr + "/*");
				return;
			}
			
			// 第二步：完整备份子目录内容
			backupPaths = backupSubDirectoriesWithConnection(subDirectories, sshManager);
			
			// 第三步：验证备份是否成功
//			for (Map.Entry<String, String> entry : backupPaths.entrySet()) {
//				String originalPath = entry.getKey();
//				String backupPath = entry.getValue();
//
//				if (!verifyBackupWithConnection(originalPath, backupPath, sshManager)) {
//					throw new RuntimeException("备份验证失败: " + originalPath);
//				}
//			}
//
			// 第四步：清空主目录（不包括子目录）
			String clearMainDirCommand = String.format(
					"find %s -maxdepth 1 -type f -delete && find %s -maxdepth 1 -type d ! -name '.' ! -name '..' | grep -v '^%s$' | xargs -I {} rm -rf {}",
					clearStr, clearStr, clearStr
			);
			sshManager.executeCommand(clearMainDirCommand);
			
			// 第五步：完整恢复子目录内容
			restoreSubDirectoriesWithConnection(backupPaths, sshManager);
			
			// 第六步：验证恢复是否成功
//			for (Map.Entry<String, String> entry : backupPaths.entrySet()) {
//				String originalPath = entry.getKey();
//				if (!verifyRestoreWithConnection(originalPath, sshManager)) {
//					throw new RuntimeException("恢复验证失败: " + originalPath);
//				}
//			}
			
			// 第七步：异步删除备份内容
			asyncDeleteBackupContent(backupPaths, dmServer);
			
		} catch (Exception e) {
			log.error("安全清空目录失败", e);
			
			// 如果恢复失败，尝试清理备份
			if (!backupPaths.isEmpty()) {
				try {
					asyncDeleteBackupContent(backupPaths, dmServer);
				} catch (Exception cleanupException) {
					log.error("清理备份失败", cleanupException);
				}
			}
			
			throw new RuntimeException("安全清空目录失败: " + e.getMessage());
		} finally {
			// 关闭 SSH 连接
			if (sshManager != null) {
				sshManager.disconnect();
			}
		}
	}
	/**
	 * 完整备份子目录内容（复用现有连接）
	 * @param subDirectories 子目录路径列表
	 * @param sshManager SSH连接管理器
	 * @return 备份路径映射（原路径 -> 备份路径）
	 */
	private Map<String, String> backupSubDirectoriesWithConnection(List<String> subDirectories, SSHConnectionManager sshManager) {
		Map<String, String> backupPaths = new HashMap<>();
		
		try {
			for (String subDir : subDirectories) {
				// 生成唯一的备份路径
				String backupPath = generateBackupPath(subDir);
				
				// 创建备份目录
				sshManager.executeCommand("mkdir -p " + backupPath);
				
				// 使用cp命令完整复制子目录
				sshManager.executeCommand("cp -r " + subDir + "/* " + backupPath + "/");
				
				backupPaths.put(subDir, backupPath);
				log.info("备份子目录成功: {} -> {}", subDir, backupPath);
			}
		} catch (Exception e) {
			log.error("备份子目录失败", e);
			throw new RuntimeException("备份子目录失败: " + e.getMessage());
		}
		
		return backupPaths;
	}

	/**
	 * 完整恢复子目录内容（复用现有连接）
	 * @param backupPaths 备份路径映射（原路径 -> 备份路径）
	 * @param sshManager SSH连接管理器
	 */
	private void restoreSubDirectoriesWithConnection(Map<String, String> backupPaths, SSHConnectionManager sshManager) {
		try {
			for (Map.Entry<String, String> entry : backupPaths.entrySet()) {
				String originalPath = entry.getKey();
				String backupPath = entry.getValue();
				
				// 确保目标目录存在
				sshManager.executeCommand("mkdir -p " + originalPath);
				
				// 使用cp命令完整恢复子目录内容
				sshManager.executeCommand("cp -r " + backupPath + "/* " + originalPath + "/");
				
				log.info("恢复子目录成功: {} <- {}", originalPath, backupPath);
			}
		} catch (Exception e) {
			log.error("恢复子目录失败", e);
			throw new RuntimeException("恢复子目录失败: " + e.getMessage());
		}
	}

	/**
	 * 验证备份是否成功（复用现有连接）
	 * @param originalPath 原始路径
	 * @param backupPath 备份路径
	 * @param sshManager SSH连接管理器
	 * @return 是否备份成功
	 */
	private boolean verifyBackupWithConnection(String originalPath, String backupPath, SSHConnectionManager sshManager) {
		try {
			// 检查备份目录是否存在
			String checkBackupResult = sshManager.executeCommand("[ -d \"" + backupPath + "\" ] && echo \"exists\" || echo \"not exists\"");
			
			if (!checkBackupResult.contains("exists")) {
				log.error("备份目录不存在: {}", backupPath);
				return false;
			}
			
			// 检查备份目录是否为空
			String checkEmptyResult = sshManager.executeCommand("[ -z \"$(ls -A " + backupPath + ")\" ] && echo \"empty\" || echo \"not empty\"");
			
			if (checkEmptyResult.contains("empty")) {
				log.error("备份目录为空: {}", backupPath);
				return false;
			}
			
			// 检查备份目录中的文件数量是否与原始目录一致
			String originalCount = sshManager.executeCommand("find " + originalPath + " -type f | wc -l");
			String backupCount = sshManager.executeCommand("find " + backupPath + " -type f | wc -l");
			
			if (!originalCount.trim().equals(backupCount.trim())) {
				log.error("备份文件数量不匹配: 原始={}, 备份={}", originalCount.trim(), backupCount.trim());
				return false;
			}
			
			log.info("备份验证成功: {} -> {}", originalPath, backupPath);
			return true;
		} catch (Exception e) {
			log.error("验证备份失败: {}", originalPath, e);
			return false;
		}
	}

	/**
	 * 验证恢复是否成功（复用现有连接）
	 * @param originalPath 原始路径
	 * @param sshManager SSH连接管理器
	 * @return 是否恢复成功
	 */
	private boolean verifyRestoreWithConnection(String originalPath, SSHConnectionManager sshManager) {
		try {
			// 检查恢复目录是否存在
			String checkRestoreResult = sshManager.executeCommand("[ -d \"" + originalPath + "\" ] && echo \"exists\" || echo \"not exists\"");
			
			if (!checkRestoreResult.contains("exists")) {
				log.error("恢复目录不存在: {}", originalPath);
				return false;
			}
			
			// 检查恢复目录是否为空
			String checkEmptyResult = sshManager.executeCommand("[ -z \"$(ls -A " + originalPath + ")\" ] && echo \"empty\" || echo \"not empty\"");
			
			if (checkEmptyResult.contains("empty")) {
				log.error("恢复目录为空: {}", originalPath);
				return false;
			}
			
			// 检查是否有文件存在
			String filesResult = sshManager.executeCommand("find " + originalPath + " -type f | head -1");
			
			if (filesResult.trim().isEmpty()) {
				log.error("恢复目录中没有文件: {}", originalPath);
				return false;
			}
			
			log.info("恢复验证成功: {}", originalPath);
			return true;
		} catch (Exception e) {
			log.error("验证恢复失败: {}", originalPath, e);
			return false;
		}
	}


}
