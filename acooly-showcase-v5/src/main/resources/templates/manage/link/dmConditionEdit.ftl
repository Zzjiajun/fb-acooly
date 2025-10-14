<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmCondition_editform" class="form-horizontal" action="/manage/link/dmCondition/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmCondition" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<#if action != 'create'>
				<div class="form-group row">
					<div class="col-sm-12">
						<div class="alert alert-light" role="alert" style="margin-bottom:8px;">
							最后更新时间：<span id="lastUpdateText">
							<#if dmCondition.updateTime?has_content>
								<#if dmCondition.updateTime?is_date>
									${dmCondition.updateTime?string('yyyy-MM-dd HH:mm:ss')}
								<#else>
									${dmCondition.updateTime}
								</#if>
							</#if>
							</span>
							最后更新人: <span id="lastUpdateText">
									${dmCondition.updateBy}
							</span>
						</div>
					</div>
				</div>
			</#if>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">IP限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isIp" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="isIp" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">IP国家限制</label>
				<div class="col-sm-3">
					<select name="ipCountry" class="form-control select2bs4" multiple="multiple">
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
						<label class="btn btn-outline-danger">
							<input type="radio" name="timeZone" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">时区洲</label>
				<div class="col-sm-3">
					<select name="timeContinent" class="form-control select2bs4">
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
						<label class="btn btn-outline-danger">
							<input type="radio" name="isChinese" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">语言选项</label>
				<div class="col-sm-3">
					<select name="language" class="form-control select2bs4" multiple="multiple">
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
						<label class="btn btn-outline-danger">
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
						<label class="btn btn-outline-danger">
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
						<label class="btn btn-outline-danger">
							<input type="radio" name="ipLimits" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">商业网检测</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isBusiness" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="isBusiness" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">限制ios系统版本</label>
				<div class="col-sm-3">
					<select name="iosVersion" class="form-control select2bs4">
						<#list iosVersionMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
				<label class="col-sm-3 col-form-label">限制安卓系统版本</label>
				<div class="col-sm-3">
					<select name="andVersion" class="form-control select2bs4">
						<#list androidVersionMap as k,v >
							<option value="${v}">${k}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">无法识别设备</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isIdentify" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="isIdentify" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">移动设备限制</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isMobile" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="isMobile" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">白名单开关</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="ipWhite" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="ipWhite" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">爬虫机器人</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isRobot" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="isRobot" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">白名单IP</label>
				<div class="col-sm-3">
					<textarea rows="1" cols="20" placeholder="请输入ip地址..." name="whiteList" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
				<label class="col-sm-3 col-form-label">虚拟机检查</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isVirtual" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="isVirtual" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">ip和设备时区验证</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="timeMatch" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="timeMatch" value="0" autocomplete="off"> 关闭
						</label>
					</div>
				</div>
				<label class="col-sm-3 col-form-label">是否限制带参数</label>
				<div class="col-sm-3">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<label class="btn btn-outline-primary">
							<input type="radio" name="isParams" value="1" autocomplete="off"> 开启
						</label>
						<label class="btn btn-outline-danger">
							<input type="radio" name="isParams" value="0" autocomplete="off"> 关闭
						</label>
					</div>
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
		isVirtual: '${dmCondition.isVirtual!"0"}',
		isBusiness: '${dmCondition.isBusiness!"0"}',
		timeMatch: '${dmCondition.timeMatch!"0"}',
		isIdentify: '${dmCondition.isIdentify!"0"}',
		isParams: '${dmCondition.isParams!"0"}',
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
	// 延迟初始化，确保对话框完全加载
	setTimeout(function() {
		// 语言多选回显
		var langString = '${dmCondition.language!""}';
		var selectedLangs = langString ? langString.split(',') : [];
		var $languageSelect = $('select[name="language"]');
		if ($languageSelect.hasClass('select2-hidden-accessible')) {
			$languageSelect.select2('destroy');
		}
		selectedLangs.forEach(function(val) {
			var $option = $languageSelect.find('option[value="'+val+'"]');
			if ($option.length > 0) {
				$option.prop('selected', true);
			}
		});
		$languageSelect.select2({
			theme: 'bootstrap4',
			width: '100%'
		});
		$languageSelect.trigger('change');

		// 手机型号多选回显 待开发


		// ipCountry多选回显
		var ipCountryString = '${dmCondition.ipCountry!""}';
		var selectedCountries = ipCountryString ? ipCountryString.split(',') : [];
		var $ipCountrySelect = $('select[name="ipCountry"]');
		if ($ipCountrySelect.hasClass('select2-hidden-accessible')) {
			$ipCountrySelect.select2('destroy');
		}
		selectedCountries.forEach(function(val) {
			var $option = $ipCountrySelect.find('option[value="'+val+'"]');
			if ($option.length > 0) {
				$option.prop('selected', true);
			}
		});
		$ipCountrySelect.select2({
			theme: 'bootstrap4',
			width: '100%'
		});
		$ipCountrySelect.trigger('change');
	}, 100); // 延迟100ms
});

