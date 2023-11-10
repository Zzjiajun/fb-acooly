package com.acooly.showcase.daliy.dao;

import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.showcase.daliy.entity.Transactionss;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

public interface TransactionsDao extends EntityMybatisDao<Transactionss> {
    @Select("SELECT SUM(amount) AS total_amount FROM dm_transactions WHERE out_account = #{outName}")
    Float getOutFolat(@Param("outName") String outName);

    @Select("SELECT SUM(amount) AS total_amount FROM dm_transactions WHERE in_account = #{inName}")
    Float getInFolat(@Param("inName") String inName);
}
