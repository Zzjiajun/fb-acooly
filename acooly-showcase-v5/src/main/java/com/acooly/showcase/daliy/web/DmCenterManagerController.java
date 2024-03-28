/*
* acooly.cn Inc.
* Copyright (c) 2024 All Rights Reserved.
* create by acooly
* date:2024-03-26
*/
package com.acooly.showcase.daliy.web;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.Utils.RemoteFileOperationsUtil;
import com.acooly.showcase.daliy.entity.*;
import com.acooly.showcase.daliy.service.*;
import com.acooly.showcase.link.entity.LinkInt;
import com.acooly.showcase.link.entity.LinkSrcs;
import com.acooly.showcase.link.service.LinkIntService;
import com.acooly.showcase.link.service.LinkSrcsService;
import com.google.common.base.Preconditions;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.ResponseBody;

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
		Map<String, String> collected = showList.stream().collect(Collectors.toMap(user -> user.getRegion() + "  :  " + user.getSerialNumber(), DmShow::getDomain));
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

	@Override
	@Async
	protected DmCenter onSave(HttpServletRequest request, HttpServletResponse response, Model model, DmCenter entity, boolean isCreate) throws Exception {
		List<String> list = Arrays.asList(entity.getPixel().split("\n"));
		User principal = (User) SecurityUtils.getSubject().getPrincipal();
		String str=entity.getDomain() + "/" + entity.getSecondaryDomain();
		entity.setUserName(principal.getUsername());
		entity.setClicksNumber(0);
		entity.setVisitsNumber(0);

		//保存时
		if (isCreate){
			if (entity.getDisplayOption()==2){
				entity.setPixel("");
				entity.setSerialNumber("");
			}
			RemoteFileOperationsUtil.clearDirectory("/www/wwwroot/" + str);
			//保存链接到链接对应表
			Map<String, Object> map = Maps.newHashMap();
			map.put("EQ_domainName",str);
			List<DmDomain> query = dmDomainService.query(map, null);
			if (!query.isEmpty()){
				DmDomain dmDomain = query.get(0);
				dmDomain.setLink(entity.getLink());
				dmDomainService.update(dmDomain);
			}else {
				DmDomain dmDomain = new DmDomain();
				dmDomain.setUserName(principal.getUsername());
				dmDomain.setLink(entity.getLink());
				dmDomain.setDomainName(str);
				dmDomainService.save(dmDomain);
			}
			//落地页
			if (entity.getDisplayOption()==1){
				Preconditions.checkNotNull(entity.getSerialNumber(), "落地页模版不能为空");
				Preconditions.checkNotNull(entity.getPixel(), "像素Id不能为空");
				//先把落地页文件复制过去  先清空
				boolean b = RemoteFileOperationsUtil.copyFiles("/www/wwwroot/" + entity.getSerialNumber(), "/www/wwwroot/" + str);
				if (!b){
					throw new NullPointerException("模版复制到"+ str+"失败");
				}




				//保存像素id
				Map<String, Object> map2 = Maps.newHashMap();
				map2.put("EQ_domain",str);
				List<DmPixel> query3 = dmPixelService.query(map2, null);
				if (!query3.isEmpty()){
					query3.forEach(s->{
						dmDomainService.removeById(s.getId());
					});
				}
				LinkedList<DmPixel> dmPixelList = new LinkedList<>();
				list.forEach(s->{
					DmPixel dmPixel = new DmPixel();
					dmPixel.setPixelId(s);
					dmPixel.setDomain(str);
					dmPixelList.add(dmPixel);
				});
				dmPixelService.saves(dmPixelList);
			}else {
				boolean b=RemoteFileOperationsUtil.copyFiles("/www/wwwroot/projectgame.top/03","/www/wwwroot/" + str);
				if (!b){
					throw new NullPointerException("模版复制到"+ str+"失败");
				}
			}

		}else {
			//修改时
			DmCenter dmCenterOld = this.getEntityService().get(entity.getId());
			Map<String, Object> map = Maps.newHashMap();
			map.put("EQ_domainName",dmCenterOld.getDomain()+"/"+dmCenterOld.getSecondaryDomain());
			List<DmDomain> query = dmDomainService.query(map, null);



			//更换了链接
			if (!dmCenterOld.getLink().equals(entity.getLink())){
				DmDomain dmDomain = query.get(0);
				dmDomain.setLink(entity.getLink());
				dmDomainService.update(dmDomain);
			}

			//更换了广告形式
			if (dmCenterOld.getDisplayOption()!=entity.getDisplayOption()){
				RemoteFileOperationsUtil.clearDirectory("/www/wwwroot/" + str);
				if (entity.getDisplayOption()==1){
					if (entity.getPixel().isEmpty()){
						throw new NullPointerException("像素Id不能为空");
					}
					Preconditions.checkNotNull(entity.getSerialNumber(), "落地页模版不能为空");
					Preconditions.checkNotNull(entity.getPixel(), "像素Id不能为空");
					//先把落地页文件复制过去  先清空
					boolean b = RemoteFileOperationsUtil.copyFiles("/www/wwwroot/" + entity.getSerialNumber(), "/www/wwwroot/" + str);
					if (!b){
						throw new NullPointerException("模版复制到"+ str+"失败");
					}
					//保存像素id
					LinkedList<DmPixel> dmPixelList = new LinkedList<>();
					list.forEach(s->{
						DmPixel dmPixel = new DmPixel();
						dmPixel.setPixelId(s);
						dmPixel.setDomain(str);
						dmPixelList.add(dmPixel);
					});
					dmPixelService.saves(dmPixelList);
				}else {
					boolean b=RemoteFileOperationsUtil.copyFiles("/www/wwwroot/projectgame.top/03","/www/wwwroot/" + str);
					if (!b){
						throw new NullPointerException("模版复制到"+ str+"失败");
					}
					entity.setPixel("");
					entity.setSerialNumber("");
					//把像素id删除了
//					List<String> list1 = Arrays.asList(dmCenterOld.getPixel().split("\n"));
//					list1.forEach(s->{
//					});
					Map<String, Object> map4 = Maps.newHashMap();
					map4.put("EQ_domain",dmCenterOld.getDomain()+"/"+dmCenterOld.getSecondaryDomain());
					List<DmPixel> dmPixels = dmPixelService.query(map4, null);
					dmPixels.forEach(s->{
						dmPixelService.remove(s);
					});

				}
			}


			//更换了模版地址
			if (entity.getDisplayOption()==1){
				if (!dmCenterOld.getSerialNumber().equals(entity.getSerialNumber())){
					boolean b = RemoteFileOperationsUtil.copyFiles("/www/wwwroot/" + entity.getSerialNumber(), "/www/wwwroot/" + str);
					if (!b){
						throw new NullPointerException("模版复制到"+ str+"失败");
					}
				}
			}


			//更换像素id
			if (!dmCenterOld.getPixel().equals(entity.getPixel())){
				Map<String, Object> map1 = Maps.newHashMap();
				map1.put("EQ_domain",str);
				List<DmPixel> query1 = dmPixelService.query(map1, null);
				if (!query1.isEmpty()){
					query1.forEach(s->{
						dmPixelService.removeById(s.getId());
					});
				}
				list.forEach(s->{
					DmPixel dmPixel = new DmPixel();
					dmPixel.setPixelId(s);
					dmPixel.setDomain(str);
					dmPixelService.save(dmPixel);
				});
			}


			//如果更换了域名
			if (!(dmCenterOld.getDomain()+"/"+dmCenterOld.getSecondaryDomain()).equals(str)){
				RemoteFileOperationsUtil.clearDirectory("/www/wwwroot/" + str);
				//把之前的链接表删除了  添加新的 并且查看这个域名有没有 已经有了  有了修改 没有删除
				dmDomainService.removeById(query.get(0).getId());
				map.put("EQ_domainName",str);
				List<DmDomain> query1 = dmDomainService.query(map, null);
				if (!query1.isEmpty()){
					DmDomain dmDomain = query1.get(0);
					dmDomain.setLink(entity.getLink());
					dmDomainService.update(dmDomain);
				}else {
					DmDomain dmDomain = new DmDomain();
					dmDomain.setUserName(principal.getUsername());
					dmDomain.setLink(entity.getLink());
					dmDomain.setDomainName(str);
					dmDomainService.save(dmDomain);
				}


				//是落地页还是单刀
				if (entity.getDisplayOption()==1){
					Preconditions.checkNotNull(entity.getSerialNumber(), "落地页模版不能为空");
					Preconditions.checkNotNull(entity.getPixel(), "像素Id不能为空");
					//先把落地页文件复制过去  先清空
					boolean b = RemoteFileOperationsUtil.copyFiles("/www/wwwroot/" + entity.getSerialNumber(), "/www/wwwroot/" + str);
					if (!b){
						throw new NullPointerException("模版复制到"+ str+"失败");
					}
					//保存像素id
					LinkedList<DmPixel> dmPixelList = new LinkedList<>();
					list.forEach(s->{
						DmPixel dmPixel = new DmPixel();
						dmPixel.setPixelId(s);
						dmPixel.setDomain(str);
						dmPixelList.add(dmPixel);
					});
					dmPixelService.saves(dmPixelList);
				}else {
					boolean b=RemoteFileOperationsUtil.copyFiles("/www/wwwroot/projectgame.top/03","/www/wwwroot/" + str);
					if (!b){
						throw new NullPointerException("模版复制到"+ str+"失败");
					}
				}
			}
		}










		return super.onSave(request, response, model, entity, isCreate);
	}


	@Override
	protected void doRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids) throws Exception {
		if (ids != null && ids.length != 0) {
			if (ids.length == 1) {
				DmCenter dmCenter = this.getEntityService().get(ids[0]);
				String str=dmCenter.getDomain() + "/" + dmCenter.getSecondaryDomain();

				RemoteFileOperationsUtil.clearDirectory("/www/wwwroot/" + str);


				Map<String, Object> map1 = Maps.newHashMap();
				map1.put("EQ_domain",str);
				List<DmPixel> query1 = dmPixelService.query(map1, null);
				if (!query1.isEmpty()){
					query1.forEach(s->{
						dmPixelService.remove(s);
					});
				}
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
		jsonResult.setMessage("清除浏览记录成功");
		return jsonResult;
	}
}
