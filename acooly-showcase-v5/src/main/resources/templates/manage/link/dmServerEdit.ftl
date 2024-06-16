<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmServer_editform" class="form-horizontal" action="/manage/link/dmServer/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmServer" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">ip地址</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入ip地址..." name="ip" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">用户名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入用户名..." name="username" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">密码</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入密码..." name="password" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">端口号</label>
				<div class="col-sm-9">
					<input type="text" name="host" placeholder="请输入端口号..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">备注</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入备注..." name="reg" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
