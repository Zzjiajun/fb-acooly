<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
	<form id="manage_shopProducts_editform" class="form-horizontal"
		  action="/manage/shop/shopProducts/<#if action=='create'>saveJson<#else>updateJson</#if>.html"
		  method="post"
		  enctype="multipart/form-data">
		<@jodd.form bean="shopProducts" scope="request">
			<input name="id" type="hidden" />
			<div class="card-body">
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品名称</label>
					<div class="col-sm-9">
						<textarea rows="1" cols="40" placeholder="请输入商品名称..." name="name" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
					</div>
				</div>
				<!-- 父级分类 -->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">父级分类</label>
					<div class="col-sm-9">
						<select id="parentSelect" class="form-control select2bs4" required>
							<option value="">请选择</option>
							<#-- 从后端 parentMap 注入父级下拉 -->
							<#list parentMap?keys as pid>
								<option value="${pid}">${parentMap[pid]!''}</option>
							</#list>
						</select>
					</div>
				</div>
				<!-- 子级分类（仅提交 subCategoryId） -->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">子级分类</label>
					<div class="col-sm-9">
						<select id="subSelect" name="subCategoryId" class="form-control select2bs4" required></select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品价格</label>
					<div class="col-sm-9">
						<input type="text" name="price" placeholder="请输入商品价格..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品原价</label>
					<div class="col-sm-9">
						<input type="text" name="originalPrice" placeholder="请输入商品原价..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品评分</label>
					<div class="col-sm-9">
						<input type="text" name="rating" placeholder="请输入商品评分..." class="easyui-validatebox form-control" data-options="validType:['number[0,99]']"/>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">评论数量</label>
					<div class="col-sm-9">
						<input type="text" name="reviewCount" placeholder="请输入评论数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品主图</label>
					<div class="col-sm-9">
						<!-- 隐藏字段：存储最终保存的URL（由后端设置，用于显示） -->
						<input type="hidden" id="manage_shopProducts_imageUrl" name="imageUrl" value="${shopProducts.imageUrl!''}"/>
						<!-- 文件上传输入框 -->
						<input type="file"
							   id="manage_shopProducts_imageFile"
							   name="imageFile"
							   accept="image/jpeg,image/png,image/gif,image/webp"
							   style="display: none;"
							   onchange="handleFileSelect(this)"/>
						<!-- 图片预览区域 -->
						<div id="manage_shopProducts_imagePreview" style="margin-bottom: 10px;">
							<#if shopProducts.imageUrl?? && shopProducts.imageUrl != ''>
								<img id="preview_imageUrl"
									 src="${shopProducts.imageUrl}"
									 style="max-width: 200px; max-height: 200px; border: 1px solid #ddd; border-radius: 4px; padding: 5px;"/>
								<br/>
								<a href="javascript:void(0);" onclick="clearImage()"
								   style="color: #dc3545; font-size: 12px;">删除图片</a>
							</#if>
						</div>
						<!-- 操作按钮 -->
						<button type="button"
								onclick="triggerFileSelect()"
								class="btn btn-sm btn-primary"
								title="选择图片文件">
							<i class="fa fa-folder-open fa-fw"></i>选择文件
						</button>
						<span style="color: #6c757d; font-size: 12px; margin-left: 10px; display: block; margin-top: 5px;">支持JPG、PNG、GIF、WEBP格式，最大5MB。文件将在保存时自动上传。</span>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">是否推荐</label>
<#--					<div class="col-sm-9">-->
<#--						<input type="text" name="featured" placeholder="请输入是否推荐..." class="easyui-validatebox form-control" data-options="validType:['number[0,127]']"/>-->
<#--					</div>-->
					<div class="d-inline">
						<input type="radio"  name="featured" value="0"  checked> 否
						<input type="radio" name="featured" value="1" > 是
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">是否包邮</label>
<#--					<div class="col-sm-9">-->
<#--						<input type="text" name="freeShipping" placeholder="请输入是否包邮..." class="easyui-validatebox form-control" data-options="validType:['number[0,127]']"/>-->
<#--					</div>-->
					<div class="d-inline">
						<input type="radio"  name="freeShipping" value="0"  checked> 否
						<input type="radio" name="freeShipping" value="1"> 是
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品描述</label>
					<div class="col-sm-9">
						<textarea rows="3" cols="40" placeholder="请输入商品描述..." name="description" class="easyui-validatebox form-control form-words" data-words="999,999,999" ></textarea>
					</div>
				</div>
			</div>
		</@jodd.form>
	</form>
