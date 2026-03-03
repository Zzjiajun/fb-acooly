<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">品牌ID:</dt>
		<dd class="col-sm-9">${shopBrand.id}</dd>
		<dt class="col-sm-3">品牌名称:</dt>
		<dd class="col-sm-9">${shopBrand.name}</dd>
		<dt class="col-sm-3">品牌logo:</dt>
		<dd class="col-sm-9">${shopBrand.logo}</dd>
		<dt class="col-sm-3">状态 1启用 0禁用:</dt>
		<dd class="col-sm-9">${shopBrand.status}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopBrand.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">更新时间:</dt>
		<dd class="col-sm-9">${(shopBrand.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
