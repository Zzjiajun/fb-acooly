<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">用户ID:</dt>
		<dd class="col-sm-9">${shopUsers.id}</dd>
		<dt class="col-sm-3">邮箱地址:</dt>
		<dd class="col-sm-9">${shopUsers.email}</dd>
		<dt class="col-sm-3">密码（加密存储）:</dt>
		<dd class="col-sm-9">${shopUsers.password}</dd>
		<dt class="col-sm-3">用户姓名:</dt>
		<dd class="col-sm-9">${shopUsers.name}</dd>
		<dt class="col-sm-3">头像URL:</dt>
		<dd class="col-sm-9">${shopUsers.avatar}</dd>
		<dt class="col-sm-3">第三方平台用户ID:</dt>
		<dd class="col-sm-9">${shopUsers.thirdPartyId}</dd>
		<dt class="col-sm-3">登录方式：GOOGLE, FACEBOOK, TIKTOK, LOCAL:</dt>
		<dd class="col-sm-9">${shopUsers.provider}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopUsers.createdAt?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">更新时间:</dt>
		<dd class="col-sm-9">${(shopUsers.updatedAt?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
