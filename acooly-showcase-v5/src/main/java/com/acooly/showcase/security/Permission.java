package com.acooly.showcase.security;

public final class Permission {
    private Permission() {}

    // Link 模块
    public static final String LINK_LIST = "link:list";
    public static final String LINK_CREATE = "link:create";
    public static final String LINK_UPDATE = "link:update";
    public static final String LINK_DELETE = "link:delete";

    // DmCenter 模块
    public static final String DMCENTER_LIST = "dmcenter:list";
    public static final String DMCENTER_CREATE = "dmcenter:create";
    public static final String DMCENTER_UPDATE = "dmcenter:update";
    public static final String DMCENTER_DELETE = "dmcenter:delete";
} 