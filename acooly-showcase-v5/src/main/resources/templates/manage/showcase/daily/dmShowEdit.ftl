<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmShow_editform" class="form-horizontal" action="/manage/showcase/daily/dmShow/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmShow" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
<#--				<label class="col-sm-3 col-form-label">地区</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入地区..." name="region" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
<#--				</div>-->
				<label class="col-sm-3 col-form-label">地区</label>
				<div class="col-sm-6">
					<select name="region" class="form-control select2bs4" data-options="required:true">
						<#list regionList as v >
							<option value="${v}">${v}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">模版编号</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入模版编号..." name="serialNumber" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">备注</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入备注..." name="remark" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">域名地址</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入域名地址..." name="domain" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
