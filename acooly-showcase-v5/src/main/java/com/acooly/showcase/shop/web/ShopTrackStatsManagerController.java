/*
* acooly.cn Inc.
* Copyright (c) 2026 All Rights Reserved.
* create by acooly
* date:2026-01-04
*/
package com.acooly.showcase.shop.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.core.common.web.MappingMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.showcase.shop.entity.ShopTrackStats;
import com.acooly.showcase.shop.service.ShopTrackStatsService;
import com.acooly.showcase.shop.dto.SiteStatsDTO;
import com.acooly.showcase.shop.dto.PageStatsDTO;
import com.acooly.showcase.shop.dto.IpStatsDTO;
import com.acooly.showcase.shop.dto.ProductStatsDTO;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

/**
 * 访问统计汇总表（提升查询性能） 管理控制器
 *
 * @author acooly
 * @date 2026-01-04 19:04:50
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/shop/shopTrackStats")
public class ShopTrackStatsManagerController extends AbstractJsonEntityController<ShopTrackStats, ShopTrackStatsService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ShopTrackStatsService shopTrackStatsService;

	/**
	 * 查询网站总体统计
	 *
	 * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
	 * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @return 网站统计信息
	 */
	@RequestMapping("/siteStatsJson.html")
	@ResponseBody
	public JsonResult getSiteStats(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {

		JsonResult result = new JsonResult();
		this.allow(request, response, MappingMethod.list);
		
		try {
			SiteStatsDTO stats = shopTrackStatsService.getSiteStats(date, startDate, endDate);
			// JsonResult的data是Map类型，使用appendData将对象放入Map
			result.appendData("stats", stats);
			result.setSuccess(true);
			result.setMessage("查询成功");
		} catch (Exception e) {
			log.error("查询网站统计失败: date={}, startDate={}, endDate={}, error={}", 
				date, startDate, endDate, e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("查询失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 查询页面统计列表
	 *
	 * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
	 * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @param pageType 页面类型（可选），如果为null则查询所有类型
	 * @return 页面统计列表
	 */
	@RequestMapping("/pageStatsJson.html")
	@ResponseBody
	public JsonListResult<PageStatsDTO> getPageStats(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String pageType) {
		
		JsonListResult<PageStatsDTO> result = new JsonListResult<>();
		this.allow(request, response, MappingMethod.list);
		
		try {
			List<PageStatsDTO> list = shopTrackStatsService.getPageStats(date, startDate, endDate, pageType);
			result.setRows(list);
			result.setTotal((long) (list != null ? list.size() : 0));
			result.setSuccess(true);
			result.setMessage("查询成功");
		} catch (Exception e) {
			log.error("查询页面统计失败: date={}, startDate={}, endDate={}, pageType={}, error={}", 
				date, startDate, endDate, pageType, e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("查询失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 查询IP分布统计
	 *
	 * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
	 * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @param limit 返回数量限制，默认20
	 * @return IP分布统计列表
	 */
	@RequestMapping("/ipStatsJson.html")
	@ResponseBody
	public JsonListResult<IpStatsDTO> getIpStats(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(defaultValue = "20") Integer limit) {
		
		JsonListResult<IpStatsDTO> result = new JsonListResult<>();
		this.allow(request, response, MappingMethod.list);
		
		try {
			List<IpStatsDTO> list = shopTrackStatsService.getIpStats(date, startDate, endDate, limit);
			result.setRows(list);
			result.setTotal((long) (list != null ? list.size() : 0));
			result.setSuccess(true);
			result.setMessage("查询成功");
		} catch (Exception e) {
			log.error("查询IP统计失败: date={}, startDate={}, endDate={}, limit={}, error={}", 
				date, startDate, endDate, limit, e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("查询失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 查询商品浏览排行
	 *
	 * @param date 统计日期（格式：yyyy-MM-dd），如果为null则查询昨天
	 * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围查询
	 * @param limit 返回数量限制，默认20
	 * @param orderBy 排序字段：pv（访问量）或 avgDuration（平均停留时间），默认pv
	 * @return 商品统计列表
	 */
	@RequestMapping("/productRankingJson.html")
	@ResponseBody
	public JsonListResult<ProductStatsDTO> getProductRanking(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(defaultValue = "20") Integer limit,
			@RequestParam(defaultValue = "pv") String orderBy) {
		
		JsonListResult<ProductStatsDTO> result = new JsonListResult<>();
		this.allow(request, response, MappingMethod.list);
		
		try {
			List<ProductStatsDTO> list = shopTrackStatsService.getProductRanking(date, startDate, endDate, limit, orderBy);
			result.setRows(list);
			result.setTotal((long) (list != null ? list.size() : 0));
			result.setSuccess(true);
			result.setMessage("查询成功");
		} catch (Exception e) {
			log.error("查询商品排行失败: date={}, startDate={}, endDate={}, limit={}, orderBy={}, error={}", 
				date, startDate, endDate, limit, orderBy, e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("查询失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 手动触发统计聚合（测试/调试用）
	 * 
	 * 说明：
	 * - 正常情况下，统计聚合每天凌晨2点自动执行
	 * - 此接口用于测试或需要立即查看统计结果时手动触发
	 * - 建议：生产环境可以添加权限控制
	 */
	@RequestMapping("/aggregateJson.html")
	@ResponseBody
	public JsonResult manualAggregate(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String date) {
		
		JsonResult result = new JsonResult();
		this.allow(request, response, MappingMethod.list);
		
		try {
			log.info("🔧 [TRACK] 手动触发统计聚合: date={}", date);
			shopTrackStatsService.aggregateStats(date);
			String message = "统计聚合完成: " + (date != null ? date : "昨天");
			// JsonResult的data是Map类型，使用appendData将消息放入Map
			result.appendData("message", message);
			result.setSuccess(true);
			result.setMessage("统计聚合完成");
		} catch (Exception e) {
			log.error("❌ [TRACK] 手动触发统计聚合失败: date={}, error={}", date, e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("统计聚合失败: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * 清理统计缓存
	 *
	 * @param date 指定日期（格式：yyyy-MM-dd），如果为null则清理所有缓存
	 * @param startDate 开始日期（格式：yyyy-MM-dd），用于日期范围
	 * @param endDate 结束日期（格式：yyyy-MM-dd），用于日期范围
	 * @return 清理结果
	 */
	@RequestMapping("/clearCacheJson.html")
	@ResponseBody
	public JsonResult clearCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String date,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		
		JsonResult result = new JsonResult();
		this.allow(request, response, MappingMethod.list);
		
		try {
			log.info("🗑️ [CACHE] 手动清理统计缓存: date={}, startDate={}, endDate={}", date, startDate, endDate);
			shopTrackStatsService.clearCache(date, startDate, endDate);
			String message = "缓存清理完成";
			if (date != null) {
				message += ": " + date;
			} else if (startDate != null && endDate != null) {
				message += ": " + startDate + " ~ " + endDate;
			} else {
				message += ": 所有缓存";
			}
			result.appendData("message", message);
			result.setSuccess(true);
			result.setMessage("缓存清理完成");
		} catch (Exception e) {
			log.error("❌ [CACHE] 清理缓存失败: date={}, startDate={}, endDate={}, error={}", 
				date, startDate, endDate, e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage("缓存清理失败: " + e.getMessage());
		}
		
		return result;
	}

}
