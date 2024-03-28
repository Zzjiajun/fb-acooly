<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmCenter.id}</dd>
		<dt class="col-sm-3">用户名:</dt>
		<dd class="col-sm-9">${dmCenter.userName}</dd>
		<dt class="col-sm-3">地区:</dt>
		<dd class="col-sm-9">${dmCenter.region}</dd>
		<dt class="col-sm-3">像素代码:</dt>
		<dd class="col-sm-9">${dmCenter.pixel}</dd>
		<dt class="col-sm-3">链接地址:</dt>
		<dd class="col-sm-9">${dmCenter.link}</dd>
		<dt class="col-sm-3">主域名:</dt>
		<dd class="col-sm-9">${dmCenter.domain}</dd>
		<dt class="col-sm-3">二级域名:</dt>
		<dd class="col-sm-9">${dmCenter.secondaryDomain}</dd>
		<dt class="col-sm-3">模版地址:</dt>
		<dd class="col-sm-9">${dmCenter.
serialNumber}</dd>
		<dt class="col-sm-3">域名访问量:</dt>
		<dd class="col-sm-9">${dmCenter.visitsNumber}</dd>
		<dt class="col-sm-3">按钮点击数量:</dt>
		<dd class="col-sm-9">${dmCenter.clicksNumber}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmCenter.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmCenter.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
