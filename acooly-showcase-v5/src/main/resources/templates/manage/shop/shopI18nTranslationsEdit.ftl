<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopI18nTranslations_editform" class="form-horizontal" action="/manage/shop/shopI18nTranslations/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopI18nTranslations" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">实体类型</label>
				<div class="col-sm-9">
					<select name="entityType" id="entityTypeSelect" class="form-control select2bs4" required>
						<option value="">请选择实体类型</option>
						<option value="product" <#if shopI18nTranslations.entityType?? && shopI18nTranslations.entityType == "product">selected</#if>>商品 (product)</option>
						<option value="parent_category" <#if shopI18nTranslations.entityType?? && shopI18nTranslations.entityType == "parent_category">selected</#if>>父级分类 (parent_category)</option>
						<option value="sub_category" <#if shopI18nTranslations.entityType?? && shopI18nTranslations.entityType == "sub_category">selected</#if>>子级分类 (sub_category)</option>
						<option value="brand" <#if shopI18nTranslations.entityType?? && shopI18nTranslations.entityType == "brand">selected</#if>>品牌 (brand)</option>
						<option value="attr" <#if shopI18nTranslations.entityType?? && shopI18nTranslations.entityType == "attr">selected</#if>>属性 (attr)</option>
						<option value="attr_value" <#if shopI18nTranslations.entityType?? && shopI18nTranslations.entityType == "attr_value">selected</#if>>属性值 (attr_value)</option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">实体ID</label>
				<div class="col-sm-9">
					<input type="text" name="entityId" placeholder="请输入实体ID（对应原表的id字段）..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">字段名称</label>
				<div class="col-sm-9">
					<select name="fieldName" id="fieldNameSelect" class="form-control select2bs4" required>
						<option value="">请选择字段名称</option>
						<option value="name" <#if shopI18nTranslations.fieldName?? && shopI18nTranslations.fieldName == "name">selected</#if>>名称 (name)</option>
						<option value="description" <#if shopI18nTranslations.fieldName?? && shopI18nTranslations.fieldName == "description">selected</#if>>描述 (description)</option>
						<option value="value" <#if shopI18nTranslations.fieldName?? && shopI18nTranslations.fieldName == "value">selected</#if>>值 (value)</option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">语言代码</label>
				<div class="col-sm-9">
					<select name="locale" id="localeSelect" class="form-control select2bs4" required>
						<option value="">请选择语言</option>
						<#if localeMap??>
							<#list localeMap?keys as localeCode>
								<option value="${localeCode}" <#if shopI18nTranslations.locale?? && shopI18nTranslations.locale == localeCode>selected</#if>>${localeMap[localeCode]} (${localeCode})</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">翻译内容</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入翻译内容..." name="translation" class="easyui-validatebox form-control form-words" data-words="999,999,999" data-options="required:true"></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>

<script>
    $(function() {
        // 初始化 select2bs4
        $('#entityTypeSelect, #fieldNameSelect, #localeSelect').select2({
            theme: 'bootstrap4',
            placeholder: function() {
                return $(this).data('placeholder') || '请选择...';
            },
            allowClear: true,
            width: '100%'
        });
    });
</script>
