<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_linkInt_editform" class="form-horizontal" action="/manage/link/linkInt/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="linkInt" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">轮询目前的数量</label>
				<div class="col-sm-9">
					<input type="text" name="countLink" placeholder="请输入轮询目前的数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">用户名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入用户名..." name="userName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">域名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入域名..." name="domain" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
