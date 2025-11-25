<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${shopOrderItems.id}</dd>
		<dt class="col-sm-3">order_id:</dt>
		<dd class="col-sm-9">${shopOrderItems.orderId}</dd>
		<dt class="col-sm-3">product_id:</dt>
		<dd class="col-sm-9">${shopOrderItems.productId}</dd>
		<dt class="col-sm-3">product_name:</dt>
		<dd class="col-sm-9">${shopOrderItems.productName}</dd>
		<dt class="col-sm-3">quantity:</dt>
		<dd class="col-sm-9">${shopOrderItems.quantity}</dd>
		<dt class="col-sm-3">price:</dt>
		<dd class="col-sm-9">${shopOrderItems.price}</dd>
		<dt class="col-sm-3">total_price:</dt>
		<dd class="col-sm-9">${shopOrderItems.totalPrice}</dd>
		<dt class="col-sm-3">create_time:</dt>
		<dd class="col-sm-9">${(shopOrderItems.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${(shopOrderItems.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
