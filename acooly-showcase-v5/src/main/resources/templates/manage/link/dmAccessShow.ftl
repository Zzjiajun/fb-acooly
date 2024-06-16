<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmAccess.id}</dd>
		<dt class="col-sm-3">数据中心绑定的id父类:</dt>
		<dd class="col-sm-9">${dmAccess.centerId}</dd>
		<dt class="col-sm-3">IP地址:</dt>
		<dd class="col-sm-9">${dmAccess.ip}</dd>
		<dt class="col-sm-3">访客地区:</dt>
		<dd class="col-sm-9">${dmAccess.region}</dd>
		<dt class="col-sm-3">访问路径:</dt>
		<dd class="col-sm-9">${dmAccess.accessPath}</dd>
		<dt class="col-sm-3">访问设备:</dt>
		<dd class="col-sm-9">${dmAccess.accessDevice}</dd>
		<dt class="col-sm-3">访客类型:</dt>
		<dd class="col-sm-9">${dmAccess.visitorType}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmAccess.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmAccess.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
