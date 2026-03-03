<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${shopTeamUserMapping.id}</dd>
		<dt class="col-sm-3">team_id:</dt>
		<dd class="col-sm-9">${shopTeamUserMapping.teamId}</dd>
		<dt class="col-sm-3">user_id:</dt>
		<dd class="col-sm-9">${shopTeamUserMapping.userId}</dd>
		<dt class="col-sm-3">create_time:</dt>
		<dd class="col-sm-9">${(shopTeamUserMapping.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
		<dt class="col-sm-3">update_time:</dt>
		<dd class="col-sm-9">${(shopTeamUserMapping.updateTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
