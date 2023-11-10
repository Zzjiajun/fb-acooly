package com.acooly.showcase.daliy.service.Impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.dao.PermissionsDao;
import com.acooly.showcase.daliy.entity.Permissions;
import com.acooly.showcase.daliy.service.PermissionsService;
import org.springframework.stereotype.Service;

@Service("PermissionsService")
public class PermissionsServiceImpl extends EntityServiceImpl<Permissions, PermissionsDao> implements PermissionsService{
}
