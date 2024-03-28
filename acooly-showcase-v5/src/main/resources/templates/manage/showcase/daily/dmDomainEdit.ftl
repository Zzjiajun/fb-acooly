<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmDomain_editform" class="form-horizontal" action="/manage/showcase/daily/dmDomain/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmDomain" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">domain_name</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入domain_name..." name="domainName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">link</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入link..." name="link" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">user_name</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入user_name..." name="userName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
        </div>
      </@jodd.form>
    </form>
</div>
