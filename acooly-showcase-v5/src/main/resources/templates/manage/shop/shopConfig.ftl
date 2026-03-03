<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="config-detail-container" style="padding: 20px;" >
    <!-- 操作栏 -->
    <div class="config-toolbar" style="padding: 10px; background: #f5f5f5; margin-bottom: 20px; border-radius: 4px;">
        <#if config??>
            <button onclick="editConfig(${config.id})" class="btn btn-primary">
                <i class="fa fa-edit"></i> 编辑配置
            </button>
        <#else>
            <button onclick="createConfig()" class="btn btn-primary">
                <i class="fa fa-plus"></i> 创建配置
            </button>
        </#if>
    </div>

    <!-- 配置内容展示区域 -->
    <div class="config-sections">
        <!-- 1. 轮播商品配置 -->
        <div class="config-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 4px; background: #fff;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #333;">
                <i class="fa fa-image"></i> 包类商品配置
            </h3>
            <div class="product-cards" style="display: flex; flex-wrap: wrap; gap: 15px;">
                <#if carouselProducts?? && carouselProducts?size gt 0>
                    <#list carouselProducts as product>
                        <div class="product-card" style="width: 180px; border: 1px solid #eee; border-radius: 4px; padding: 10px; background: #fafafa;">
                            <img src="${product.imageUrl!''}"
                                 alt="${product.name!''}"
                                 style="width: 80%; height: 80px; object-fit: cover; border-radius: 4px; margin-bottom: 8px;"
                                 onerror="this.src='/static/images/no-image.png'"/>
                            <div class="product-name" style="font-size: 14px; font-weight: bold; margin-bottom: 5px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                ${product.name!''}
                            </div>
                            <div class="product-price" style="color: #e74c3c; font-size: 16px; font-weight: bold;">
                                ¥${product.price!0}
                            </div>
                        </div>
                    </#list>
                <#else>
                    <div class="empty-state" style="width: 100%; text-align: center; padding: 40px; color: #999;">
                        <i class="fa fa-inbox" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                        暂无配置的轮播商品
                    </div>
                </#if>
            </div>
        </div>

        <!-- 2. 展示商品配置 -->
        <div class="config-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 4px; background: #fff;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #333;">
                <i class="fa fa-th-list"></i> 手表商品配置
            </h3>
            <div class="product-cards" style="display: flex; flex-wrap: wrap; gap: 15px;">
                <#if displayProducts?? && displayProducts?size gt 0>
                    <#list displayProducts as product>
                        <div class="product-card" style="width: 180px; border: 1px solid #eee; border-radius: 4px; padding: 10px; background: #fafafa;">
                            <img src="${product.imageUrl!''}"
                                 alt="${product.name!''}"
                                 style="width: 80%; height: 80px; object-fit: cover; border-radius: 4px; margin-bottom: 8px;"
                                 onerror="this.src='/static/images/no-image.png'"/>
                            <div class="product-name" style="font-size: 14px; font-weight: bold; margin-bottom: 5px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                ${product.name!''}
                            </div>
                            <div class="product-price" style="color: #e74c3c; font-size: 16px; font-weight: bold;">
                                ¥${product.price!0}
                            </div>
                        </div>
                    </#list>
                <#else>
                    <div class="empty-state" style="width: 100%; text-align: center; padding: 40px; color: #999;">
                        <i class="fa fa-inbox" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                        暂无配置的展示商品
                    </div>
                </#if>
            </div>
        </div>

        <!-- 3. 展示评论配置 -->
        <div class="config-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 4px; background: #fff;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #333;">
                <i class="fa fa-comments"></i> 展示评论配置
            </h3>
            <div class="review-cards" style="display: flex; flex-direction: column; gap: 15px;">
                <#if showComments?? && showComments?size gt 0>
                    <#list showComments as review>
                        <div class="review-card" style="border: 1px solid #eee; border-radius: 4px; padding: 15px; background: #fafafa;">
                            <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                <div class="review-rating" style="margin-right: 15px;">
                                    <#list 1..5 as i>
                                        <i class="fa fa-star <#if i <= (review.rating!0)>text-warning<#else>text-muted</#if>"></i>
                                    </#list>
                                    <span style="margin-left: 5px; color: #666;">(${review.rating!0}星)</span>
                                </div>
                                <div class="review-meta" style="color: #999; font-size: 12px;">
                                    用户ID: ${review.userId!''} |
                                    <#if review.createdAt??>${review.createdAt?string('yyyy-MM-dd HH:mm')}</#if>
                                </div>
                            </div>
                            <div class="review-comment" style="color: #333; line-height: 1.6;">
                                ${review.comment!''}
                            </div>
                        </div>
                    </#list>
                <#else>
                    <div class="empty-state" style="width: 100%; text-align: center; padding: 40px; color: #999;">
                        <i class="fa fa-inbox" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                        暂无配置的评论
                    </div>
                </#if>
            </div>
        </div>

        <!-- 4. 头部展示配置 -->
        <div class="config-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 4px; background: #fff;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #333;">
                <i class="fa fa-header"></i> 头部展示配置
            </h3>
            <div class="head-display-content">
                <#if headDisplay??>
                    <div style="margin-bottom: 10px;">
                        <strong>类型：</strong>
                        <span class="badge badge-info">${headDisplay.type!''}</span>
                    </div>
                    <#if headDisplay.type == "messages">
                        <#if headDisplay.items?? && headDisplay.items?size gt 0>
                            <!-- 显示消息列表 -->
                            <div style="background: #f8f9fa; padding: 15px; border-radius: 4px; margin-bottom: 15px;">
                                <strong>消息列表：</strong>
                                <ul style="margin-top: 10px; margin-bottom: 0;">
                                    <#list headDisplay.items as item>
                                        <li style="margin-bottom: 5px;">${item.text!''}</li>
                                    </#list>
                                </ul>
                            </div>
                        <#elseif headDisplay.messages?? && headDisplay.messages?size gt 0>
                            <!-- 兼容旧格式 -->
                            <div style="background: #f8f9fa; padding: 15px; border-radius: 4px; margin-bottom: 15px;">
                                <strong>消息列表：</strong>
                                <ul style="margin-top: 10px; margin-bottom: 0;">
                                    <#list headDisplay.messages as message>
                                        <li style="margin-bottom: 5px;">${message}</li>
                                    </#list>
                                </ul>
                            </div>
                        </#if>
                    </#if>
                    <!-- 原始文本预览 -->
                    <div style="margin-top: 15px;">
                        <strong>原始文本：</strong>
                        <pre id="headDisplayText" style="background: #f8f9fa; padding: 10px; border-radius: 4px; overflow-x: auto; font-size: 12px; margin-top: 5px; border: 1px solid #ddd; white-space: pre-wrap; word-wrap: break-word;"></pre>
                    </div>
                <#else>
                    <div class="empty-state" style="width: 100%; text-align: center; padding: 40px; color: #999;">
                        <i class="fa fa-inbox" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                        暂无头部展示配置
                    </div>
                </#if>
            </div>
        </div>

        <!-- 5. Logo 展示 -->
        <div class="config-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 4px; background: #fff;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #333;">
                <i class="fa fa-image"></i> Logo
            </h3>
            <#if config??>
                <img src="${config.logoUrl!''}" alt="Logo" style="max-width: 200px; max-height: 100px; object-fit: contain; border: 1px solid #ddd; padding: 5px; border-radius: 4px;"/>
            <#else>
                <div class="empty-state" style="text-align: center; padding: 20px; color: #999;">
                    <i class="fa fa-inbox" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                    暂无Logo
                </div>
            </#if>
        </div>

        <!-- 6. 商品描述图片展示 -->
        <div class="config-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 4px; background: #fff;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #333;">
                <i class="fa fa-image"></i> 商品描述图片
            </h3>
            <#if config??>
                <img src="${config.productDescriptionUrl!''}" alt="商品描述图片" style="max-width: 500px; max-height: 300px; object-fit: contain; border: 1px solid #ddd; padding: 5px; border-radius: 4px;"/>
            <#else>
                <div class="empty-state" style="text-align: center; padding: 20px; color: #999;">
                    <i class="fa fa-inbox" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                    暂无商品描述图片
                </div>
            </#if>
        </div>

        <!-- 7. 商品描述文本展示 -->
        <div class="config-section" style="margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 4px; background: #fff;">
            <h3 style="margin-top: 0; margin-bottom: 15px; color: #333;">
                <i class="fa fa-file-text"></i> 商品描述
            </h3>
            <#if config??>
                <div style="color: #333; line-height: 1.6; white-space: pre-wrap; background: #f8f9fa; padding: 15px; border-radius: 4px; border: 1px solid #ddd;">${config.productDescription!''}</div>
            <#else>
                <div class="empty-state" style="text-align: center; padding: 20px; color: #999;">
                    <i class="fa fa-inbox" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
                    暂无商品描述
                </div>
            </#if>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function() {
        // 显示原始文本
        <#if config?? && config.headDisplay??>
        var textStr = '${config.headDisplay?js_string}';
        $('#headDisplayText').text(textStr);
        </#if>
    });

    // 编辑配置
    function editConfig(id) {
        $.acooly.framework.edit({
            url: '/manage/shop/shopConfig/edit.html',
            id: id,
            entity: 'shopConfig',
            width: 900,
            height: 700
        });
    }

    // 创建配置
    function createConfig() {
        $.acooly.framework.create({
            url: '/manage/shop/shopConfig/create.html',
            entity: 'shopConfig',
            width: 900,
            height: 700
        });
    }
</script>
