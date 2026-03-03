<#-- 属性值多语言翻译管理弹窗（重构版 - 使用公共 CSS/JS） -->
<link rel="stylesheet" href="/manage/assert/translation-dialog-common.css">
<script src="/manage/assert/translation-dialog-common.js"></script>

<div id="attrValueTranslationDialog" style="padding: 0;">
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
        // 配置属性值翻译弹窗参数
        var translationConfig = {
            entityId: ${attrValueId!0},
            entityName: '<#if attrValueName??>${attrValueName?js_string}<#else></#if>',
            entityIdParam: 'attrValueId',
            getTranslationsUrl: '/manage/shop/shopAttrValue/getAttrValueTranslations.html',
            saveTranslationsUrl: '/manage/shop/shopAttrValue/batchSaveAttrValueTranslations.html',
            deleteTranslationUrl: '/manage/shop/shopAttrValue/deleteAttrValueTranslation.html',
            entityNameKey: 'attrValueName',
            includeFieldName: false,  // 属性值翻译后端固定为 value，不需要传 fieldName
            fields: [
                {
                    name: 'value',
                    label: '属性值',
                    placeholder: '请输入属性值的翻译...',
                    hasFieldKey: 'hasName'  // 属性值翻译使用 hasName 字段来存储 value 的翻译状态
                }
            ]
        };

        // 初始化翻译弹窗
        TranslationDialog.init(translationConfig);
    });
</script>
