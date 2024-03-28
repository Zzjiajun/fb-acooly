<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmDomain.id}</dd>
		<dt class="col-sm-3">domain_name:</dt>
		<dd class="col-sm-9">${dmDomain.domainName}</dd>
		<dt class="col-sm-3">link:</dt>
		<dd class="col-sm-9">${dmDomain.link}</dd>
		<dt class="col-sm-3">user_name:</dt>
		<dd class="col-sm-9">${dmDomain.userName}</dd>
		<dt class="col-sm-3">create_time:</dt>
		<dd class="col-sm-9">${(dmDomain.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${(dmDomain.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
