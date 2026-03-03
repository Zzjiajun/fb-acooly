<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">主键ID:</dt>
		<dd class="col-sm-9">${shopTrackStats.id}</dd>
		<dt class="col-sm-3">统计类型：daily_site/daily_page/daily_product:</dt>
		<dd class="col-sm-9">${shopTrackStats.statType}</dd>
		<dt class="col-sm-3">统计日期（格式：YYYY-MM-DD）:</dt>
		<dd class="col-sm-9">${(shopTrackStats.statDate?string('yyyy-MM-dd'))!}</dd>
		<dt class="col-sm-3">实体ID（商品ID，stat_type=daily_product时使用）:</dt>
		<dd class="col-sm-9">${shopTrackStats.entityId}</dd>
		<dt class="col-sm-3">实体标识（页面Key，stat_type=daily_page时使用）:</dt>
		<dd class="col-sm-9">${shopTrackStats.entityKey}</dd>
		<dt class="col-sm-3">语言代码（可为空表示全语言统计）:</dt>
		<dd class="col-sm-9">${shopTrackStats.locale}</dd>
		<dt class="col-sm-3">页面浏览量（Page View）:</dt>
		<dd class="col-sm-9">${shopTrackStats.pv}</dd>
		<dt class="col-sm-3">独立访客数（Unique Visitor）:</dt>
		<dd class="col-sm-9">${shopTrackStats.uv}</dd>
		<dt class="col-sm-3">总停留时间（毫秒）:</dt>
		<dd class="col-sm-9">${shopTrackStats.totalDuration}</dd>
		<dt class="col-sm-3">平均停留时间（毫秒）:</dt>
		<dd class="col-sm-9">${shopTrackStats.avgDuration}</dd>
		<dt class="col-sm-3">最大停留时间（毫秒）:</dt>
		<dd class="col-sm-9">${shopTrackStats.maxDuration}</dd>
		<dt class="col-sm-3">最小停留时间（毫秒）:</dt>
		<dd class="col-sm-9">${shopTrackStats.minDuration}</dd>
		<dt class="col-sm-3">跳出率（百分比，0-100）:</dt>
		<dd class="col-sm-9">${shopTrackStats.bounceRate}</dd>
		<dt class="col-sm-3">create_time:</dt>
		<dd class="col-sm-9">${(shopTrackStats.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${(shopTrackStats.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
