<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
	<form id="manage_shopCoupon_editform" class="form-horizontal" action="/manage/shop/shopCoupon/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopCoupon" scope="request">
			<input name="id" type="hidden" />
			<div class="card-body">
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">优惠方式</label>
					<div class="col-sm-9">
						<select name="discountType" class="form-control select2bs4" data-options="required:true">
							<option value="percent" <#if shopCoupon.discountType?? && shopCoupon.discountType == "percent">selected</#if>>打折</option>
							<#--						<option value="EXPIRED">过期</option>-->
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">优惠力度</label>
					<div class="col-sm-9">
						<select name="discountValue" id="discountValueSelect" class="form-control select2bs4" data-options="required:true">
							<option value="0.95" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.95">selected</#if>>95折</option>
							<option value="0.9" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.9">selected</#if>>9折</option>
							<option value="0.85" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.85">selected</#if>>85折</option>
							<option value="0.8" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.8">selected</#if>>8折</option>
							<option value="0.75" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.75">selected</#if>>75折</option>
							<option value="0.7" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.7">selected</#if>>7折</option>
							<option value="0.65" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.65">selected</#if>>65折</option>
							<option value="0.6" <#if shopCoupon.discountValue?? && shopCoupon.discountValue?string == "0.6">selected</#if>>6折</option>
						</select>
					</div>
				</div>

				<div class="form-group row">
					<label class="col-sm-3 col-form-label">券可被全局使用的总次数</label>
					<div class="col-sm-9">
						<input type="text" name="totalUses" placeholder="请输入券可被全局使用的总次数，0 表示不限..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
					</div>
				</div>
				<#--			<div class="form-group row">-->
				<#--				<label class="col-sm-3 col-form-label">已被使用次数</label>-->
				<#--				<div class="col-sm-9">-->
				<#--					<input type="text" name="usedCount" placeholder="请输入已被使用次数..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
				<#--				</div>-->
				<#--			</div>-->
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">单用户使用次数</label>
					<div class="col-sm-9">
						<input type="text" name="perUserLimit" placeholder="请输入每个用户最多使用次数，0 表示不限..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">团队</label>
					<div class="col-sm-9">
						<select name="teamId" class="form-control select2bs4" data-options="required:true" required>
							<#list teamMap as k,v >
								<option value="${k}">${v}</option>
							</#list>
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">状态</label>
					<div class="col-sm-9">
						<select name="status" class="form-control select2bs4" data-options="required:true">
							<option value="ACTIVE" <#if shopCoupon.status?? && shopCoupon.status == "ACTIVE">selected</#if>>启用</option>
							<option value="EXPIRED" <#if shopCoupon.status?? && shopCoupon.status == "EXPIRED">selected</#if>>过期</option>
						</select>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">有效期开始</label>
					<div class="col-sm-9">
						<input type="text" name="validFrom" placeholder="请输入有效期开始..." class="easyui-validatebox form-control" value="<#if shopCoupon.validFrom??>${shopCoupon.validFrom?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">有效期结束</label>
					<div class="col-sm-9">
						<input type="text" name="validTo" placeholder="请输入有效期结束..." class="easyui-validatebox form-control" value="<#if shopCoupon.validTo??>${shopCoupon.validTo?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
					</div>
				</div>
			</div>
		</@jodd.form>
	</form>
</div>

<style>
    /* 加载动画样式 */
    .image-upload-loading {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        z-index: 9999;
        display: none;
        justify-content: center;
        align-items: center;
    }
    
    .image-upload-loading.show {
        display: flex;
    }
    
    .image-upload-loading-content {
        background: white;
        padding: 30px 40px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
        text-align: center;
        min-width: 200px;
    }
    
    .image-upload-loading-spinner {
        border: 4px solid #f3f3f3;
        border-top: 4px solid #3498db;
        border-radius: 50%;
        width: 50px;
        height: 50px;
        animation: spin 1s linear infinite;
        margin: 0 auto 20px;
    }
    
    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }
    
    .image-upload-loading-text {
        color: #333;
        font-size: 16px;
        font-weight: 500;
        margin-top: 10px;
    }
</style>

<!-- 加载动画容器 -->
<div id="imageUploadLoading" class="image-upload-loading">
    <div class="image-upload-loading-content">
        <div class="image-upload-loading-spinner"></div>
        <div class="image-upload-loading-text">正在上传图片，请稍候...</div>
    </div>
</div>

