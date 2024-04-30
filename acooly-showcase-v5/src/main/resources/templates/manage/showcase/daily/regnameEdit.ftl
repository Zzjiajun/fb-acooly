<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<style>
	.pixel-id-container {
		display: inline-block;
		margin: 5px;
		padding: 5px;
		border: 1px solid #ccc;
		border-radius: 5px;
	}
</style>
<div>
	<form id="manage_regname_editform" class="form-horizontal" action="/manage/showcase/daily/regname/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="regname" scope="request">
			<input name="id" type="hidden" />
			<div class="card-body">
				<#--			<div class="form-group row">-->
				<#--				<label class="col-sm-3 col-form-label">用户id</label>-->
				<#--				<div class="col-sm-9">-->
				<#--					<input type="text" name="userId" placeholder="请输入user_id..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>-->
				<#--				</div>-->
				<#--			</div>-->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">持有者</label>
					<div class="col-sm-9">
						<#--					<textarea rows="3" cols="40" placeholder="请输入name..." name="name" class="easyui-validatebox form-control" ></textarea>-->
						<select name="name" class="form-control select2bs4" data-options="required:true">
							<#list mapName1 as k,v >
								<option value="${k}">${k}:${v}</option>
							</#list>
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">域名</label>
					<div class="col-sm-9">
						<textarea rows="3"  id="inputTextArea" cols="40" placeholder="请输入region_name..." name="regionName" class="easyui-validatebox form-control" ></textarea>
					</div>
				</div>
			</div>
		</@jodd.form>
	</form>


	<script>
		// function formatInput() {
		// 	var inputTextArea = document.getElementById('inputTextArea');
		// 	var inputValue = inputTextArea.value.replace(/\D/g, ''); // 移除非数字字符
		// 	var formattedValue = '';
		// 	for (var i = 0; i < inputValue.length; i++) {
		// 		formattedValue += inputValue[i];
		// 		if ((i + 1) % 15 === 0) { // 每15个数字添加换行符
		// 			formattedValue += ","+'\n';
		// 		}
		// 	}
		// 	inputTextArea.value = formattedValue;
		// }
	</script>
</div>
