<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${emStamp.id}</dd>
		<dt class="col-sm-3">类型数据名:</dt>
		<dd class="col-sm-9">${emStamp.name}</dd>
		<dt class="col-sm-3">字段名集合:</dt>
		<dd class="col-sm-9">${emStamp.gather}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(emStamp.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(emStamp.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
