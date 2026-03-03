<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopTeam_editform" class="form-horizontal" action="/manage/shop/shopTeam/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopTeam" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<#if action=='create'>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">团队名称 <span class="text-danger">*</span></label>
				<div class="col-sm-9">
					<input type="text" placeholder="请输入团队名称..." name="teamName" class="easyui-validatebox form-control" data-options="validType:['length[1,255]'],required:true" />
					<small class="form-text text-muted">团队分享链接将自动生成</small>
				</div>
			</div>
			<#else>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">团队名称</label>
				<div class="col-sm-9">
					<input type="text" placeholder="请输入团队名称..." name="teamName" class="easyui-validatebox form-control" data-options="validType:['length[1,255]']" value="${(shopTeam.teamName)!}" />
				</div>
			</div>
			</#if>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">选择用户 <span class="text-danger">*</span></label>
				<div class="col-sm-9">
					<select name="userIds" id="manage_shopTeam_userIds" class="form-control select2bs4"
							data-placeholder="请选择后台用户（可多选）..."
							multiple="true" style="width: 100%;">
						<#if availableUsers??>
							<#list availableUsers as user>
								<option value="${user.id}" <#if currentTeamUserIds?? && currentTeamUserIds?seq_contains(user.id)>selected</#if>>
									${user.username}<#if user.realName??> - ${user.realName}</#if>
								</option>
							</#list>
						</#if>
					</select>
					<small class="form-text text-muted">
						<#if action=='create'>只能选择未绑定团队的用户<#else>可以选择未绑定团队的用户或当前团队已绑定的用户</#if>
					</small>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        // 初始化 select2bs4 多选下拉框
        $('#manage_shopTeam_userIds').select2({
            theme: 'bootstrap4',
            width: '100%',
            placeholder: '请选择后台用户（可多选）...',
            allowClear: true
        });
    });
</script>
