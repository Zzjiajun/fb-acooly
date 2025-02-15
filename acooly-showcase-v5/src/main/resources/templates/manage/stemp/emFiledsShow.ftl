<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${emFileds.id}</dd>
		<dt class="col-sm-3">字段名称:</dt>
		<dd class="col-sm-9">${emFileds.name}</dd>
		<dt class="col-sm-3">字段备注名称:</dt>
		<dd class="col-sm-9">${emFileds.remark}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(emFileds.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(emFileds.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
