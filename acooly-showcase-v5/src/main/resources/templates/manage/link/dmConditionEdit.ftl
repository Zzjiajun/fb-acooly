<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmCondition_editform" class="form-horizontal" action="/manage/link/dmCondition/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmCondition" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">用户名</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入用户名..." name="userName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">二级域名</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入二级域名..." name="accessAddress" class="easyui-validatebox form-control form-words" data-words="255"  ></textarea>-->
<#--				</div>-->
<#--			</div>-->
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">Ip</label>
				<div class="col-sm-9">
					<select name="isIp"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list statusMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">Ip国家</label>
				<div class="col-sm-9">
					<select name="ipCountry"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list ipMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">是否添加时区</label>
				<div class="col-sm-9">
					<select name="timeZone"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list statusMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">时区洲</label>
				<div class="col-sm-9">
					<select name="timeContinent"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list conMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">禁止中文设备语言</label>
				<div class="col-sm-9">
					<select name="isChinese"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list statusMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">移动设备</label>
				<div class="col-sm-9">
					<select name="isMobile"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list statusMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">指定设备</label>
				<div class="col-sm-9">
					<select name="isSpecificDevice"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list statusMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">VPN检查</label>
				<div class="col-sm-9">
					<select name="isVpn"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list statusMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
