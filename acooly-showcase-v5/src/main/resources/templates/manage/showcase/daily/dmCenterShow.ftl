<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">id:</dt>
		<dd class="col-sm-9">${dmCenter.id}</dd>
		<dt class="col-sm-3">用户名:</dt>
		<dd class="col-sm-9">${dmCenter.userName}</dd>
		<dt class="col-sm-3">地区:</dt>
		<dd class="col-sm-9">${dmCenter.region}</dd>
		<dt class="col-sm-3">广告类型:</dt>
		<dd class="col-sm-9"><#if dmCenter.displayOption==1>落地页<#else>表单</#if></dd>
		<dt class="col-sm-3">像素类型:</dt>
		<dd class="col-sm-9">
			<#if dmCenter.pixelType=='FB'>Facebook
			<#elseif dmCenter.pixelType=='TK'>TikTok
			<#elseif dmCenter.pixelType=='GOOGLE'>Google Ads
			<#else>未设置</#if>
		</dd>
		<dt class="col-sm-3">像素代码:</dt>
		<dd class="col-sm-9">${dmCenter.pixel}</dd>
		<#if dmCenter.pixelType=='GOOGLE'>
		<dt class="col-sm-3">Google AW ID:</dt>
		<dd class="col-sm-9">${dmCenter.googleAwId!}</dd>
		<dt class="col-sm-3">Google Conversion ID:</dt>
		<dd class="col-sm-9">${dmCenter.googleConversionId!}</dd>
		</#if>
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
