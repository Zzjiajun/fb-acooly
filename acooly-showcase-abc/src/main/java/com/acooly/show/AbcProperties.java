package com.acooly.show;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiuboboy@qq.com
 * @date 2018-03-25 16:58
 */
@ConfigurationProperties(prefix = "acooly.abc")
@Data
public class AbcProperties {
    private String name;
}
