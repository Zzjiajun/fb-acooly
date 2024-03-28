<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmPixel.id}</dd>
		<dt class="col-sm-3">domain:</dt>
		<dd class="col-sm-9">${dmPixel.domain}</dd>
		<dt class="col-sm-3">pixel_id:</dt>
		<dd class="col-sm-9">${dmPixel.pixelId}</dd>
		<dt class="col-sm-3">create_time:</dt>
		<dd class="col-sm-9">${(dmPixel.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${(dmPixel.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
