<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopConfig_editform" class="form-horizontal"
          action="/manage/shop/shopConfig/<#if action=='create'>saveJson<#else>updateJson</#if>.html"
          method="post"
          enctype="multipart/form-data">
        <@jodd.form bean="shopConfig" scope="request">
            <input name="id" type="hidden" value="${shopConfig.id!''}"/>

            <div class="card-body">
                <!-- 1. 轮播商品选择 -->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">包类商品</label>
                    <div class="col-sm-9">
                        <button type="button" class="btn btn-sm btn-primary" onclick="openProductSelector('carousel')">
                            <i class="fa fa-plus"></i> 选择商品
                        </button>
                        <div id="carouselSelectedProducts" class="selected-items"
                             style="margin-top: 10px; min-height: 40px;">
                            <!-- 已选商品标签将显示在这里 -->
                        </div>
                        <input type="hidden" name="carouselProducts" id="carouselProductsJson"
                               value="${shopConfig.carouselProducts!''}"/>
                    </div>
                </div>

                <!-- 2. 展示商品选择 -->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">手表商品</label>
                    <div class="col-sm-9">
                        <button type="button" class="btn btn-sm btn-primary" onclick="openProductSelector('display')">
                            <i class="fa fa-plus"></i> 选择商品
                        </button>
                        <div id="displaySelectedProducts" class="selected-items"
                             style="margin-top: 10px; min-height: 40px;">
                            <!-- 已选商品标签将显示在这里 -->
                        </div>
                        <input type="hidden" name="displayProducts" id="displayProductsJson"
                               value="${shopConfig.displayProducts!''}"/>
                    </div>
                </div>

                <!-- 3. 评论选择 -->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">展示评论</label>
                    <div class="col-sm-9">
                        <button type="button" class="btn btn-sm btn-primary" onclick="openReviewSelector()">
                            <i class="fa fa-plus"></i> 选择评论
                        </button>
                        <div id="selectedReviews" class="selected-items" style="margin-top: 10px; min-height: 40px;">
                            <!-- 已选评论标签将显示在这里 -->
                        </div>
                        <input type="hidden" name="showComments" id="showCommentsJson"
                               value="${shopConfig.showComments!''}"/>
                    </div>
                </div>

                <!-- 4. 头部展示编辑 -->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">头部展示</label>
                    <div class="col-sm-9">
                        <select id="headDisplayType" class="form-control" style="margin-bottom: 10px;"
                                onchange="switchHeadDisplayType()">
                            <option value="messages">消息轮播</option>
                        </select>

                        <!-- messages类型表单 -->
                        <div id="messagesForm" class="head-form">
                            <label>消息列表（每行一条）：</label>
                            <textarea name="headDisplay" id="messagesText" rows="8" class="form-control"
                                      placeholder="请输入消息，每行一条&#10;例如：&#10;🌵 Free U.S. shipping on orders >$75 USD 🌵 Lifetime Guarantee 🌵&#10;🌟 BLACK FRIDAY IS HERE – UP TO 40% OFF 🌟">${shopConfig.headDisplay!''}</textarea>
                            <small class="form-text text-muted">每行一条消息，支持emoji和特殊字符。直接保存为纯文本格式，避免JSON转义问题</small>
                        </div>
                    </div>
                </div>

                <!-- 5. Logo 上传 -->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">Logo</label>
                    <div class="col-sm-9">
                        <!-- 隐藏字段：存储URL -->
                        <input type="hidden" id="logoUrl" name="logoUrl" value="${shopConfig.logoUrl!''}"/>

                        <!-- 文件上传输入框 -->
                        <input type="file"
                               id="logoFile"
                               name="logoFile"
                               accept="image/jpeg,image/png,image/gif,image/webp"
                               style="display: none;"
                               onchange="handleLogoFileSelect(this)"/>

                        <!-- 预览区域 -->
                        <div id="logoPreview" style="margin-bottom: 10px;">
                            <#if shopConfig.logoUrl?? && shopConfig.logoUrl != ''>
                                <img src="${shopConfig.logoUrl}"
                                     alt="Logo预览"
                                     style="max-width: 200px; max-height: 100px; object-fit: contain; border: 1px solid #ddd; padding: 5px; border-radius: 4px;"/>
                            </#if>
                        </div>

                        <!-- 操作按钮 -->
                        <button type="button" onclick="triggerLogoFileSelect()" class="btn btn-sm btn-primary">
                            <i class="fa fa-image fa-fw"></i>选择Logo图片
                        </button>
                        <button type="button" onclick="clearLogo()" class="btn btn-sm btn-secondary"
                                style="margin-left: 10px;">
                            <i class="fa fa-times fa-fw"></i>清除
                        </button>

                        <span style="color: #6c757d; font-size: 12px; display: block; margin-top: 8px;">
                            支持JPG、PNG、GIF、WEBP格式，最大5MB
                        </span>
                    </div>
                </div>

                <!-- 6. 商品描述图片上传 -->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">商品描述图片</label>
                    <div class="col-sm-9">
                        <!-- 隐藏字段：存储URL -->
                        <input type="hidden" id="productDescriptionUrl" name="productDescriptionUrl"
                               value="${shopConfig.productDescriptionUrl!''}"/>

                        <!-- 文件上传输入框 -->
                        <input type="file"
                               id="productDescriptionFile"
                               name="productDescriptionFile"
                               accept="image/jpeg,image/png,image/gif,image/webp"
                               style="display: none;"
                               onchange="handleProductDescriptionFileSelect(this)"/>

                        <!-- 预览区域 -->
                        <div id="productDescriptionPreview" style="margin-bottom: 10px;">
                            <#if shopConfig.productDescriptionUrl?? && shopConfig.productDescriptionUrl != ''>
                                <img src="${shopConfig.productDescriptionUrl}"
                                     alt="商品描述图片预览"
                                     style="max-width: 300px; max-height: 200px; object-fit: contain; border: 1px solid #ddd; padding: 5px; border-radius: 4px;"/>
                            </#if>
                        </div>

                        <!-- 操作按钮 -->
                        <button type="button" onclick="triggerProductDescriptionFileSelect()"
                                class="btn btn-sm btn-primary">
                            <i class="fa fa-image fa-fw"></i>选择图片
                        </button>
                        <button type="button" onclick="clearProductDescription()" class="btn btn-sm btn-secondary"
                                style="margin-left: 10px;">
                            <i class="fa fa-times fa-fw"></i>清除
                        </button>

                        <span style="color: #6c757d; font-size: 12px; display: block; margin-top: 8px;">
                            支持JPG、PNG、GIF、WEBP格式，最大5MB
                        </span>
                    </div>
                </div>

                <!-- 7. 商品描述文本 -->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">商品描述</label>
                    <div class="col-sm-9">
                        <textarea rows="3" cols="40"
                                  placeholder="请输入商品描述..."
                                  name="productDescription"
                                  class="easyui-validatebox form-control form-words"
                                  data-words="500"></textarea>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
