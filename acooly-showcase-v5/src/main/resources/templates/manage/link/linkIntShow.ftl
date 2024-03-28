<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${linkInt.id}</dd>
		<dt class="col-sm-3">轮询目前的数量:</dt>
		<dd class="col-sm-9">${linkInt.countLink}</dd>
		<dt class="col-sm-3">用户名:</dt>
		<dd class="col-sm-9">${linkInt.userName}</dd>
		<dt class="col-sm-3">域名:</dt>
		<dd class="col-sm-9">${linkInt.domain}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(linkInt.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(linkInt.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
