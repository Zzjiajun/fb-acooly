<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${shopConfig.id}</dd>
		<dt class="col-sm-3">轮播商品:</dt>
		<dd class="col-sm-9">${shopConfig.carouselProducts}</dd>
		<dt class="col-sm-3">展示商品:</dt>
		<dd class="col-sm-9">${shopConfig.displayProducts}</dd>
		<dt class="col-sm-3">展示评论:</dt>
		<dd class="col-sm-9">${shopConfig.showComments}</dd>
		<dt class="col-sm-3">头部展示:</dt>
		<dd class="col-sm-9">${shopConfig.headDisplay}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopConfig.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${(shopConfig.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
