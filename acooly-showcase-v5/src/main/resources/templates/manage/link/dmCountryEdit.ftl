<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmCountry_editform" class="form-horizontal" action="/manage/link/dmCountry/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmCountry" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">国家</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入国家..." name="country" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">中文名称</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入中文名称..." name="name" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
