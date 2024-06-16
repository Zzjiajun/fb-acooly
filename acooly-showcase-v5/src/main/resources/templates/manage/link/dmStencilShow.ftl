<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmStencil.id}</dd>
		<dt class="col-sm-3">地址:</dt>
		<dd class="col-sm-9">${dmStencil.pathName}</dd>
		<dt class="col-sm-3">备注:</dt>
		<dd class="col-sm-9">${dmStencil.reg}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmStencil.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmStencil.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
