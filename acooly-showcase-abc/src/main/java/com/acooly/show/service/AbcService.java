package com.acooly.show.service;

import com.acooly.show.AbcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qiuboboy@qq.com
 * @date 2018-03-25 16:59
 */
@Component
@Slf4j
public class AbcService {
  @Autowired private AbcProperties abcProperties;

  public void hello() {
    log.info("hello world,{}", abcProperties.getName());
  }
}
