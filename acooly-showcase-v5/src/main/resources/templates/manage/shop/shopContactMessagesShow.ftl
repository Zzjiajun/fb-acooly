<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">消息ID:</dt>
		<dd class="col-sm-9">${shopContactMessages.id}</dd>
		<dt class="col-sm-3">联系人姓名:</dt>
		<dd class="col-sm-9">${shopContactMessages.name}</dd>
		<dt class="col-sm-3">联系人邮箱:</dt>
		<dd class="col-sm-9">${shopContactMessages.email}</dd>
		<dt class="col-sm-3">消息主题:</dt>
		<dd class="col-sm-9">${shopContactMessages.subject}</dd>
		<dt class="col-sm-3">消息内容:</dt>
		<dd class="col-sm-9">${shopContactMessages.message}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopContactMessages.createdAt?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
