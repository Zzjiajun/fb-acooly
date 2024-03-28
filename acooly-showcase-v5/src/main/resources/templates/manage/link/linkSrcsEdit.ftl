<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_linkSrcs_editform" class="form-horizontal" action="/manage/link/linkSrcs/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="linkSrcs" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">链接</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入链接." name="linkSrc" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">user_name</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入user_name..." name="userName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">域名</label>
				<div class="col-sm-9">
					<select name="domain" class="form-control select2bs4" data-options="required:true" required>
						<#list list as list >
							<option value="${list}">${list}</option>
						</#list>
					</select>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
