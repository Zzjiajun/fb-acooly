<#-- 商品多语言翻译管理弹窗（重构版 - 使用公共 CSS/JS） -->
<link rel="stylesheet" href="/manage/assert/translation-dialog-common.css">
<script src="/manage/assert/translation-dialog-common.js"></script>

<div id="productTranslationDialog" style="padding: 0;">
    <div class="translation-dialog-container">
        <div class="locale-list-panel">
            <div class="locale-list-title">
                <i class="fa fa-globe"></i> 支持的语言
            </div>
            <div id="localeListContainer">
                <div class="loading-container">
                    <i class="fa fa-spinner fa-spin"></i>
                    <div>加载中...</div>
                </div>
            </div>
        </div>

        <div class="translation-editor-panel">
            <div id="translationEditorContainer">
                <div class="empty-state">
                    <i class="fa fa-language"></i>
                    <div>请从左侧选择语言开始编辑翻译</div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        // 配置商品翻译弹窗参数
        var translationConfig = {
            entityId: ${productId!0},
            entityName: '<#if productName??>${productName?js_string}<#else></#if>',
            entityIdParam: 'productId',
            getTranslationsUrl: '/manage/shop/shopProducts/getProductTranslations.html',
            saveTranslationsUrl: '/manage/shop/shopProducts/batchSaveProductTranslations.html',
            deleteTranslationUrl: '/manage/shop/shopProducts/deleteProductTranslation.html',
            entityNameKey: 'productName',
            includeFieldName: true,  // 商品翻译需要 fieldName 字段
            fields: [
                {
                    name: 'name',
                    label: '商品名称',
                    placeholder: '请输入商品名称的翻译...'
                },
                {
                    name: 'description',
                    label: '商品描述',
                    placeholder: '请输入商品描述的翻译...'
                }
            ]
        };

        // 初始化翻译弹窗
        TranslationDialog.init(translationConfig);
    });
</script>
