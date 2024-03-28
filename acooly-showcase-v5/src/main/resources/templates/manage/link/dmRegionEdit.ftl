<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmRegion_editform" class="form-horizontal" action="/manage/link/dmRegion/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmRegion" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">地区</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入地区..." name="region" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
