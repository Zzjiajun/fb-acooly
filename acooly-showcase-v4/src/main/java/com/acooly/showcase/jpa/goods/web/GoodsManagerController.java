/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-09-12
 */
package com.acooly.showcase.jpa.goods.web;

import com.acooly.core.common.web.AbstractManageController;
import com.acooly.showcase.jpa.goods.entity.Goods;
import com.acooly.showcase.jpa.goods.entity.GoodsCategory;
import com.acooly.showcase.jpa.goods.enums.GoodsUnitEnum;
import com.acooly.showcase.jpa.goods.service.GoodsCategoryService;
import com.acooly.showcase.jpa.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 商品信息 管理控制器
 *
 * @author acooly Date: 2017-09-12 22:42:00
 */
@Controller
@RequestMapping(value = "/manage/showcase/goods")
public class GoodsManagerController extends AbstractManageController<Goods, GoodsService> {

  @Autowired private GoodsService goodsService;
  @Autowired private GoodsCategoryService goodsCategoryService;

  {
    allowMapping = "*";
  }

  @Override
  protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
    String categoryId = request.getParameter("categoryId");
    GoodsCategory goodsCategory = goodsCategoryService.get(Long.valueOf(categoryId));
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("categoryName", goodsCategory.getName());
    super.onCreate(request, response, model);
  }

  @Override
  protected void onEdit(
      HttpServletRequest request, HttpServletResponse response, Model model, Goods entity) {
    model.addAttribute("categoryId", entity.getCategoryId());
    model.addAttribute("categoryName", entity.getCategoryName());
    super.onEdit(request, response, model, entity);
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allUnits", GoodsUnitEnum.mapping());
    model.put("categoryId", request.getParameter("categoryId"));
  }
}