<script type="text/javascript">
    $(function() {
        // 修复 discountValue 选中问题：BigDecimal 可能格式化为 "0.90" 而不是 "0.9"
        // 使用 JavaScript 确保正确选中（兼容 Select2）
        <#if shopCoupon.discountValue??>
            var discountValue = parseFloat('${shopCoupon.discountValue}');
            
            // 函数：设置选中值
            function setSelectedDiscountValue() {
                var found = false;
                $('#discountValueSelect option').each(function() {
                    var optionValue = parseFloat($(this).val());
                    // 浮点数比较，允许小的误差（处理精度问题）
                    if (Math.abs(optionValue - discountValue) < 0.001) {
                        $(this).prop('selected', true);
                        found = true;
                        return false; // 找到匹配项后退出循环
                    }
                });
                
                // 如果使用 Select2，需要触发更新
                if ($('#discountValueSelect').hasClass('select2bs4') || $('#discountValueSelect').data('select2')) {
                    $('#discountValueSelect').trigger('change.select2');
                }
                
                return found;
            }
            
            // 立即尝试设置
            setSelectedDiscountValue();
            
            // 如果 Select2 还未初始化，延迟设置（等待 Select2 初始化完成）
            setTimeout(function() {
                setSelectedDiscountValue();
            }, 100);
        </#if>
        
        // ========== 图片上传加载动画 ==========
        var $loadingOverlay = $('#imageUploadLoading');
        var loadingTimer = null;
        
        // 检查表单中是否有图片文件输入框
        var $imageFileInput = $('input[type="file"]');
        var hasImageFiles = false;
        
        // 监听文件选择
        $imageFileInput.on('change', function() {
            var files = this.files;
            hasImageFiles = files && files.length > 0;
            console.log('检测到文件选择 - 文件数量:', files ? files.length : 0);
        });
        
        // 检查当前是否有文件
        $imageFileInput.each(function() {
            if (this.files && this.files.length > 0) {
                hasImageFiles = true;
            }
        });
        
        // 监听表单提交
        $('#manage_shopCoupon_editform').on('submit', function(e) {
            // 检查是否有图片文件
            var hasFiles = false;
            $imageFileInput.each(function() {
                if (this.files && this.files.length > 0) {
                    hasFiles = true;
                    return false; // 退出循环
                }
            });
            
            // 如果有图片文件，显示加载动画
            if (hasFiles) {
                console.log('检测到图片上传，显示加载动画');
                showImageUploadLoading();
                
                // 方式1：监听全局AJAX完成事件（框架使用AJAX提交表单）
                var ajaxCompleteHandler = function(event, xhr, settings) {
                    // 检查是否是当前表单的提交
                    var url = settings.url || '';
                    if (url.indexOf('/shopCoupon/saveJson') >= 0 || 
                        url.indexOf('/shopCoupon/updateJson') >= 0) {
                        console.log('表单提交完成，隐藏加载动画');
                        hideImageUploadLoading();
                        // 移除一次性监听器
                        $(document).off('ajaxComplete', ajaxCompleteHandler);
                        $(document).off('ajaxSuccess', ajaxSuccessHandler);
                        $(document).off('ajaxError', ajaxErrorHandler);
                    }
                };
                
                var ajaxSuccessHandler = function(event, xhr, settings) {
                    var url = settings.url || '';
                    if (url.indexOf('/shopCoupon/saveJson') >= 0 || 
                        url.indexOf('/shopCoupon/updateJson') >= 0) {
                        console.log('表单提交成功，隐藏加载动画');
                        setTimeout(function() {
                            hideImageUploadLoading();
                        }, 500); // 延迟500ms，确保消息提示已显示
                    }
                };
                
                var ajaxErrorHandler = function(event, xhr, settings) {
                    var url = settings.url || '';
                    if (url.indexOf('/shopCoupon/saveJson') >= 0 || 
                        url.indexOf('/shopCoupon/updateJson') >= 0) {
                        console.log('表单提交失败，隐藏加载动画');
                        hideImageUploadLoading();
                    }
                };
                
                $(document).on('ajaxComplete', ajaxCompleteHandler);
                $(document).on('ajaxSuccess', ajaxSuccessHandler);
                $(document).on('ajaxError', ajaxErrorHandler);
                
                // 方式2：备用方案 - 定时器检查（防止AJAX事件未触发）
                if (loadingTimer) {
                    clearTimeout(loadingTimer);
                }
                loadingTimer = setTimeout(function() {
                    // 如果5秒后还在显示，检查页面状态
                    if ($loadingOverlay.hasClass('show')) {
                        // 检查是否有消息提示（成功或失败）
                        var hasMessage = $('.messager-body, .alert, .message, .ui-dialog').length > 0;
                        if (hasMessage) {
                            console.log('检测到消息提示，隐藏加载动画');
                            hideImageUploadLoading();
                        }
                    }
                }, 5000);
            }
        });
        
        // 显示加载动画
        function showImageUploadLoading() {
            $loadingOverlay.addClass('show');
            // 禁用表单提交按钮，防止重复提交
            $('#manage_shopCoupon_editform').find('button[type="submit"], input[type="submit"]').prop('disabled', true);
        }
        
        // 隐藏加载动画
        function hideImageUploadLoading() {
            $loadingOverlay.removeClass('show');
            // 恢复表单提交按钮
            $('#manage_shopCoupon_editform').find('button[type="submit"], input[type="submit"]').prop('disabled', false);
            // 清除定时器
            if (loadingTimer) {
                clearTimeout(loadingTimer);
                loadingTimer = null;
            }
        }
        
        // 监听页面卸载事件（表单提交后页面可能刷新）
        $(window).on('beforeunload', function() {
            if ($loadingOverlay.hasClass('show')) {
                // 页面即将卸载，隐藏加载动画
                hideImageUploadLoading();
            }
        });
    });
</script>
