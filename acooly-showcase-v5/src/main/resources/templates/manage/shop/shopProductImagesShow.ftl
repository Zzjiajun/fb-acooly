<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">图片ID:</dt>
		<dd class="col-sm-9">${shopProductImages.id}</dd>
		<dt class="col-sm-3">商品ID:</dt>
		<dd class="col-sm-9">${shopProductImages.productId}</dd>
		<dt class="col-sm-3">图片URL:</dt>
		<dd class="col-sm-9">${shopProductImages.imageUrl}</dd>
		<dt class="col-sm-3">排序顺序:</dt>
		<dd class="col-sm-9">${shopProductImages.sortOrder}</dd>
		<dt class="col-sm-3">是否为主图:</dt>
		<dd class="col-sm-9">${shopProductImages.isPrimary}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopProductImages.createdAt?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
