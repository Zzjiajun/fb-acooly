package com.acooly.showcase.stemp.enums;

import lombok.Getter;

@Getter
public enum FieldEnum {

    GROUP_NAME("群名", "groupName"),
    BUSINESS("业务", "business"),
    COUNTRY("国家", "country"),
    REMARK("备注", "remark"),
    PHONE("电话", "phone"),
    SHARE("股民", "share"),
    INTENT("意向", "intent"),
    NAME("姓名", "name"),
    EMAIL("邮箱", "email");

    private final String chineseName;
    private final String fieldName;

    FieldEnum(String chineseName, String fieldName) {
        this.chineseName = chineseName;
        this.fieldName = fieldName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getFieldName() {
        return fieldName;
    }

    // 根据中文名获取对应的字段名
    public static String getFieldNameByChineseName(String chineseName) {
        for (FieldEnum mapping : FieldEnum.values()) {
            if (mapping.getChineseName().equals(chineseName)) {
                return mapping.getFieldName();
            }
        }
        return null; // 如果没有找到对应的字段名，返回null
    }
}
