<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmCondition.id}</dd>
		<dt class="col-sm-3">用户名:</dt>
		<dd class="col-sm-9">${dmCondition.userName}</dd>
		<dt class="col-sm-3">二级域名:</dt>
		<dd class="col-sm-9">${dmCondition.accessAddress}</dd>
		<dt class="col-sm-3">国家限制:</dt>
		<dd class="col-sm-9">${dmCondition.ipCountry}</dd>
		<dt class="col-sm-3">是否添加时区:</dt>
		<dd class="col-sm-9">${dmCondition.timeZone}</dd>
		<dt class="col-sm-3">时区洲:</dt>
		<dd class="col-sm-9">${dmCondition.timeContinent}</dd>
		<dt class="col-sm-3">设备语言:</dt>
		<dd class="col-sm-9">${dmCondition.isChinese}</dd>
		<dt class="col-sm-3">移动设备:</dt>
		<dd class="col-sm-9">${dmCondition.isMobile}</dd>
		<dt class="col-sm-3">指定设备:</dt>
		<dd class="col-sm-9">${dmCondition.isSpecificDevice}</dd>
		<dt class="col-sm-3">访问路径:</dt>
		<dd class="col-sm-9">${dmCondition.isFbclid}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(dmCondition.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmCondition.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
