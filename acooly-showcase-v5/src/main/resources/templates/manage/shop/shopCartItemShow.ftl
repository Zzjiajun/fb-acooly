<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">购物车项ID:</dt>
		<dd class="col-sm-9">${shopCartItem.id}</dd>
		<dt class="col-sm-3">cart_id:</dt>
		<dd class="col-sm-9">${shopCartItem.cartId}</dd>
		<dt class="col-sm-3">用户ID:</dt>
		<dd class="col-sm-9">${shopCartItem.userId}</dd>
		<dt class="col-sm-3">商品ID:</dt>
		<dd class="col-sm-9">${shopCartItem.productId}</dd>
		<dt class="col-sm-3">商品数量:</dt>
		<dd class="col-sm-9">${shopCartItem.quantity}</dd>
		<dt class="col-sm-3">price:</dt>
		<dd class="col-sm-9">${shopCartItem.price}</dd>
		<dt class="col-sm-3">total_price:</dt>
		<dd class="col-sm-9">${shopCartItem.totalPrice}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopCartItem.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">更新时间:</dt>
		<dd class="col-sm-9">${(shopCartItem.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
