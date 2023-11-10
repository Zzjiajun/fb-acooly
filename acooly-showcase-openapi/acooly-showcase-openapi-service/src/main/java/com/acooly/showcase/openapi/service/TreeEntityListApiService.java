/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:59 创建
 */
package com.acooly.showcase.openapi.service;

import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.showcase.openapi.message.request.TreeEntityListApiRequest;
import com.acooly.showcase.openapi.message.response.TreeEntityListApiResponse;

/**
 * @author zhangpu
 * @date 2019-01-07 17:59
 */
@ApiDocType(code = "test", name = "测试")
@OpenApiService(
        name = "treeNodeList",
        desc = "测试：树形结构查询",
        responseType = ResponseType.SYN,
        owner = "openApi-arch",
        busiType = ApiBusiType.Trade
)
public abstract class TreeEntityListApiService extends BaseApiService<TreeEntityListApiRequest, TreeEntityListApiResponse> {
    @Override
    protected void doService(TreeEntityListApiRequest request, TreeEntityListApiResponse response) {

    }
}
