<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmObserverPermission.id}</dd>
		<dt class="col-sm-3">观察者用户ID:</dt>
		<dd class="col-sm-9">${dmObserverPermission.userId}</dd>
		<dt class="col-sm-3">dmCenter记录ID:</dt>
		<dd class="col-sm-9">${dmObserverPermission.dmCenterId}</dd>
		<dt class="col-sm-3">授权时间:</dt>
		<dd class="col-sm-9">${(dmObserverPermission.grantTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">授权人:</dt>
		<dd class="col-sm-9">${dmObserverPermission.grantBy}</dd>
		<dt class="col-sm-3">状态：1=有效，0=无效:</dt>
		<dd class="col-sm-9">${dmObserverPermission.status}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmObserverPermission.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmObserverPermission.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
