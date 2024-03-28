<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmRegion.id}</dd>
		<dt class="col-sm-3">地区:</dt>
		<dd class="col-sm-9">${dmRegion.region}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmRegion.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmRegion.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
