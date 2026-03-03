<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    /* 🌿 全局 - 使用作用域限制避免冲突 */
    .shop-products-container {
        background: #f5f6f8;
        font-family: "Inter", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
        color: #2d3748;
    }

    /* 🌈 搜索栏（优化高度和分层）- 使用作用域限制避免冲突 */
    .shop-products-container .search-panel {
        background: #fafafa; /* 更浅的灰色区分工具栏 */
        padding: 8px 14px;    /* 高度减小 */
        margin-bottom: 8px;
        border-radius: 12px;
        box-shadow: 0 1px 4px rgba(0,0,0,0.08); /* 轻微阴影形成分层 */
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        gap: 10px;
        border-bottom: 2px solid #e5e7eb; /* 增强工具栏分隔感 */
    }

    .shop-products-container .search-panel .form-group {
        display: flex;
        align-items: center;
    }

    .shop-products-container .search-panel label {
        color: #4b5563;
        font-weight: 500;
        margin-right: 6px;
    }

    .shop-products-container .search-panel .form-control {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 4px 8px; /* 高度进一步减小 */
        font-size: 13px;
        transition: border-color 0.2s;
    }

    .shop-products-container .search-panel .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99,102,241,0.15);
    }

    .shop-products-container .search-btn {
        background: linear-gradient(135deg, #6366f1, #8b5cf6);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 5px 12px; /* 按钮更紧凑 */
        font-size: 13px;
        font-weight: 500;
        transition: 0.3s;
    }
    .shop-products-container .search-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 10px rgba(99,102,241,0.3);
    }

    /* 🧭 工具栏 */
    #manage_shopProducts_toolbar {
        background: #fff;
        border-radius: 12px;
        padding: 10px 16px;
        margin-bottom: 12px;
        box-shadow: 0 1px 6px rgba(0,0,0,0.05);
        display: flex;
        align-items: center;
        gap: 10px;
    }

    #manage_shopProducts_toolbar .easyui-linkbutton {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 8px;
        padding: 6px 12px;
        transition: 0.2s;
    }
    #manage_shopProducts_toolbar .easyui-linkbutton:hover {
        background: #eef2ff;
        border-color: #6366f1;
        color: #6366f1;
    }

    /* 📋 表格 */
    #manage_shopProducts_datagrid {
        background: #fff;
        overflow: visible; /* 必须 */
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    }

    .shop-products-container .datagrid-header-row {
        background: #f9fafb !important;
    }

    #manage_shopProducts_datagrid .datagrid-header th {
        font-weight: 600;
        color: #4b5563;
        font-size: 13px;
        border-bottom: 2px solid #e5e7eb;
        padding: 16px 14px;
        background: #f9fafb;
        white-space: nowrap;
    }

    #manage_shopProducts_datagrid .datagrid-body td {
        border-bottom: 1px solid #f3f4f6;
        padding: 18px 14px;
        vertical-align: middle;
        line-height: 1.8;
    }

    #manage_shopProducts_datagrid .datagrid-row {
        transition: background-color 0.2s;
    }
    
    #manage_shopProducts_datagrid .datagrid-row:hover {
        background: #f9fafc !important;
    }
    
    /* 优化列宽和间距 */
    #manage_shopProducts_datagrid .datagrid-cell {
        padding: 10px 14px;
        white-space: nowrap;   /* ✅ */
        word-wrap: break-word;
        font-size: 13px;
    }
    
    /* 优化表格行间距 */
    #manage_shopProducts_datagrid .datagrid-body tr {
        border-bottom: 1px solid #f3f4f6;
    }
    
    #manage_shopProducts_datagrid .datagrid-body tr:last-child {
        border-bottom: none;
    }

    /* 🖼️ 商品图片 */
    .product-image {
        width: 70px;
        height: 70px;
        border-radius: 10px;
        object-fit: cover;
        border: 2px solid #e5e7eb;
        cursor: zoom-in;
        transition: 0.3s;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .product-image:hover {
        border-color: #6366f1;
        transform: scale(1.08);
        box-shadow: 0 4px 8px rgba(99,102,241,0.2);
    }

    /* 💵 价格样式 */
    .product-price {
        color: #ef4444;
        font-weight: 700;
        font-size: 16px;
        letter-spacing: 0.3px;
    }
    .product-original-price {
        color: #9ca3af;
        text-decoration: line-through;
        font-size: 14px;
        margin-right: 8px;
        font-weight: 500;
    }

    /* ⭐ 评分 */
    .star-rating {
        color: #fbbf24;
    }

    /* 🏷️ 状态 - 使用作用域限制避免冲突 */
    .shop-products-container .badge {
        border-radius: 999px;
        padding: 4px 10px;
        font-size: 12px;
        font-weight: 600;
    }
    .shop-products-container .badge-featured {
        background: linear-gradient(135deg, #f59e0b, #fbbf24);
        color: #fff;
    }
    .shop-products-container .badge-normal {
        background: #e5e7eb;
        color: #6b7280;
    }

    /* ⚙️ 操作按钮 - 使用作用域限制避免冲突 */
    .shop-products-container .action-buttons .btn {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 8px;
        padding: 6px 12px;
        font-size: 12px;
        margin-right: 6px;
        transition: 0.2s;
        font-weight: 500;
    }
    .shop-products-container .action-buttons .btn:hover {
        background: #eef2ff;
        color: #4f46e5;
        transform: translateY(-2px);
        box-shadow: 0 2px 6px rgba(99,102,241,0.2);
    }
    
    /* 🏷️ 品牌标签 - 使用作用域限制避免冲突 */
    .shop-products-container .badge-brand {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 600;
    }
    
    /* 📝 商品编号样式 */
    .serial-number-badge {
        display: inline-block;
        padding: 8px 16px;
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: #fff;
        border-radius: 10px;
        font-size: 12px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
        box-shadow: 0 2px 6px rgba(102,126,234,0.3);
        letter-spacing: 0.5px;
        font-family: 'Courier New', monospace;
        border: none;
    }
    
    .serial-number-badge:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(102,126,234,0.4);
        background: linear-gradient(135deg, #764ba2, #667eea);
    }
    
    .serial-number-badge:active {
        transform: translateY(0);
        box-shadow: 0 2px 6px rgba(102,126,234,0.3);
    }

    /* 🖼️ 图片预览弹窗 */
    #imagePreviewDialog {
        border-radius: 14px !important;
        overflow: hidden;
        background: #fff;
    }
    #previewImage {
        max-width: 95%;
        max-height: 70vh;
        border-radius: 12px;
        box-shadow: 0 10px 40px rgba(0,0,0,0.25);
        transition: 0.3s;
    }
