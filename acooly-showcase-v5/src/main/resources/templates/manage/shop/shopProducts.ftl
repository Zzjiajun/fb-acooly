<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    /* 🌿 全局 */
    body, .shop-products-container {
        background: #f5f6f8;
        font-family: "Inter", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
        color: #2d3748;
    }

    /* 🌈 搜索栏（优化高度和分层） */
    .search-panel {
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

    .search-panel .form-group {
        display: flex;
        align-items: center;
    }

    .search-panel label {
        color: #4b5563;
        font-weight: 500;
        margin-right: 6px;
    }

    .search-panel .form-control {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 4px 8px; /* 高度进一步减小 */
        font-size: 13px;
        transition: border-color 0.2s;
    }

    .search-panel .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99,102,241,0.15);
    }

    .search-btn {
        background: linear-gradient(135deg, #6366f1, #8b5cf6);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 5px 12px; /* 按钮更紧凑 */
        font-size: 13px;
        font-weight: 500;
        transition: 0.3s;
    }
    .search-btn:hover {
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
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    }

    .datagrid-header-row {
        background: #f9fafb !important;
    }

    #manage_shopProducts_datagrid .datagrid-header th {
        font-weight: 600;
        color: #4b5563;
        font-size: 13px;
        border-bottom: 1px solid #e5e7eb;
    }

    #manage_shopProducts_datagrid .datagrid-body td {
        border-bottom: 1px solid #f3f4f6;
        padding: 12px 8px;
        vertical-align: middle;
    }

    #manage_shopProducts_datagrid .datagrid-row:hover {
        background: #f9fafc !important;
        transform: scale(1.001);
    }

    /* 🖼️ 商品图片 */
    .product-image {
        width: 60px;
        height: 60px;
        border-radius: 8px;
        object-fit: cover;
        border: 2px solid #e5e7eb;
        cursor: zoom-in;
        transition: 0.3s;
    }
    .product-image:hover {
        border-color: #6366f1;
        transform: scale(1.06);
    }

    /* 💵 价格样式 */
    .product-price {
        color: #ef4444;
        font-weight: 600;
        font-size: 15px;
    }
    .product-original-price {
        color: #9ca3af;
        text-decoration: line-through;
        font-size: 13px;
        margin-right: 6px;
    }

    /* ⭐ 评分 */
    .star-rating {
        color: #fbbf24;
    }

    /* 🏷️ 状态 */
    .badge {
        border-radius: 999px;
        padding: 4px 10px;
        font-size: 12px;
        font-weight: 600;
    }
    .badge-featured {
        background: linear-gradient(135deg, #f59e0b, #fbbf24);
        color: #fff;
    }
    .badge-normal {
        background: #e5e7eb;
        color: #6b7280;
    }

    /* ⚙️ 操作按钮 */
    .action-buttons .btn {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 6px;
        padding: 4px 8px;
        font-size: 12px;
        margin-right: 4px;
        transition: 0.2s;
    }
    .action-buttons .btn:hover {
        background: #eef2ff;
        color: #4f46e5;
        transform: translateY(-1px);
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
                <label class="col-form-label">商品评分：</label>
                    <input type="text" class="form-control form-control-sm" name="search_EQ_rating" placeholder="评分"/>
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
        <table id="manage_shopProducts_datagrid" class="easyui-datagrid" url="/manage/shop/shopProducts/listJson.html" toolbar="#manage_shopProducts_toolbar" fit="true" border="false" fitColumns="true"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true" rownumbers="false">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter" width="40">选择</th>
                <th field="id" sortable="true" width="80">商品ID</th>
                <th field="imageUrl" formatter="imageFormatter" width="90">商品图片</th>
                <th field="name" formatter="nameFormatter" width="180">商品名称</th>
                <th field="price" formatter="priceFormatter" width="140" sortable="true">价格</th>
                <th field="rating" formatter="ratingFormatter" width="100" sortable="true">评分</th>
                <th field="featured" formatter="featuredFormatter" width="90" sortable="true">推荐状态</th>
                <th field="subCategoryId" formatter="showParentSub" width="100" sortable="true">分类ID</th>
                <th field="description" formatter="descriptionFormatter" width="180">商品描述</th>
                <th field="createTime" formatter="dateTimeFormatter" width="140" sortable="true">创建时间</th>
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


        // 渲染商品图片 - 优化样式
        function imageFormatter(value) {
            if (!value) return '<span style="color:#9ca3af;">暂无图片</span>';
            // 自动补前缀（如果URL不是http开头）
            var imageUrl = value;
            if (!/^https?:\/\//.test(imageUrl)) {
                imageUrl = window.location.origin + imageUrl;
            }
            return '<img src="' + imageUrl + '" class="product-image" onclick="showImagePreview(\'' + imageUrl + '\')">';
        }

        // 渲染商品名称
        function nameFormatter(value) {
            if (!value) return '';
            return '<span class="product-name" title="' + value + '">' + value + '</span>';
        }

        // 渲染价格 - 先显示原价，再显示折扣价
        function priceFormatter(value, row) {
            if (!value && !row.originalPrice) return '<span style="color:#9ca3af;">--</span>';
            var html = '';
            // 先显示原价
            if (row.originalPrice) {
                var originalPrice = parseFloat(row.originalPrice).toFixed(2);
                if (row.originalPrice > value) {
                    // 有折扣：原价显示删除线，折扣价高亮显示
                    html = '<span class="product-original-price">$' + originalPrice + '</span> ';
                    var price = parseFloat(value / 100).toFixed(2);
                    html += '<span class="product-price">$' + price + '</span>';
                } else {
                    // 无折扣：只显示原价
                    html = '<span class="product-price">$' + originalPrice + '</span>';
                }
            } else {
                // 没有原价，只显示当前价格
                var price = parseFloat(value / 100).toFixed(2);
                html = '<span class="product-price">$' + price + '</span>';
            }
            return html;
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
            if (!value) return '<span style="color:#9ca3af;">暂无描述</span>';
            return '<span class="product-description" title="' + value + '">' + value + '</span>';
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
    </script>
</div>
