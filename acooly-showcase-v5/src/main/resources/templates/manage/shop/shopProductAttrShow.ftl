<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">商品ID:</dt>
		<dd class="col-sm-9">${shopProductAttr.productId}</dd>
		<dt class="col-sm-3">属性值ID:</dt>
		<dd class="col-sm-9">${shopProductAttr.attrValueId}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopProductAttr.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
