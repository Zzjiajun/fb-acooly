package com.acooly.showcase.daliy.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.showcase.daliy.entity.Phone;

import java.util.List;

public interface PhoneService extends EntityService<Phone> {
    List<String> getGroupName();
}
