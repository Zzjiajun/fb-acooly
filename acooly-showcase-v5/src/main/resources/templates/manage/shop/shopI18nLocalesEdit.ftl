<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<style>
    .locale-edit-container {
        background: #f5f6f8;
        padding: 20px;
    }
    
    .form-card {
        background: #fff;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    }
    
    .form-group label {
        color: #374151;
        font-weight: 600;
        font-size: 14px;
    }
    
    .form-control, .select2bs4 {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 8px 12px;
        transition: all 0.2s;
    }
    
    .form-control:focus, .select2bs4:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99,102,241,0.15);
        outline: none;
    }

    .form-hint {
        color: #6b7280;
        font-size: 12px;
        margin-top: 4px;
    }
</style>
<div class="locale-edit-container">
    <div class="form-card">
        <form id="manage_shopI18nLocales_editform" class="form-horizontal" action="/manage/shop/shopI18nLocales/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
            <@jodd.form bean="shopI18nLocales" scope="request">
                <input name="id" type="hidden" />
                <div class="card-body">
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label">语言代码</label>
                        <div class="col-sm-9">
                            <input type="text" name="localeCode" placeholder="例如：zh_CN, en_US, it_IT, ja_JP, zh_HK" class="easyui-validatebox form-control" data-options="validType:['text','length[1,10]']" required="true"/>
                            <div class="form-hint">标准语言代码格式，如：zh_CN（简体中文）、en_US（英语）</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label">语言名称</label>
                        <div class="col-sm-9">
                            <input type="text" name="localeName" placeholder="例如：简体中文, English, Italiano, 日本語, 繁體中文" class="easyui-validatebox form-control" data-options="validType:['text','length[1,50]']" required="true"/>
                            <div class="form-hint">该语言的显示名称</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label">是否默认语言</label>
                        <div class="col-sm-9">
                            <select name="isDefault" class="form-control select2bs4" style="width: 100%;">
                                <option value="0" <#if shopI18nLocales.isDefault?? && shopI18nLocales.isDefault == 0>selected</#if>>否</option>
                                <option value="1" <#if shopI18nLocales.isDefault?? && shopI18nLocales.isDefault == 1>selected</#if>>是</option>
                            </select>
                            <div class="form-hint">设置为默认语言后，将自动取消其他语言的默认状态</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label">是否启用</label>
                        <div class="col-sm-9">
                            <select name="isActive" class="form-control select2bs4" style="width: 100%;">
                                <option value="0" <#if shopI18nLocales.isActive?? && shopI18nLocales.isActive == 0>selected</#if>>禁用</option>
                                <option value="1" <#if shopI18nLocales.isActive?? && shopI18nLocales.isActive == 1>selected</#if>>启用</option>
                            </select>
                            <div class="form-hint">只有启用的语言才会在翻译管理中使用</div>
                        </div>
                    </div>
                    <#-- 排序顺序由后端自动计算，不显示给用户 -->
                    <input name="sortOrder" type="hidden" value="${shopI18nLocales.sortOrder!0}"/>
                </div>
            </@jodd.form>
        </form>
    </div>
</div>

<script>
    $(document).ready(function() {
        // 初始化 select2bs4
        if (typeof $ !== 'undefined' && $.fn.select2) {
            $('.select2bs4').select2({
                theme: 'bootstrap4',
                width: '100%'
            });
        }
    });
</script>
