/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-12
 */
package com.acooly.showcase.jpa.goods.web;

import com.acooly.core.common.web.AbstractManageController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Strings;
import com.acooly.showcase.jpa.goods.entity.GoodsCategory;
import com.acooly.showcase.jpa.goods.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商品分类 管理控制器
 *
 * @author acooly Date: 2017-09-12 22:42:00
 */
@Controller
@RequestMapping(value = "/manage/showcase/goodsCategory")
public class GoodsCategoryManagerController
    extends AbstractManageController<GoodsCategory, GoodsCategoryService> {

  @Autowired private GoodsCategoryService goodsCategoryService;

  {
    allowMapping = "*";
  }

  /** 添加视图处理 */
  @Override
  protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("parentId", request.getParameter("parentId"));
    super.onCreate(request, response, model);
  }

  /** 编辑视图处理 */
  @Override
  protected void onEdit(
      HttpServletRequest request, HttpServletResponse response, Model model, GoodsCategory entity) {
    model.addAttribute("parentId", entity.getParentId());
    super.onEdit(request, response, model, entity);
  }

  /** 保持分类对象 支持 新增和编辑 */
  @Override
  protected GoodsCategory doSave(
      HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate)
      throws Exception {
    GoodsCategory entity = loadEntity(request);
    if (entity == null) {
      entity = getEntityClass().newInstance();
    }

    String name = request.getParameter("name");
    String comments = request.getParameter("comments");
    if (isCreate) {
      String reqParentId = request.getParameter("parentId");
      Long parentId = null;
      if (Strings.isNotBlank(reqParentId)) {
        parentId = Long.valueOf(reqParentId);
      }
      entity = getEntityService().create(parentId, name, comments);
    } else {
      entity.setName(name);
      entity.setComments(comments);
      getEntityService().save(entity);
    }
    return entity;
  }

  /** 加载分类树 */
  @RequestMapping("loadTree")
  @ResponseBody
  public JsonListResult<GoodsCategory> loadTree(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    JsonListResult<GoodsCategory> result = new JsonListResult<GoodsCategory>();
    try {
      result.setRows(getEntityService().loadTree(null));
      result.setTotal((long) result.getRows().size());
    } catch (Exception e) {
      handleException(result, "loadTree", e);
    }
    return result;
  }

  /** 树型节点移动 */
  @RequestMapping("move")
  @ResponseBody
  public JsonResult move(HttpServletRequest request, HttpServletResponse response, Model model) {
    JsonResult result = new JsonResult();
    String moveType = request.getParameter("moveType");
    String sourceId = request.getParameter("sourceId");
    String targetId = request.getParameter("targetId");
    try {
      GoodsCategory source = getEntityService().get(Long.valueOf(sourceId));
      GoodsCategory target = getEntityService().get(Long.valueOf(targetId));
      if ("inner".equals(moveType)) {
        source.setParentId(target.getId());
      } else if ("prev".equals(moveType)) {
        source.setSortTime(target.getSortTime() + 1);
        // 不同级
        if (source.getParentId() != null
            && target.getParentId() != null
            && !source.getParentId().equals(target.getParentId())) {
          source.setParentId(target.getParentId());
        }
      } else if ("next".equals(moveType)) {
        source.setSortTime(target.getSortTime() - 1);
        // 不同级
        if (source.getParentId() != null
            && target.getParentId() != null
            && !source.getParentId().equals(target.getParentId())) {
          source.setParentId(target.getParentId());
        }
      }
      getEntityService().save(source);
    } catch (Exception e) {
      handleException(result, "移动[" + moveType + "]", e);
    }
    return result;
  }
}
