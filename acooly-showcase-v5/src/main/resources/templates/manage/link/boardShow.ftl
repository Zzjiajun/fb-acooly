<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${board.id}</dd>
		<dt class="col-sm-3">管理员名字:</dt>
		<dd class="col-sm-9">${board.manageName}</dd>
		<dt class="col-sm-3">被管理员工id:</dt>
		<dd class="col-sm-9">${board.attachedName}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(board.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(board.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
