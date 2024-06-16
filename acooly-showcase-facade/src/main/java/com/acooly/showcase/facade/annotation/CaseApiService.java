package com.acooly.showcase.facade.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 创建自己的注解类型，用于标识接口服务。caseNo、desc、owner三个属性为自定义属性，用于标识用例的编码、描述、开发人员。
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface CaseApiService {

    /**
     * 用例编码
     * @return
     */
    String caseNo();

    /**
     * 用例描述
     * @return
     */
    String desc();

    /**
     * 用例的开发人员
     * @return
     */
    String owner() default "unknown";
}
