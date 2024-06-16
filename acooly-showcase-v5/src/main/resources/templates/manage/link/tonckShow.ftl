<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${tonck.id}</dd>
		<dt class="col-sm-3">图片识别tonck:</dt>
		<dd class="col-sm-9">${tonck.tonck}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(tonck.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(tonck.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
