<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_emSumdata_editform" class="form-horizontal" action="/manage/stemp/emSumdata/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="emSumdata" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row" <#if !gatherList?seq_contains("groupName")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">群名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入群名..." name="groupName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row" <#if !gatherList?seq_contains("business")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">业务</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入业务..." name="business" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row" <#if !gatherList?seq_contains("country")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">国家</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入国家..." name="country" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row" <#if !gatherList?seq_contains("remark")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">备注</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入备注..." name="remark" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row" <#if !gatherList?seq_contains("phone")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">电话</label>
				<div class="col-sm-9">
					<input type="text" placeholder="请输入电话号码..." name="phone"
						   class="easyui-validatebox form-control form-words"
						   data-words="255"
						   data-options="validType:'phoneValid'">
				</div>
			</div>

			<div class="form-group row" <#if !gatherList?seq_contains("name")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">名字</label>
				<div class="col-sm-9">
					<input type="text"  placeholder="请输入名字..." name="name" class="easyui-validatebox form-control form-words" data-words="255" 	/>
				</div>
			</div>
			<div class="form-group row" <#if !gatherList?seq_contains("email")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">邮箱</label>
				<div class="col-sm-9">
					<input type="text"
							  placeholder="请输入邮箱..."
							  name="email"
							  class="easyui-validatebox form-control form-words"
							  data-words="255"
							  data-options="validType:'emailOrEmpty'"/>
				</div>
			</div>

			<div class="form-group row" <#if !gatherList?seq_contains("share")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">股民</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入股民..." name="share" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row" <#if !gatherList?seq_contains("intent")>style="display: none;"</#if>>
				<label class="col-sm-3 col-form-label">意向</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入意向..." name="intent" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row" style="display: none">
				<label class="col-sm-3 col-form-label"></label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40"  name="stampId" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
<script>
	$(function(){
		var stampId = "${stampId}";
		console.log(stampId);
		if (stampId) {
			// 使用textarea而不是input
			$("textarea[name='stampId']").val(stampId);
		}
	});



</script>

<script>
	// 添加自定义验证规则（需在页面中定义）
	$.extend($.fn.validatebox.defaults.rules, {
		emailOrEmpty: {
			validator: function(value){
				// 允许空值，非空时验证邮箱格式
				return value === '' || /^[\w.-]+@[a-zA-Z_-]+?\.[a-zA-Z]{2,3}$/.test(value);
			},
			message: '请输入有效的邮箱地址'
		}
	});



	// 扩展easyui验证规则
	$.extend($.fn.validatebox.defaults.rules, {
		phoneValid: {
			validator: function(value){
				// 国际电话号码正则（允许+开头，包含数字、空格、括号和短横线）
				var reg = /^(\+?\d{1,4}[\s-]?)?($\d{1,4}$|\d{1,4})[\s-]?\d{3,}[\s-]?\d{3,}$/;
				return reg.test(value) || value === "";
			},
			message: '请输入有效的电话号码（示例：+86 13812345678 或 0510-1234567）'
		}
	});
</script>

