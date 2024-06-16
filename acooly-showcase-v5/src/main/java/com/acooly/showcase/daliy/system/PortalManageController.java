package com.acooly.showcase.daliy.system;

import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.daliy.entity.DmCenter;
import com.acooly.showcase.daliy.service.DmCenterService;
import com.acooly.showcase.daliy.system.dto.DmCenterBo;
import com.acooly.showcase.daliy.system.dto.DmCenterDto;
import com.acooly.showcase.daliy.system.dto.ParameterStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @RequestMapping(value = "/portal")
    public String portal(HttpServletRequest request, HttpServletResponse response, Model model) {
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