</style>
<div class="easyui-layout shop-products-container" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:10px 10px 0 10px;">
        <div class="search-panel">
        <form id="manage_shopProducts_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">商品编号：</label>
                    <input type="text" class="form-control form-control-sm" name="search_LIKE_serialNumber" placeholder="商品编号"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">是否推荐：</label>
                    <select class="form-control form-control-sm" name="search_EQ_featured" style="width: 100px;">
                        <option value="">全部</option>
                        <option value="1">推荐</option>
                        <option value="0">不推荐</option>
                    </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">品牌：</label>
                <select class="form-control form-control-sm" name="search_EQ_brandId" style="width: 100px;">
                    <option>全部</option>
                    <#list brandMap as k,v >
                        <option value="${k}">${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">是否包邮：</label>
                    <select class="form-control form-control-sm" name="search_EQ_freeShipping" style="width: 100px;">
                        <option value="">全部</option>
                        <option value="1">包邮</option>
                        <option value="0">不包邮</option>
                    </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                    <input type="text" class="form-control form-control-sm" id="search_GTE_createdAt" name="search_GTE_createdAt" placeholder="开始日期" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                    <span class="mr-1 ml-1" style="margin: 0 8px;">至</span>
                    <input type="text" class="form-control form-control-sm" id="search_LTE_createdAt" name="search_LTE_createdAt" placeholder="结束日期" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                    <button class="btn btn-sm search-btn" type="button" onclick="$.acooly.framework.search('manage_shopProducts_searchform','manage_shopProducts_datagrid');">
                        <i class="fa fa-search fa-fw"></i> 查询
                    </button>
            </div>
    </form>
        </div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false" style="padding: 0 10px 10px 10px;">
        <table id="manage_shopProducts_datagrid" class="easyui-datagrid" url="/manage/shop/shopProducts/listJson.html" toolbar="#manage_shopProducts_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true" rownumbers="false">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter" width="40">选择</th>
                <th field="id" sortable="true" width="60">商品ID</th>
                <th field="imageUrl" formatter="imageFormatter" width="100">商品图片</th>
                <th field="serialNumber" formatter="serialNumberFunction" width="180">商品编号</th>
                <th field="name" formatter="nameFormatter" width="150">商品名称</th>
                <th field="brandId" formatter="brandFormatter" width="100">品牌</th>
                <th field="price" formatter="priceFormatter" width="130">价格</th>
                <th field="rating" formatter="ratingFormatter" width="110">评分</th>
                <th field="featured" formatter="featuredFormatter" width="100">推荐状态</th>
                <th field="subCategoryId" formatter="showParentSub" width="150">分类</th>
                <th field="attributes" formatter="attributesFormatter" width="220">商品属性</th>
                <th field="description" formatter="descriptionFormatter" width="180">商品描述</th>
                <th field="createTime" formatter="dateTimeFormatter" width="100" sortable="true">创建时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" width="200" data-options="formatter:function(value, row, index){return formatAction('manage_shopProducts_action',value,row)}">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopProducts_action" style="display: none;">
            <div class="btn-group btn-group-xs">
              <button onclick="$.acooly.framework.show('/manage/shop/shopProducts/show.html?id={0}',600,600);" class="btn btn-outline-info btn-xs" type="button" title="查看详情">
                  <i class="fa fa-eye"></i>查看
              </button>
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopProducts/edit.html',id:'{0}',entity:'shopProducts',width:600,height:650});" class="btn btn-outline-primary btn-xs" type="button" title="编辑商品">
                  <i class="fa fa-pencil"></i>编辑
              </button>
              <button onclick="openProductTranslationDialog('{0}')" class="btn btn-outline-success btn-xs" type="button" title="多语言翻译">
                  <i class="fa fa-language"></i>翻译
              </button>
              <button onclick="$.acooly.framework.remove('/manage/shop/shopProducts/deleteJson.html','{0}','manage_shopProducts_datagrid');" class="btn btn-outline-danger btn-xs" type="button" title="删除商品">
                  <i class="fa fa-trash"></i>删除
              </button>
          </div>
        </div>




        <!-- 图片预览弹窗 -->
        <div id="imagePreviewDialog" class="easyui-dialog" title="商品图片预览"
             style="width: 50%; height: 60%; padding: 0; display: none;"
             data-options="modal:true, closed:true, resizable:false">
            <div style="text-align:center; padding: 20px;">
                <img id="previewImage" src="" alt="预览" style="max-width:100%; max-height:70vh; border-radius:12px; box-shadow:0 10px 30px rgba(0,0,0,0.2);">
            </div>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_shopProducts_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopProducts/create.html',entity:'shopProducts',width:600,height:650})">
                <i class="fa fa-plus-circle fa-fw"></i>添加商品
            </a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopProducts/deleteJson.html','manage_shopProducts_datagrid')">
                <i class="fa fa-trash fa-fw"></i>批量删除
            </a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopProducts_exports_menu'">
                <i class="fa fa-cloud-download fa-fw"></i>批量导出
            </a>
            <div id="manage_shopProducts_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopProducts/exportXls.html','manage_shopProducts_searchform','商品表（关联子级分类）')">
                  <i class="fa fa-file-excel-o fa-lg fa-fw"></i>Excel
              </div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopProducts/exportCsv.html','manage_shopProducts_searchform','商品表（关联子级分类）')">
                  <i class="fa fa-file-text-o fa-lg fa-fw"></i>CSV
              </div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopProducts/importView.html',uploader:'manage_shopProducts_import_uploader_file'});">
                <i class="fa fa-cloud-upload fa-fw"></i>批量导入
            </a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopProducts_searchform', 'manage_shopProducts_datagrid');
        });


        // 渲染商品图片 - 支持多图片显示
        function imageFormatter(value, row) {
            if (!value) return '<span style="color:#9ca3af;">暂无图片</span>';
            
            // 自动补前缀（如果URL不是http开头）
            var imageUrl = value;
            if (!/^https?:\/\//.test(imageUrl)) {
                imageUrl = window.location.origin + imageUrl;
            }
            return '<img src="' + imageUrl + '" class="product-image" onclick="showImagePreview(\'' + imageUrl + '\')">';
            // 显示主图，点击可查看所有图片
            // var html = '<div style="position: relative; display: inline-block;">';
            // html += '<img src="' + imageUrl + '" class="product-image" onclick="showProductImages(' + row.id + ')" title="点击查看所有图片"/>';
            // html += '<span style="position: absolute; bottom: 2px; right: 2px; background: rgba(0,0,0,0.6); color: white; padding: 2px 6px; border-radius: 4px; font-size: 10px;">主图</span>';
            // html += '</div>';
            //
            // return html;
        }


        function serialNumberFunction(value, row) {
            if (!value) return '<span style="color:#9ca3af;">--</span>';
            
            return '<div style="text-align: center;">' +
                   '<span class="serial-number-badge" onclick="copyToCode(' + JSON.stringify(row) + ')" ' +
                   'title="点击复制商品编号：' + value + '" ' +
                   'style="display: inline-block; padding: 8px 16px; background: linear-gradient(135deg, #667eea, #764ba2); ' +
                   'color: #fff; border-radius: 10px; font-size: 12px; font-weight: 600; ' +
                   'cursor: pointer; transition: all 0.3s; box-shadow: 0 2px 6px rgba(102,126,234,0.3); ' +
                   'letter-spacing: 0.5px; font-family: \'Courier New\', monospace;">' +
                   '<i class="fa fa-copy fa-fw" style="margin-right: 4px;"></i>' + value +
                   '</span>' +
                   '</div>';
        }
        
        function copyToCode(row) {
            navigator.clipboard.writeText(row.serialNumber).then(() => {
                $.messager.show({
                    title: '成功',
                    msg: '商品编号已复制：' + row.serialNumber,
                    timeout: 2000,
                    showType: 'slide'
                });
            }).catch(err => {
                $.messager.alert('错误', '复制失败：' + err);
            });
        }

        // 显示商品所有图片（新增函数）
        function showProductImages(productId) {
            $.ajax({
                url: '/manage/shop/shopProducts/getProductImages.html',
                type: 'GET',
                data: { productId: productId },
                success: function(result) {
                    if (result.success && result.rows && result.rows.length > 0) {
                        var images = result.rows;
                        var imageUrls = images.map(function(img) {
                            var url = img.imageUrl;
                            if (!/^https?:\/\//.test(url)) {
                                url = window.location.origin + url;
                            }
                            return url;
                        });
                        
                        // 显示图片轮播
                        showImageCarousel(imageUrls, images[0].imageUrl);
                    } else {
                        // 如果没有多图片，只显示主图
                        var mainImage = $('#manage_shopProducts_datagrid').datagrid('getRows').find(function(row) {
                            return row.id == productId;
                        });
                        if (mainImage && mainImage.imageUrl) {
                            var url = mainImage.imageUrl;
                            if (!/^https?:\/\//.test(url)) {
                                url = window.location.origin + url;
                            }
                            showImagePreview(url);
                        }
                    }
                },
                error: function() {
                    $.acooly.messager.error('加载图片失败');
                }
            });
        }

        // 图片轮播弹窗（新增函数）
        function showImageCarousel(imageUrls, currentImage) {
            var currentIndex = imageUrls.indexOf(currentImage);
            if (currentIndex === -1) currentIndex = 0;
            
            var html = '<div style="text-align: center; padding: 20px; position: relative;">';
            html += '<button onclick="carouselPrev()" style="position: absolute; left: 10px; top: 50%; transform: translateY(-50%); background: rgba(0,0,0,0.5); color: white; border: none; padding: 10px 15px; border-radius: 4px; cursor: pointer; font-size: 18px;">‹</button>';
            html += '<img id="carouselImage" src="' + imageUrls[currentIndex] + '" style="max-width: 80%; max-height: 70vh; border-radius: 12px; box-shadow: 0 10px 40px rgba(0,0,0,0.25);"/>';
            html += '<button onclick="carouselNext()" style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); background: rgba(0,0,0,0.5); color: white; border: none; padding: 10px 15px; border-radius: 4px; cursor: pointer; font-size: 18px;">›</button>';
            html += '<div style="margin-top: 15px; color: #6b7280;">图片 ' + (currentIndex + 1) + ' / ' + imageUrls.length + '</div>';
            html += '</div>';
            
            $('#imagePreviewDialog').html(html);
            $('#imagePreviewDialog').dialog({
                title: '商品图片',
                width: '70%',
                height: '80%',
                modal: true,
                closed: false
            });
            
            // 保存轮播数据到全局
            window.carouselData = {
                images: imageUrls,
                currentIndex: currentIndex
            };
        }

        // 轮播上一张
        function carouselPrev() {
            if (!window.carouselData) return;
            window.carouselData.currentIndex = (window.carouselData.currentIndex - 1 + window.carouselData.images.length) % window.carouselData.images.length;
            updateCarousel();
        }

        // 轮播下一张
        function carouselNext() {
            if (!window.carouselData) return;
            window.carouselData.currentIndex = (window.carouselData.currentIndex + 1) % window.carouselData.images.length;
            updateCarousel();
        }

        // 更新轮播显示
        function updateCarousel() {
            if (!window.carouselData) return;
            var data = window.carouselData;
            $('#carouselImage').attr('src', data.images[data.currentIndex]);
            $('#imagePreviewDialog').find('div').last().text('图片 ' + (data.currentIndex + 1) + ' / ' + data.images.length);
        }

        // 渲染商品名称
        function nameFormatter(value) {
            if (!value) return '<span style="color:#9ca3af;">--</span>';
            return '<span class="product-name" title="' + value + '" style="font-weight: 600; color: #374151; font-size: 14px;">' + value + '</span>';
        }

        // 渲染价格（后端为 BigDecimal：直接按“元/美元”展示，不做 /100 转换）
        function priceFormatter(value, row) {
            var hasPrice = !(value == null || value === '' || isNaN(parseFloat(value)));
            var hasOriginal = row && !(row.originalPrice == null || row.originalPrice === '' || isNaN(parseFloat(row.originalPrice)));
            if (!hasPrice && !hasOriginal) return '<span style="color:#9ca3af;">--</span>';
            var priceVal = hasPrice ? parseFloat(value) : null;
            var originalVal = hasOriginal ? parseFloat(row.originalPrice) : null;
            // 优先显示：有原价且原价 > 现价 → 展示划线原价 + 红色现价
            if (hasOriginal && hasPrice && originalVal > priceVal) {
                return '<span class="product-original-price">$' + originalVal.toFixed(2) + '</span> ' +
                    '<span class="product-price">$' + priceVal.toFixed(2) + '</span>';
            }

            // 其次：只有原价 → 展示原价；否则展示现价
            var showVal = hasOriginal ? originalVal : priceVal;
            return '<span class="product-price">$' + (showVal != null ? showVal.toFixed(2) : '--') + '</span>';
        }

        // 渲染评分 - 星级显示
        function ratingFormatter(value) {
            if (!value || value == 0) return '<span style="color:#9ca3af;">暂无评分</span>';
            var stars = '';
            var rating = parseInt(value);
            for (var i = 0; i < 5; i++) {
                if (i < rating) {
                    stars += '<i class="fa fa-star star-rating"></i>';
                } else {
                    stars += '<i class="fa fa-star-o" style="color:#d1d5db;"></i>';
                }
            }
            return '<span class="badge badge-rating">' + stars + ' ' + value + '</span>';
        }

        // 渲染推荐状态
        function featuredFormatter(value) {
            if (value == 1 || value == '1') {
                return '<span class="badge badge-featured"><i class="fa fa-star fa-fw"></i> 推荐</span>';
            } else {
                return '<span class="badge badge-normal">普通</span>';
            }
        }

        // 渲染商品描述
        function descriptionFormatter(value) {
            if (!value) return '<span style="color:#9ca3af; font-size: 12px;">暂无描述</span>';
            // 限制描述长度，避免过长
            var maxLength = 50;
            var displayValue = value.length > maxLength ? value.substring(0, maxLength) + '...' : value;
            return '<span class="product-description" title="' + value + '" style="color: #6b7280; font-size: 12px; line-height: 1.6;">' + displayValue + '</span>';
        }

        // 点击图片显示大图
        function showImagePreview(url) {
            $('#previewImage').attr('src', url);
            $('#imagePreviewDialog').dialog('open');
        }


        var categoryFullNameMap = {};
        <#if categoryFullNameMap??>
        <#list categoryFullNameMap?keys as subId>
        var subIdStr = "${subId?js_string}";
        var fullName = "${categoryFullNameMap[subId]?js_string}";
        categoryFullNameMap[subIdStr] = fullName;
        </#list>
        </#if>

        function showParentSub(value, row, index) {
            if (!value || value == 0 || value === '') {
                return '<span style="color:#9ca3af;"><i class="fa fa-minus"></i> 未分类</span>';
            }

            // 尝试多种方式获取完整名称
            var fullName = categoryFullNameMap[value] ||
                categoryFullNameMap[String(value)] ||
                categoryFullNameMap[Number(value)];

            if (fullName) {
                // 拆分父类名称和子类名称，分别显示不同样式
                var parts = fullName.split('-');
                if (parts.length >= 2) {
                    var parentName = parts[0];
                    var subName = parts.slice(1).join('-');
                    return '<span title="分类ID: ' + value + '" style="display:inline-block;">' +
                        '<span style="color:#667eea;font-weight:500;">' + parentName + '</span>' +
                        ' <i class="fa fa-angle-right" style="color:#ccc;margin:0 4px;"></i> ' +
                        '<span style="color:#764ba2;font-weight:600;">' + subName + '</span>' +
                        '</span>';
                } else {
                    // 如果没有分隔符，直接显示
                    return '<span class="badge badge-primary" title="分类ID: ' + value + '">' + fullName + '</span>';
                }
            } else {
                // 找不到时显示原始分类ID，并添加警告样式
                return '<span style="color:#dc3545;" title="未找到分类信息">' +
                    '<i class="fa fa-exclamation-triangle"></i> 分类ID: ' + value +
                    '</span>';
            }
        }

        // 属性值格式化函数
        function attributesFormatter(value, row) {
            if (!row.id) return '<span style="color:#9ca3af;">--</span>';
            
            // 异步加载属性值
            var productId = row.id;
            var containerId = 'attr_' + productId;
            var html = '<div id="' + containerId + '" style="min-height: 24px; padding: 4px 0;">';
            html += '<span style="color:#9ca3af; font-size: 12px;"><i class="fa fa-spinner fa-spin"></i> 加载中...</span>';
            html += '</div>';
            
            // 异步加载
            setTimeout(function() {
                loadProductAttributesForList(productId, containerId);
            }, 100);
            
            return html;
        }

        // 加载商品属性值（用于列表显示）
        function loadProductAttributesForList(productId, containerId) {
            $.ajax({
                url: '/manage/shop/shopProducts/getProductAttributes.html',
                type: 'GET',
                data: { productId: productId },
                success: function(result) {
                    if (result.success && result.rows && result.rows.length > 0) {
                        // 按属性ID分组
                        var attrGroups = {};
                        result.rows.forEach(function(item) {
                            var attrId = item.attrId;
                            if (!attrGroups[attrId]) {
                                attrGroups[attrId] = [];
                            }
                            attrGroups[attrId].push(item.attrValueId);
                        });
                        
                        // 构建显示HTML
                        var html = '';
                        var first = true;
                        for (var attrId in attrGroups) {
                            if (!first) html += '<br/>';
                            first = false;
                            
                            // 获取属性名称
                            var attrName = attrNameMap[attrId] || attrNameMap[String(attrId)] || '属性' + attrId;
                            html += '<span style="color:#6366f1; font-weight: 600; font-size: 12px; margin-right: 4px;">' + attrName + ':</span> ';
                            
                            // 获取属性值名称
                            var valueNames = [];
                            attrGroups[attrId].forEach(function(attrValueId) {
                                var valueName = attrValueNameMap[attrValueId] || attrValueNameMap[String(attrValueId)] || '';
                                if (valueName) {
                                    valueNames.push(valueName);
                                }
                            });
                            
                            if (valueNames.length > 0) {
                                html += '<span style="color:#374151; font-size: 12px; line-height: 1.8;">' + valueNames.join(', ') + '</span>';
                            } else {
                                html += '<span style="color:#9ca3af; font-size: 12px;">--</span>';
                            }
                        }
                        
                        $('#' + containerId).html(html || '<span style="color:#9ca3af; font-size: 12px;">暂无属性</span>');
                    } else {
                        $('#' + containerId).html('<span style="color:#9ca3af; font-size: 12px;">暂无属性</span>');
                    }
                },
                error: function() {
                    $('#' + containerId).html('<span style="color:#dc3545; font-size: 12px;">加载失败</span>');
                }
            });
        }

        // 品牌格式化函数
        function brandFormatter(value, row) {
            if (!value || value == 0) {
                return '<span style="color:#9ca3af; font-size: 12px;"><i class="fa fa-minus"></i> 无品牌</span>';
            }
            
            // 从品牌Map获取品牌名称
            var brandName = brandNameMap[value] || brandNameMap[String(value)] || brandNameMap[Number(value)];
            if (brandName) {
                return '<span class="badge badge-brand" style="background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; padding: 5px 12px; border-radius: 12px; font-size: 12px; font-weight: 600; display: inline-block;">' +
                       '<i class="fa fa-tag fa-fw"></i> ' + brandName +
                       '</span>';
            } else {
                return '<span style="color:#dc3545; font-size: 12px;" title="品牌ID: ' + value + '">' +
                       '<i class="fa fa-exclamation-triangle"></i> 品牌ID: ' + value +
                       '</span>';
            }
        }

        // 属性名称和属性值名称映射（从后端注入）
        var attrNameMap = {};
        <#if attrMap??>
        <#list attrMap?keys as attrId>
        attrNameMap["${attrId}"] = "${attrMap[attrId]!''}";
        </#list>
        </#if>

        var attrValueNameMap = {};
        <#if attrValueMap??>
        <#list attrValueMap?keys as attrId>
        <#if attrValueMap[attrId]??>
        <#list attrValueMap[attrId] as attrValue>
        attrValueNameMap["${attrValue.id}"] = "${attrValue.value!''}";
        </#list>
        </#if>
        </#list>
        </#if>

        // 品牌名称映射（从后端注入）
        var brandNameMap = {};
        <#if brandMap??>
        <#list brandMap?keys as brandId>
        brandNameMap["${brandId}"] = "${brandMap[brandId]!''}";
        </#list>
        </#if>
        
        // ========== 多语言翻译管理 ==========
        /**
         * 打开商品翻译管理弹窗
         * @param productId 商品ID
         */
        function openProductTranslationDialog(productId) {
            if (!productId) {
                $.acooly.messager.error('商品ID不能为空');
                return;
            }
            
            // 从表格中获取商品名称
            var productName = '';
            try {
                var rows = $('#manage_shopProducts_datagrid').datagrid('getRows');
                var row = rows.find(function(r) {
                    return r.id == productId;
                });
                if (row && row.name) {
                    productName = row.name;
                }
            } catch (e) {
                console.warn('无法获取商品名称:', e);
            }
            
            // 使用 EasyUI Dialog 打开弹窗
            var dialog = $('<div/>').dialog({
                href: '/manage/shop/shopProducts/translationDialog.html?productId=' + productId,
                title: '<i class="fa fa-language"></i> 商品多语言翻译管理 - ' + (productName || '商品ID: ' + productId),
                width: 900,
                height: 650,
                modal: true,
                maximizable: true,
                onClose: function() {
                    $(this).dialog('destroy');
                },
                onOpen: function() {
                    // 弹窗打开后，确保内容已加载
                    var that = $(this);
                    setTimeout(function() {
                        // 检查是否有初始化函数，如果有则调用
                        if (typeof window.initTranslationDialog === 'function') {
                            window.initTranslationDialog();
                        }
                    }, 100);
                }
            });
        }
    </script>
</div>
