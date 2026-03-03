/*
 * acooly.cn Inc.
 * Copyright (c) 2026 All Rights Reserved.
 * create by acooly
 * date:2026-01-04
 */
package com.acooly.showcase.shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.shop.service.ShopTrackStatsService;
import com.acooly.showcase.shop.service.ShopTrackLogService;
import com.acooly.showcase.shop.dao.ShopTrackStatsDao;
import com.acooly.showcase.shop.entity.ShopTrackStats;
import com.acooly.showcase.shop.entity.ShopTrackLog;
import com.acooly.showcase.shop.dto.SiteStatsDTO;
import com.acooly.showcase.shop.dto.PageStatsDTO;
import com.acooly.showcase.shop.dto.IpStatsDTO;
import com.acooly.showcase.shop.dto.ProductStatsDTO;
import com.acooly.showcase.shop.service.ShopProductsService;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.utils.RedisShopUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * 访问统计汇总表（提升查询性能） Service实现
 *
 * @author acooly
 * @date 2026-01-04 19:04:50
 */
@Slf4j
@Service("shopTrackStatsService")
public class ShopTrackStatsServiceImpl extends EntityServiceImpl<ShopTrackStats, ShopTrackStatsDao> implements ShopTrackStatsService {

    @Autowired
    private ShopTrackLogService shopTrackLogService;

    @Autowired(required = false)
    private ShopProductsService shopProductsService;

    @Autowired(required = false)
    private RedisShopUtil redisUtil;

    /**
     * 缓存前缀
     */
    private static final String CACHE_PREFIX = "shop.track.stats";

