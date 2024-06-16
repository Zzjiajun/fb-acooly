<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmServer.id}</dd>
		<dt class="col-sm-3">ip地址:</dt>
		<dd class="col-sm-9">${dmServer.ip}</dd>
		<dt class="col-sm-3">用户名:</dt>
		<dd class="col-sm-9">${dmServer.username}</dd>
		<dt class="col-sm-3">密码:</dt>
		<dd class="col-sm-9">${dmServer.password}</dd>
		<dt class="col-sm-3">端口号:</dt>
		<dd class="col-sm-9">${dmServer.host}</dd>
		<dt class="col-sm-3">备注:</dt>
		<dd class="col-sm-9">${dmServer.reg}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmServer.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmServer.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
