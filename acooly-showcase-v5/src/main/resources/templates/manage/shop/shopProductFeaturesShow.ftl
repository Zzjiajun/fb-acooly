<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">特性ID:</dt>
		<dd class="col-sm-9">${shopProductFeatures.id}</dd>
		<dt class="col-sm-3">商品ID:</dt>
		<dd class="col-sm-9">${shopProductFeatures.productId}</dd>
		<dt class="col-sm-3">特性名称:</dt>
		<dd class="col-sm-9">${shopProductFeatures.feature}</dd>
		<dt class="col-sm-3">特性值:</dt>
		<dd class="col-sm-9">${shopProductFeatures.featureValue}</dd>
		<dt class="col-sm-3">排序顺序:</dt>
		<dd class="col-sm-9">${shopProductFeatures.sortOrder}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopProductFeatures.createdAt?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
