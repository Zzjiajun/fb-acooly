package com.acooly.showcase.daliy.service.Impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.showcase.daliy.dao.PhoneDao;
import com.acooly.showcase.daliy.entity.Phone;
import com.acooly.showcase.daliy.service.PhoneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("PhoneService")
public class PhoneServiceImpl extends EntityServiceImpl<Phone, PhoneDao> implements PhoneService {
    @Override
    public List<String> getGroupName() {
        return this.getEntityDao().getGroupName();
    }
}
