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
				<!-- 品牌选择 -->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">品牌</label>
					<div class="col-sm-9">
						<select name="BrandId" id="brandSelect" class="form-control select2bs4">
							<option value="">请选择品牌</option>
							<#if brandMap??>
								<#list brandMap?keys as brandId>
									<option value="${brandId}" <#if shopProducts.brandId?? && shopProducts.brandId == brandId?number>selected</#if>>${brandMap[brandId]!''}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品价格</label>
					<div class="col-sm-9">
						<input type="text" name="price" placeholder="请输入商品价格..." class="easyui-numberbox form-control" data-options="precision:2,min:0,max:999999999,required:true"/>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品原价</label>
					<div class="col-sm-9">
						<input type="text" name="originalPrice" placeholder="请输入商品原价..." class="easyui-numberbox form-control" data-options="precision:2,min:0,max:999999999"/>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品评分</label>
					<div class="col-sm-9">
						<select name="rating" class="form-control select2bs4" required>
							<option value="1" <#if shopProducts.rating?? && shopProducts.rating == 1>selected</#if>>1星</option>
							<option value="2" <#if shopProducts.rating?? && shopProducts.rating == 2>selected</#if>>2星</option>
							<option value="3" <#if shopProducts.rating?? && shopProducts.rating == 3>selected</#if>>3星</option>
							<option value="4" <#if shopProducts.rating?? && shopProducts.rating == 4>selected</#if>>4星</option>
							<option value="5" <#if !shopProducts.rating?? || shopProducts.rating == 5>selected</#if>>5星</option>
						</select>
					</div>
				</div>
