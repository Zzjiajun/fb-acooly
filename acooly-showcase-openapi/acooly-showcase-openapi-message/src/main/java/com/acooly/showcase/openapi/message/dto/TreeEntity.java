/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:55 创建
 */
package com.acooly.showcase.openapi.message.dto;

import com.acooly.core.utils.arithmetic.tree.TreeNode;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Data;

import java.util.List;

/**
 * 树形结构实体demo
 *
 * @author zhangpu
 * @date 2019-01-07 17:55
 */
@Data
public class TreeEntity implements TreeNode {

    @OpenApiField(desc = "ID", constraint = "ID")
    private Long id;
    @OpenApiField(desc = "父ID", constraint = "父ID")
    private Long parentId;
    @OpenApiField(desc = "标题", constraint = "标题")
    private String title;
    @OpenApiField(desc = "子节点", constraint = "子节点")
    private List<TreeEntity> children;

    @Override
    public void setChildren(List children) {

    }
}
