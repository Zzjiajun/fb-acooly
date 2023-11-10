package com.acooly.showcase.daliy.web;


import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.showcase.base.AbstractShowcaseController;
import com.acooly.showcase.daliy.Pagination;
import com.acooly.showcase.daliy.dto.UserPermissions;
import com.acooly.showcase.daliy.entity.Permissions;
import com.acooly.showcase.daliy.service.AccountsService;
import com.acooly.showcase.daliy.service.PermissionsService;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/showcase/daily/permissions")
public class PermissionsMangerController  extends AbstractShowcaseController<Permissions, PermissionsService> {


    {
        allowMapping = "*";
    }
    @Autowired
    private UserService userService;

    @RequestMapping(value = "userPermissions")
    public String userPermissions(Model model, HttpServletRequest request, HttpServletResponse response) {
        return "/manage/showcase/daily/userPermissions";
    }


    @RequestMapping(value = "userPermissionsList")
    @ResponseBody
    public JsonListResult<UserPermissions> userPermissionsList(HttpServletRequest request, HttpServletResponse response){
        Integer page = Integer.valueOf(request.getParameter("page"));
        Integer rows = Integer.valueOf(request.getParameter("rows"));
        JsonListResult<UserPermissions> result = new JsonListResult<>();
        ArrayList<UserPermissions> arrayList = new ArrayList<>();
        List<User> userList = userService.getAll();
        Map<String, Object> mapQuery = Maps.newHashMap();
        userList.forEach(s->{
            mapQuery.put("EQ_userName",s.getUsername());
            List<Permissions> query = this.getEntityService().query(mapQuery, null);
            UserPermissions userPermissions = new UserPermissions();
            userPermissions.setId(s.getId());
            userPermissions.setUsername(s.getUsername());
            userPermissions.setPinyin(s.getPinyin());
            userPermissions.setRealName(s.getRealName());
            if (query.size()>0){
                userPermissions.setStatus(1);
            }else {
                userPermissions.setStatus(0);
            }
            arrayList.add(userPermissions);
        });
        Pagination pagination = new Pagination(arrayList.size(), page,rows,arrayList);
        pagination.calculatePagination();
        result.setPageNo(pagination.getCurrentPage());
        result.setTotal(Long.valueOf(pagination.getTotalItems()));
        result.setPageSize(pagination.getItemsPerPage());
        result.setRows(pagination.getCurrentPageItems());
        return result;
    }
    @RequestMapping(value = "addPermissions")
    @ResponseBody
    public JsonResult addPermissions(HttpServletRequest request, HttpServletResponse response){
        JsonResult result = new JsonResult();
        String id = request.getParameter("id");
        Map<String, Object> mapQuery = Maps.newHashMap();
        String username = userService.get(Long.valueOf(id)).getUsername();
        mapQuery.put("EQ_userName",username);
        List<Permissions> query = this.getEntityService().query(mapQuery, null);
        if (!(query.size() >0)){
            Permissions permissions = new Permissions();
            permissions.setId(Long.valueOf(id));
            permissions.setUserName(username);
            this.getEntityService().save(permissions);
            result.setMessage("添加成功");
        }else {
            result.setMessage("添加失败");
            result.setSuccess(false);
        }
        return  result;
    }

    @RequestMapping(value = "deletePermissions")
    @ResponseBody
    public JsonResult deletePermissions(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        JsonResult result = new JsonResult();
        Map<String, Object> mapQuery = Maps.newHashMap();
        String username = userService.get(Long.valueOf(id)).getUsername();
        mapQuery.put("EQ_userName",username);
        List<Permissions> query = this.getEntityService().query(mapQuery, null);
        if (query.size() >0){
            this.getEntityService().removeById(query.get(0).getId());
            result.setMessage("删除成功");
        }else {
            result.setMessage("删除失败");
            result.setSuccess(false);
        }
        return  result;
    }
}
