<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">群名:</dt>
		<dd class="col-sm-9">${dmSieve.name}</dd>
		<dt class="col-sm-3">业务:</dt>
		<dd class="col-sm-9">${dmSieve.business}</dd>
		<dt class="col-sm-3">国家:</dt>
		<dd class="col-sm-9">${dmSieve.country}</dd>
		<dt class="col-sm-3">remark:</dt>
		<dd class="col-sm-9">${dmSieve.remark}</dd>
		<dt class="col-sm-3">电话号:</dt>
		<dd class="col-sm-9">${dmSieve.phone}</dd>
<#--		<dt class="col-sm-3">是否股民:</dt>-->
<#--		<dd class="col-sm-9">${dmSieve.decide}</dd>-->
		<dt class="col-sm-3">意向:</dt>
		<dd class="col-sm-9">${dmSieve.intent}</dd>
		<dt class="col-sm-3">时间:</dt>
		<dd class="col-sm-9">${(dmSieve.createTime?string('yyyy-MM-dd HH:mm:ss'))!}</dd>
	</dl>
</div>