// 保存成功后刷新父页面数据表格
function refreshParentGrid() {
    try {
        // 检查是否在对话框中
        if (window.parent && window.parent.$) {
            // 触发父页面的查询刷新
            window.parent.$.acooly.framework.search('manage_dmCenter_searchform', 'manage_dmCenter_datagrid');
            // 或者直接刷新数据表格
            // window.parent.$('#manage_dmCenter_datagrid').datagrid('reload');
        }
    } catch (e) {
        console.log('刷新父页面失败:', e);
    }
}

// 重写表单提交，添加保存成功后的回调
$(document).ready(function() {
    $('#manage_dmCondition_editform').on('submit', function(e) {
        e.preventDefault();
        var form = $(this);
        $.ajax({
            url: form.attr('action'),
            data: form.serialize(),
            type: 'POST',
            dataType: 'json',
            success: function(result) {
                if (result.success) {
                    // 保存成功后刷新父页面
                    refreshParentGrid();
                    // 关闭当前对话框
                    if (window.parent && window.parent.$) {
                        window.parent.$('.ui-dialog-content').dialog('close');
                    }
                    $.acooly.messager('提示', '保存成功！', 'success');
                } else {
                    $.acooly.messager('错误', result.message || '保存失败！', 'danger');
                }
            },
            error: function() {
                $.acooly.messager('错误', '网络错误，保存失败！', 'danger');
            }
        });
    });
	});
</script>
<script>
$(function() {
	if ('${action!"create"}' != 'create') {
		var idVal = $('input[name="id"]').val();
		if (idVal) {
			$.getJSON('/manage/link/dmCondition/listJson.html', {'search_EQ_id': idVal}, function(res) {
				try {
					var rows = res.rows || res.data || [];
					if (rows.length > 0) {
						var ut = rows[0].updateTime || rows[0].updatedTime || rows[0].modifyTime || rows[0].lastModifyTime;
						if (ut) {
							$('#lastUpdateText').text(formatUpdateTime(ut));
						}
					}
				} catch(e) {
					console.log('获取更新时间失败:', e);
				}
			});
		}
	}
	function formatUpdateTime(ut) {
		if (typeof ut === 'number') {
			var d = new Date(ut);
			return formatDateObj(d);
		}
		if (typeof ut === 'string') {
			if (/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}/.test(ut)) {
				return ut.substring(0,19).replace('T',' ');
			}
			return ut;
		}
		return '' + ut;
	}
	function pad(n){return n<10?'0'+n:n;}
	function formatDateObj(d){
		return d.getFullYear()+'-'+pad(d.getMonth()+1)+'-'+pad(d.getDate())+' '+pad(d.getHours())+':'+pad(d.getMinutes())+':'+pad(d.getSeconds());
	}
});
</script>
<#--<script>-->
<#--	$(function() {-->
<#--		try {-->
<#--			const deviceContainer = $('#detailsContainer');-->
<#--			const deviceContent = "${dmTrolls.details}";-->
<#--			if (deviceContent) {-->
<#--				const items = deviceContent.split(',').map(item => item.trim()).filter(Boolean);-->
<#--				deviceContainer.empty();-->
<#--				items.forEach(function(item) {-->
<#--					deviceContainer.append(-->
<#--						'<li style="margin-bottom:2px;list-style:none;"><i class="fa fa-times-circle" style="color:#d9534f;margin-right:4px;"></i>' +-->
<#--						$('<div>').text(item).html() + '</li>'-->
<#--					);-->
<#--				});-->
<#--			} else {-->
<#--				deviceContainer.append('<li style="color:#aaa;">无失败明细</li>');-->
<#--			}-->
<#--		} catch (error) {-->
<#--			console.error('格式化失败明细时出错:', error);-->
<#--		}-->
<#--	});-->
<#--</script>-->
