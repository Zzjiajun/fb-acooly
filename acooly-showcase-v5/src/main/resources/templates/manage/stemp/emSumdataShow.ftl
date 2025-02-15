<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${emSumdata.id}</dd>
		<dt class="col-sm-3">群名:</dt>
		<dd class="col-sm-9">${emSumdata.groupName}</dd>
		<dt class="col-sm-3">业务:</dt>
		<dd class="col-sm-9">${emSumdata.business}</dd>
		<dt class="col-sm-3">国家:</dt>
		<dd class="col-sm-9">${emSumdata.country}</dd>
		<dt class="col-sm-3">备注:</dt>
		<dd class="col-sm-9">${emSumdata.remark}</dd>
		<dt class="col-sm-3">电话:</dt>
		<dd class="col-sm-9">${emSumdata.phone}</dd>
		<dt class="col-sm-3">股民:</dt>
		<dd class="col-sm-9">${emSumdata.share}</dd>
		<dt class="col-sm-3">意向:</dt>
		<dd class="col-sm-9">${emSumdata.intent}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(emSumdata.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(emSumdata.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
