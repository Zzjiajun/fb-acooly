<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${linkSrcs.id}</dd>
		<dt class="col-sm-3">link_src:</dt>
		<dd class="col-sm-9">${linkSrcs.linkSrc}</dd>
		<dt class="col-sm-3">user_name:</dt>
		<dd class="col-sm-9">${linkSrcs.userName}</dd>
		<dt class="col-sm-3">domain:</dt>
		<dd class="col-sm-9">${linkSrcs.domain}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(linkSrcs.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(linkSrcs.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
