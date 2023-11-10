package com.acooly.showcase.daliy.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.daliy.entity.Phone;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PhoneDao extends EntityMybatisDao<Phone> {
    @Select("SELECT DISTINCT group_name FROM dm_phone")
    List<String> getGroupName();
}
