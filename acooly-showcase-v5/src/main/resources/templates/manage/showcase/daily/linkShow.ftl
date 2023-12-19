<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${link.id}</dd>
		<dt class="col-sm-3">用户id:</dt>
		<dd class="col-sm-9">${link.userId}</dd>
		<dt class="col-sm-3">持有者:</dt>
		<dd class="col-sm-9">${link.holder}</dd>
		<dt class="col-sm-3">一级域名:</dt>
		<dd class="col-sm-9">${link.regionName}</dd>
		<dt class="col-sm-3">二级域名:</dt>
		<dd class="col-sm-9">${link.domain}</dd>
		<dt class="col-sm-3">访问地址:</dt>
		<dd class="col-sm-9">${link.accessAddress}</dd>
		<dt class="col-sm-3">所属地区:</dt>
		<dd class="col-sm-9">${link.regional}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${link.createTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${link.updateTime?string('yyyy-MM-dd HH:mm:ss')}</dd>
	</dl>
</div>
