<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmClick_editform" class="form-horizontal" action="/manage/link/dmClick/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmClick" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">数据中心id</label>
				<div class="col-sm-9">
					<input type="text" name="centerId" placeholder="请输入数据中心id..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">点击ip</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入点击ip..." name="ip" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">region</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入region..." name="region" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">点击设备</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入点击设备..." name="clickDevice" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">点击类型</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入点击类型..." name="clickType" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
