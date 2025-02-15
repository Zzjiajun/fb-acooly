/*
* acooly.cn Inc.
* Copyright (c) 2025 All Rights Reserved.
* create by acooly
* date:2025-02-14
*/
package com.acooly.showcase.daliy.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.utils.Encodes;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.showcase.daliy.entity.DmSieve;
import com.acooly.showcase.daliy.service.DmSieveService;

import com.google.common.collect.Maps;

/**
 * dm_sieve 管理控制器
 *
 * @author acooly
 * @date 2025-02-14 17:32:44
 */
@Controller
@RequestMapping(value = "/manage/showcase/daily/dmSieve")
public class DmSieveManagerController extends AbstractJsonEntityController<DmSieve, DmSieveService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private DmSieveService dmSieveService;

	/** 导出标题 */
	@Override
	protected List<String> getExportTitles() {
		return Lists.newArrayList( "群名", "业务", "电话号", "是否股民", "意向"
				, "备注","日期");
	}

	/** 导出数据修改 */
	@Override
	protected List<Object> doExportRow(DmSieve entity) {
		String decide = "";
		if (entity.getDecide() == 1){
			decide = "是";
		}else {
			decide = "否";
		}
		Date createTime = entity.getCreateTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedCreateTime = dateFormat.format(createTime);
		return Lists.newArrayList(entity.getName(),entity.getBusiness(),entity.getPhone()
		,decide,entity.getIntent(),entity.getRemark(),formattedCreateTime);
	}

	/**
	 * 导入处理
	 *
	 * <p>导入前，需要准备模板，用户更加模板上传后，在这里处理每行数据转换为entity
	 *
	 * <p>列说明（按顺序）：用户名,姓名,身份证号码,手机,邮箱
	 */
	@Override
	protected DmSieve doImportEntity(List<String> fields) {
		String decide = fields.get(4);
		if ("是".equals(decide)) {
			decide = "1";
		}else {
			decide = "0";
		}
		DmSieve dmSieve = new DmSieve();
		dmSieve.setDecide(Integer.valueOf(decide));
		dmSieve.setName(fields.get(0));
		dmSieve.setBusiness(fields.get(1));
		dmSieve.setCountry(fields.get(2));
		dmSieve.setPhone(fields.get(3));
		dmSieve.setRemark(fields.get(5));
		dmSieve.setIntent(fields.get(4));
		return dmSieve;
	}


	@Override
	protected void afterUnmarshal(List<DmSieve> entities) {
		if (!entities.isEmpty()) {
			entities.remove(0);
		}
	}

	@Override
	protected void doExportExcelHeader(HttpServletRequest request, HttpServletResponse response) {
		String domainName = "导出数据";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment");
		response.setHeader("Content-Disposition", "filename=\"" + Encodes.urlEncode(domainName) + ".xlsx\"");
	}
}
