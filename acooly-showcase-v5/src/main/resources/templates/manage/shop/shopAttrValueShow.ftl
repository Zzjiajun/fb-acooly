<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">属性值ID:</dt>
		<dd class="col-sm-9">${shopAttrValue.id}</dd>
		<dt class="col-sm-3">属性ID:</dt>
		<dd class="col-sm-9">${shopAttrValue.attrId}</dd>
		<dt class="col-sm-3">属性值（男/女/中性）:</dt>
		<dd class="col-sm-9">${shopAttrValue.value}</dd>
		<dt class="col-sm-3">排序:</dt>
		<dd class="col-sm-9">${shopAttrValue.sort}</dd>
		<dt class="col-sm-3">状态:</dt>
		<dd class="col-sm-9">${shopAttrValue.status}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopAttrValue.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">更新时间:</dt>
		<dd class="col-sm-9">${(shopAttrValue.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
