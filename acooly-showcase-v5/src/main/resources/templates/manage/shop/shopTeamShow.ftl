<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${shopTeam.id}</dd>
		<dt class="col-sm-3">团队分享链接:</dt>
		<dd class="col-sm-9">${shopTeam.teamLink}</dd>
		<dt class="col-sm-3">团队名称:</dt>
		<dd class="col-sm-9">${shopTeam.teamName}</dd>
		<dt class="col-sm-3">user_ids:</dt>
		<dd class="col-sm-9">${shopTeam.userIds}</dd>
		<dt class="col-sm-3">创建时间:</dt>
		<dd class="col-sm-9">${(shopTeam.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">修改时间:</dt>
		<dd class="col-sm-9">${(shopTeam.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