</div>
<#--<script>-->
<#--	// ========== 方式1：弹窗上传（现有功能，保持不变） ==========-->
<#--	function openOfileUpload(targetFieldId, callbackName) {-->
<#--		var uploadUrl = '/manage/module/ofile/onlineFile/uploadFilePage.html?' +-->
<#--				'targetField=' + targetFieldId +-->
<#--				'&callback=' + callbackName +-->
<#--				'&storageNameSpace=shop' +-->
<#--				'&allowedExtensions=jpg,png,jpeg,gif,webp' +-->
<#--				'&maxSize=5242880' +  // 5MB-->
<#--				'&fieldType=image' +-->
<#--				'&_csrf=${_csrf.token}';-->

<#--		$.acooly.framework.show(uploadUrl, 800, 600);-->
<#--	}-->

<#--	function imageUrlCallback(fileUrl, fileInfo) {-->
<#--		// 更新隐藏字段的值-->
<#--		$('#manage_shopProducts_imageUrl').val(fileUrl);-->

<#--		// 清空文件输入框（避免冲突）-->
<#--		$('#manage_shopProducts_imageFile').val('');-->

<#--		// 更新预览图片-->
<#--		updateImagePreview(fileUrl);-->

<#--		// 提示成功-->
<#--		$.acooly.messager.success('图片上传成功');-->
<#--	}-->
<#--
</script>-->
<script>
	// ========== 文件选择相关函数 ==========

	/**
	 * 触发文件选择框
	 */
	function triggerFileSelect() {
		document.getElementById('manage_shopProducts_imageFile').click();
	}

	/**
	 * 处理文件选择事件
	 * @param input 文件输入框元素
	 */
	function handleFileSelect(input) {
		var file = input.files[0];

		if (!file) {
			return;
		}

		// 1. 文件类型验证
		var allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
		var allowedExtensions = ['jpg', 'jpeg', 'png', 'gif', 'webp'];

		// 检查MIME类型
		var isValidType = allowedTypes.indexOf(file.type) !== -1;

		// 检查文件扩展名（双重验证）
		var fileName = file.name.toLowerCase();
		var fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
		var isValidExtension = allowedExtensions.indexOf(fileExtension) !== -1;

		if (!isValidType && !isValidExtension) {
			$.acooly.messager.error('不支持的文件类型，请选择JPG、PNG、GIF或WEBP格式的图片');
			resetFileInput(input);
			return;
		}

		// 2. 文件大小验证（5MB）
		var maxSize = 5 * 1024 * 1024; // 5MB
		if (file.size > maxSize) {
			$.acooly.messager.error('文件大小超过5MB限制，请选择较小的文件');
			resetFileInput(input);
			return;
		}

		// 3. 清空隐藏字段（使用文件上传方式）
		$('#manage_shopProducts_imageUrl').val('');

		// 4. 预览图片
		previewSelectedFile(file);

		// 5. 提示用户
		$.acooly.messager.info('文件已选择，点击保存按钮时将自动上传');
	}

	/**
	 * 预览选择的文件（使用FileReader读取本地文件）
	 * @param file 文件对象
	 */
	function previewSelectedFile(file) {
		var reader = new FileReader();

		reader.onload = function(e) {
			var imageUrl = e.target.result; // 这是base64格式的本地预览URL

			// 更新预览区域
			updateImagePreview(imageUrl, file.name);
		};

		reader.onerror = function() {
			$.acooly.messager.error('文件读取失败，请重试');
			resetFileInput();
		};

		reader.readAsDataURL(file);
	}

	/**
	 * 更新图片预览区域
	 * @param imageUrl 图片URL（可以是本地预览URL或服务器URL）
	 * @param fileName 文件名（可选）
	 */
	function updateImagePreview(imageUrl, fileName) {
		var previewHtml = '<img id="preview_imageUrl" src="' + imageUrl + '" ' +
				'style="max-width: 200px; max-height: 200px; border: 1px solid #ddd; border-radius: 4px; padding: 5px;"/>';

		if (fileName) {
			previewHtml += '<br/><span style="color: #6c757d; font-size: 11px;">' + fileName + '</span>';
		}

		previewHtml += '<br/><a href="javascript:void(0);" onclick="clearImage()" ' +
				'style="color: #dc3545; font-size: 12px; margin-top: 5px; display: inline-block;">删除图片</a>';

		$('#manage_shopProducts_imagePreview').html(previewHtml);
	}

	/**
	 * 清除图片（清空文件输入框和预览）
	 */
	function clearImage() {
		// 清空隐藏字段
		$('#manage_shopProducts_imageUrl').val('');
		// 清空文件输入框
		resetFileInput();
		// 清空预览区域
		$('#manage_shopProducts_imagePreview').html('');

	}

	/**
	 * 重置文件输入框
	 * @param input 文件输入框元素（可选，不传则自动获取）
	 */
	function resetFileInput(input) {
		if (!input) {
			input = document.getElementById('manage_shopProducts_imageFile');
		}
		if (input) {
			input.value = '';
		}
	}

	// ========== 表单提交相关函数 ==========



	// 将后端 Map 注入为 JS 对象（key 为字符串）
	var parentMap = {};
	<#list parentMap?keys as pid>
	parentMap["${pid}"] = "${parentMap[pid]!''}";
	</#list>

	var subMap = {};
	<#list subMap?keys as pid>
	subMap["${pid}"] = {};
	<#list subMap[pid]?keys as sid>
	subMap["${pid}"]["${sid}"] = "${subMap[pid][sid]!''}";
	</#list>
	</#list>



	// 父级联动：选择父级 → 用 subMap[pid] 填充子级选项（subId->subName）
	$('#parentSelect').on('change', function () {
		var pid = $(this).val();
		$('#subSelect').empty();

		if (!pid || !subMap[pid]) {
			return;
		}
		var children = subMap[pid]; // Map<subId, subName>
		// 和你 dmCenterEdit.ftl 里对二级域名的写法一致：append option
		Object.keys(children).forEach(function (sid) {
			var name = children[sid] || '';
			$('#subSelect').append('<option value="'+ sid +'">'+ name +'</option>');
		});
	});

	/**
	 * 表单提交前的验证和处理
	 */
	$(document).ready(function() {
		// 监听表单提交事件
		$('#manage_shopProducts_editform').on('submit', function(e) {
			var fileInput = document.getElementById('manage_shopProducts_imageFile');
			var hasFile = fileInput && fileInput.files && fileInput.files.length > 0;

			if (hasFile) {
				// 如果选择了文件，显示上传提示
				var fileName = fileInput.files[0].name;
				var fileSize = (fileInput.files[0].size / 1024 / 1024).toFixed(2);
				$.acooly.messager.info('正在上传图片 [' + fileName + ', ' + fileSize + 'MB]，请稍候...');
			} else {
				// 如果没有选择文件，检查是否有已存在的图片URL
				var existingUrl = $('#manage_shopProducts_imageUrl').val();
				if (!existingUrl && typeof action !== 'undefined' && action === 'create') {
					// 如果是新增操作且没有图片，给出提示（可选）
					// $.acooly.messager.warning('未选择商品主图');
					// 根据业务需求决定是否阻止提交
				}
			}
			// 允许表单继续提交
			return true;
		});

		// ✅ 如果是编辑页面
		<#if action=='edit'>
		// 假设后端通过模板注入了当前子分类 ID
		var currentSubId = '${shopProducts.subCategoryId!""}';  // ← 例如 "8"
		var currentParentId = null;

		// ① 根据 subMap 反推父分类 ID
		Object.keys(subMap).forEach(function(pid) {
			var children = subMap[pid];
			if (children && children[currentSubId]) {
				currentParentId = pid;
			}
		});

		//设置父级下拉框选中
		if (currentParentId) {
			$('#parentSelect').val(currentParentId).trigger('change.select2'); // ✅ 关键：刷新 select2 显示
		}

		//重新填充子级下拉框
		if (currentParentId && subMap[currentParentId]) {
			$('#subSelect').empty();
			var children = subMap[currentParentId];
			Object.keys(children).forEach(function(sid) {
				var name = children[sid] || '';
				var selected = (sid == currentSubId) ? 'selected' : '';
				$('#subSelect').append('<option value="'+ sid +'" '+ selected +'>'+ name +'</option>');
			});
		}
		<#else>

		</#if>

	});

	// ========== 表单提交成功后的处理 ==========

	/**
	 * 处理保存成功后的回调（更新预览图片为服务器URL）
	 * 这个函数需要在表单提交成功后调用
	 */
	function handleSaveSuccess(savedEntity) {
		if (savedEntity && savedEntity.imageUrl) {
			// 更新隐藏字段为服务器返回的URL
			$('#manage_shopProducts_imageUrl').val(savedEntity.imageUrl);

			// 更新预览图片为服务器URL
			updateImagePreview(savedEntity.imageUrl);

			// 清空文件输入框（因为已经上传成功）
			resetFileInput();

			$.acooly.messager.success('保存成功，图片已上传');
		}
	}



</script>