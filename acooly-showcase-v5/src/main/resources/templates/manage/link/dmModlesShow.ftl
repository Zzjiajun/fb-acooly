<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmModles.id}</dd>
		<dt class="col-sm-3">机型名字:</dt>
		<dd class="col-sm-9">${dmModles.modelName}</dd>
		<dt class="col-sm-3">宽度:</dt>
		<dd class="col-sm-9">${dmModles.screenWidth}</dd>
		<dt class="col-sm-3">screen_height:</dt>
		<dd class="col-sm-9">${dmModles.screenHeight}</dd>
		<dt class="col-sm-3">像素:</dt>
		<dd class="col-sm-9">${dmModles.pixelRatio}</dd>
		<dt class="col-sm-3">is_delete:</dt>
		<dd class="col-sm-9">${dmModles.isDelete}</dd>
		<dt class="col-sm-3">注册时间:</dt>
		<dd class="col-sm-9">${(dmModles.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(dmModles.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
