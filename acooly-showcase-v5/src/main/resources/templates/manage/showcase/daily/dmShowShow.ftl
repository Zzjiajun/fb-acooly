<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmShow.id}</dd>
		<dt class="col-sm-3">地区:</dt>
		<dd class="col-sm-9">${dmShow.region}</dd>
		<dt class="col-sm-3">模版编号:</dt>
		<dd class="col-sm-9">${dmShow.serialNumber}</dd>
		<dt class="col-sm-3">备注:</dt>
		<dd class="col-sm-9">${dmShow.remark}</dd>
		<dt class="col-sm-3">添加时间:</dt>
		<dd class="col-sm-9">${(dmShow.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmShow.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">域名地址:</dt>
		<dd class="col-sm-9">${dmShow.domain}</dd>
	</dl>
</div>
