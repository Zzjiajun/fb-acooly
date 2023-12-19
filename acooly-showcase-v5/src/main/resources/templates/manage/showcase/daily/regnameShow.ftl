<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${regname.id}</dd>
		<dt class="col-sm-3">user_id:</dt>
		<dd class="col-sm-9">${regname.userId}</dd>
		<dt class="col-sm-3">name:</dt>
		<dd class="col-sm-9">${regname.name}</dd>
		<dt class="col-sm-3">region_name:</dt>
		<dd class="col-sm-9">${regname.regionName}</dd>
		<dt class="col-sm-3">create_time:</dt>
		<dd class="col-sm-9">${regname.createTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${regname.updateTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
	</dl>
</div>
