<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmTrolls.id}</dd>
		<dt class="col-sm-3">数据中心绑定的id父类:</dt>
		<dd class="col-sm-9">${dmTrolls.centerId}</dd>
		<dt class="col-sm-3">IP地址:</dt>
		<dd class="col-sm-9">${dmTrolls.ip}</dd>
		<dt class="col-sm-3">访客地区:</dt>
		<dd class="col-sm-9">${dmTrolls.region}</dd>
		<dt class="col-sm-3">访问路径:</dt>
		<dd class="col-sm-9">${dmTrolls.trollsPath}</dd>
		<dt class="col-sm-3">访问设备:</dt>
		<dd class="col-sm-9">${dmTrolls.trollsDevice}</dd>
		<dt class="col-sm-3">访客类型:</dt>
		<dd class="col-sm-9">${dmTrolls.visitorType}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmTrolls.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmTrolls.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
