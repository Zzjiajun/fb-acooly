<#-- 属性多语言翻译管理弹窗（重构版 - 使用公共 CSS/JS） -->
<link rel="stylesheet" href="/manage/assert/translation-dialog-common.css">
<script src="/manage/assert/translation-dialog-common.js"></script>

<div id="attrTranslationDialog" style="padding: 0;">
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
        // 配置属性翻译弹窗参数
        var translationConfig = {
            entityId: ${attrId!0},
            entityName: '<#if attrName??>${attrName?js_string}<#else></#if>',
            entityIdParam: 'attrId',
            getTranslationsUrl: '/manage/shop/shopAttr/getAttrTranslations.html',
            saveTranslationsUrl: '/manage/shop/shopAttr/batchSaveAttrTranslations.html',
            deleteTranslationUrl: '/manage/shop/shopAttr/deleteAttrTranslation.html',
            entityNameKey: 'attrName',
            includeFieldName: false,  // 属性翻译后端固定为 name，不需要传 fieldName
            fields: [
                {
                    name: 'name',
                    label: '属性名称',
                    placeholder: '请输入属性名称的翻译...'
                }
            ]
        };

        // 初始化翻译弹窗
        TranslationDialog.init(translationConfig);
    });
</script>
