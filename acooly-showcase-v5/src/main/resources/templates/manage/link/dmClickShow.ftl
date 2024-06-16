<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmClick.id}</dd>
		<dt class="col-sm-3">数据中心id:</dt>
		<dd class="col-sm-9">${dmClick.centerId}</dd>
		<dt class="col-sm-3">点击ip:</dt>
		<dd class="col-sm-9">${dmClick.ip}</dd>
		<dt class="col-sm-3">region:</dt>
		<dd class="col-sm-9">${dmClick.region}</dd>
		<dt class="col-sm-3">点击设备:</dt>
		<dd class="col-sm-9">${dmClick.clickDevice}</dd>
		<dt class="col-sm-3">点击类型:</dt>
		<dd class="col-sm-9">${dmClick.clickType}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmClick.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmClick.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
