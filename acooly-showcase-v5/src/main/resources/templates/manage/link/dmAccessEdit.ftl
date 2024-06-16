<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmAccess_editform" class="form-horizontal" action="/manage/link/dmAccess/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmAccess" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">数据中心绑定的id父类</label>
				<div class="col-sm-9">
					<input type="text" name="centerId" placeholder="请输入数据中心绑定的id父类..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">IP地址</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入IP地址..." name="ip" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">访客地区</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入访客地区..." name="region" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">访问路径</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入访问路径..." name="accessPath" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">访问设备</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入访问设备..." name="accessDevice" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">访客类型</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入访客类型..." name="visitorType" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