</div>

<!-- 商品选择器模态框 -->
<div id="productSelectorModal" class="modal"
     style="display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5);">
    <div class="modal-content"
         style="background: #fff; margin: 5% auto; padding: 20px; width: 80%; max-width: 900px; border-radius: 4px; max-height: 80vh; overflow-y: auto;">
        <div class="modal-header"
             style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px;">
            <h4 style="margin: 0;">选择商品</h4>
            <span class="close" onclick="closeProductSelector()"
                  style="font-size: 28px; font-weight: bold; cursor: pointer; color: #999;">&times;</span>
        </div>
        <div class="modal-body">
            <div style="margin-bottom: 15px;">
                <input type="text" id="productSearch" class="form-control" placeholder="搜索商品名称..."
                       style="width: 300px; display: inline-block;"/>
                <button type="button" class="btn btn-sm btn-primary" onclick="searchProducts()"
                        style="margin-left: 10px;">
                    <i class="fa fa-search"></i> 搜索
                </button>
            </div>
            <table id="productGrid"></table>
        </div>
        <div class="modal-footer"
             style="margin-top: 20px; text-align: right; border-top: 1px solid #eee; padding-top: 10px;">
            <button type="button" class="btn btn-primary" onclick="confirmProductSelection()">确定</button>
            <button type="button" class="btn btn-secondary" onclick="closeProductSelector()">取消</button>
        </div>
    </div>
