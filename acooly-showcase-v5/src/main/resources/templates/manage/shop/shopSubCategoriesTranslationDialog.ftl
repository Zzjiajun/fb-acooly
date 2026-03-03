<#-- 子分类多语言翻译管理弹窗（重构版 - 使用公共 CSS/JS） -->
<link rel="stylesheet" href="/manage/assert/translation-dialog-common.css">
<script src="/manage/assert/translation-dialog-common.js"></script>

<div id="subCategoryTranslationDialog" style="padding: 0;">
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
        // 配置子分类翻译弹窗参数
        var translationConfig = {
            entityId: ${categoryId!0},
            entityName: '<#if categoryName??>${categoryName?js_string}<#else></#if>',
            entityIdParam: 'categoryId',
            getTranslationsUrl: '/manage/shop/shopSubCategories/getSubCategoryTranslations.html',
            saveTranslationsUrl: '/manage/shop/shopSubCategories/batchSaveSubCategoryTranslations.html',
            deleteTranslationUrl: '/manage/shop/shopSubCategories/deleteSubCategoryTranslation.html',
            entityNameKey: 'categoryName',
            includeFieldName: false,  // 子分类翻译后端固定为 name，不需要传 fieldName
            fields: [
                {
                    name: 'name',
                    label: '子分类名称',
                    placeholder: '请输入子分类名称的翻译...'
                }
            ]
        };

        // 初始化翻译弹窗
        TranslationDialog.init(translationConfig);
    });
</script>