<#--				<div class="form-group row">-->
<#--					<label class="col-sm-3 col-form-label">评论数量</label>-->
<#--					<div class="col-sm-9">-->
<#--						<input type="text" name="reviewCount" placeholder="请输入评论数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
<#--					</div>-->
<#--				</div>-->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品图片</label>
					<div class="col-sm-9">
						<!-- 隐藏字段：单图片模式兼容 -->
						<input type="hidden" id="manage_shopProducts_imageUrl" name="imageUrl" value="${shopProducts.imageUrl!''}"/>
						
						<!-- 单文件上传（兼容旧逻辑） -->
						<input type="file" 
							   id="manage_shopProducts_imageFile" 
							   name="imageFile" 
							   accept="image/jpeg,image/png,image/gif,image/webp"
							   style="display: none;"
							   onchange="handleSingleFileSelect(this)"/>
						
						<!-- 多文件上传（新功能） -->
						<input type="file" 
							   id="manage_shopProducts_imageFiles" 
							   name="imageFiles" 
							   multiple
							   accept="image/jpeg,image/png,image/gif,image/webp"
							   style="display: none;"
							   onchange="handleMultipleFileSelect(this)"/>
						
						<!-- 要删除的图片ID列表 -->
						<input type="hidden" id="deletedImageIds" name="deletedImageIds" value=""/>
						<input type="hidden" id="existingImageIds" name="existingImageIds" value=""/>
						<!-- 图片排序顺序（格式：imageId:sortOrder,imageId:sortOrder） -->
						<input type="hidden" id="imageSortOrders" name="imageSortOrders" value=""/>
						<!-- 主图ID（已存在的图片设为主图时使用） -->
						<input type="hidden" id="primaryImageId" name="primaryImageId" value=""/>
						<!-- 主图URL（新上传的图片设为主图时使用，用于匹配） -->
						<input type="hidden" id="primaryImageUrl" name="primaryImageUrl" value=""/>
						
						<!-- 图片预览区域 -->
						<div id="manage_shopProducts_imagePreview" style="margin-bottom: 15px;">
							<div id="imagePreviewContainer" style="display: flex; flex-wrap: wrap; gap: 15px;">
								<!-- 动态插入图片卡片 -->
							</div>
						</div>
						
						<!-- 操作按钮 -->
						<div style="margin-top: 10px;">
							<button type="button" onclick="triggerSingleFileSelect()" class="btn btn-sm btn-primary">
								<i class="fa fa-image fa-fw"></i>选择单张图片
							</button>
							<button type="button" onclick="triggerMultipleFileSelect()" class="btn btn-sm btn-success" style="margin-left: 10px;">
								<i class="fa fa-images fa-fw"></i>选择多张图片
							</button>
						</div>
						
						<span style="color: #6c757d; font-size: 12px; display: block; margin-top: 8px;">
							支持JPG、PNG、GIF、WEBP格式，单张最大5MB。<br/>
							<strong>说明：</strong>所有图片统一保存到商品图片表，第一张自动设为主图并同步到商品主图字段。
						</span>
					</div>
				</div>
				
				<style>
					/* 图片卡片样式 */
					.image-card {
						position: relative;
						width: 150px;
						border: 2px solid #e5e7eb;
						border-radius: 8px;
						padding: 10px;
						background: #fff;
						box-shadow: 0 2px 4px rgba(0,0,0,0.1);
						transition: all 0.3s;
					}
					
					.image-card:hover {
						border-color: #6366f1;
						box-shadow: 0 4px 8px rgba(99,102,241,0.2);
						transform: translateY(-2px);
					}
					
					.image-card img {
						width: 100%;
						height: 120px;
						object-fit: cover;
						border-radius: 4px;
						border: 1px solid #e5e7eb;
					}
					
					.image-card .badge-primary {
						position: absolute;
						top: 15px;
						right: 15px;
						background: linear-gradient(135deg, #10b981, #059669);
						color: white;
						padding: 4px 10px;
						border-radius: 12px;
						font-size: 11px;
						font-weight: 600;
						box-shadow: 0 2px 4px rgba(0,0,0,0.2);
					}
					
					.image-card .image-actions {
						margin-top: 8px;
						text-align: center;
					}
					
					.image-card .image-actions button {
						margin: 0 3px;
						padding: 4px 8px;
						font-size: 11px;
						border-radius: 4px;
						border: 1px solid #e5e7eb;
						background: #f9fafb;
						cursor: pointer;
						transition: all 0.2s;
					}
					
					.image-card .image-actions button:hover {
						background: #eef2ff;
						border-color: #6366f1;
						color: #6366f1;
					}
					
					.image-card .image-actions button:disabled {
						opacity: 0.5;
						cursor: not-allowed;
					}
					
					.image-card .sort-order {
						margin-top: 5px;
						text-align: center;
						font-size: 11px;
						color: #6b7280;
					}
					
					.image-card .sort-controls {
						margin-top: 5px;
						text-align: center;
					}
					
					.image-card .sort-controls button {
						margin: 0 2px;
						padding: 2px 6px;
						font-size: 10px;
						border-radius: 3px;
						border: 1px solid #d1d5db;
						background: #ffffff;
						cursor: pointer;
						transition: all 0.2s;
					}
					
					.image-card .sort-controls button:hover:not(:disabled) {
						background: #f3f4f6;
						border-color: #6366f1;
						color: #6366f1;
					}
				</style>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">是否推荐</label>
<#--					<div class="col-sm-9">-->
<#--						<input type="text" name="featured" placeholder="请输入是否推荐..." class="easyui-validatebox form-control" data-options="validType:['number[0,127]']"/>-->
<#--					</div>-->
					<div class="d-inline">
						<input type="radio"  name="featured" value="0"  > 否
						<input type="radio" name="featured" value="1" checked> 是
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">是否包邮</label>
<#--					<div class="col-sm-9">-->
<#--						<input type="text" name="freeShipping" placeholder="请输入是否包邮..." class="easyui-validatebox form-control" data-options="validType:['number[0,127]']"/>-->
<#--					</div>-->
					<div class="d-inline">
						<input type="radio"  name="freeShipping" value="0"  > 否
						<input type="radio" name="freeShipping" value="1"checked> 是
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品描述</label>
					<div class="col-sm-9">
						<textarea rows="3" cols="40" placeholder="请输入商品描述..." name="description" class="easyui-validatebox form-control form-words" data-words="999,999,999" ></textarea>
					</div>
				</div>
				<!-- 商品属性选择 -->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">商品属性</label>
					<div class="col-sm-9">
						<!-- 隐藏字段：存储选中的属性值ID（多个用逗号分隔） -->
						<input type="hidden" id="productAttrValueIds" name="attrValueIds" value=""/>
						<div id="productAttrContainer" style="border: 2px solid #e5e7eb; border-radius: 12px; padding: 20px; background: linear-gradient(135deg, #f9fafb 0%, #ffffff 100%); box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
							<#if attrMap?? && attrValueMap??>
								<#list attrMap?keys as attrId>
									<div class="attr-group" style="margin-bottom: 20px; padding: 16px; background: #ffffff; border-radius: 10px; border: 1px solid #e5e7eb; box-shadow: 0 1px 4px rgba(0,0,0,0.05); transition: all 0.3s;">
										<label style="font-weight: 700; color: #374151; margin-bottom: 12px; display: block; font-size: 14px; padding-bottom: 8px; border-bottom: 2px solid #e5e7eb;">
											<i class="fa fa-tag" style="color: #6366f1; margin-right: 6px;"></i>${attrMap[attrId]!''}
										</label>
										<select class="form-control select2bs4 attr-value-select" 
												data-attr-id="${attrId}" 
												multiple 
												style="width: 100%; border: 1px solid #d1d5db; border-radius: 8px;">
											<#if attrValueMap[attrId]??>
												<#list attrValueMap[attrId] as attrValue>
													<option value="${attrValue.id}">${attrValue.value}</option>
												</#list>
											</#if>
										</select>
									</div>
								</#list>
							<#else>
								<div style="text-align: center; padding: 40px; color: #9ca3af;">
									<i class="fa fa-info-circle" style="font-size: 48px; margin-bottom: 15px; opacity: 0.5;"></i>
									<p style="font-size: 14px;">暂无属性数据，请先在"商品属性"和"商品属性值"中配置</p>
								</div>
							</#if>
						</div>
						<div style="margin-top: 12px; padding: 12px; background: #f0f4ff; border-radius: 8px; border-left: 4px solid #6366f1;">
							<i class="fa fa-info-circle" style="color: #6366f1; margin-right: 6px;"></i>
							<span style="color: #4b5563; font-size: 12px; line-height: 1.6;">
								可以为商品选择多个属性值，每个属性可以选择多个值。选择后会自动保存。
							</span>
						</div>
					</div>
				</div>
<#--				<div class="form-group row">-->
<#--					<label class="col-sm-3 col-form-label">商品编号</label>-->
<#--					<div class="col-sm-9">-->
<#--						<textarea rows="3" cols="40" placeholder="必须要和A网站自动生成的一样" name="serialNumber" class="easyui-validatebox form-control form-words" data-words="999,999,999" required ></textarea>-->
<#--					</div>-->
<#--				</div>-->
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
	// ========== 全局变量 ==========
	var productImages = {
		existing: [],  // 已存在的图片 [{id, imageUrl, sortOrder, isPrimary}]
		newFiles: [],  // 新选择的文件 [File对象]
		deleted: []    // 要删除的图片ID [id]
	};
	
	var displayImages = [];  // 合并后的图片列表（用于显示）
	var originalPrimaryImageId = null;  // 【修复】记录原始主图ID，用于检测主图是否变化

	// ========== 页面加载时初始化 ==========
	$(document).ready(function() {
		<#if action=='edit' && shopProducts.id??>
			loadProductImages(${shopProducts.id});
			loadProductAttributes(${shopProducts.id});
		</#if>
		
		// 初始化select2
		$('.select2bs4').select2({
			theme: 'bootstrap4',
			placeholder: "请选择",
			allowClear: true,
			width: '100%'
		});
		
		// 初始化属性选择器：监听变化，更新隐藏字段
		$('.attr-value-select').on('change', function() {
			updateProductAttrValueIds();
		});
	});
	
	// ========== 加载商品已选属性 ==========
	function loadProductAttributes(productId) {
		$.ajax({
			url: '/manage/shop/shopProducts/getProductAttributes.html',
			type: 'GET',
			data: { productId: productId },
			success: function(result) {
				if (result.success && result.rows) {
					// 按属性ID分组已选中的属性值
					var selectedAttrValues = {};
					result.rows.forEach(function(item) {
						var attrId = item.attrId;
						if (!selectedAttrValues[attrId]) {
							selectedAttrValues[attrId] = [];
						}
						selectedAttrValues[attrId].push(item.attrValueId);
					});
					
					// 设置每个属性选择器的选中值
					$('.attr-value-select').each(function() {
						var attrId = $(this).data('attr-id');
						var selectedIds = selectedAttrValues[attrId] || [];
						$(this).val(selectedIds).trigger('change.select2');
					});
					
					// 更新隐藏字段
					updateProductAttrValueIds();
				}
			},
			error: function() {
				console.error('加载商品属性失败');
			}
		});
	}
	
	// ========== 更新商品属性值ID隐藏字段 ==========
	function updateProductAttrValueIds() {
		var allSelectedIds = [];
		$('.attr-value-select').each(function() {
			var selected = $(this).val();
			if (selected && selected.length > 0) {
				allSelectedIds = allSelectedIds.concat(selected);
			}
		});
		$('#productAttrValueIds').val(allSelectedIds.join(','));
	}

	// ========== 加载已有图片 ==========
	function loadProductImages(productId) {
		$.ajax({
			url: '/manage/shop/shopProducts/getProductImages.html',
			type: 'GET',
			data: { productId: productId },
			success: function(result) {
				if (result.success && result.rows) {
					productImages.existing = result.rows.map(function(img) {
						return {
							id: img.id,
							imageUrl: img.imageUrl,
							sortOrder: img.sortOrder || 0,
							isPrimary: img.isPrimary === 1 || img.isPrimary === '1'
						};
					});
					// 【修复】记录原始主图ID
					var primaryImg = productImages.existing.find(function(img) {
						return img.isPrimary === true;
					});
					originalPrimaryImageId = primaryImg ? primaryImg.id : null;
					console.log('记录原始主图ID:', originalPrimaryImageId);
					// 初始化已存在图片ID列表
					updateExistingImageIds();
					updateDisplayImages();
					renderImagePreview();
				}
			},
			error: function() {
				console.error('加载图片失败');
			}
		});
	}

	// ========== 单文件选择处理 ==========
	function handleSingleFileSelect(input) {
		var file = input.files[0];
		if (!file) return;
		
		if (!validateImageFile(file)) {
			resetFileInput(input);
			return;
		}
		
		// 清空多文件选择（包括输入框和JavaScript数组）
		productImages.newFiles = [];
		var multipleFileInput = document.getElementById('manage_shopProducts_imageFiles');
		if (multipleFileInput) {
			multipleFileInput.value = '';
		}
		
		// 添加单文件到数组（用于预览）
		// 注意：文件已经在input.files中，不需要手动添加
		// 但为了预览，我们添加到数组中
		productImages.newFiles = [file];
		
		console.log('单文件选择 - 文件名:', file.name, '大小:', file.size);
		
		updateDisplayImages();
		renderImagePreview();
	}

	// ========== 多文件选择处理 ==========
	function handleMultipleFileSelect(input) {
		var files = Array.from(input.files);
		if (files.length === 0) {
			console.log('多文件选择 - 没有文件');
			return;
		}
		
		console.log('多文件选择 - 原始文件数量:', files.length);
		
		// 验证文件
		var validFiles = files.filter(function(file) {
			return validateImageFile(file);
		});
		
		if (validFiles.length === 0) {
			input.value = '';
			console.log('多文件选择 - 没有有效文件');
			return;
		}
		
		console.log('多文件选择 - 有效文件数量:', validFiles.length);
		
		// 清空单文件选择
		var singleFileInput = document.getElementById('manage_shopProducts_imageFile');
		if (singleFileInput) {
			singleFileInput.value = '';
		}
		
		// 重要：文件已经在input.files中，会被表单自动提交
		// 同时也在JavaScript中维护文件列表用于预览和管理
		// 注意：不要清空input，文件需要保留在input.files中才能提交
		productImages.newFiles = productImages.newFiles.concat(validFiles);
		
		console.log('多文件选择后 - productImages.newFiles数量:', productImages.newFiles.length);
		console.log('多文件选择后 - input.files数量:', input.files.length);
		console.log('多文件选择后 - input.name:', input.name);
		
		updateDisplayImages();
		renderImagePreview();
	}

	// ========== 文件验证 ==========
	function validateImageFile(file) {
		var allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
		var allowedExtensions = ['jpg', 'jpeg', 'png', 'gif', 'webp'];
		var maxSize = 5 * 1024 * 1024; // 5MB
		
		// 类型验证
		var isValidType = allowedTypes.indexOf(file.type) !== -1;
		var fileName = file.name.toLowerCase();
		var fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
		var isValidExtension = allowedExtensions.indexOf(fileExtension) !== -1;
		
		if (!isValidType && !isValidExtension) {
			$.acooly.messager.error('不支持的文件类型：' + file.name);
			return false;
		}
		
		// 大小验证
		if (file.size > maxSize) {
			$.acooly.messager.error('文件大小超过5MB：' + file.name);
			return false;
		}
		
		return true;
	}

	// ========== 更新显示图片列表 ==========
	function updateDisplayImages() {
		displayImages = [];
		
		// 1. 添加已存在的图片（过滤掉已删除的）
		productImages.existing.forEach(function(img) {
			if (productImages.deleted.indexOf(img.id) === -1) {
				displayImages.push({
					id: img.id,
					url: img.imageUrl,
					sortOrder: img.sortOrder || 0,
					isPrimary: img.isPrimary,
					isNew: false
				});
			}
		});
		
		// 2. 添加新选择的文件
		// 重要：如果已有主图，新图片不应该默认为主图
		// 只有在没有任何图片（包括已存在的）时，第一张新图片才默认为主图
		var hasExistingPrimary = displayImages.some(function(img) {
			return img.isPrimary === true;
		});
		var nextSortOrder = displayImages.length + 1;
		productImages.newFiles.forEach(function(file, index) {
			displayImages.push({
				id: null,
				file: file,
				url: null,  // 预览时使用FileReader生成
				sortOrder: nextSortOrder++,
				// 只有在没有任何主图时，第一张新图片才默认为主图
				isPrimary: !hasExistingPrimary && displayImages.length === 0,
				isNew: true
			});
		});
		
		// 3. 按排序顺序排序（确保顺序正确）
		displayImages.sort(function(a, b) {
			return (a.sortOrder || 0) - (b.sortOrder || 0);
		});
		
		// 4. 重新编号排序顺序（确保连续）
		updateImageSortOrder();
		
		// 5. 更新已存在图片ID列表
		updateExistingImageIds();
	}
	// 输出 product 对象到 JS
	var productTEST = {
		imageUrl: "${shopProducts.imageUrl!''}"
	};
	// ========== 渲染图片预览 ==========
	function renderImagePreview() {
		var container = $('#imagePreviewContainer');
		container.empty();

		if (displayImages.length === 0) {
			container.html('<div style="text-align: center; padding: 20px;">' +
					'<img src="' + (productTEST.imageUrl || '') + '" alt="Product Image" style="max-width: 100%; max-height: 200px; object-fit: contain;" />' +
					'</div>');
		}

		
		displayImages.forEach(function(img, index) {
			var card = createImageCard(img, index);
			container.append(card);
			
			// 如果是新文件，生成预览
			if (img.isNew && img.file) {
				var reader = new FileReader();
				reader.onload = function(e) {
					$('#' + img.cardId + ' img').attr('src', e.target.result);
				};
				reader.readAsDataURL(img.file);
			}
		});
	}

	// ========== 创建图片卡片 ==========
	function createImageCard(img, index) {
		var cardId = 'img_' + (img.id || 'new_' + Date.now() + '_' + index);
		img.cardId = cardId;
		
		var primaryBadge = img.isPrimary ? 
			'<span class="badge-primary">主图</span>' : '';
		
		var imageSrc = img.url || 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7';
		
		var setPrimaryBtn = img.isPrimary ? '' : 
			'<button type="button" onclick="setPrimary(' + index + ')">设为主图</button>';
		
		// 排序控制按钮
		var canMoveUp = index > 0;
		var canMoveDown = index < displayImages.length - 1;
		var sortControls = '<div class="sort-controls">' +
			'<button type="button" onclick="moveImageUp(' + index + ')" ' + (canMoveUp ? '' : 'disabled') + ' title="上移">' +
			'<i class="fa fa-arrow-up"></i> 上移</button>' +
			'<button type="button" onclick="moveImageDown(' + index + ')" ' + (canMoveDown ? '' : 'disabled') + ' title="下移">' +
			'<i class="fa fa-arrow-down"></i> 下移</button>' +
			'</div>';
		
		return '<div id="' + cardId + '" class="image-card" data-index="' + index + '">' +
			'<img src="' + imageSrc + '" alt="商品图片"/>' +
			primaryBadge +
			'<div class="image-actions">' +
			setPrimaryBtn +
			'<button type="button" onclick="removeImage(' + index + ')">删除</button>' +
			'</div>' +
			'<div class="sort-order">排序: ' + (index + 1) + '</div>' +
			sortControls +
			'</div>';
	}

	// ========== 设置主图 ==========
	function setPrimary(index) {
		// 清除所有主图标记
		displayImages.forEach(function(img) {
			img.isPrimary = false;
		});
		
		// 设置新的主图
		var primaryImg = displayImages[index];
		primaryImg.isPrimary = true;
		
		// 更新主图信息到隐藏字段
		updatePrimaryImageInfo();
		
		renderImagePreview();
	}
	
	// ========== 图片排序功能 ==========
	function moveImageUp(index) {
		if (index <= 0) {
			return; // 已经是第一张，无法上移
		}
		
		// 交换位置
		var temp = displayImages[index];
		displayImages[index] = displayImages[index - 1];
		displayImages[index - 1] = temp;
		
		// 更新排序顺序
		updateImageSortOrder();
		
		// 重新渲染
		renderImagePreview();
	}
	
	function moveImageDown(index) {
		if (index >= displayImages.length - 1) {
			return; // 已经是最后一张，无法下移
		}
		
		// 交换位置
		var temp = displayImages[index];
		displayImages[index] = displayImages[index + 1];
		displayImages[index + 1] = temp;
		
		// 更新排序顺序
		updateImageSortOrder();
		
		// 重新渲染
		renderImagePreview();
	}
	
	// ========== 更新图片排序顺序 ==========
	function updateImageSortOrder() {
		displayImages.forEach(function(img, index) {
			img.sortOrder = index + 1;
		});
		console.log('更新图片排序顺序:', displayImages.map(function(img) {
			return {id: img.id, sortOrder: img.sortOrder};
		}));
		
		// 更新隐藏字段：将图片ID和排序顺序传递给后端
		updateImageSortOrdersField();
	}
	
	// ========== 更新图片排序顺序隐藏字段 ==========
	function updateImageSortOrdersField() {
		// 收集所有已存在图片的ID和排序顺序（格式：imageId:sortOrder,imageId:sortOrder）
		var sortOrders = [];
		displayImages.forEach(function(img, index) {
			if (img.id) {
				// 只包含已存在的图片（有ID的）
				sortOrders.push(img.id + ':' + (index + 1));
			}
		});
		
		var sortOrdersStr = sortOrders.join(',');
		$('#imageSortOrders').val(sortOrdersStr);
		console.log('更新图片排序顺序隐藏字段:', sortOrdersStr);
	}
	
	// ========== 更新主图信息 ==========
	function updatePrimaryImageInfo() {
		// 查找主图
		var primaryImg = displayImages.find(function(img) {
			return img.isPrimary === true;
		});
		
		if (primaryImg) {
			if (primaryImg.id) {
				// 已存在的图片设为主图
				$('#primaryImageId').val(primaryImg.id);
				$('#primaryImageUrl').val('');
				console.log('设置已存在图片为主图 - ID:', primaryImg.id);
			} else if (primaryImg.isNew && primaryImg.file) {
				// 新上传的图片设为主图
				// 通过文件名匹配（上传后，后端可以通过文件名找到对应的图片）
				$('#primaryImageId').val('');
				$('#primaryImageUrl').val(primaryImg.file.name);  // 使用文件名作为标识
				console.log('设置新图片为主图 - 文件名:', primaryImg.file.name);
			} else {
				// 新图片但还没有文件对象（不应该发生）
				$('#primaryImageId').val('');
				$('#primaryImageUrl').val('');
			}
		} else {
			// 没有主图
			$('#primaryImageId').val('');
			$('#primaryImageUrl').val('');
		}
	}

	// ========== 更新已存在图片ID列表 ==========
	function updateExistingImageIds() {
		// 收集所有未删除的已存在图片ID
		var existingIds = productImages.existing
			.filter(function(img) {
				var isDeleted = productImages.deleted && productImages.deleted.indexOf(img.id) !== -1;
				if (isDeleted) {
					console.log('排除已删除的图片ID:', img.id);
				}
				return !isDeleted;
			})
			.map(function(img) {
				return img.id;
			});
		
		// 更新隐藏字段
		var existingIdsStr = existingIds.join(',');
		$('#existingImageIds').val(existingIdsStr);
		console.log('✅ 更新已存在图片ID列表 - 原始列表:', productImages.existing.map(function(img) { return img.id; }), 
				'删除列表:', productImages.deleted, 
				'保留列表:', existingIds, 
				'最终值:', existingIdsStr);
	}

	// ========== 删除图片 ==========
	function removeImage(index) {
		// 重要：检查是否只剩最后一张图片
		// 计算实际保留的图片数量（不包括已标记删除的）
		var remainingImages = displayImages.filter(function(img) {
			if (img.id) {
				// 已存在的图片：检查是否在删除列表中
				return productImages.deleted.indexOf(img.id) === -1;
			} else {
				// 新文件：检查是否还在newFiles列表中
				return productImages.newFiles.indexOf(img.file) !== -1;
			}
		});
		
		// 如果只剩最后一张图片，不允许删除
		if (remainingImages.length <= 1) {
			$.acooly.alert("至少需要保留一张图片！","error");
			console.log('阻止删除：只剩最后一张图片');
			return;  // 直接返回，不执行删除操作
		}
		
		var img = displayImages[index];
		
		if (img.id) {
			// 已存在的图片：标记删除
			// 【修复】确保 productImages.deleted 是数组
			if (!productImages.deleted || !Array.isArray(productImages.deleted)) {
				productImages.deleted = [];
				console.log('⚠️ productImages.deleted不是数组，已重新初始化');
			}
			
			if (productImages.deleted.indexOf(img.id) === -1) {
				productImages.deleted.push(img.id);
				console.log('✅ 标记删除图片 - ID:', img.id, '当前删除列表:', productImages.deleted, '长度:', productImages.deleted.length);
			} else {
				console.log('ℹ️ 图片ID已在删除列表中:', img.id);
			}
			
			// 【关键修复】立即更新 deletedImageIds 隐藏字段，确保值被保留
			var currentDeletedIds = $('#deletedImageIds').val();
			var deletedIdsArray = currentDeletedIds ? currentDeletedIds.split(',').filter(function(id) { return id.trim() !== ''; }) : [];
			if (deletedIdsArray.indexOf(String(img.id)) === -1) {
				deletedIdsArray.push(String(img.id));
			}
			$('#deletedImageIds').val(deletedIdsArray.join(','));
			console.log('✅ 【立即更新】deletedImageIds隐藏字段:', $('#deletedImageIds').val());
			
			// 【修复】如果删除的是主图，需要清除主图标记
			if (img.isPrimary) {
				$('#primaryImageId').val('');
				$('#primaryImageUrl').val('');
				console.log('⚠️ 删除的是主图，清除主图标记');
			}
			// 更新已存在图片ID列表
			updateExistingImageIds();
			// 更新图片排序顺序
			updateImageSortOrdersField();
		} else {
			// 新文件：从列表中移除
			var fileIndex = productImages.newFiles.indexOf(img.file);
			if (fileIndex > -1) {
				productImages.newFiles.splice(fileIndex, 1);
			}
			
			// 重要：由于浏览器安全限制，无法直接修改input.files删除单个文件
			// 如果删除了所有新文件，清空输入框
			// 否则，文件输入框会保留所有文件，后端会处理所有文件
			// 这是可以接受的，因为后端会处理所有上传的文件
			if (productImages.newFiles.length === 0) {
				var singleFileInput = document.getElementById('manage_shopProducts_imageFile');
				var multipleFileInput = document.getElementById('manage_shopProducts_imageFiles');
				if (singleFileInput) {
					singleFileInput.value = '';
				}
				if (multipleFileInput) {
					multipleFileInput.value = '';
				}
				console.log('删除所有新文件，清空文件输入框');
			} else {
				// 还有剩余文件，但由于浏览器限制无法从input.files中删除单个文件
				// 所以文件输入框会保留所有文件
				// 后端会处理所有文件，这是可以接受的
				console.log('删除部分新文件后，文件输入框仍保留所有文件（浏览器限制），剩余文件数:', productImages.newFiles.length);
				console.log('注意：提交时，后端会收到所有文件，包括已删除预览的文件');
			}
		}
		
		// 【修复】不需要手动splice，updateDisplayImages会重新构建displayImages
		// displayImages.splice(index, 1);  // 移除这行，因为updateDisplayImages会重新构建
		updateDisplayImages();
		renderImagePreview();
	}

	// ========== 触发文件选择 ==========
	function triggerSingleFileSelect() {
		$('#manage_shopProducts_imageFile').click();
	}

	function triggerMultipleFileSelect() {
		$('#manage_shopProducts_imageFiles').click();
	}

	// ========== 表单提交前处理 ==========
	// 在document.ready中绑定，确保只绑定一次
	$(document).ready(function() {
		// 移除可能存在的旧监听器，避免重复绑定
		$('#manage_shopProducts_editform').off('submit.imageUpload');
		
		// 绑定新的提交监听器
		$('#manage_shopProducts_editform').on('submit.imageUpload', function(e) {
			// 【修复】先确保所有隐藏字段都被正确设置，无论是否有图片变化
			// 0. 【重要】确保属性值ID被正确设置
			updateProductAttrValueIds();
			var attrValueIds = $('#productAttrValueIds').val();
			console.log('========== 【表单提交开始】 ==========');
			console.log('【属性值】提交的属性值ID:', attrValueIds || '(空)');
			
			// 1. 设置要删除的图片ID（优先处理，确保删除操作能正确传递）
			console.log('【调试】productImages.deleted:', productImages.deleted, '类型:', typeof productImages.deleted, '长度:', productImages.deleted ? productImages.deleted.length : 0);
			console.log('【调试】productImages.existing:', productImages.existing.map(function(img) { return img.id; }));
			
			if (productImages.deleted && productImages.deleted.length > 0) {
				var deletedIdsStr = productImages.deleted.join(',');
				$('#deletedImageIds').val(deletedIdsStr);
				console.log('✅ 【提交前】设置删除图片ID:', deletedIdsStr, '删除列表:', productImages.deleted);
			} else {
				$('#deletedImageIds').val('');
				console.log('ℹ️ 【提交前】没有删除的图片 - productImages.deleted:', productImages.deleted);
			}
			
			// 2. 更新已存在图片ID列表（排除已删除的）
			updateExistingImageIds();
			console.log('✅ 【提交前】设置已存在图片ID:', $('#existingImageIds').val());
			
			// 3. 更新图片排序顺序
			updateImageSortOrdersField();
			console.log('✅ 【提交前】设置图片排序顺序:', $('#imageSortOrders').val());
			
			// 4. 更新主图信息
			updatePrimaryImageInfo();
			console.log('✅ 【提交前】设置主图信息 - primaryImageId:', $('#primaryImageId').val(), 'primaryImageUrl:', $('#primaryImageUrl').val());
			
			// 【性能优化】检查是否有图片变化
			var hasImageChanges = checkImageChanges();
			
			// 【修复】重要：即使 checkImageChanges 返回 false，如果有删除操作，也要保留删除参数
			// 【关键修复】通过对比 existing 和 existingImageIds 来判断是否有删除操作（更可靠）
			var allExistingIds = productImages.existing.map(function(img) { return img.id; });
			var currentExistingIdsStr = $('#existingImageIds').val();
			var keptIds = currentExistingIdsStr ? currentExistingIdsStr.split(',').filter(function(id) { return id.trim() !== ''; }).map(function(id) { return parseInt(id.trim()); }) : [];
			var hasDeleteByComparison = allExistingIds.length > keptIds.length;  // 通过对比判断是否有删除
			var hasDeleteOperation = (productImages.deleted && productImages.deleted.length > 0) || hasDeleteByComparison;
			
			console.log('【删除检测】productImages.deleted:', productImages.deleted, '长度:', productImages.deleted ? productImages.deleted.length : 0);
			console.log('【删除检测】allExistingIds:', allExistingIds, 'keptIds:', keptIds);
			console.log('【删除检测】hasDeleteByComparison:', hasDeleteByComparison, 'hasDeleteOperation:', hasDeleteOperation);
			
			// 【关键修复】在判断之前，先检查 deletedImageIds 隐藏字段是否有值（最可靠的方法）
			var checkDeletedIdsFromField = $('#deletedImageIds').val();
			var hasDeleteFromField = checkDeletedIdsFromField && checkDeletedIdsFromField.trim() !== '';
			console.log('【删除检测】deletedImageIds隐藏字段值:', checkDeletedIdsFromField || '(空)', 'hasDeleteFromField:', hasDeleteFromField);
			
			if (!hasImageChanges && !hasDeleteOperation && !hasDeleteFromField) {
				// 没有图片变化，也没有删除操作，清空所有图片相关参数，让后端跳过图片处理
				$('#deletedImageIds').val('');
				$('#existingImageIds').val('');
				$('#primaryImageId').val('');
				$('#primaryImageUrl').val('');
				// 清空文件输入框
				$('#manage_shopProducts_imageFile').val('');
				$('#manage_shopProducts_imageFiles').val('');
				console.log('✅ 没有图片变化，跳过图片处理');
				return true;  // 继续提交表单，但后端会跳过图片处理
			}
			
			// 【关键修复】如果隐藏字段有值，确保保留它（优先级最高）
			if (hasDeleteFromField) {
				console.log('✅ 检测到deletedImageIds隐藏字段有值，保留:', checkDeletedIdsFromField);
				// 确保值不被清空
				$('#deletedImageIds').val(checkDeletedIdsFromField);
			}
			
			// 【修复】如果有删除操作，确保删除图片ID被设置（即使 checkImageChanges 返回 false）
			// 重要：无论 checkImageChanges 的结果如何，只要有删除操作，都要设置
			if (hasDeleteOperation) {
				var deletedIdsStr = '';
				
				// 优先使用 productImages.deleted
				if (productImages.deleted && Array.isArray(productImages.deleted) && productImages.deleted.length > 0) {
					deletedIdsStr = productImages.deleted.join(',');
					console.log('✅ 【提交时】使用productImages.deleted设置删除图片ID:', deletedIdsStr);
				} else if (hasDeleteByComparison) {
					// 如果 productImages.deleted 无效，但通过对比发现有删除，则通过对比计算删除的ID
					var deletedIds = allExistingIds.filter(function(id) {
						return keptIds.indexOf(id) === -1;
					});
					if (deletedIds.length > 0) {
						deletedIdsStr = deletedIds.join(',');
						console.log('✅ 【提交时】通过对比计算删除图片ID:', deletedIdsStr, 'allExistingIds:', allExistingIds, 'keptIds:', keptIds);
					}
				}
				
				if (deletedIdsStr) {
					$('#deletedImageIds').val(deletedIdsStr);
					console.log('✅ 【提交时】最终设置删除图片ID:', deletedIdsStr);
					// 同时更新 existingImageIds（排除已删除的）
					updateExistingImageIds();
					console.log('✅ 【提交时】更新已存在图片ID（排除已删除的）:', $('#existingImageIds').val());
				} else {
					console.error('❌ 错误：hasDeleteOperation为true，但无法确定删除的图片ID');
				}
			} else {
				console.log('ℹ️ 【提交时】没有删除操作 - productImages.deleted:', productImages.deleted);
			}
			
			// 重要：检查文件输入框的实际状态，确保文件能正常提交
			var singleFileInput = document.getElementById('manage_shopProducts_imageFile');
			var multipleFileInput = document.getElementById('manage_shopProducts_imageFiles');
			
			var hasSingleFile = singleFileInput && singleFileInput.files && singleFileInput.files.length > 0;
			var hasMultipleFiles = multipleFileInput && multipleFileInput.files && multipleFileInput.files.length > 0;
			
			// 调试信息
			console.log('========== 表单提交检查 ==========');
			console.log('单文件输入框:', hasSingleFile ? singleFileInput.files.length + ' 个文件' : '无文件');
			console.log('多文件输入框:', hasMultipleFiles ? multipleFileInput.files.length + ' 个文件' : '无文件');
			console.log('要删除的图片ID:', productImages.deleted);
			console.log('productImages.newFiles数量:', productImages.newFiles.length);
			
			// 如果两个输入框都有文件，需要决定使用哪个
			// 规则：优先使用多文件输入框（如果它有文件）
			if (hasSingleFile && hasMultipleFiles) {
				// 两个都有文件，清空单文件输入框，使用多文件输入框
				singleFileInput.value = '';
				console.log('✅ 使用多文件输入框（两个都有文件）');
			} else if (hasSingleFile && !hasMultipleFiles) {
				// 只有单文件，确保多文件输入框为空
				if (multipleFileInput) {
					multipleFileInput.value = '';
				}
				console.log('✅ 使用单文件输入框');
				// 显示上传提示和加载动画
				var fileName = singleFileInput.files[0].name;
				var fileSize = (singleFileInput.files[0].size / 1024 / 1024).toFixed(2);
				$.acooly.messager.info('正在上传图片 [' + fileName + ', ' + fileSize + 'MB]，请稍候...');
			} else if (hasMultipleFiles && !hasSingleFile) {
				// 只有多文件，确保单文件输入框为空
				// 重要：不要清空多文件输入框，文件需要保留才能提交
				if (singleFileInput) {
					singleFileInput.value = '';
				}
				console.log('✅ 使用多文件输入框，文件数量: ' + multipleFileInput.files.length);
				
				// 验证：确保文件还在
				if (multipleFileInput.files.length === 0) {
					console.error('❌ 错误：多文件输入框在清空单文件输入框后变为空！');
				}
				
				// 显示上传提示
				if (multipleFileInput.files.length > 0) {
					var totalSize = 0;
					var fileNames = [];
					for (var i = 0; i < multipleFileInput.files.length; i++) {
						totalSize += multipleFileInput.files[i].size;
						fileNames.push(multipleFileInput.files[i].name);
					}
					var totalSizeMB = (totalSize / 1024 / 1024).toFixed(2);
					console.log('准备上传的文件:', JSON.stringify(fileNames));
					$.acooly.messager.info('正在上传 ' + multipleFileInput.files.length + ' 张图片 [' + totalSizeMB + 'MB]，请稍候...');
				}
			} else {
				// 没有新文件，只处理删除
				console.log('ℹ️ 没有新文件，只处理删除操作');
			}
			
			// 最终检查：确保文件输入框有文件（如果应该有的话）
			if (productImages.newFiles.length > 0) {
				var finalCheckSingle = singleFileInput && singleFileInput.files && singleFileInput.files.length > 0;
				var finalCheckMultiple = multipleFileInput && multipleFileInput.files && multipleFileInput.files.length > 0;
				
				if (!finalCheckSingle && !finalCheckMultiple) {
					console.error('❌ 警告：productImages.newFiles有文件，但文件输入框为空！');
					console.error('这可能是因为文件输入框被意外清空，请检查代码');
					$.acooly.messager.warning('文件选择异常，请重新选择文件');
					// 不阻止提交，让后端处理
				}
			}
			
			// 最终验证：再次检查文件输入框（在清空操作之后）
			var finalHasMultipleFiles = multipleFileInput && multipleFileInput.files && multipleFileInput.files.length > 0;
			var finalHasSingleFile = singleFileInput && singleFileInput.files && singleFileInput.files.length > 0;
			console.log('========== 最终检查 ==========');
			console.log('单文件输入框:', finalHasSingleFile ? singleFileInput.files.length + ' 个文件' : '无文件');
			console.log('多文件输入框:', finalHasMultipleFiles ? multipleFileInput.files.length + ' 个文件' : '无文件');
			
			// 【修复】最终验证：确保删除和保留的图片ID都被正确设置
			var finalDeletedIds = $('#deletedImageIds').val();
			var finalExistingIds = $('#existingImageIds').val();
			var finalPrimaryId = $('#primaryImageId').val();
			var finalPrimaryUrl = $('#primaryImageUrl').val();
			console.log('========== 【最终验证】 ==========');
			console.log('【最终验证】删除图片ID:', finalDeletedIds || '(空)');
			console.log('【最终验证】保留图片ID:', finalExistingIds || '(空)');
			console.log('【最终验证】主图ID:', finalPrimaryId || '(空)');
			console.log('【最终验证】主图URL:', finalPrimaryUrl || '(空)');
			console.log('【最终验证】productImages.deleted:', productImages.deleted, '类型:', typeof productImages.deleted, '长度:', productImages.deleted ? productImages.deleted.length : 0);
			console.log('【最终验证】productImages.existing IDs:', productImages.existing.map(function(img) { return img.id; }));
			
			// 【修复】强制设置 deletedImageIds（如果 productImages.deleted 有值）
			// 重要：无论之前是否设置过，都要在最后强制设置一次，确保值正确
			if (productImages.deleted && productImages.deleted.length > 0) {
				var deletedIdsStr = productImages.deleted.join(',');
				$('#deletedImageIds').val(deletedIdsStr);
				console.log('✅ 【最终强制设置】deletedImageIds:', deletedIdsStr);
				
				// 验证设置是否成功
				var verifyDeletedIds = $('#deletedImageIds').val();
				if (verifyDeletedIds !== deletedIdsStr) {
					console.error('❌ 错误：设置deletedImageIds失败！期望:', deletedIdsStr, '实际:', verifyDeletedIds);
					// 再次尝试设置
					$('#deletedImageIds').val(deletedIdsStr);
				} else {
					console.log('✅ 验证成功：deletedImageIds已正确设置');
				}
			} else {
				// 【修复】如果 productImages.deleted 为空，但 existingImageIds 已更新，尝试反推删除的图片ID
				var allExistingIds = productImages.existing.map(function(img) { return img.id; });
				var keptIds = finalExistingIds ? finalExistingIds.split(',').filter(function(id) { return id.trim() !== ''; }).map(function(id) { return parseInt(id.trim()); }) : [];
				
				if (allExistingIds.length > keptIds.length) {
					// 有图片被删除了，计算删除的ID
					var deletedIds = allExistingIds.filter(function(id) {
						return keptIds.indexOf(id) === -1;
					});
					
					if (deletedIds.length > 0) {
						var deletedIdsStr = deletedIds.join(',');
						$('#deletedImageIds').val(deletedIdsStr);
						console.log('✅ 【反推删除】通过对比existing和kept列表，发现删除的图片ID:', deletedIdsStr);
					} else {
						$('#deletedImageIds').val('');
						console.log('ℹ️ 没有删除操作，清空deletedImageIds');
					}
				} else {
					$('#deletedImageIds').val('');
					console.log('ℹ️ 没有删除操作，清空deletedImageIds');
				}
			}
			
			// 【修复】强制更新 existingImageIds（确保排除已删除的）
			updateExistingImageIds();
			var finalExistingIdsAfterUpdate = $('#existingImageIds').val();
			console.log('✅ 【最终强制更新】existingImageIds:', finalExistingIdsAfterUpdate);
			console.log('================================');
			
			// 【修复】如果 existingImageIds 为空，但应该有保留的图片，强制更新
			if (productImages.existing && productImages.existing.length > 0 && (!finalExistingIds || finalExistingIds.trim() === '')) {
				console.error('❌ 警告：应该有保留的图片，但existingImageIds为空，强制更新！');
				updateExistingImageIds();
				console.log('✅ 已强制更新existingImageIds:', $('#existingImageIds').val());
			}
			
			// 【关键修复】在返回之前，最后一次强制设置 deletedImageIds
			// 通过对比 productImages.existing 和 existingImageIds 来确保 deletedImageIds 正确
			var finalAllExistingIds = productImages.existing.map(function(img) { return img.id; });
			var finalCurrentExistingIdsStr = $('#existingImageIds').val();
			var finalKeptIds = finalCurrentExistingIdsStr ? finalCurrentExistingIdsStr.split(',').filter(function(id) { return id.trim() !== ''; }).map(function(id) { return parseInt(id.trim()); }) : [];
			
			// 计算应该删除的ID（所有existing ID中不在kept列表中的）
			var shouldDeleteIds = finalAllExistingIds.filter(function(id) {
				return finalKeptIds.indexOf(id) === -1;
			});
			
			// 【关键修复】无论如何都要设置 deletedImageIds
			// 优先级：1. 隐藏字段已有值（最可靠） 2. 对比结果 3. productImages.deleted
			var finalDeletedIdsStr = '';
			var currentDeletedIdsFromField = $('#deletedImageIds').val();
			
			if (currentDeletedIdsFromField && currentDeletedIdsFromField.trim() !== '') {
				// 优先使用隐藏字段已有的值（最可靠，因为是在删除时立即设置的）
				finalDeletedIdsStr = currentDeletedIdsFromField.trim();
				console.log('✅ 【最后强制设置】使用隐藏字段已有值:', finalDeletedIdsStr);
			} else if (shouldDeleteIds.length > 0) {
				// 其次使用对比结果
				finalDeletedIdsStr = shouldDeleteIds.join(',');
				$('#deletedImageIds').val(finalDeletedIdsStr);
				console.log('✅ 【最后强制设置】通过对比计算，设置deletedImageIds:', finalDeletedIdsStr);
				console.log('   所有existing IDs:', finalAllExistingIds);
				console.log('   保留的IDs:', finalKeptIds);
				console.log('   应该删除的IDs:', shouldDeleteIds);
			} else if (productImages.deleted && productImages.deleted.length > 0) {
				// 最后使用 productImages.deleted
				finalDeletedIdsStr = productImages.deleted.join(',');
				$('#deletedImageIds').val(finalDeletedIdsStr);
				console.log('✅ 【最后强制设置】使用productImages.deleted，设置deletedImageIds:', finalDeletedIdsStr);
			} else {
				// 都没有，清空
				$('#deletedImageIds').val('');
				console.log('ℹ️ 【最后强制设置】没有删除操作，清空deletedImageIds');
			}
			
			// 确保最终值被设置
			if (finalDeletedIdsStr) {
				$('#deletedImageIds').val(finalDeletedIdsStr);
			}
			
			// 最终验证
			var finalCheckDeletedIds = $('#deletedImageIds').val();
			console.log('========== 【提交前最终检查】 ==========');
			console.log('deletedImageIds最终值:', finalCheckDeletedIds || '(空)');
			console.log('existingImageIds最终值:', $('#existingImageIds').val() || '(空)');
			console.log('productImages.deleted:', productImages.deleted);
			console.log('productImages.existing IDs:', finalAllExistingIds);
			console.log('保留的IDs:', finalKeptIds);
			console.log('应该删除的IDs:', shouldDeleteIds);
			console.log('========================================');
			
			// 【最后保障】如果最终值还是空，但应该有删除，再次强制设置
			if (!finalCheckDeletedIds || finalCheckDeletedIds.trim() === '') {
				if (shouldDeleteIds.length > 0) {
					var forceDeletedIdsStr = shouldDeleteIds.join(',');
					$('#deletedImageIds').val(forceDeletedIdsStr);
					console.error('❌ 警告：deletedImageIds为空，但应该删除的IDs:', forceDeletedIdsStr, '已强制设置！');
				}
			}
			
			// 【加载动画】检测是否有文件上传，如果有则显示加载动画（使用layer.load方式，参考dmCenterEdit.ftl）
			var hasFileUpload = hasSingleFile || hasMultipleFiles;
			if (hasFileUpload) {
				console.log('检测到文件上传，显示加载动画');
				var loadingIndex = layer.load(2, {
					shade: [0.5, '#fff'], // 加载遮罩背景颜色和透明度
					content: '正在上传图片，请稍候...',
					success: function (layero) {
						layero.find('.layui-layer-content').css({
							'padding-top': '39px',
							'width': '60px'
						});
					}
				});
				
				// 监控AJAX完成事件，关闭加载动画（使用一次性监听器，避免重复关闭）
				var ajaxStopHandler = function() {
					layer.close(loadingIndex);
					// 移除一次性监听器
					$(document).off('ajaxStop', ajaxStopHandler);
				};
				$(document).on('ajaxStop', ajaxStopHandler);
			}
			
			return true;
		});
	}); // 闭合 $(document).ready

	// ========== 重置文件输入框 ==========
	function resetFileInput(input) {
		if (!input) {
			input = document.getElementById('manage_shopProducts_imageFile');
		}
		if (input) {
			input.value = '';
		}
	}

	// ========== 【性能优化】检查是否有图片变化 ==========
	function checkImageChanges() {
		// 1. 检查是否有新文件上传
		var singleFileInput = document.getElementById('manage_shopProducts_imageFile');
		var multipleFileInput = document.getElementById('manage_shopProducts_imageFiles');
		var hasNewFiles = (singleFileInput && singleFileInput.files && singleFileInput.files.length > 0)
				|| (multipleFileInput && multipleFileInput.files && multipleFileInput.files.length > 0);
		if (hasNewFiles) {
			console.log('✅ 检测到新文件上传');
			return true;
		}
		
		// 2. 检查是否有删除图片（必须检查，这是删除功能的关键）
		if (productImages.deleted && productImages.deleted.length > 0) {
			console.log('✅ 检测到删除图片:', productImages.deleted, '长度:', productImages.deleted.length);
			return true;
		} else {
			console.log('ℹ️ 检查删除图片 - productImages.deleted:', productImages.deleted, '长度:', productImages.deleted ? productImages.deleted.length : 0);
		}
		
		// 3. 【修复】检查是否有主图变化（对比当前主图ID与原始主图ID）
		var currentPrimaryImageId = $('#primaryImageId').val();
		var currentPrimaryImageUrl = $('#primaryImageUrl').val();
		
		// 如果有新上传的图片设为主图，肯定有变化
		if (currentPrimaryImageUrl && currentPrimaryImageUrl.trim() !== '') {
			console.log('✅ 检测到主图变化 - 新上传的图片设为主图:', currentPrimaryImageUrl);
			return true;
		}
		
		// 如果当前主图ID与原始主图ID不同，说明主图发生了变化
		if (currentPrimaryImageId && currentPrimaryImageId.trim() !== '') {
			var currentPrimaryId = parseInt(currentPrimaryImageId.trim());
			// 如果原始主图ID为null，说明原来没有主图，现在有了，算变化
			if (originalPrimaryImageId === null) {
				console.log('✅ 检测到主图变化 - 原来没有主图，现在设置主图ID:', currentPrimaryId);
				return true;
			}
			// 如果当前主图ID与原始主图ID不同，说明主图发生了变化
			if (currentPrimaryId !== originalPrimaryImageId) {
				console.log('✅ 检测到主图变化 - 原始主图ID:', originalPrimaryImageId, '当前主图ID:', currentPrimaryId);
				return true;
			}
			// 如果相同，说明没有变化（用户将原主图重新设为主图）
			console.log('ℹ️ 主图未变化 - 原始主图ID:', originalPrimaryImageId, '当前主图ID:', currentPrimaryId);
		} else {
			// 如果当前主图ID为空，但原始主图ID不为空，说明主图被清空了（不应该发生，但也要处理）
			if (originalPrimaryImageId !== null) {
				console.log('⚠️ 检测到主图被清空 - 原始主图ID:', originalPrimaryImageId);
				return true;
			}
		}
		
		// 4. 没有图片变化
		console.log('ℹ️ 没有检测到图片变化');
		return false;
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
	 * 页面初始化
	 */
	$(document).ready(function() {

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
		
		// 【修复】设置品牌下拉框选中值
		var currentBrandId = '${shopProducts.brandId!""}';
		if (currentBrandId && currentBrandId.trim() !== '') {
			$('#brandSelect').val(currentBrandId).trigger('change.select2');
			console.log('设置品牌选中值 - brandId:', currentBrandId);
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