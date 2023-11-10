/**
 * acooly-showcase-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-11-14 17:18
 */
package com.acooly.showcase.demo.roweditor.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpu
 * @date 2021-11-14 17:18
 */
@Data
@NoArgsConstructor
public class OptionData {

    private String key;
    private String value;

    public OptionData(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
