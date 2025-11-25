<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">评论ID:</dt>
		<dd class="col-sm-9">${shopReviews.id}</dd>
		<dt class="col-sm-3">商品ID:</dt>
		<dd class="col-sm-9">${shopReviews.productId}</dd>
		<dt class="col-sm-3">用户ID:</dt>
		<dd class="col-sm-9">${shopReviews.userId}</dd>
		<dt class="col-sm-3">评分（1-5）:</dt>
		<dd class="col-sm-9">${shopReviews.rating}</dd>
		<dt class="col-sm-3">评论内容:</dt>
		<dd class="col-sm-9">${shopReviews.comment}</dd>
		<dt class="col-sm-3">是否验证购买:</dt>
		<dd class="col-sm-9">${shopReviews.isVerified}</dd>
		<dt class="col-sm-3">有用数量:</dt>
		<dd class="col-sm-9">${shopReviews.helpfulCount}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopReviews.createdAt?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
