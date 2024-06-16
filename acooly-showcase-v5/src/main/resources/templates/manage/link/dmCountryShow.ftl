<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmCountry.id}</dd>
		<dt class="col-sm-3">国家:</dt>
		<dd class="col-sm-9">${dmCountry.country}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmCountry.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmCountry.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">中文名称:</dt>
		<dd class="col-sm-9">${dmCountry.name}</dd>
	</dl>
</div>
