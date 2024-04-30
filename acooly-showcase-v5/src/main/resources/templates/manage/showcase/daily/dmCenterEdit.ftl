<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<style>
	.hidden {
		display: none;
	}
</style>
<div>
	<form id="manage_dmCenter_editform" class="form-horizontal" action="/manage/showcase/daily/dmCenter/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmCenter" scope="request">
			<input name="id" type="hidden" />
			<div class="card-body">
				<#--			<div class="form-group row">-->
				<#--				<label class="col-sm-3 col-form-label">用户名</label>-->
				<#--				<div class="col-sm-9">-->
				<#--					<textarea rows="3" cols="40" placeholder="请输入用户名..." name="userName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
				<#--				</div>-->
				<#--			</div>-->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">地区</label>
					<div class="col-sm-9">
						<select name="region" class="form-control select2bs4" data-options="required:true" required>
							<#list regionList as v >
								<option value="${v}">${v}</option>
							</#list>
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">主域名</label>
					<div class="col-sm-9">
						<select name="domain" id="domainTest" class="form-control select2bs4" data-options="required:true" required>
							<option value=" "> </option>
							<#list domainList as domainList >
								<option value="${domainList}">${domainList}</option>
							</#list>
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">二级域名</label>
					<div class="col-sm-9">
						<select name="secondaryDomain" id="secondaryDomain" class="form-control select2bs4" data-options="required:true" required>
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">链接地址</label>
					<div class="col-sm-9">
						<textarea rows="2" cols="40" placeholder="请输入链接地址..." name="link" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
					</div>
				</div>

				<div class="form-group row">
					<label class="col-sm-3 col-form-label">类型选择</label>
					<div class="icheck-primary d-inline">
						<input type="radio"  name="displayOption" value="1" id="showRadio" checked> 落地页
						<input type="radio" name="displayOption" value="2" id="hideRadio"> 表单
					</div>
				</div>


				<div class="form-group row">
					<label class="col-sm-3 col-form-label">是否分流</label>
					<div class="icheck-primary d-inline">
						<input type="radio"  name="diversion" value="0" id="showRadio1" checked> 否
						<input type="radio" name="diversion" value="1" id="hideRadio1"> 是
					</div>
				</div>


				<div class="form-group row">
					<label class="col-sm-3 col-form-label">像素代码</label>
					<div id="pixelId" class="col-sm-9">
						<textarea rows="3"   id="inputTextArea"  cols="40" oninput="formatInput()"  placeholder="请输入像素id,如果多个像素id请在每个像素id结尾后面输入英文,它会自动换行..." name="pixel" class="easyui-validatebox form-control" ></textarea>
					</div>
				</div>

				<div  class="form-group row">
					<label class="col-sm-3 col-form-label">模版地址</label>
					<div id="serialNumberId" class="col-sm-6">
						<select id="serialNumberValue" name="serialNumber"  class="form-control select2bs4" data-options="required:true">
								<#list collected as k,v >
									<option value="${v}">${k}</option>
								</#list>
						</select>
					</div>

					<div id="serialNumberId1" class="col-sm-3">
						<button onclick="showLink2('/manage/showcase/daily/dmShow/showLink1.html',800,800); return false;" class="btn btn-success btn-sm" ><i class="fa fa-file-o fa-lg fa-fw fa-col"></i>预览</button>
					</div>

				</div>
				<#--			<div class="form-group row">-->
				<#--				<label class="col-sm-3 col-form-label">域名访问量</label>-->
				<#--				<div class="col-sm-9">-->
				<#--					<input type="text" name="visitsNumber" placeholder="请输入域名访问量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
				<#--				</div>-->
				<#--			</div>-->
				<#--			<div class="form-group row">-->
				<#--				<label class="col-sm-3 col-form-label">按钮点击数量</label>-->
				<#--				<div class="col-sm-9">-->
				<#--					<input type="text" name="clicksNumber" placeholder="请输入按钮点击数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
				<#--				</div>-->
				<#--			</div>-->
			</div>
		</@jodd.form>
	</form>
	<script>

		function showLink2(url, width, height) {

			var t = null;
			var w = width ? width : 500;
			var h = height ? height : 'auto';
			var tt = '落地页查看';
			tt = '<i class="fa fa-lg fa-fw fa-col fa-floppy-o" ></i>' + tt;
			var addB = '确定';
			addB = '<i class="fa fa-lg fa-fw fa-col fa-plus-circle" ></i>' + addB;
			var max = true;
			var selectElement = document.querySelector('select[name="serialNumber"]');
			var selectedValue = selectElement.value;
			url = $.acooly.framework.buildCanonicalUrl(url, {"name":selectedValue});


			// 构建buttons

			var closeBtn = {
				text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col" ></i>关闭',
				handler: function () {
					// var d = $(this).closest('.window-body');
					d.dialog('close');

				}
			};
			var buttons = buttons || [];
			buttons.push(closeBtn);

			var d = null;
			$.acooly.divdialog = d = $('<div/>').dialog({
				href: url,
				title: tt,
				top: t,
				width: w,
				height: h,
				modal: true,
				/*iconCls : 'icon-save',*/
				maximizable: max,
				buttons: buttons,
				onClose: function () {
					$(this).dialog('destroy');
				},
				onOpen: function () {
					// 打开dialog，EASYUI渲染完成后，处理combobox的宽度
					var that = $(this);
					$.parser.onComplete = function () {
						//要执行的操作
						$.acooly.framework.extendCombobox($(that));
						//Initialize
						$.acooly.framework.initPlugins($(that));
						//最后把坑爹的事件绑定解除
						$.parser.onComplete = function () {
						};
					}
				}
			});
		}







		function formatInput() {
			var inputTextArea = document.getElementById('inputTextArea');
			var inputValue = inputTextArea.value;
			var formattedValue = '';
			for (var i = 0; i < inputValue.length; i++) {
				if (inputValue[i] === ',') {
					formattedValue += '\n';
				} else {
					formattedValue += inputValue[i];
				}
			}
			inputTextArea.value = formattedValue;
		}



		var dataTest2 = [];

		$('#domainTest').change(function () {
			<#list mapDomain as k,v >
			if ($('#domainTest').val() === '${k}') {
				dataTest2.length = 0;
				<#list v as item >
				dataTest2.push('${item}');
				</#list>
			} else {
			}
			</#list>
			$('#secondaryDomain').empty();
			$(function () {
				$.each(dataTest2, function () {
					$('#secondaryDomain').append("<option value=" + this + ">" + this + "</option>");
				});
			})
		})


		// 获取单选按钮和要控制的DIV
		var showRadio = document.getElementById('showRadio');
		var hideRadio = document.getElementById('hideRadio');
		var contentDiv = document.getElementById('serialNumberId');
		var contentDiv1 = document.getElementById('serialNumberId1');
		var pixelIdDiv = document.getElementById('pixelId');
		var inputTextArea1 = document.getElementById('inputTextArea');
		var serialNumberValue = document.getElementById('serialNumberValue');
		console.log(${dmCenter.displayOption});

		if ('${dmCenter.displayOption}' === '1') {
			// console.log("xxxxxx");
			// $('#inputTextArea').attr('required', true);
			contentDiv.style.display = 'block'; // 显示DIV
			contentDiv1.style.display = 'block'; // 显示DIV
			pixelIdDiv.style.display = 'block'; // 显示DIV
		} else if ('${dmCenter.displayOption}' === '2') {
			// console.log("xxxxxx");
			contentDiv1.style.display = 'none'; // 显示DIV
			contentDiv.style.display = 'none'; // 隐藏DIV
			pixelIdDiv.style.display = 'none'; // 隐藏DIV
			${'inputTextArea'}.value='';
			document.getElementById('serialNumberValue').selectedIndex = -1;
			// $('#inputTextArea').attr('required', false);
		}

		// 添加事件监听器
		showRadio.addEventListener('change', function() {
			if (showRadio.checked) {
				// console.log("xxxxxx");
				// $('#inputTextArea').attr('required', true);
				contentDiv1.style.display = 'block'; // 显示DIV
				contentDiv.style.display = 'block'; // 显示DIV
				pixelIdDiv.style.display = 'block'; // 显示DIV

			}
		});

		hideRadio.addEventListener('change', function() {
			if (hideRadio.checked) {
				// console.log("xxxxxx");
				contentDiv1.style.display = 'none'; // 显示DIV
				contentDiv.style.display = 'none'; // 隐藏DIV
				pixelIdDiv.style.display = 'none'; // 隐藏DIV


				// $('#inputTextArea').attr('required', false);
			}
		});



		var formElement = document.getElementById("manage_dmCenter_editform");
		if (formElement.getAttribute("action")==='/manage/showcase/daily/dmCenter/updateJson.html'){
			console.log('test');
			// var dataTest3= [];
			<#list collected1 as a >
			$('#secondaryDomain').append("<option value=" + '${a}' + ">" + '${a}'+ "</option>");
			<#--dataTest3.push('${a}');-->
			</#list>
			$('#secondaryDomain').val('${dmCenter.secondaryDomain}');
			// $(function () {
			// 	$.each(dataTest3, function () {
			// 		$('#secondaryDomain').append("<option value=" + this + ">" + this + "</option>");
			// 	});
			// })
		}


		$(document).ready(function() {
			// 定义处理程序函数
			function handleButtonClick() {
				var loadingIndex = layer.load(2, {
					shade: [0.5, '#fff'], // 加载遮罩背景颜色和透明度
					content: '加载中...',
					success: function (layero) {
						layero.find('.layui-layer-content').css({
							'padding-top': '39px',
							'width': '60px'
						});
					}
				});

				// 模拟一个异步操作，例如发送 AJAX 请求
				// setTimeout(function() {
				// 	// 这里可以替换成你的实际业务逻辑
				//
				// 	// 隐藏加载动画
				// 	layer.close(loadingIndex);
				// }, 7000); // 假设 2 秒后完成操作
				// 监控对话框销毁事件
				$(document).ajaxStop(function() {
					// 在这里可以添加 loading 层的关闭逻辑
					layer.close(loadingIndex);
				});
			}

			// 绑定点击事件处理程序到编辑按钮
			$('#acooly-framework-edit-btn').click(handleButtonClick);

			// // 绑定点击事件处理程序到新增按钮
			$('#acooly-framework-add-btn').click(handleButtonClick);
		});


	</script>
</div>