    /**
     * 构建缓存key
     */
    private String buildCacheKey(String type, String date, String startDate, String endDate) {
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            return redisUtil != null ? redisUtil.buildKey(CACHE_PREFIX, type, "range", startDate, endDate) 
                    : CACHE_PREFIX + "." + type + ".range." + startDate + "." + endDate;
        } else if (date != null && !date.isEmpty()) {
            return redisUtil != null ? redisUtil.buildKey(CACHE_PREFIX, type, "date", date)
                    : CACHE_PREFIX + "." + type + ".date." + date;
            } else {
                // 默认使用今天
                LocalDate today = LocalDate.now();
                String defaultDate = today.toString();
                return redisUtil != null ? redisUtil.buildKey(CACHE_PREFIX, type, "date", defaultDate)
                        : CACHE_PREFIX + "." + type + ".date." + defaultDate;
            }
    }

    /**
     * 获取缓存过期时间（根据日期判断）
     * - 今天的数据：5分钟（数据实时变化）
     * - 昨天的数据：30分钟
     * - 3天前的数据：2小时
     * - 更久的数据：24小时
     */
    private long getCacheExpireSeconds(String date, String startDate, String endDate) {
        LocalDate today = LocalDate.now();
        LocalDate queryDate;
        
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            // 日期范围：使用结束日期判断
            try {
                queryDate = LocalDate.parse(endDate);
            } catch (Exception e) {
                return 30 * 60; // 默认30分钟
            }
        } else if (date != null && !date.isEmpty()) {
            try {
                queryDate = LocalDate.parse(date);
            } catch (Exception e) {
                return 30 * 60; // 默认30分钟
            }
        } else {
            queryDate = today; // 默认今天
        }
        
        long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(queryDate, today);
        
        if (daysDiff == 0) {
            // 今天：5分钟
            return 5 * 60;
        } else if (daysDiff == 1) {
            // 昨天：30分钟
            return 30 * 60;
        } else if (daysDiff <= 3) {
            // 3天内：2小时
            return 2 * 60 * 60;
        } else {
            // 更久：24小时
            return 24 * 60 * 60;
        }
    }

    /**
     * 清除指定日期的缓存（内部方法）
     */
    protected void clearCacheU(String date, String startDate, String endDate) {
        if (redisUtil == null) {
            return;
        }
        
        try {
            // 清除网站统计缓存
            String siteKey = buildCacheKey("site", date, startDate, endDate);
            redisUtil.del(siteKey);
            
            // 清除页面统计缓存
            String pageKey = buildCacheKey("page", date, startDate, endDate);
            redisUtil.del(pageKey);
            
            // 清除IP统计缓存
            String ipKey = buildCacheKey("ip", date, startDate, endDate);
            redisUtil.del(ipKey);
            
            // 清除商品排行缓存
            String productKey = buildCacheKey("product", date, startDate, endDate);
            redisUtil.del(productKey);
            
            log.debug("🗑️ [CACHE] 已清除统计缓存: date={}, startDate={}, endDate={}", date, startDate, endDate);
        } catch (Exception e) {
            log.warn("清除缓存失败: {}", e.getMessage());
        }
    }

    @Override
    public void clearCache(String date, String startDate, String endDate) {
        if (redisUtil == null) {
            log.warn("Redis未配置，无法清理缓存");
            return;
        }
        
        try {
            if (date == null && startDate == null && endDate == null) {
                // 清理所有统计缓存
                String pattern = CACHE_PREFIX + "*";
                Set<String> keys = redisUtil.scan(pattern);
                if (keys != null && !keys.isEmpty()) {
                    long count = redisUtil.delete(keys.toArray(new String[0]));
                    log.info("🗑️ [CACHE] 已清理所有统计缓存，共 {} 个key", count);
                } else {
                    log.info("🗑️ [CACHE] 没有找到需要清理的缓存");
                }
            } else {
                // 清理指定日期或日期范围的缓存
                // 先清理基础缓存
                clearCacheU(date, startDate, endDate);
                
                // 同时清理可能存在的变体缓存（如不同limit、pageType等）
                String basePattern;
                if (startDate != null && endDate != null) {
                    // 日期范围：清理所有相关的缓存
                    basePattern = redisUtil.buildKey(CACHE_PREFIX, "*", "range", startDate, endDate) + "*";
                } else if (date != null) {
                    // 单日期：清理所有相关的缓存
                    basePattern = redisUtil.buildKey(CACHE_PREFIX, "*", "date", date) + "*";
                } else {
                    basePattern = CACHE_PREFIX + "*";
                }
                
                Set<String> keys = redisUtil.scan(basePattern);
                if (keys != null && !keys.isEmpty()) {
                    long count = redisUtil.delete(keys.toArray(new String[0]));
                    log.info("🗑️ [CACHE] 已清理日期范围缓存，共 {} 个key", count);
                }
            }
        } catch (Exception e) {
            log.error("❌ [CACHE] 清理缓存失败: date={}, startDate={}, endDate={}, error={}", 
                date, startDate, endDate, e.getMessage(), e);
            throw new RuntimeException("清理缓存失败: " + e.getMessage(), e);
        }
    }

    /**
     * 唯一性查询
     * uk_stat
     *
     * @param statType
     * @param statDate
     * @param entityId
     * @param entityKey
     * @param locale
     * @return
     */
    @Override
    public ShopTrackStats uniqueByStatTypeAndStatDateAndEntityIdAndEntityKeyAndLocale(String statType, Date statDate, Long entityId, String entityKey, String locale) {
        return getEntityDao().uniqueByStatTypeAndStatDateAndEntityIdAndEntityKeyAndLocale(statType, statDate, entityId, entityKey, locale);
    }

    @Override
    public SiteStatsDTO getSiteStats(String date, String startDate, String endDate) {
        // 尝试从缓存获取
        String cacheKey = buildCacheKey("site", date, startDate, endDate);
        if (redisUtil != null) {
            SiteStatsDTO cached = redisUtil.get(cacheKey, SiteStatsDTO.class);
            if (cached != null) {
                log.debug("✅ [CACHE] 从缓存获取网站统计: key={}", cacheKey);
                return cached;
            }
        }
        
        try {
            Date queryStartDate = null;
            Date queryEndDate = null;
            boolean isDateRange = false;
            
            // 优先使用日期范围查询
            if (startDate != null && !startDate.trim().isEmpty() && 
                endDate != null && !endDate.trim().isEmpty()) {
                queryStartDate = parseDate(startDate);
                queryEndDate = parseDate(endDate);
                if (queryStartDate != null && queryEndDate != null) {
                    isDateRange = true;
                }
            }
            
            // 如果没有日期范围，使用单日期查询
            if (!isDateRange) {
                Date queryDate = parseDate(date);
                if (queryDate == null) {
                    // 默认查询今天
                    LocalDate today = LocalDate.now();
                    queryDate = java.sql.Date.valueOf(today);
                }
                queryStartDate = queryDate;
                queryEndDate = queryDate;
            }

            SiteStatsDTO dto = new SiteStatsDTO();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            if (isDateRange) {
                dto.setDate(sdf.format(queryStartDate) + " ~ " + sdf.format(queryEndDate));
            } else {
                String dateStr = sdf.format(queryStartDate);
                dto.setDate(dateStr);
            }

            // 判断是否包含今天
            LocalDate today = LocalDate.now();
            LocalDate startLocalDate = new java.sql.Date(queryStartDate.getTime()).toLocalDate();
            LocalDate endLocalDate = new java.sql.Date(queryEndDate.getTime()).toLocalDate();
            boolean containsToday = !endLocalDate.isBefore(today);

            // 查询日志数据（用于计算页面类型统计）
            List<ShopTrackLog> allLogs;
            if (isDateRange) {
                allLogs = shopTrackLogService.findByDateRange(queryStartDate, queryEndDate, null);
            } else {
                allLogs = shopTrackLogService.findByDateAndPageType(queryStartDate, null);
            }
            
            // 如果是日期范围查询，或者包含今天，从日志表实时计算
            if (isDateRange || containsToday) {
                log.debug("📊 [TRACK] 实时查询数据: startDate={}, endDate={}", 
                    sdf.format(queryStartDate), sdf.format(queryEndDate));
                
                if (allLogs != null && !allLogs.isEmpty()) {
                    long pv = allLogs.size();
                    long uv = allLogs.stream()
                            .map(ShopTrackLog::getVisitorId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .count();
                    
                    long totalDuration = allLogs.stream()
                            .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                            .sum();
                    int avgDuration = pv > 0 ? (int) (totalDuration / pv) : 0;
                    
                    dto.setTotalPv(pv);
                    dto.setTotalUv(uv);
                    dto.setAvgDuration(avgDuration);
                    
                    // 计算独立IP数
                    long totalIp = allLogs.stream()
                            .map(ShopTrackLog::getIpAddress)
                            .filter(Objects::nonNull)
                            .filter(ip -> !ip.trim().isEmpty())
                            .distinct()
                            .count();
                    dto.setTotalIp(totalIp);
                    
                    // 计算跳出率：跳出率 = (只访问一个页面的会话数 / 总会话数) * 100
                    Map<String, List<ShopTrackLog>> sessionGroups = allLogs.stream()
                            .filter(log -> log.getSessionId() != null && !log.getSessionId().trim().isEmpty())
                            .collect(Collectors.groupingBy(ShopTrackLog::getSessionId));
                    
                    long totalSessions = sessionGroups.size();
                    long bounceSessions = sessionGroups.values().stream()
                            .filter(logs -> logs.size() == 1) // 只访问一个页面的会话
                            .count();
                    
                    if (totalSessions > 0) {
                        dto.setBounceRate((double) bounceSessions / totalSessions * 100);
                    } else {
                        dto.setBounceRate(0.0);
                    }
                } else {
                    dto.setTotalPv(0L);
                    dto.setTotalUv(0L);
                    dto.setAvgDuration(0);
                    dto.setTotalIp(0L);
                    dto.setBounceRate(0.0);
                }
            } else {
                // 历史单日查询：从汇总表查询（性能更好）
                log.debug("📊 [TRACK] 从汇总表查询历史数据: date={}", sdf.format(queryStartDate));
                ShopTrackStats stats = getEntityDao().findSiteStats(queryStartDate);
                
                if (stats != null) {
                    dto.setTotalPv(stats.getPv() != null ? stats.getPv() : 0L);
                    dto.setTotalUv(stats.getUv() != null ? stats.getUv() : 0L);
                    dto.setAvgDuration(stats.getAvgDuration() != null ? stats.getAvgDuration() : 0);
                    // 汇总表中没有IP数和跳出率，需要从日志计算
                    if (allLogs != null && !allLogs.isEmpty()) {
                        long totalIp = allLogs.stream()
                                .map(ShopTrackLog::getIpAddress)
                                .filter(Objects::nonNull)
                                .filter(ip -> !ip.trim().isEmpty())
                                .distinct()
                                .count();
                        dto.setTotalIp(totalIp);
                        
                        Map<String, List<ShopTrackLog>> sessionGroups = allLogs.stream()
                                .filter(log -> log.getSessionId() != null && !log.getSessionId().trim().isEmpty())
                                .collect(Collectors.groupingBy(ShopTrackLog::getSessionId));
                        
                        long totalSessions = sessionGroups.size();
                        long bounceSessions = sessionGroups.values().stream()
                                .filter(logs -> logs.size() == 1)
                                .count();
                        
                        if (totalSessions > 0) {
                            dto.setBounceRate((double) bounceSessions / totalSessions * 100);
                        } else {
                            dto.setBounceRate(0.0);
                        }
                    } else {
                        dto.setTotalIp(0L);
                        dto.setBounceRate(0.0);
                    }
                } else {
                    // 如果汇总表没有数据，尝试从日志表计算（兼容未聚合的历史数据）
                    if (allLogs != null && !allLogs.isEmpty()) {
                        long pv = allLogs.size();
                        long uv = allLogs.stream()
                                .map(ShopTrackLog::getVisitorId)
                                .filter(Objects::nonNull)
                                .distinct()
                                .count();
                        
                        long totalDuration = allLogs.stream()
                                .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                                .sum();
                        int avgDuration = pv > 0 ? (int) (totalDuration / pv) : 0;
                        
                        dto.setTotalPv(pv);
                        dto.setTotalUv(uv);
                        dto.setAvgDuration(avgDuration);
                        
                        // 计算独立IP数
                        long totalIp = allLogs.stream()
                                .map(ShopTrackLog::getIpAddress)
                                .filter(Objects::nonNull)
                                .filter(ip -> !ip.trim().isEmpty())
                                .distinct()
                                .count();
                        dto.setTotalIp(totalIp);
                        
                        // 计算跳出率
                        Map<String, List<ShopTrackLog>> sessionGroups = allLogs.stream()
                                .filter(log -> log.getSessionId() != null && !log.getSessionId().trim().isEmpty())
                                .collect(Collectors.groupingBy(ShopTrackLog::getSessionId));
                        
                        long totalSessions = sessionGroups.size();
                        long bounceSessions = sessionGroups.values().stream()
                                .filter(logs -> logs.size() == 1)
                                .count();
                        
                        if (totalSessions > 0) {
                            dto.setBounceRate((double) bounceSessions / totalSessions * 100);
                        } else {
                            dto.setBounceRate(0.0);
                        }
                    } else {
                        dto.setTotalPv(0L);
                        dto.setTotalUv(0L);
                        dto.setAvgDuration(0);
                        dto.setTotalIp(0L);
                        dto.setBounceRate(0.0);
                    }
                }
            }

            // 按页面类型统计（使用已查询的日志数据）
            if (allLogs != null && !allLogs.isEmpty()) {
                Map<String, SiteStatsDTO.PageTypeStats> pageTypeStatsMap = new HashMap<>();
                
                Map<String, List<ShopTrackLog>> pageTypeGroups = allLogs.stream()
                        .collect(Collectors.groupingBy(ShopTrackLog::getPageType));
                
                for (Map.Entry<String, List<ShopTrackLog>> entry : pageTypeGroups.entrySet()) {
                    String pageType = entry.getKey();
                    List<ShopTrackLog> logs = entry.getValue();
                    
                    SiteStatsDTO.PageTypeStats stats = new SiteStatsDTO.PageTypeStats();
                    stats.setPageType(pageType);
                    stats.setPv((long) logs.size());
                    stats.setUv(logs.stream()
                            .map(ShopTrackLog::getVisitorId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .count());
                    
                    long totalDuration = logs.stream()
                            .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                            .sum();
                    stats.setAvgDuration((int) (stats.getPv() > 0 ? totalDuration / stats.getPv() : 0));
                    
                    pageTypeStatsMap.put(pageType, stats);
                }
                
                dto.setPageTypeStats(pageTypeStatsMap);
                
                // 计算商品访问率：访问商品页面的独立访客数 / 总独立访客数 * 100
                // 使用productId不为null的记录来计算，更准确（因为商品页的pageType可能不是"product"）
                long productUv = allLogs.stream()
                        .filter(log -> log.getProductId() != null) // 有商品ID的记录
                        .map(ShopTrackLog::getVisitorId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count();
                
                if (dto.getTotalUv() > 0) {
                    dto.setProductVisitRate((double) productUv / dto.getTotalUv() * 100);
                } else {
                    dto.setProductVisitRate(0.0);
                }
                
                log.debug("📊 [TRACK] 商品访问率计算: 商品页UV={}, 总UV={}, 访问率={}%", 
                    productUv, dto.getTotalUv(), dto.getProductVisitRate());
                
                // 计算独立IP数
                long totalIp = allLogs.stream()
                        .map(ShopTrackLog::getIpAddress)
                        .filter(Objects::nonNull)
                        .filter(ip -> !ip.trim().isEmpty())
                        .distinct()
                        .count();
                dto.setTotalIp(totalIp);
                
                // 计算跳出率：跳出率 = (只访问一个页面的会话数 / 总会话数) * 100
                // 按sessionId分组，统计每个会话的页面访问数
                Map<String, List<ShopTrackLog>> sessionGroups = allLogs.stream()
                        .filter(log -> log.getSessionId() != null && !log.getSessionId().trim().isEmpty())
                        .collect(Collectors.groupingBy(ShopTrackLog::getSessionId));
                
                long totalSessions = sessionGroups.size();
                long bounceSessions = sessionGroups.values().stream()
                        .filter(logs -> logs.size() == 1) // 只访问一个页面的会话
                        .count();
                
                if (totalSessions > 0) {
                    dto.setBounceRate((double) bounceSessions / totalSessions * 100);
                } else {
                    dto.setBounceRate(0.0);
                }
                
                log.debug("📊 [TRACK] 跳出率计算: 总会话数={}, 跳出会话数={}, 跳出率={}%", 
                    totalSessions, bounceSessions, dto.getBounceRate());
            } else {
                dto.setPageTypeStats(new HashMap<>());
                dto.setProductVisitRate(0.0);
                dto.setTotalIp(0L);
                dto.setBounceRate(0.0);
            }

            return dto;
        } catch (Exception e) {
            log.error("查询网站统计失败: date={}, error={}", date, e.getMessage(), e);
            return new SiteStatsDTO();
        }
    }

    @Override
    public List<PageStatsDTO> getPageStats(String date, String startDate, String endDate, String pageType) {
        // 尝试从缓存获取（pageType作为key的一部分）
        String cacheKey = buildCacheKey("page", date, startDate, endDate) + (pageType != null ? "." + pageType : "");
        if (redisUtil != null) {
            List<PageStatsDTO> cached = redisUtil.getList(cacheKey, PageStatsDTO.class);
            if (cached != null) {
                log.debug("✅ [CACHE] 从缓存获取页面统计: key={}", cacheKey);
                return cached;
            }
        }
        
        try {
            Date queryStartDate = null;
            Date queryEndDate = null;
            boolean isDateRange = false;
            
            // 优先使用日期范围查询
            if (startDate != null && !startDate.trim().isEmpty() && 
                endDate != null && !endDate.trim().isEmpty()) {
                queryStartDate = parseDate(startDate);
                queryEndDate = parseDate(endDate);
                if (queryStartDate != null && queryEndDate != null) {
                    isDateRange = true;
                }
            }
            
            // 如果没有日期范围，使用单日期查询
            if (!isDateRange) {
                Date queryDate = parseDate(date);
                if (queryDate == null) {
                    // 默认查询今天
                    LocalDate today = LocalDate.now();
                    queryDate = java.sql.Date.valueOf(today);
                }
                queryStartDate = queryDate;
                queryEndDate = queryDate;
            }

            List<ShopTrackLog> logs;
            if (isDateRange) {
                logs = shopTrackLogService.findByDateRange(queryStartDate, queryEndDate, pageType);
            } else {
                logs = shopTrackLogService.findByDateAndPageType(queryStartDate, pageType);
            }
            
            if (logs == null || logs.isEmpty()) {
                return new ArrayList<>();
            }

            // 按pageKey分组统计
            Map<String, List<ShopTrackLog>> pageKeyGroups = logs.stream()
                    .filter(log -> log.getPageKey() != null)
                    .collect(Collectors.groupingBy(ShopTrackLog::getPageKey));

            List<PageStatsDTO> result = new ArrayList<>();
            
            for (Map.Entry<String, List<ShopTrackLog>> entry : pageKeyGroups.entrySet()) {
                String pageKey = entry.getKey();
                List<ShopTrackLog> pageLogs = entry.getValue();
                
                PageStatsDTO dto = new PageStatsDTO();
                dto.setPageKey(pageKey);
                
                if (!pageLogs.isEmpty()) {
                    dto.setPageType(pageLogs.get(0).getPageType());
                }
                
                dto.setPv((long) pageLogs.size());
                dto.setUv(pageLogs.stream()
                        .map(ShopTrackLog::getVisitorId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count());
                
                long totalDuration = pageLogs.stream()
                        .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                        .sum();
                dto.setTotalDuration(totalDuration);
                dto.setAvgDuration((int) (dto.getPv() > 0 ? totalDuration / dto.getPv() : 0));
                
                result.add(dto);
            }

            // 按PV排序
            result.sort((a, b) -> Long.compare(b.getPv(), a.getPv()));

            // 缓存结果
            if (redisUtil != null && result != null) {
                long expireSeconds = getCacheExpireSeconds(date, startDate, endDate);
                redisUtil.set(cacheKey, result, expireSeconds, TimeUnit.SECONDS);
                log.debug("💾 [CACHE] 缓存页面统计: key={}, expire={}秒", cacheKey, expireSeconds);
            }

            return result;
        } catch (Exception e) {
            log.error("查询页面统计失败: date={}, startDate={}, endDate={}, pageType={}, error={}", 
                date, startDate, endDate, pageType, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<IpStatsDTO> getIpStats(String date, String startDate, String endDate, Integer limit) {
        // 尝试从缓存获取（limit作为key的一部分）
        String cacheKey = buildCacheKey("ip", date, startDate, endDate) + "." + (limit != null ? limit : 20);
        if (redisUtil != null) {
            List<IpStatsDTO> cached = redisUtil.getList(cacheKey, IpStatsDTO.class);
            if (cached != null) {
                log.debug("✅ [CACHE] 从缓存获取IP统计: key={}", cacheKey);
                return cached;
            }
        }
        
        try {
            Date queryStartDate = null;
            Date queryEndDate = null;
            boolean isDateRange = false;
            
            // 优先使用日期范围查询
            if (startDate != null && !startDate.trim().isEmpty() && 
                endDate != null && !endDate.trim().isEmpty()) {
                queryStartDate = parseDate(startDate);
                queryEndDate = parseDate(endDate);
                if (queryStartDate != null && queryEndDate != null) {
                    isDateRange = true;
                }
            }
            
            // 如果没有日期范围，使用单日期查询
            if (!isDateRange) {
                Date queryDate = parseDate(date);
                if (queryDate == null) {
                    // 默认查询今天
                    LocalDate today = LocalDate.now();
                    queryDate = java.sql.Date.valueOf(today);
                }
                queryStartDate = queryDate;
                queryEndDate = queryDate;
            }

            if (limit == null || limit <= 0) {
                limit = 20;
            }

            List<ShopTrackLog> logs;
            if (isDateRange) {
                logs = shopTrackLogService.findByDateRange(queryStartDate, queryEndDate, null);
            } else {
                logs = shopTrackLogService.findByDateAndPageType(queryStartDate, null);
            }
            
            if (logs == null || logs.isEmpty()) {
                return new ArrayList<>();
            }

            // 按IP地址分组统计
            Map<String, List<ShopTrackLog>> ipGroups = logs.stream()
                    .filter(log -> log.getIpAddress() != null && !log.getIpAddress().trim().isEmpty())
                    .collect(Collectors.groupingBy(ShopTrackLog::getIpAddress));

            List<IpStatsDTO> result = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (Map.Entry<String, List<ShopTrackLog>> entry : ipGroups.entrySet()) {
                String ipAddress = entry.getKey();
                List<ShopTrackLog> ipLogs = entry.getValue();
                
                IpStatsDTO dto = new IpStatsDTO();
                dto.setIpAddress(ipAddress);
                dto.setVisitCount((long) ipLogs.size());
                dto.setUniqueVisitorCount(ipLogs.stream()
                        .map(ShopTrackLog::getVisitorId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count());
                
                long totalDuration = ipLogs.stream()
                        .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                        .sum();
                dto.setTotalDuration(totalDuration);
                dto.setAvgDuration((int) (dto.getVisitCount() > 0 ? totalDuration / dto.getVisitCount() : 0));
                
                // 设置访问日期
                if (isDateRange) {
                    dto.setVisitDate(sdf.format(queryStartDate) + " ~ " + sdf.format(queryEndDate));
                } else {
                    dto.setVisitDate(sdf.format(queryStartDate));
                }
                
                result.add(dto);
            }

            // 按访问次数排序
            result.sort((a, b) -> Long.compare(b.getVisitCount(), a.getVisitCount()));

            // 限制返回数量
            if (result.size() > limit) {
                result = result.subList(0, limit);
            }

            // 缓存结果
            if (redisUtil != null && result != null) {
                long expireSeconds = getCacheExpireSeconds(date, startDate, endDate);
                redisUtil.set(cacheKey, result, expireSeconds, TimeUnit.SECONDS);
                log.debug("💾 [CACHE] 缓存IP统计: key={}, expire={}秒", cacheKey, expireSeconds);
            }

            return result;
        } catch (Exception e) {
            log.error("查询IP统计失败: date={}, startDate={}, endDate={}, limit={}, error={}", 
                date, startDate, endDate, limit, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ProductStatsDTO> getProductRanking(String date, String startDate, String endDate, Integer limit, String orderBy) {
        // 尝试从缓存获取（limit和orderBy作为key的一部分）
        String cacheKey = buildCacheKey("product", date, startDate, endDate) + "." + 
                (limit != null ? limit : 20) + "." + (orderBy != null ? orderBy : "pv");
        if (redisUtil != null) {
            List<ProductStatsDTO> cached = redisUtil.getList(cacheKey, ProductStatsDTO.class);
            if (cached != null) {
                log.debug("✅ [CACHE] 从缓存获取商品排行: key={}", cacheKey);
                return cached;
            }
        }
        
        try {
            Date queryStartDate = null;
            Date queryEndDate = null;
            boolean isDateRange = false;
            
            // 优先使用日期范围查询
            if (startDate != null && !startDate.trim().isEmpty() && 
                endDate != null && !endDate.trim().isEmpty()) {
                queryStartDate = parseDate(startDate);
                queryEndDate = parseDate(endDate);
                if (queryStartDate != null && queryEndDate != null) {
                    isDateRange = true;
                }
            }
            
            // 如果没有日期范围，使用单日期查询
            if (!isDateRange) {
                Date queryDate = parseDate(date);
                if (queryDate == null) {
                    // 默认查询今天
                    LocalDate today = LocalDate.now();
                    queryDate = java.sql.Date.valueOf(today);
                }
                queryStartDate = queryDate;
                queryEndDate = queryDate;
            }

            if (limit == null || limit <= 0) {
                limit = 20;
            }

            if (orderBy == null || orderBy.trim().isEmpty()) {
                orderBy = "pv";
            }

            // 查询有商品ID的日志
            List<ShopTrackLog> productLogs;
            if (isDateRange) {
                productLogs = shopTrackLogService.findByDateRangeAndProductIdNotNull(queryStartDate, queryEndDate);
            } else {
                productLogs = shopTrackLogService.findByDateAndProductIdNotNull(queryStartDate);
            }
            
            log.debug("📊 [TRACK] 查询商品排行: startDate={}, endDate={}, isDateRange={}, productLogsCount={}", 
                queryStartDate, queryEndDate, isDateRange, productLogs != null ? productLogs.size() : 0);

            if (productLogs == null || productLogs.isEmpty()) {
                return new ArrayList<>();
            }

            // 按productId分组统计
            Map<Long, List<ShopTrackLog>> productGroups = productLogs.stream()
                    .filter(log -> log.getProductId() != null)
                    .collect(Collectors.groupingBy(ShopTrackLog::getProductId));

            List<ProductStatsDTO> result = new ArrayList<>();

            for (Map.Entry<Long, List<ShopTrackLog>> entry : productGroups.entrySet()) {
                Long productId = entry.getKey();
                List<ShopTrackLog> logs = entry.getValue();

                ProductStatsDTO dto = new ProductStatsDTO();
                dto.setProductId(productId);

                // 查询商品名称
                if (shopProductsService != null) {
                    try {
                        ShopProducts product = shopProductsService.get(productId);
                        if (product != null) {
                            dto.setProductName(product.getName());
                        } else {
                            dto.setProductName("商品已删除");
                        }
                    } catch (Exception e) {
                        log.warn("查询商品名称失败: productId={}, error={}", productId, e.getMessage());
                        dto.setProductName("商品ID: " + productId);
                    }
                } else {
                    dto.setProductName("商品ID: " + productId);
                }

                // 计算统计指标
                long pv = logs.size();
                long uv = logs.stream()
                        .map(ShopTrackLog::getVisitorId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count();

                long totalDuration = logs.stream()
                        .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                        .sum();
                int avgDuration = pv > 0 ? (int) (totalDuration / pv) : 0;

                int maxDuration = logs.stream()
                        .mapToInt(log -> log.getStayDuration() != null ? log.getStayDuration() : 0)
                        .max()
                        .orElse(0);

                int minDuration = logs.stream()
                        .mapToInt(log -> log.getStayDuration() != null ? log.getStayDuration() : 0)
                        .min()
                        .orElse(0);

                dto.setPv(pv);
                dto.setUv(uv);
                dto.setTotalDuration(totalDuration);
                dto.setAvgDuration(avgDuration);
                dto.setMaxDuration(maxDuration);
                dto.setMinDuration(minDuration);

                result.add(dto);
            }

            // 排序
            if ("avgDuration".equals(orderBy)) {
                result.sort((a, b) -> Integer.compare(b.getAvgDuration(), a.getAvgDuration()));
            } else {
                // 默认按PV排序
                result.sort((a, b) -> Long.compare(b.getPv(), a.getPv()));
            }

            // 限制返回数量
            if (result.size() > limit) {
                result = result.subList(0, limit);
            }

            // 缓存结果
            if (redisUtil != null && result != null) {
                long expireSeconds = getCacheExpireSeconds(date, startDate, endDate);
                redisUtil.set(cacheKey, result, expireSeconds, TimeUnit.SECONDS);
                log.debug("💾 [CACHE] 缓存商品排行: key={}, expire={}秒", cacheKey, expireSeconds);
            }

            return result;
        } catch (Exception e) {
            log.error("查询商品排行失败: date={}, startDate={}, endDate={}, limit={}, orderBy={}, error={}", 
                date, startDate, endDate, limit, orderBy, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 解析日期字符串
     */
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            log.warn("日期格式错误: {}", dateStr);
            return null;
        }
    }

    /**
     * 每日统计聚合（每天凌晨2点执行）
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Override
    public void aggregateDailyStats() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("⏰ [TRACK] 开始聚合统计: date={}", yesterday);
        aggregateStats(yesterday.toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void aggregateStats(String date) {
        try {
            Date sqlDate;
            if (date == null || date.trim().isEmpty()) {
                // 默认聚合昨天的数据
                LocalDate yesterday = LocalDate.now().minusDays(1);
                sqlDate = java.sql.Date.valueOf(yesterday);
                date = yesterday.toString();
            } else {
                sqlDate = parseDate(date);
                if (sqlDate == null) {
                    log.error("❌ [TRACK] 日期格式错误: {}", date);
                    return;
                }
            }

            log.info("⏰ [TRACK] 开始聚合统计: date={}", date);

            // 1. 聚合网站统计
            aggregateSiteStats(sqlDate);

            // 2. 聚合页面统计
            aggregatePageStats(sqlDate);

            // 3. 聚合商品统计（只统计有productId的记录）
            aggregateProductStats(sqlDate);

            log.info("✅ [TRACK] 统计聚合完成: date={}", date);
            
            // 清除该日期的所有相关缓存
            clearCacheU(date, null, null);
            log.info("🗑️ [CACHE] 已清除日期 {} 的所有统计缓存", date);

        } catch (Exception e) {
            log.error("❌ [TRACK] 统计聚合失败: date={}, error={}", date, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 聚合网站统计
     */
    private void aggregateSiteStats(Date date) {
        try {
            // 查询所有日志
            List<ShopTrackLog> allLogs = shopTrackLogService.findByDateAndPageType(date, null);

            if (allLogs == null || allLogs.isEmpty()) {
                log.debug("📊 [TRACK] 网站统计：无数据，跳过聚合: date={}", date);
                return;
            }

            // 计算PV和UV
            long pv = allLogs.size();
            long uv = allLogs.stream()
                    .map(ShopTrackLog::getVisitorId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();

            // 计算平均停留时间
            long totalDuration = allLogs.stream()
                    .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                    .sum();
            int avgDuration = pv > 0 ? (int) (totalDuration / pv) : 0;

            // 计算跳出率：跳出率 = (只访问一个页面的会话数 / 总会话数) * 100
            Map<String, List<ShopTrackLog>> sessionGroups = allLogs.stream()
                    .filter(log -> log.getSessionId() != null && !log.getSessionId().trim().isEmpty())
                    .collect(Collectors.groupingBy(ShopTrackLog::getSessionId));
            
            long totalSessions = sessionGroups.size();
            long bounceSessions = sessionGroups.values().stream()
                    .filter(logs -> logs.size() == 1) // 只访问一个页面的会话
                    .count();
            
            long bounceRate = 0L;
            if (totalSessions > 0) {
                bounceRate = Math.round((double) bounceSessions / totalSessions * 100);
            }

            // 查询是否已存在
            ShopTrackStats existing = getEntityDao().findSiteStats(date);
            
            ShopTrackStats stats;
            if (existing != null) {
                stats = existing;
            } else {
                stats = new ShopTrackStats();
            }

            stats.setStatType("daily_site");
            stats.setStatDate(date);
            stats.setPv(pv);
            stats.setUv(uv);
            stats.setTotalDuration(totalDuration);
            stats.setAvgDuration(avgDuration);
            stats.setMaxDuration(0);
            stats.setMinDuration(0);
            stats.setBounceRate(bounceRate); // 设置跳出率
            stats.setLocale(null); // 全语言统计

            if (existing != null) {
                update(stats);
            } else {
                save(stats);
            }

            log.debug("✅ [TRACK] 网站统计聚合完成: date={}, pv={}, uv={}, bounceRate={}%", 
                date, pv, uv, bounceRate);

        } catch (Exception e) {
            log.error("❌ [TRACK] 网站统计聚合失败: date={}, error={}", date, e.getMessage(), e);
        }
    }

    /**
     * 聚合页面统计（所有页面类型）
     */
    private void aggregatePageStats(Date date) {
        try {
            // 查询所有日志
            List<ShopTrackLog> allLogs = shopTrackLogService.findByDateAndPageType(date, null);

            if (allLogs == null || allLogs.isEmpty()) {
                log.debug("📊 [TRACK] 页面统计：无数据，跳过聚合: date={}", date);
                return;
            }

            // 按pageKey分组统计
            Map<String, List<ShopTrackLog>> pageGroups = allLogs.stream()
                    .filter(log -> log.getPageKey() != null)
                    .collect(Collectors.groupingBy(ShopTrackLog::getPageKey));

            List<ShopTrackStats> statsList = new ArrayList<>();

            for (Map.Entry<String, List<ShopTrackLog>> entry : pageGroups.entrySet()) {
                String pageKey = entry.getKey();
                List<ShopTrackLog> logs = entry.getValue();

                // 计算统计指标
                long pv = logs.size();
                long uv = logs.stream()
                        .map(ShopTrackLog::getVisitorId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count();

                long totalDuration = logs.stream()
                        .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                        .sum();
                int avgDuration = pv > 0 ? (int) (totalDuration / pv) : 0;

                int maxDuration = logs.stream()
                        .mapToInt(log -> log.getStayDuration() != null ? log.getStayDuration() : 0)
                        .max()
                        .orElse(0);

                int minDuration = logs.stream()
                        .mapToInt(log -> log.getStayDuration() != null ? log.getStayDuration() : 0)
                        .min()
                        .orElse(0);

                // 查询是否已存在
                ShopTrackStats existing = getEntityDao().uniqueByStatTypeAndStatDateAndEntityIdAndEntityKeyAndLocale(
                        "daily_page", date, null, pageKey, null);

                ShopTrackStats stats;
                if (existing != null) {
                    stats = existing;
                } else {
                    stats = new ShopTrackStats();
                }

                stats.setStatType("daily_page");
                stats.setStatDate(date);
                stats.setEntityKey(pageKey);
                stats.setPv(pv);
                stats.setUv(uv);
                stats.setTotalDuration(totalDuration);
                stats.setAvgDuration(avgDuration);
                stats.setMaxDuration(maxDuration);
                stats.setMinDuration(minDuration);
                stats.setLocale(null);

                statsList.add(stats);
            }

            // 批量保存或更新
            if (!statsList.isEmpty()) {
                for (ShopTrackStats stats : statsList) {
                    if (stats.getId() != null) {
                        update(stats);
                    } else {
                        save(stats);
                    }
                }
                log.debug("✅ [TRACK] 页面统计聚合完成: date={}, count={}", date, statsList.size());
            }

        } catch (Exception e) {
            log.error("❌ [TRACK] 页面统计聚合失败: date={}, error={}", date, e.getMessage(), e);
        }
    }

    /**
     * 聚合商品统计（只统计有productId的记录）
     */
    private void aggregateProductStats(Date date) {
        try {
            // ⚠️ 关键：只查询有productId的记录
            List<ShopTrackLog> productLogs = shopTrackLogService.findByDateAndProductIdNotNull(date);

            if (productLogs == null || productLogs.isEmpty()) {
                log.debug("📊 [TRACK] 商品统计：无数据，跳过聚合: date={}", date);
                return;
            }

            // 按productId分组统计
            Map<Long, List<ShopTrackLog>> productGroups = productLogs.stream()
                    .filter(log -> log.getProductId() != null) // 再次过滤
                    .collect(Collectors.groupingBy(ShopTrackLog::getProductId));

            List<ShopTrackStats> statsList = new ArrayList<>();

            for (Map.Entry<Long, List<ShopTrackLog>> entry : productGroups.entrySet()) {
                Long productId = entry.getKey();
                List<ShopTrackLog> logs = entry.getValue();

                // 计算统计指标
                long pv = logs.size();
                long uv = logs.stream()
                        .map(ShopTrackLog::getVisitorId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count();

                long totalDuration = logs.stream()
                        .mapToLong(log -> log.getStayDuration() != null ? log.getStayDuration() : 0L)
                        .sum();
                int avgDuration = pv > 0 ? (int) (totalDuration / pv) : 0;

                int maxDuration = logs.stream()
                        .mapToInt(log -> log.getStayDuration() != null ? log.getStayDuration() : 0)
                        .max()
                        .orElse(0);

                int minDuration = logs.stream()
                        .mapToInt(log -> log.getStayDuration() != null ? log.getStayDuration() : 0)
                        .min()
                        .orElse(0);

                // 查询是否已存在
                ShopTrackStats existing = getEntityDao().uniqueByStatTypeAndStatDateAndEntityIdAndEntityKeyAndLocale(
                        "daily_product", date, productId, null, null);

                ShopTrackStats stats;
                if (existing != null) {
                    stats = existing;
                } else {
                    stats = new ShopTrackStats();
                }

                stats.setStatType("daily_product");
                stats.setStatDate(date);
                stats.setEntityId(productId);
                stats.setPv(pv);
                stats.setUv(uv);
                stats.setTotalDuration(totalDuration);
                stats.setAvgDuration(avgDuration);
                stats.setMaxDuration(maxDuration);
                stats.setMinDuration(minDuration);
                stats.setLocale(null);

                statsList.add(stats);
            }

            // 批量保存或更新
            if (!statsList.isEmpty()) {
                for (ShopTrackStats stats : statsList) {
                    if (stats.getId() != null) {
                        update(stats);
                    } else {
                        save(stats);
                    }
                }
                log.debug("✅ [TRACK] 商品统计聚合完成: date={}, count={}", date, statsList.size());
            }

        } catch (Exception e) {
            log.error("❌ [TRACK] 商品统计聚合失败: date={}, error={}", date, e.getMessage(), e);
        }
    }

}
