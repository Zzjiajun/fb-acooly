<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmCondition_editform" class="form-horizontal" action="/manage/link/dmCondition/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmCondition" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">IP限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isIp" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="isIp" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">IP国家限制</label>
				<div class="col-sm-3">
					<select name="ipCountry"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list ipMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">时区限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="timeZone" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="timeZone" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">时区洲</label>
				<div class="col-sm-3">
					<select name="timeContinent"  class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list conMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">语言限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isChinese" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="isChinese" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">语言选项</label>
				<div class="col-sm-3">
					<select name="language" class="form-control select2bs4" data-options="required:true"  multiple="true">
						<option value=" "> </option>
						<#list languageMap as k, v>
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">指定设备限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isSpecificDevice" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="isSpecificDevice" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">VPN检查</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isVpn" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="isVpn" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">IP访问限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="ipLimits" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="ipLimits" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">爬虫机器人</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isRobot" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="isRobot" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">限制ios系统版本</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<select name="iosVersion"  class="form-control select2bs4" data-options="required:true" required>
							<option value=" "> </option>
							<#list iosVersionMap as k,v >
								<option value="${v}">${k}</option>
							</#list>
						</select>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">限制安卓系统版本</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<select name="andVersion"  class="form-control select2bs4" data-options="required:true" required>
							<option value=" "> </option>
							<#list androidVersionMap as k,v >
								<option value="${v}">${k}</option>
							</#list>
						</select>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">无法识别设备</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isIdentify" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="isIdentify" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>

				<label class="col-sm-3 col-form-label">白名单开关</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="ipWhite" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="ipWhite" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">移动设备限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isMobile" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-primary">
							<input type="radio" name="isMobile" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">白名单IP</label>
				<div class="col-sm-3">
					<textarea rows="1" cols="20" placeholder="请输入ip地址..." name="whiteList" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>

<style>
.custom-switch {
    padding-left: 2.25rem;
}
.custom-control-label {
    margin-bottom: 0;
}
.btn-group-toggle .btn {
    padding: 0.25rem 0.75rem;
    font-size: 0.675rem;
    line-height: 1.5;
    border-radius: 0.2rem;
    min-width: 60px;
}
.btn-group-toggle .btn-outline-primary {
    color: #6c757d;
    border-color: #ced4da;
    background-color: #fff;
}
.btn-group-toggle .btn-outline-primary:hover,
.btn-group-toggle .btn-outline-primary:focus {
    color: #57494c;
    background-color: #e9ecef;
    border-color: #ced4da;
}
.btn-group-toggle .btn-outline-primary.active {
    color: #fff;
    background-color: #ee074c;
    border-color: #ee074c;
}
.btn-group-toggle .btn-outline-primary.active:hover {
    background-color: #36d482;
    border-color: #36d482;
}
</style>
<script>
	// 在JSP中初始化状态映射
	var switchStates = {
		isIp: '${dmCondition.isIp!"0"}',
		timeZone: '${dmCondition.timeZone!"0"}',
		isChinese: '${dmCondition.isChinese!"0"}',
		isMobile: '${dmCondition.isMobile!"0"}',
		isSpecificDevice: '${dmCondition.isSpecificDevice!"0"}',
		isVpn: '${dmCondition.isVpn!"0"}',
		ipLimits: '${dmCondition.ipLimits!"0"}',
		ipWhite: '${dmCondition.ipWhite!"0"}',
		isRobot: '${dmCondition.isRobot!"0"}',
		isIdentify: '${dmCondition.isIdentify!"0"}',
		iosVersion: '${dmCondition.iosVersion!" "}',
		andVersion: '${dmCondition.andVersion!" "}'
	};
</script>
<script>
$(function() {
    // 初始化单选按钮状态
    $('input[type="radio"]').each(function() {
        var $radio = $(this);
        var fieldName = $radio.attr('name');
        var initialValue = switchStates[fieldName] || '0';
        if ($radio.val() === initialValue) {
            $radio.prop('checked', true);
            $radio.closest('label').addClass('active');
        }
    });
});
$(document).ready(function() {
	// 假设从后端获取的值是 "zh,zh-CN"
	var langString = '${dmCondition.language!""}';  // 使用!""防止null值
	var selectedValues = langString ? langString.split(',') : [];

	console.log(selectedValues);
	var $languageSelect = $('select[name="language"]');
	// 设置 select2 的选中状态
	selectedValues.forEach(function(val) {
		$languageSelect.find('option[value="'+val+'"]').prop('selected', true);
	});
	$languageSelect.trigger('change');
});
</script>