</div>

<!-- 评论选择器模态框 -->
<div id="reviewSelectorModal" class="modal"
     style="display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5);">
    <div class="modal-content"
         style="background: #fff; margin: 5% auto; padding: 20px; width: 80%; max-width: 900px; border-radius: 4px; max-height: 80vh; overflow-y: auto;">
        <div class="modal-header"
             style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px;">
            <h4 style="margin: 0;">选择评论</h4>
            <span class="close" onclick="closeReviewSelector()"
                  style="font-size: 28px; font-weight: bold; cursor: pointer; color: #999;">&times;</span>
        </div>
        <div class="modal-body">
            <div style="margin-bottom: 15px;">
                <input type="text" id="reviewSearch" class="form-control" placeholder="搜索评论内容..."
                       style="width: 300px; display: inline-block;"/>
                <button type="button" class="btn btn-sm btn-primary" onclick="searchReviews()"
                        style="margin-left: 10px;">
                    <i class="fa fa-search"></i> 搜索
                </button>
            </div>
            <table id="reviewGrid"></table>
        </div>
        <div class="modal-footer"
             style="margin-top: 20px; text-align: right; border-top: 1px solid #eee; padding-top: 10px;">
            <button type="button" class="btn btn-primary" onclick="confirmReviewSelection()">确定</button>
            <button type="button" class="btn btn-secondary" onclick="closeReviewSelector()">取消</button>
        </div>
    </div>
</div>

