<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_emSumdata_editform" class="form-horizontal" action="/manage/stemp/emSumdata/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="emSumdata" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">群名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入群名..." name="groupName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">业务</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入业务..." name="business" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">国家</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入国家..." name="country" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">备注</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入备注..." name="remark" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">电话</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入电话..." name="phone" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">股民</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入股民..." name="share" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">意向</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入意向..." name="intent" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
