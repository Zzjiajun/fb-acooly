<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmModles_editform" class="form-horizontal" action="/manage/link/dmModles/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmModles" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">机型名字</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入机型名字..." name="modelName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">宽度</label>
				<div class="col-sm-9">
					<input type="text" name="screenWidth" placeholder="请输入宽度..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">screen_height</label>
				<div class="col-sm-9">
					<input type="text" name="screenHeight" placeholder="请输入screen_height..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">像素</label>
				<div class="col-sm-9">
					<input type="text" name="pixelRatio" placeholder="请输入像素..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">is_delete</label>
				<div class="col-sm-9">
					<input type="text" name="isDelete" placeholder="请输入is_delete..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
