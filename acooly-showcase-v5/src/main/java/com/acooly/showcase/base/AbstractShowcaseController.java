/**
 * acooly-showcase-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author Administrator
 * @date 2022-04-28 14:17
 */
package com.acooly.showcase.base;

import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.module.ofile.OFileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * ShowCase项目后台控制器基类
 *
 * @author zhangpu
 * @date 2022-04-28 14:17
 */
@Slf4j
public class AbstractShowcaseController<T extends Entityable, M extends EntityService<T>>
        extends AbstractJsonEntityController<T, M> {

    // 1G
    public static final long MAX_UPLOAD_FILE_SIZE = 1024 * 1024;

    @Autowired
    protected OFileProperties oFileProperties;


    /**
     * 统一上传配置
     *
     * @return
     */
    @Override
    protected UploadConfig getUploadConfig() {

        long storageSize = getDirSize(new File(oFileProperties.getStorageRoot()));
        if (storageSize >= MAX_UPLOAD_FILE_SIZE) {
            log.warn("上传文件 失败 超过最大允许的上传文件总空间:{}", MAX_UPLOAD_FILE_SIZE);
            throw new BusinessException("MAX_UPLOAD_FILE_SIZE", "超过最大允许的上传文件总空间", "");
        }

        UploadConfig uploadConfig = super.getUploadConfig();
        uploadConfig.setStorageRoot(oFileProperties.getStorageRoot());
        uploadConfig.setAllowExtentions("pdf,doc,docx,xlsx,xls,zip,png,gif,jpg,jpeg");
        uploadConfig.setUseMemery(false);
        // 单文件最大1M
        uploadConfig.setMaxSize(1024 * 1024);
        uploadConfig.setNeedRemaneToTimestamp(false);
        uploadConfig.setNeedTimePartPath(false);
        return uploadConfig;
    }


    public long getDirSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();
        for (File subFile : files) {
            if (subFile.isFile()) {
                size += subFile.length();
            } else {
                size += getDirSize(subFile);
            }
        }
        return size;
    }
}
