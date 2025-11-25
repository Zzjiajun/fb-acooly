<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopContactMessages_editform" class="form-horizontal" action="/manage/shop/shopContactMessages/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopContactMessages" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">联系人姓名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入联系人姓名..." name="name" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">联系人邮箱</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入联系人邮箱..." name="email" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">消息主题</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入消息主题..." name="subject" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">消息内容</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入消息内容..." name="message" class="easyui-validatebox form-control form-words" data-words="999,999,999" data-options="required:true"></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">创建时间</label>
				<div class="col-sm-9">
					<input type="text" name="createdAt" placeholder="请输入创建时间..." class="easyui-validatebox form-control" value="<#if shopContactMessages.createdAt??>${shopContactMessages.createdAt?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
