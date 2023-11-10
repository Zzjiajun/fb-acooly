<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${showcaseMemberExtend.id}</dd>
		<dt class="col-sm-3">context:</dt>
		<dd class="col-sm-9">${showcaseMemberExtend.context}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${showcaseMemberExtend.createTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${showcaseMemberExtend.updateTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">comments:</dt>
		<dd class="col-sm-9">${showcaseMemberExtend.comments}</dd>
	</dl>
</div>
