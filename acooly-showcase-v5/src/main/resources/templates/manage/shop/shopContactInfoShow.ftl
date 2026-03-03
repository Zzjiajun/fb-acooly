<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${shopContactInfo.id}</dd>
		<dt class="col-sm-3">-- address, phone, email, line, whatsapp, instagram, ...:</dt>
		<dd class="col-sm-9">${shopContactInfo.type}</dd>
		<dt class="col-sm-3">可选显示名字，例如 "Line":</dt>
		<dd class="col-sm-9">${shopContactInfo.name}</dd>
		<dt class="col-sm-3">实际内容:</dt>
		<dd class="col-sm-9">${shopContactInfo.value}</dd>
		<dt class="col-sm-3">顺序:</dt>
		<dd class="col-sm-9">${shopContactInfo.sortOrder}</dd>
		<dt class="col-sm-3">是否显示:</dt>
		<dd class="col-sm-9">${shopContactInfo.isActive}</dd>
		<dt class="col-sm-3">create_time:</dt>
		<dd class="col-sm-9">${(shopContactInfo.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${(shopContactInfo.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
