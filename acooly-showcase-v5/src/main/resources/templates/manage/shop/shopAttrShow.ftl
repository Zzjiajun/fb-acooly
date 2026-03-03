<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">属性ID:</dt>
		<dd class="col-sm-9">${shopAttr.id}</dd>
		<dt class="col-sm-3">属性名称（性别/风格/材质）:</dt>
		<dd class="col-sm-9">${shopAttr.name}</dd>
		<dt class="col-sm-3">状态:</dt>
		<dd class="col-sm-9">${shopAttr.status}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopAttr.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">更新时间:</dt>
		<dd class="col-sm-9">${(shopAttr.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
