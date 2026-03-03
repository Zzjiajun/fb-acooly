<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<style>
    /* 品牌编辑页面样式 */
    .brand-edit-container {
        background: #f5f6f8;
        padding: 20px;
    }
    
    .form-card {
        background: #fff;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    }
    
    .form-group label {
        color: #374151;
        font-weight: 600;
        font-size: 14px;
    }
    
    .form-control {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 8px 12px;
        transition: all 0.2s;
    }
    
    .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99,102,241,0.15);
    }
    
    /* Logo上传区域 */
    .logo-upload-area {
        border: 2px dashed #e5e7eb;
        border-radius: 12px;
        padding: 20px;
        background: #f9fafb;
        text-align: center;
        transition: all 0.3s;
    }
    
    .logo-upload-area:hover {
        border-color: #6366f1;
        background: #f0f4ff;
    }
    
    .logo-preview-container {
        position: relative;
        display: inline-block;
        margin-bottom: 15px;
    }
    
    .logo-preview-image {
        max-width: 200px;
        max-height: 200px;
        border: 2px solid #e5e7eb;
        border-radius: 12px;
        padding: 8px;
        cursor: pointer;
        transition: all 0.3s;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    
    .logo-preview-image:hover {
        border-color: #6366f1;
        transform: scale(1.05);
        box-shadow: 0 4px 12px rgba(99,102,241,0.2);
    }
    
    .logo-remove-btn {
        position: absolute;
        top: 8px;
        right: 8px;
        background: linear-gradient(135deg, #ef4444, #dc2626);
        color: white;
        border: none;
        border-radius: 50%;
        width: 28px;
        height: 28px;
        cursor: pointer;
        font-size: 16px;
        font-weight: bold;
        box-shadow: 0 2px 6px rgba(239,68,68,0.3);
        transition: all 0.2s;
    }
    
    .logo-remove-btn:hover {
        transform: scale(1.1);
        box-shadow: 0 4px 10px rgba(239,68,68,0.4);
    }
    
    .upload-btn {
        background: linear-gradient(135deg, #6366f1, #8b5cf6);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 10px 20px;
        font-weight: 500;
        transition: all 0.3s;
        box-shadow: 0 2px 6px rgba(99,102,241,0.2);
    }
    
    .upload-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(99,102,241,0.3);
    }
    
    .upload-hint {
        color: #6b7280;
        font-size: 12px;
        margin-top: 10px;
        line-height: 1.6;
    }
</style>
<div class="brand-edit-container">
    <div class="form-card">
        <form id="manage_shopBrand_editform" class="form-horizontal" action="/manage/shop/shopBrand/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" enctype="multipart/form-data">
		<@jodd.form bean="shopBrand" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">品牌名称</label>
				<div class="col-sm-9">
					<input type="text" name="name" placeholder="请输入品牌名称..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,100]']" required="true"/>
				</div>
			</div>
			<div class="form-group row">
                        <label class="col-sm-3 col-form-label">品牌Logo</label>
				<div class="col-sm-9">
                            <!-- 隐藏字段：存储logo URL -->
                            <input type="hidden" id="manage_shopBrand_logo" name="logo" value="${shopBrand.logo!''}"/>
                            
                            <!-- 文件上传input -->
                            <input type="file" 
                                   id="manage_shopBrand_logoFile" 
                                   name="logoFile" 
                                   accept="image/jpeg,image/png,image/gif,image/webp"
                                   style="display: none;"
                                   onchange="handleLogoFileSelect(this)"/>
                            
                            <!-- 图片预览区域 -->
                            <div id="manage_shopBrand_logoPreview" style="margin-bottom: 15px;">
                                <#if shopBrand.logo?? && shopBrand.logo != ''>
                                    <div id="logoPreviewContainer" class="logo-preview-container">
                                        <img id="logoPreviewImage" src="${shopBrand.logo}" alt="品牌Logo" 
                                             class="logo-preview-image"
                                             onclick="showLogoPreview('${shopBrand.logo}')"/>
                                        <button type="button" onclick="clearLogo()" 
                                                class="logo-remove-btn"
                                                title="清除Logo">×</button>
                                    </div>
                                <#else>
                                    <div id="logoPreviewContainer" class="logo-upload-area">
                                        <i class="fa fa-cloud-upload" style="font-size: 48px; color: #9ca3af; margin-bottom: 10px;"></i>
                                        <p style="color: #6b7280; margin: 10px 0; font-size: 14px;">点击下方按钮选择Logo图片</p>
                                        <p style="color: #9ca3af; font-size: 12px;">支持JPG、PNG、GIF、WEBP格式</p>
                                    </div>
                                </#if>
                            </div>
                            
                            <!-- 操作按钮 -->
                            <div style="margin-top: 10px;">
                                <button type="button" onclick="triggerLogoFileSelect()" class="upload-btn">
                                    <i class="fa fa-image fa-fw"></i>选择Logo图片
                                </button>
                            </div>
                            
                            <div class="upload-hint">
                                <i class="fa fa-info-circle"></i> 支持JPG、PNG、GIF、WEBP格式，单张最大5MB。图片将自动压缩并转换为WebP格式。
                            </div>
				</div>
			</div>
			<div class="form-group row">
                        <label class="col-sm-3 col-form-label">状态</label>
				<div class="col-sm-9">
                            <select name="status" class="form-control select2bs4">
                                <option value="1" <#if shopBrand.status?? && shopBrand.status == 1>selected</#if>>启用</option>
                                <option value="0" <#if shopBrand.status?? && shopBrand.status == 0>selected</#if>>禁用</option>
                            </select>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
</div>

<script>
	// ========== Logo文件选择处理 ==========
	function handleLogoFileSelect(input) {
		var file = input.files[0];
		if (!file) return;
		
		// 验证文件类型
		var allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
		var allowedExtensions = ['jpg', 'jpeg', 'png', 'gif', 'webp'];
		var maxSize = 5 * 1024 * 1024; // 5MB
		
		var isValidType = allowedTypes.indexOf(file.type) !== -1;
		var fileName = file.name.toLowerCase();
		var fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
		var isValidExtension = allowedExtensions.indexOf(fileExtension) !== -1;
		
		if (!isValidType && !isValidExtension) {
			$.acooly.messager.error('不支持的文件类型：' + file.name);
			resetLogoFileInput();
			return;
		}
		
		// 验证文件大小
		if (file.size > maxSize) {
			$.acooly.messager.error('文件大小超过5MB：' + file.name);
			resetLogoFileInput();
			return;
		}
		
		// 显示预览
		var reader = new FileReader();
		reader.onload = function(e) {
			var container = $('#logoPreviewContainer');
			container.removeClass('logo-upload-area').addClass('logo-preview-container');
			container.html(
				'<img id="logoPreviewImage" src="' + e.target.result + '" alt="品牌Logo" ' +
				'class="logo-preview-image" ' +
				'onclick="showLogoPreview(\'' + e.target.result + '\')"/>' +
				'<button type="button" onclick="clearLogo()" ' +
				'class="logo-remove-btn" ' +
				'title="清除Logo">×</button>'
			);
		};
		reader.readAsDataURL(file);
	}
	
	// ========== 触发文件选择 ==========
	function triggerLogoFileSelect() {
		$('#manage_shopBrand_logoFile').click();
	}
	
	// ========== 清除Logo ==========
	function clearLogo() {
		// 清空文件输入框
		resetLogoFileInput();
		
		// 清空隐藏字段
		$('#manage_shopBrand_logo').val('');
		
		// 更新预览区域
		var container = $('#logoPreviewContainer');
		container.removeClass('logo-preview-container').addClass('logo-upload-area');
		container.html(
			'<i class="fa fa-cloud-upload" style="font-size: 48px; color: #9ca3af; margin-bottom: 10px;"></i>' +
			'<p style="color: #6b7280; margin: 10px 0; font-size: 14px;">点击下方按钮选择Logo图片</p>' +
			'<p style="color: #9ca3af; font-size: 12px;">支持JPG、PNG、GIF、WEBP格式</p>'
		);
	}
	
	// ========== 重置文件输入框 ==========
	function resetLogoFileInput() {
		$('#manage_shopBrand_logoFile').val('');
	}
	
	// ========== 显示Logo预览大图 ==========
	function showLogoPreview(url) {
		// 自动补前缀（如果URL不是http开头）
		var imageUrl = url;
		if (!/^https?:\/\//.test(imageUrl)) {
			imageUrl = window.location.origin + imageUrl;
		}
		
		var html = '<div style="text-align: center; padding: 20px;">' +
			'<img src="' + imageUrl + '" alt="Logo预览" style="max-width: 80%; max-height: 70vh; border-radius: 12px; box-shadow: 0 10px 40px rgba(0,0,0,0.25);"/>' +
			'</div>';
		
		$.messager.alert('品牌Logo预览', html, 'info');
	}
</script>
