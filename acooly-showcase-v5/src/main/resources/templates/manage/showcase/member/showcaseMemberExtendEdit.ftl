<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_showcaseMemberExtend_editform" class="form-horizontal" action="/manage/showcase/member/showcaseMemberExtend/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="showcaseMemberExtend" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">context</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入context..." name="context" class="easyui-validatebox form-control" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">comments</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入comments..." name="comments" class="easyui-validatebox form-control" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
