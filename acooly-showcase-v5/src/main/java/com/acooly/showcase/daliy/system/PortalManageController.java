package com.acooly.showcase.daliy.system;

import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.daliy.system.dto.DmCenterBo;
import com.acooly.showcase.daliy.system.dto.DmCenterDto;
import com.acooly.showcase.daliy.system.dto.ParameterStatusDto;
import com.acooly.showcase.shop.entity.ShopOrders;
import com.acooly.showcase.shop.entity.ShopProducts;
import com.acooly.showcase.shop.entity.ShopUsers;
import com.acooly.showcase.shop.service.ShopOrdersService;
import com.acooly.showcase.shop.service.ShopProductsService;
import com.acooly.showcase.shop.service.ShopUsersService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping(value = "/manage/layout")
public class PortalManageController {
    @Autowired
    private DmCenterService dmCenterService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopOrdersService shopOrdersService;
    @Autowired
    private ShopProductsService shopProductsService;
    @Autowired
    private ShopUsersService shopUsersService;
    @RequestMapping(value = "/LinkPortal")
    public String LinkPortal(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<DmCenter> all = dmCenterService.getAll();
        long allLandingPages = all.stream()
                .filter(dmCenter -> dmCenter.getDisplayOption() == 1)
                .count();
        long allForms = all.stream()
                .filter(dmCenter -> dmCenter.getDisplayOption() == 2)
                .count();
        long allPolling = all.stream()
                .filter(dmCenter -> dmCenter.getDiversion() == 1)
                .count();
        List<ParameterStatusDto> parameterStatusDtoList = dmCenterService.countParameterStatus();
        model.addAttribute("allPolling", allPolling);
        model.addAttribute("allLandingPages", allLandingPages);
        model.addAttribute("allForms", allForms);
        model.addAttribute("parameterStatusDto", parameterStatusDtoList);
//柱状图数据
        List<DmCenterDto> dmCenterDtoList = dmCenterService.countUserRegionType();
        Map<String, DmCenterBo> dmCenterBoMap = new HashMap<>();

        // 遍历 List<User>
        for (DmCenterDto dto : dmCenterDtoList) {
            // 构建 key，由 user_name 和 region 组成
            String key = dto.getUserName() + dto.getRegion();
            // 如果 user1Map 中不存在该 key，则新建一个 User1 对象
            if (!dmCenterBoMap.containsKey(key)) {
                dmCenterBoMap.put(key, new DmCenterBo());
            }
            // 获取对应的 User1 对象
            DmCenterBo dmCenterBo = dmCenterBoMap.get(key);
            dmCenterBo.setUserName(dto.getUserName());
            dmCenterBo.setRegion(dto.getRegion());
            // 根据 type 的不同来赋值给不同字段
            if (dto.getDisplayOption() == 2) {
                dmCenterBo.setFormsCount(dto.getTotalVisits());
            } else if (dto.getDisplayOption() == 1) {
                dmCenterBo.setLandingCount(dto.getTotalVisits());
                dmCenterBo.setLandingButtonViews(dto.getTotalClicks());
            }
        }

        List<DmCenterBo> dmCenterBoArrayList = new ArrayList<>(dmCenterBoMap.values());
        List<User> userList = userService.getAll();
        Map<String, String> userMap = userList.stream()
                .collect(Collectors.toMap(User::getUsername, User::getRealName));
        dmCenterBoArrayList.forEach(s->{
            s.setUserName(userMap.get(s.getUserName()));
        });
        Map<String, List<DmCenterBo>> dmCenterBoArrayMap = dmCenterBoArrayList.stream()
                .collect(Collectors.groupingBy(DmCenterBo::getRegion));
        model.addAttribute("dmCenterBoArrayMap", dmCenterBoArrayMap);

        return "/manage/link/protal";
    }

