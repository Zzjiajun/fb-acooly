<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    /* 🌿 全局样式 */
    .shop-brand-container {
        background: #f5f6f8;
        font-family: "Inter", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
    }

    /* 🌈 搜索栏 - 使用作用域限制避免冲突 */
    .shop-brand-container .search-panel {
        background: #fafafa;
        padding: 8px 14px;
        margin-bottom: 8px;
        border-radius: 12px;
        box-shadow: 0 1px 4px rgba(0,0,0,0.08);
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        gap: 10px;
        border-bottom: 2px solid #e5e7eb;
    }

    .shop-brand-container .search-panel .form-group {
        display: flex;
        align-items: center;
    }

    .shop-brand-container .search-panel label {
        color: #4b5563;
        font-weight: 500;
        margin-right: 6px;
    }

    .shop-brand-container .search-panel .form-control {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 4px 8px;
        font-size: 13px;
        transition: border-color 0.2s;
    }

    .shop-brand-container .search-panel .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99,102,241,0.15);
    }

    .shop-brand-container .search-btn {
        background: linear-gradient(135deg, #6366f1, #8b5cf6);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 5px 12px;
        font-size: 13px;
        font-weight: 500;
        transition: 0.3s;
    }
    .shop-brand-container .search-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 10px rgba(99,102,241,0.3);
    }

    /* 🧭 工具栏 */
    #manage_shopBrand_toolbar {
        background: #fff;
        border-radius: 12px;
        padding: 10px 16px;
        margin-bottom: 12px;
        box-shadow: 0 1px 6px rgba(0,0,0,0.05);
        display: flex;
        align-items: center;
        gap: 10px;
    }

    #manage_shopBrand_toolbar .easyui-linkbutton {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 8px;
        padding: 6px 12px;
        transition: 0.2s;
    }
    #manage_shopBrand_toolbar .easyui-linkbutton:hover {
        background: #eef2ff;
        border-color: #6366f1;
        color: #6366f1;
    }

    /* 📋 表格 */
    #manage_shopBrand_datagrid {
        background: #fff;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    }

    .datagrid-header-row {
        background: #f9fafb !important;
    }

    #manage_shopBrand_datagrid .datagrid-header th {
        font-weight: 600;
        color: #4b5563;
        font-size: 13px;
        border-bottom: 1px solid #e5e7eb;
    }

    #manage_shopBrand_datagrid .datagrid-body td {
        border-bottom: 1px solid #f3f4f6;
        padding: 12px 8px;
        vertical-align: middle;
    }

    #manage_shopBrand_datagrid .datagrid-row:hover {
        background: #f9fafc !important;
    }

    /* 🖼️ Logo图片 */
    .brand-logo {
        width: 50px;
        height: 50px;
        border-radius: 8px;
        object-fit: cover;
        border: 2px solid #e5e7eb;
        cursor: zoom-in;
        transition: 0.3s;
    }
    .brand-logo:hover {
        border-color: #6366f1;
        transform: scale(1.1);
    }

    /* 🏷️ 状态标签 - 使用作用域限制避免冲突 */
    .shop-brand-container .status-badge {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 600;
    }
    .shop-brand-container .status-badge-enabled {
        background: linear-gradient(135deg, #10b981, #059669);
        color: #fff;
    }
    .shop-brand-container .status-badge-disabled {
        background: #e5e7eb;
        color: #6b7280;
    }

    /* ⚙️ 操作按钮 - 使用作用域限制避免冲突 */
    .shop-brand-container .action-buttons .btn {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 6px;
        padding: 4px 8px;
        font-size: 12px;
        margin-right: 4px;
        transition: 0.2s;
    }
    .shop-brand-container .action-buttons .btn:hover {
        background: #eef2ff;
        color: #4f46e5;
        transform: translateY(-1px);
    }
</style>
<div class="easyui-layout shop-brand-container" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:10px 10px 0 10px;">
        <div class="search-panel">
        <form id="manage_shopBrand_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">品牌名称：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_name" placeholder="品牌名称"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">状态：</label>
                <select class="form-control form-control-sm" name="search_EQ_status" style="width: 100px;">
                    <option value="">全部</option>
                    <option value="1">启用</option>
                    <option value="0">禁用</option>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" placeholder="开始日期" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1" style="margin: 0 8px;">至</span>
                <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" placeholder="结束日期" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm search-btn" type="button" onclick="$.acooly.framework.search('manage_shopBrand_searchform','manage_shopBrand_datagrid');">
                    <i class="fa fa-search fa-fw"></i> 查询
                </button>
            </div>
    </form>
        </div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false" style="padding: 0 10px 10px 10px;">
        <table id="manage_shopBrand_datagrid" class="easyui-datagrid" url="/manage/shop/shopBrand/listJson.html" toolbar="#manage_shopBrand_toolbar" fit="true" border="false" fitColumns="true"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true" rownumbers="false">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter" width="40">选择</th>
                <th field="id" sortable="true" width="60">品牌ID</th>
                <th field="logo" formatter="logoFormatter" width="80">Logo</th>
                <th field="name" width="150">品牌名称</th>
                <th field="status" formatter="statusFormatter" width="90" sortable="true">状态</th>
                <th field="createTime" formatter="dateTimeFormatter" width="140" sortable="true">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter" width="140" sortable="true">更新时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" width="200" data-options="formatter:function(value, row, index){return formatAction('manage_shopBrand_action',value,row)}">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopBrand_action" style="display: none;">
            <div class="btn-group btn-group-xs action-buttons">
              <button onclick="$.acooly.framework.show('/manage/shop/shopBrand/show.html?id={0}',600,600);" class="btn btn-outline-info btn-xs" type="button" title="查看详情">
                  <i class="fa fa-eye"></i>查看
              </button>
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopBrand/edit.html',id:'{0}',entity:'shopBrand',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button" title="编辑品牌">
                  <i class="fa fa-pencil"></i>编辑
              </button>
              <button onclick="$.acooly.framework.remove('/manage/shop/shopBrand/deleteJson.html','{0}','manage_shopBrand_datagrid');" class="btn btn-outline-danger btn-xs" type="button" title="删除品牌">
                  <i class="fa fa-trash"></i>删除
              </button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopBrand_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopBrand/create.html',entity:'shopBrand',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopBrand/deleteJson.html','manage_shopBrand_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopBrand_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_shopBrand_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopBrand/exportXls.html','manage_shopBrand_searchform','品牌表')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopBrand/exportCsv.html','manage_shopBrand_searchform','品牌表')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopBrand/importView.html',uploader:'manage_shopBrand_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopBrand_searchform', 'manage_shopBrand_datagrid');
        });

        // Logo图片格式化
        function logoFormatter(value, row) {
            if (!value) return '<span style="color:#9ca3af;">暂无Logo</span>';
            
            // 自动补前缀（如果URL不是http开头）
            var imageUrl = value;
            if (!/^https?:\/\//.test(imageUrl)) {
                imageUrl = window.location.origin + imageUrl;
            }
            return '<img src="' + imageUrl + '" class="brand-logo" onclick="showLogoPreview(\'' + imageUrl + '\')" title="点击查看大图">';
        }

        // 状态格式化
        function statusFormatter(value) {
            if (value == 1 || value == '1') {
                return '<span class="status-badge status-badge-enabled"><i class="fa fa-check-circle"></i> 启用</span>';
            } else {
                return '<span class="status-badge status-badge-disabled"><i class="fa fa-times-circle"></i> 禁用</span>';
            }
        }

        // 显示Logo预览大图
        function showLogoPreview(url) {
            var html = '<div style="text-align: center; padding: 20px;">' +
                '<img src="' + url + '" alt="Logo预览" style="max-width: 80%; max-height: 70vh; border-radius: 12px; box-shadow: 0 10px 40px rgba(0,0,0,0.25);"/>' +
                '</div>';
            
            $.messager.alert('品牌Logo预览', html, 'info');
        }
    </script>
</div>
