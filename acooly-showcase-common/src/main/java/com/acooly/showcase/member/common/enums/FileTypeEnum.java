/*
 * acooly.cn Inc.
 * Copyright (c) 2021 All Rights Reserved.
 * create by acooly
 * date:2021-10-26
 *
 */
package com.acooly.showcase.member.common.enums;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;

/**
 * 会员附件 FileTypeEnum 枚举定义
 *
 * @author acooly
 * @date 2021-10-26 22:23:06
 */
public enum FileTypeEnum implements Messageable {

    pdf("pdf", "pdf", "pdf"),
    doc("doc", "文档", "doc,docx,xlsx,xls"),
    pic("pic", "图片", "png,gif,jpg,jpeg"),
    zip("zip", "归档", "zip,rar,iso,dat"),
    other("other", "其他", "");

    private final String code;
    private final String message;
    private final String exts;

    private FileTypeEnum(String code, String message, String exts) {
        this.code = code;
        this.message = message;
        this.exts = exts;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (FileTypeEnum type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
     */
    public static FileTypeEnum find(String code) {
        for (FileTypeEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<FileTypeEnum> getAll() {
        List<FileTypeEnum> list = new ArrayList<FileTypeEnum>();
        for (FileTypeEnum status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllCode() {
        List<String> list = new ArrayList<String>();
        for (FileTypeEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

    public static FileTypeEnum of(String ext) {
        for (FileTypeEnum fileType : values()) {
            if (Strings.contains(fileType.exts, ext)) {
                return fileType;
            }
        }
        return FileTypeEnum.other;
    }

}
