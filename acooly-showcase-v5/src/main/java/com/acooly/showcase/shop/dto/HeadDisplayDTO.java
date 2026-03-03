package com.acooly.showcase.shop.dto;

import lombok.Data;
import java.util.List;

/**
 * 头部展示配置DTO
 *
 * @author acooly
 * @date 2025-11-28
 */
@Data
public class HeadDisplayDTO {
    /**
     * 类型：messages
     */
    private String type;

    /**
     * 消息列表（旧格式，兼容）
     */
    private List<String> messages;

    /**
     * 消息项列表（新格式，支持text字段）
     */
    private List<MessageItem> items;

    @Data
    public static class MessageItem {
        /**
         * 消息文本内容
         */
        private String text;
    }
}

