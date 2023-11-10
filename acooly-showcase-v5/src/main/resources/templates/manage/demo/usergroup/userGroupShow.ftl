<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">ID:</dt>
		<dd class="col-sm-9">${userGroup.id}</dd>
		<dt class="col-sm-3">名称:</dt>
		<dd class="col-sm-9">${userGroup.name}</dd>
		<dt class="col-sm-3">描述:</dt>
		<dd class="col-sm-9">${userGroup.descn}</dd>
		<dt class="col-sm-3">用户名:</dt>
		<dd class="col-sm-9">${userGroup.users}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${userGroup.createTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${userGroup.updateTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">备注:</dt>
		<dd class="col-sm-9">${userGroup.comments}</dd>
	</dl>
</div>
