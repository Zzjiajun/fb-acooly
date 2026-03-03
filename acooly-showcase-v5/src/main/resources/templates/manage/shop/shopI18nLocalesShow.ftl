<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">语言ID:</dt>
		<dd class="col-sm-9">${shopI18nLocales.id}</dd>
		<dt class="col-sm-3">语言代码：zh_CN, en_US, it_IT, ja_JP, zh_HK:</dt>
		<dd class="col-sm-9">${shopI18nLocales.localeCode}</dd>
		<dt class="col-sm-3">语言名称：简体中文, English, Italiano, 日本語, 繁體中文:</dt>
		<dd class="col-sm-9">${shopI18nLocales.localeName}</dd>
		<dt class="col-sm-3">是否默认语言（1是 0否）:</dt>
		<dd class="col-sm-9">${shopI18nLocales.isDefault}</dd>
		<dt class="col-sm-3">是否启用（1启用 0禁用）:</dt>
		<dd class="col-sm-9">${shopI18nLocales.isActive}</dd>
		<dt class="col-sm-3">排序顺序:</dt>
		<dd class="col-sm-9">${shopI18nLocales.sortOrder}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopI18nLocales.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">更新时间:</dt>
		<dd class="col-sm-9">${(shopI18nLocales.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