    @RequestMapping(value = "/portal")
    public String portal(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            // ========== 1. 核心指标统计 ==========
            // 总订单数
            long totalOrders = shopOrdersService.getAll().size();
            // 总销售额
            BigDecimal totalSales = shopOrdersService.getAll().stream()
                    .map(ShopOrders::getTotalPrice)
                    .filter(price -> price != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 总用户数
            long totalUsers = shopUsersService.getAll().size();
            // 总商品数
            long totalProducts = shopProductsService.getAll().size();

            // ========== 2. 待处理事务统计（按订单状态） ==========
            Map<String, Object> orderStatusParams = Maps.newHashMap();
            List<ShopOrders> allOrders = shopOrdersService.getAll();
            
            long pendingOrders = allOrders.stream()
                    .filter(o -> "PENDING".equalsIgnoreCase(o.getStatus()))
                    .count();
            long processingOrders = allOrders.stream()
                    .filter(o -> "PROCESSING".equalsIgnoreCase(o.getStatus()))
                    .count();
            long shippedOrders = allOrders.stream()
                    .filter(o -> "SHIPPED".equalsIgnoreCase(o.getStatus()))
                    .count();
            long deliveredOrders = allOrders.stream()
                    .filter(o -> "DELIVERED".equalsIgnoreCase(o.getStatus()))
                    .count();
            long cancelledOrders = allOrders.stream()
                    .filter(o -> "CANCELLED".equalsIgnoreCase(o.getStatus()))
                    .count();

            Map<String, Long> pendingTasks = new HashMap<>();
            pendingTasks.put("pendingPayment", pendingOrders);
            pendingTasks.put("pendingShipment", processingOrders);
            pendingTasks.put("shipped", shippedOrders);
            pendingTasks.put("completed", deliveredOrders);
            pendingTasks.put("cancelled", cancelledOrders);

            // ========== 3. 商品总览统计 ==========
            List<ShopProducts> allProducts = shopProductsService.getAll();
            long featuredProducts = allProducts.stream()
                    .filter(p -> p.getFeatured() != null && p.getFeatured() == 1)
                    .count();
            long freeShippingProducts = allProducts.stream()
                    .filter(p -> p.getFreeShipping() != null && p.getFreeShipping() == 1)
                    .count();
            // 打折商品：商品价格低于商品原价
            long discountedProducts = allProducts.stream()
                    .filter(p -> {
                        if (p.getPrice() == null || p.getOriginalPrice() == null) return false;
                        return p.getPrice().compareTo(p.getOriginalPrice()) < 0;
                    })
                    .count();

            Map<String, Long> productOverview = new HashMap<>();
            productOverview.put("total", totalProducts);
            productOverview.put("featured", featuredProducts);
            productOverview.put("freeShipping", freeShippingProducts);
            productOverview.put("discounted", discountedProducts);

            // ========== 4. 用户总览统计 ==========
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime yesterdayStart = todayStart.minusDays(1);
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

            long todayNewUsers = shopUsersService.getAll().stream()
                    .filter(u -> {
                        if (u.getCreateTime() == null) return false;
                        LocalDateTime createTime = u.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(todayStart);
                    })
                    .count();
            long yesterdayNewUsers = shopUsersService.getAll().stream()
                    .filter(u -> {
                        if (u.getCreateTime() == null) return false;
                        LocalDateTime createTime = u.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(yesterdayStart) && createTime.isBefore(todayStart);
                    })
                    .count();
            long monthNewUsers = shopUsersService.getAll().stream()
                    .filter(u -> {
                        if (u.getCreateTime() == null) return false;
                        LocalDateTime createTime = u.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(monthStart);
                    })
                    .count();

            Map<String, Long> userOverview = new HashMap<>();
            userOverview.put("todayNew", todayNewUsers);
            userOverview.put("yesterdayNew", yesterdayNewUsers);
            userOverview.put("monthNew", monthNewUsers);
            userOverview.put("total", totalUsers);

            // ========== 5. 订单统计（本月、本周、同比） ==========
            LocalDateTime weekStart = LocalDateTime.now().with(java.time.DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime lastMonthStart = monthStart.minusMonths(1);
            LocalDateTime lastWeekStart = weekStart.minusWeeks(1);

            // 本月订单
            long monthOrderCount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(monthStart);
                    })
                    .count();
            // 上月订单
            long lastMonthOrderCount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(lastMonthStart) && createTime.isBefore(monthStart);
                    })
                    .count();
            // 本周订单
            long weekOrderCount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(weekStart);
                    })
                    .count();
            // 上周订单
            long lastWeekOrderCount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(lastWeekStart) && createTime.isBefore(weekStart);
                    })
                    .count();

            // 本月销售额
            BigDecimal monthSalesAmount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(monthStart);
                    })
                    .map(ShopOrders::getTotalPrice)
                    .filter(price -> price != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 上月销售额
            BigDecimal lastMonthSalesAmount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(lastMonthStart) && createTime.isBefore(monthStart);
                    })
                    .map(ShopOrders::getTotalPrice)
                    .filter(price -> price != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 本周销售额
            BigDecimal weekSalesAmount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(weekStart);
                    })
                    .map(ShopOrders::getTotalPrice)
                    .filter(price -> price != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 上周销售额
            BigDecimal lastWeekSalesAmount = allOrders.stream()
                    .filter(o -> {
                        if (o.getCreateTime() == null) return false;
                        LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                        return createTime.isAfter(lastWeekStart) && createTime.isBefore(weekStart);
                    })
                    .map(ShopOrders::getTotalPrice)
                    .filter(price -> price != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 计算同比百分比
            double monthOrderCompare = lastMonthOrderCount > 0 ? 
                    ((monthOrderCount - lastMonthOrderCount) * 100.0 / lastMonthOrderCount) : 0.0;
            double weekOrderCompare = lastWeekOrderCount > 0 ? 
                    ((weekOrderCount - lastWeekOrderCount) * 100.0 / lastWeekOrderCount) : 0.0;
            double monthSalesCompare = lastMonthSalesAmount.compareTo(BigDecimal.ZERO) > 0 ? 
                    monthSalesAmount.subtract(lastMonthSalesAmount).divide(lastMonthSalesAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue() : 0.0;
            double weekSalesCompare = lastWeekSalesAmount.compareTo(BigDecimal.ZERO) > 0 ? 
                    weekSalesAmount.subtract(lastWeekSalesAmount).divide(lastWeekSalesAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue() : 0.0;

            Map<String, Object> orderStats = new HashMap<>();
            orderStats.put("monthOrderCount", monthOrderCount);
            orderStats.put("monthOrderCompare", monthOrderCompare);
            orderStats.put("weekOrderCount", weekOrderCount);
            orderStats.put("weekOrderCompare", weekOrderCompare);
            orderStats.put("monthSalesAmount", monthSalesAmount);
            orderStats.put("monthSalesCompare", monthSalesCompare);
            orderStats.put("weekSalesAmount", weekSalesAmount);
            orderStats.put("weekSalesCompare", weekSalesCompare);

            // ========== 6. 订单趋势数据（最近7天） ==========
            List<Map<String, Object>> trendData = new ArrayList<>();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = 6; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                LocalDateTime dayStart = date.atStartOfDay();
                LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

                long dayOrderCount = allOrders.stream()
                        .filter(o -> {
                            if (o.getCreateTime() == null) return false;
                            LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                            return createTime.isAfter(dayStart) && createTime.isBefore(dayEnd);
                        })
                        .count();
                BigDecimal daySalesAmount = allOrders.stream()
                        .filter(o -> {
                            if (o.getCreateTime() == null) return false;
                            LocalDateTime createTime = o.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                            return createTime.isAfter(dayStart) && createTime.isBefore(dayEnd);
                        })
                        .map(ShopOrders::getTotalPrice)
                        .filter(price -> price != null)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", date.format(dateFormatter));
                dayData.put("orderCount", dayOrderCount);
                dayData.put("salesAmount", daySalesAmount);
                trendData.add(dayData);
            }

            // ========== 7. 最新订单列表（最近10条） ==========
            List<ShopOrders> latestOrders = allOrders.stream()
                    .sorted((o1, o2) -> {
                        if (o1.getCreateTime() == null || o2.getCreateTime() == null) return 0;
                        return o2.getCreateTime().compareTo(o1.getCreateTime());
                    })
                    .limit(10)
                    .collect(Collectors.toList());

            // ========== 8. 最新商品列表（最近10条） ==========
            List<ShopProducts> latestProducts = allProducts.stream()
                    .sorted((p1, p2) -> {
                        if (p1.getCreateTime() == null || p2.getCreateTime() == null) return 0;
                        return p2.getCreateTime().compareTo(p1.getCreateTime());
                    })
                    .limit(10)
                    .collect(Collectors.toList());

            // ========== 将数据放入Model ==========
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("totalSales", totalSales);
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("pendingTasks", pendingTasks);
            model.addAttribute("productOverview", productOverview);
            model.addAttribute("userOverview", userOverview);
            model.addAttribute("orderStats", orderStats);
            model.addAttribute("trendData", trendData);
            model.addAttribute("latestOrders", latestOrders);
            model.addAttribute("latestProducts", latestProducts);

        } catch (Exception e) {
            log.error("获取大屏数据失败", e);
        }

        return "/manage/shop/shopProtal";
    }


    @RequestMapping(value = "/portalCharts")
    public String portalCharts(HttpServletRequest request, HttpServletResponse response, Model model) {
        //柱状图数据
        List<DmCenterDto> dmCenterDtoList = dmCenterService.countUserRegionType();
        Map<String, DmCenterBo> dmCenterBoMap = new HashMap<>();

        // 遍历 List<User>
        for (DmCenterDto dto : dmCenterDtoList) {
            // 构建 key，由 user_name 和 region 组成
            String key = dto.getUserName() + dto.getRegion();
            // 如果 user1Map 中不存在该 key，则新建一个 User1 对象
            if (!dmCenterBoMap.containsKey(key)) {
                dmCenterBoMap.put(key, new DmCenterBo());
            }
            // 获取对应的 User1 对象
            DmCenterBo dmCenterBo = dmCenterBoMap.get(key);
            dmCenterBo.setUserName(dto.getUserName());
            dmCenterBo.setRegion(dto.getRegion());
            // 根据 type 的不同来赋值给不同字段
            if (dto.getDisplayOption() == 2) {
                dmCenterBo.setFormsCount(dto.getTotalVisits());
            } else if (dto.getDisplayOption() == 1) {
                dmCenterBo.setLandingCount(dto.getTotalVisits());
                dmCenterBo.setLandingButtonViews(dto.getTotalClicks());
            }
        }

        List<DmCenterBo> dmCenterBoArrayList = new ArrayList<>(dmCenterBoMap.values());
        List<User> userList = userService.getAll();
        Map<String, String> userMap = userList.stream()
                .collect(Collectors.toMap(User::getUsername, User::getRealName));
        dmCenterBoArrayList.forEach(s->{
            s.setUserName(userMap.get(s.getUserName()));
        });
        Map<String, List<DmCenterBo>> dmCenterBoArrayMap = dmCenterBoArrayList.stream()
                .collect(Collectors.groupingBy(DmCenterBo::getRegion));
        model.addAttribute("dmCenterBoArrayMap", dmCenterBoArrayMap);

        return "/manage/link/protalCharts";

    }
}
