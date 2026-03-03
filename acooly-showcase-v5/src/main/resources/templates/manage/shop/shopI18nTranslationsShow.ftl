<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">翻译ID:</dt>
		<dd class="col-sm-9">${shopI18nTranslations.id}</dd>
		<dt class="col-sm-3">实体类型：product:</dt>
		<dd class="col-sm-9">${shopI18nTranslations.entityType}</dd>
		<dt class="col-sm-3">实体ID，对应原表的id字段:</dt>
		<dd class="col-sm-9">${shopI18nTranslations.entityId}</dd>
		<dt class="col-sm-3">字段名称：name:</dt>
		<dd class="col-sm-9">${shopI18nTranslations.fieldName}</dd>
		<dt class="col-sm-3">语言代码：zh_CN:</dt>
		<dd class="col-sm-9">${shopI18nTranslations.locale}</dd>
		<dt class="col-sm-3">翻译内容:</dt>
		<dd class="col-sm-9">${shopI18nTranslations.translation}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopI18nTranslations.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">更新时间:</dt>
		<dd class="col-sm-9">${(shopI18nTranslations.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
