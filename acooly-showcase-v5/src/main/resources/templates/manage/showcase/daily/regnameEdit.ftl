<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_regname_editform" class="form-horizontal" action="/manage/showcase/daily/regname/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="regname" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">用户id</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="userId" placeholder="请输入user_id..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">用户姓名</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入name..." name="name" class="easyui-validatebox form-control" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">域名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入region_name..." name="regionName" class="easyui-validatebox form-control" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