<script type="text/javascript">
    // ========== 全局变量 ==========
    var currentSelectorType = ''; // 'carousel' 或 'display'

    // ========== 商品选择器 ==========
    var productGridInitialized = false;
    var reviewGridInitialized = false;

    function openProductSelector(type) {
        currentSelectorType = type;
        $('#productSelectorModal').show();

        // 如果datagrid未初始化，先初始化
        if (!productGridInitialized) {
            initProductGrid();
            productGridInitialized = true;
        } else {
            // 先清除所有选择，然后重新加载并标记当前类型的已选商品
            $('#productGrid').datagrid('clearSelections');
            $('#productGrid').datagrid('reload');
        }
    }

    function closeProductSelector() {
        $('#productSelectorModal').hide();
    }

    function initProductGrid() {
        $('#productGrid').datagrid({
            width: '100%',
            height: 400,
            checkbox: true,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            url: '/manage/shop/shopConfig/productsJson.html',
            method: 'get',
            idField: 'id',
            columns: [[
                {field: 'id', checkbox: true},
                {field: 'name', title: '商品名称', width: 200},
                {field: 'price', title: '价格', width: 100, formatter: formatPrice},
                {field: 'imageUrl', title: '图片', width: 150, formatter: formatImage}
            ]],
            onLoadSuccess: markSelectedProducts
        });
    }

    function loadProductGrid() {
        if (productGridInitialized) {
            $('#productGrid').datagrid('reload');
        }
    }

    function searchProducts() {
        var keyword = $('#productSearch').val();
        $('#productGrid').datagrid('load', {
            keyword: keyword
        });
    }

    function markSelectedProducts() {
        // 先清除所有选择
        $('#productGrid').datagrid('clearSelections');

        // 根据当前选择器类型获取对应的已选ID列表
        var fieldName = currentSelectorType === 'carousel' ? 'carouselProductsJson' : 'displayProductsJson';
        var currentIds = getCurrentSelectedIds(fieldName);

        // 标记当前类型的已选商品
        if (currentIds && currentIds.length > 0) {
            $('#productGrid').datagrid('getRows').forEach(function (row) {
                if (currentIds.indexOf(row.id) >= 0) {
                    $('#productGrid').datagrid('selectRecord', row.id);
                }
            });
        }
    }

    function confirmProductSelection() {
        var selected = $('#productGrid').datagrid('getSelections');
        var ids = selected.map(function (item) {
            return item.id;
        });

        var fieldName = currentSelectorType === 'carousel' ? 'carouselProducts' : 'displayProducts';
        var jsonFieldName = fieldName + 'Json';

        // 更新隐藏字段（存储为JSON数组字符串）
        $('#' + jsonFieldName).val(JSON.stringify(ids));

        // 更新显示
        updateSelectedProductsDisplay(currentSelectorType, selected);

        closeProductSelector();
    }

    function updateSelectedProductsDisplay(type, products) {
        var containerId = type === 'carousel' ? 'carouselSelectedProducts' : 'displaySelectedProducts';
        var html = '';

        products.forEach(function (product) {
            html += '<span class="badge badge-info" style="margin-right: 5px; margin-bottom: 5px; padding: 5px 10px; font-size: 12px; display: inline-block;">' +
                product.name +
                ' <i class="fa fa-times" style="cursor: pointer; margin-left: 5px;" onclick="removeProduct(' + product.id + ', \'' + type + '\')"></i>' +
                '</span>';
        });

        $('#' + containerId).html(html);
    }

    function removeProduct(productId, type) {
        var fieldName = type === 'carousel' ? 'carouselProductsJson' : 'displayProductsJson';
        var currentIds = getCurrentSelectedIds(fieldName);
        currentIds = currentIds.filter(function (id) {
            return id !== productId;
        });
        $('#' + fieldName).val(JSON.stringify(currentIds));

        // 重新加载显示
        loadSelectedProducts(type);
    }

    function loadSelectedProducts(type) {
        var fieldName = type === 'carousel' ? 'carouselProductsJson' : 'displayProductsJson';
        var ids = getCurrentSelectedIds(fieldName);

        if (ids.length === 0) {
            var containerId = type === 'carousel' ? 'carouselSelectedProducts' : 'displaySelectedProducts';
            $('#' + containerId).html('');
            return;
        }

        // 通过ID查询商品详情
        $.ajax({
            url: '/manage/shop/shopConfig/productsJson.html',
            data: {ids: ids.join(',')},
            success: function (result) {
                if (result.rows) {
                    updateSelectedProductsDisplay(type, result.rows);
                }
            }
        });
    }

    // ========== 评论选择器 ==========
    function openReviewSelector() {
        $('#reviewSelectorModal').show();

        // 如果datagrid未初始化，先初始化
        if (!reviewGridInitialized) {
            initReviewGrid();
            reviewGridInitialized = true;
        } else {
            $('#reviewGrid').datagrid('reload');
        }
    }

    function closeReviewSelector() {
        $('#reviewSelectorModal').hide();
    }

    function initReviewGrid() {
        $('#reviewGrid').datagrid({
            width: '100%',
            height: 400,
            checkbox: true,
            pagination: true,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            url: '/manage/shop/shopConfig/reviewsJson.html',
            method: 'get',
            idField: 'id',
            columns: [[
                {field: 'id', checkbox: true},
                {field: 'rating', title: '评分', width: 80, formatter: formatRating},
                {field: 'comment', title: '评论内容', width: 300},
                // {field: 'userId', title: '用户ID', width: 100},
                {field: 'createTime', title: '创建时间', width: 150, formatter: formatDate}
            ]],
            onLoadSuccess: markSelectedReviews
        });
    }

    function loadReviewGrid() {
        if (reviewGridInitialized) {
            $('#reviewGrid').datagrid('reload');
        }
    }

    function searchReviews() {
        var keyword = $('#reviewSearch').val();
        $('#reviewGrid').datagrid('load', {
            keyword: keyword
        });
    }

    function markSelectedReviews() {
        var currentIds = getCurrentSelectedIds('showCommentsJson');

        $('#reviewGrid').datagrid('getRows').forEach(function (row) {
            if (currentIds.indexOf(row.id) >= 0) {
                $('#reviewGrid').datagrid('selectRecord', row.id);
            }
        });
    }

    function confirmReviewSelection() {
        var selected = $('#reviewGrid').datagrid('getSelections');
        var ids = selected.map(function (item) {
            return item.id;
        });

        $('#showCommentsJson').val(JSON.stringify(ids));
        updateSelectedReviewsDisplay(selected);
        closeReviewSelector();
    }

    function updateSelectedReviewsDisplay(reviews) {
        var html = '';
        reviews.forEach(function (review) {
            var comment = review.comment || '';
            if (comment.length > 50) comment = comment.substring(0, 50) + '...';
            html += '<span class="badge badge-info" style="margin-right: 5px; margin-bottom: 5px; padding: 5px 10px; font-size: 12px; display: inline-block;">' +
                comment +
                ' <i class="fa fa-times" style="cursor: pointer; margin-left: 5px;" onclick="removeReview(' + review.id + ')"></i>' +
                '</span>';
        });
        $('#selectedReviews').html(html);
    }

    function removeReview(reviewId) {
        var currentIds = getCurrentSelectedIds('showCommentsJson');
        currentIds = currentIds.filter(function (id) {
            return id !== reviewId;
        });
        $('#showCommentsJson').val(JSON.stringify(currentIds));
        loadSelectedReviews();
    }

    function loadSelectedReviews() {
        var ids = getCurrentSelectedIds('showCommentsJson');
        if (ids.length === 0) {
            $('#selectedReviews').html('');
            return;
        }

        $.ajax({
            url: '/manage/shop/shopConfig/reviewsJson.html',
            data: {ids: ids.join(',')},
            success: function (result) {
                if (result.rows) {
                    updateSelectedReviewsDisplay(result.rows);
                }
            }
        });
    }

    // ========== 头部展示编辑 ==========
    function switchHeadDisplayType() {
        var type = $('#headDisplayType').val();
        $('.head-form').hide();
        $('#' + type + 'Form').show();
    }

    // ========== Logo 图片上传处理 ==========
    function triggerLogoFileSelect() {
        $('#logoFile').click();
    }

    function handleLogoFileSelect(input) {
        var file = input.files[0];
        if (!file) return;

        // 验证文件
        if (!validateImageFile(file)) {
            input.value = '';
            return;
        }

        // 预览图片
        var reader = new FileReader();
        reader.onload = function (e) {
            var previewHtml = '<img src="' + e.target.result + '" ' +
                'alt="Logo预览" ' +
                'style="max-width: 200px; max-height: 100px; object-fit: contain; border: 1px solid #ddd; padding: 5px; border-radius: 4px;"/>';
            $('#logoPreview').html(previewHtml);
        };
        reader.readAsDataURL(file);
    }

    function clearLogo() {
        $('#logoFile').val('');
        $('#logoUrl').val('');
        $('#logoPreview').html('');
    }

    // ========== 商品描述图片上传处理 ==========
    function triggerProductDescriptionFileSelect() {
        $('#productDescriptionFile').click();
    }

    function handleProductDescriptionFileSelect(input) {
        var file = input.files[0];
        if (!file) return;

        // 验证文件
        if (!validateImageFile(file)) {
            input.value = '';
            return;
        }

        // 预览图片
        var reader = new FileReader();
        reader.onload = function (e) {
            var previewHtml = '<img src="' + e.target.result + '" ' +
                'alt="商品描述图片预览" ' +
                'style="max-width: 300px; max-height: 200px; object-fit: contain; border: 1px solid #ddd; padding: 5px; border-radius: 4px;"/>';
            $('#productDescriptionPreview').html(previewHtml);
        };
        reader.readAsDataURL(file);
    }

    function clearProductDescription() {
        $('#productDescriptionFile').val('');
        $('#productDescriptionUrl').val('');
        $('#productDescriptionPreview').html('');
    }

    // ========== 文件验证函数（复用）==========
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

    // ========== 工具函数 ==========
    function getCurrentSelectedIds(fieldName) {
        var jsonStr = $('#' + fieldName).val();
        if (!jsonStr || jsonStr.trim() === '') {
            return [];
        }
        try {
            return JSON.parse(jsonStr);
        } catch (e) {
            return [];
        }
    }

    function formatPrice(value) {
        return '¥' + (value || 0).toFixed(2);
    }

    function formatImage(value) {
        if (value) {
            return '<img src="' + value + '" style="width: 50px; height: 50px; object-fit: cover;"/>';
        }
        return '-';
    }

    function formatRating(value) {
        var stars = '';
        for (var i = 1; i <= 5; i++) {
            stars += '<i class="fa fa-star ' + (i <= value ? 'text-warning' : 'text-muted') + '"></i>';
        }
        return stars;
    }

    function formatDate(value) {
        if (!value) return '-';
        return new Date(value).toLocaleString('zh-CN');
    }

    // ========== 页面初始化 ==========
    $(function () {
        // 页面加载时，反填数据
        <#if shopConfig??>
        // 反填商品
        var carouselIds = getCurrentSelectedIds('carouselProductsJson');
        if (carouselIds.length > 0) {
            loadSelectedProducts('carousel');
        }

        var displayIds = getCurrentSelectedIds('displayProductsJson');
        if (displayIds.length > 0) {
            loadSelectedProducts('display');
        }

        // 反填评论
        var reviewIds = getCurrentSelectedIds('showCommentsJson');
        if (reviewIds.length > 0) {
            loadSelectedReviews();
        }
        var headDisplay = '${shopConfig.headDisplay?js_string}';
        if (headDisplay) {
            try {
                var headJson = JSON.parse(headDisplay);
                if (headJson.type === 'messages') {
                    var messages = [];
                    if (headJson.items && headJson.items.length > 0) {
                        messages = headJson.items.map(function (item) {
                            return item.text || '';
                        });
                    } else if (headJson.messages) {
                        messages = headJson.messages;
                    }
                    $('#messagesText').val(messages.join('\n'));
                }
            } catch (e) {
                $('#messagesText').val(headDisplay);
            }
        }
        </#if>

        // 监听保存成功事件，自动刷新父页面
        $(document).ready(function () {
            // 方式1：监听全局AJAX成功事件（最可靠的方式）
            $(document).ajaxSuccess(function (event, xhr, settings) {
                // 检查是否是保存或更新的请求
                var url = settings.url || '';
                if (url.indexOf('/manage/shop/shopConfig/') > -1 &&
                    (url.indexOf('saveJson') > -1 || url.indexOf('updateJson') > -1)) {
                    try {
                        var result = typeof xhr.responseJSON !== 'undefined' ? xhr.responseJSON : JSON.parse(xhr.responseText);
                        if (result && result.success) {
                            // 延迟一下，确保框架处理完对话框关闭和消息提示
                            setTimeout(function () {
                                refreshParentPage();
                            }, 800);
                        }
                    } catch (e) {
                        // 解析失败，忽略
                    }
                }
            });

            // 方式2：重写框架的保存成功回调（如果框架支持）
            if (typeof $.acooly !== 'undefined' && $.acooly.framework) {
                // 保存原始的成功回调
                var originalOnSaveSuccess = $.acooly.framework.onSaveSuccess;
                var originalOnUpdateSuccess = $.acooly.framework.onUpdateSuccess;

                if (originalOnSaveSuccess) {
                    $.acooly.framework.onSaveSuccess = function (dialog, datagrid, result, reload) {
                        originalOnSaveSuccess.call(this, dialog, datagrid, result, reload);
                        // 刷新父页面
                        setTimeout(function () {
                            refreshParentPage();
                        }, 500);
                    };
                }

                if (originalOnUpdateSuccess) {
                    $.acooly.framework.onUpdateSuccess = function (dialog, datagrid, result, reload) {
                        originalOnUpdateSuccess.call(this, dialog, datagrid, result, reload);
                        // 刷新父页面
                        setTimeout(function () {
                            refreshParentPage();
                        }, 500);
                    };
                }
            }
        });

        // 刷新父页面的函数
        function refreshParentPage() {
            try {
                // 检查是否在对话框中（iframe或dialog）
                if (window.parent && window.parent !== window) {
                    // 在对话框中，刷新父页面
                } else {
                    // 不在对话框中，刷新当前页面
                }
            } catch (e) {
                console.log('刷新父页面失败:', e);
                // 如果跨域或其他原因失败，尝试刷新当前页面
                try {
                } catch (e2) {
                    console.log('刷新当前页面也失败:', e2);
                }
            }
        }
    });
</script>
