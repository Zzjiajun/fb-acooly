<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    /* 属性值列表样式 */
    .shop-attr-value-container {
        background: #f5f6f8;
        font-family: "Inter", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
        font-size: 13px;
        color: #374151;
    }

    /* 查询面板 */
    .search-panel {
        background: #fafafa;
        padding: 10px 16px;
        margin-bottom: 12px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        gap: 12px;
        border-bottom: 2px solid #e5e7eb;
    }

    .search-panel .form-group {
        display: flex;
        align-items: center;
        gap: 6px;
    }

    .search-panel label {
        color: #4b5563;
        font-weight: 500;
    }

    .search-panel .form-control {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 4px 8px;
        font-size: 13px;
        transition: border-color 0.2s, box-shadow 0.2s;
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
        padding: 6px 14px;
        font-size: 13px;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s;
    }

    .search-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 10px rgba(99,102,241,0.3);
    }

    /* 工具栏 */
    #manage_shopAttrValue_toolbar {
        background: #fff;
        border-radius: 12px;
        padding: 10px 16px;
        margin-bottom: 12px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        display: flex;
        align-items: center;
        gap: 10px;
    }

    /* 工具栏按钮 */
    #manage_shopAttrValue_toolbar .easyui-linkbutton {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 8px;
        padding: 6px 12px;
        font-size: 13px;
        transition: all 0.2s;
        cursor: pointer;
    }

    #manage_shopAttrValue_toolbar .easyui-linkbutton:hover {
        background: #eef2ff;
        border-color: #6366f1;
        color: #6366f1;
    }

    /* datagrid 容器 */
    #manage_shopAttrValue_datagrid {
        background: #fff;
        border-radius: 12px;
        overflow-x: auto;
        overflow-y: visible;
        position: relative;
        box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    }

    /* 表头 */
    .datagrid-header-row {
        background: #f9fafb;
        z-index: 2;
    }

    #manage_shopAttrValue_datagrid .datagrid-header th {
        font-weight: 600;
        color: #4b5563;
        font-size: 12px;
        border-bottom: 1px solid #e5e7eb;
        padding: 8px;
    }

    /* 表格行 */
    #manage_shopAttrValue_datagrid .datagrid-body td {
        border-bottom: 1px solid #f3f4f6;
        padding: 12px 8px;
        vertical-align: middle;
    }

    #manage_shopAttrValue_datagrid .datagrid-row:hover {
        background: #f9fafc;
    }

    /* 状态徽章 */
    .status-badge {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 600;
    }

    .status-badge-enabled {
        background: linear-gradient(135deg, #10b981, #059669);
        color: #fff;
    }

    .status-badge-disabled {
        background: #e5e7eb;
        color: #6b7280;
    }

    /* 行操作按钮 */
    .action-buttons .btn {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 6px;
        padding: 4px 8px;
        font-size: 12px;
        margin-right: 4px;
        transition: all 0.2s;
        position: relative;
        z-index: 10;
        cursor: pointer;
    }

    .action-buttons .btn:hover {
        background: #eef2ff;
        color: #4f46e5;
        transform: translateY(-1px);
    }

    /* datagrid 内部浮层/弹出菜单 */
    #manage_shopAttrValue_datagrid .dropdown-menu,
    #manage_shopAttrValue_datagrid .cell-editor {
        position: absolute;
        z-index: 100;
    }

    /* 响应式支持 */
    @media (max-width: 768px) {
        .search-panel {
            flex-direction: column;
            align-items: flex-start;
        }
        .search-panel .form-group {
            width: 100%;
        }
        #manage_shopAttrValue_toolbar {
            flex-wrap: wrap;
        }
    }
</style>
<div class="easyui-layout shop-attr-value-container" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:10px 10px 0 10px;">
        <div class="search-panel">
        <form id="manage_shopAttrValue_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">属性值：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_value" placeholder="属性值"/>
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
                <button class="btn btn-sm search-btn" type="button" onclick="$.acooly.framework.search('manage_shopAttrValue_searchform','manage_shopAttrValue_datagrid');">
                    <i class="fa fa-search fa-fw"></i> 查询
                </button>
            </div>
    </form>
        </div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false" style="padding: 0 10px 10px 10px;">
        <table id="manage_shopAttrValue_datagrid" class="easyui-datagrid" url="/manage/shop/shopAttrValue/listJson.html" toolbar="#manage_shopAttrValue_toolbar" fit="true" border="false" fitColumns="true"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true" rownumbers="false">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter" width="40">选择</th>
                <th field="id" sortable="true" width="20">属性值ID</th>
                <th field="attrId" sortable="true" width="20">属性ID</th>
                <th field="value" width="150">属性值</th>
                <th field="sort" sortable="true" width="80">排序</th>
                <th field="status" formatter="statusFormatter" width="90" sortable="true">状态</th>
                <th field="createTime" formatter="dateTimeFormatter" width="140" sortable="true">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter" width="140" sortable="true">更新时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" width="200" data-options="formatter:function(value, row, index){return formatAction('manage_shopAttrValue_action',value,row)}">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopAttrValue_action" style="display: none;">
            <div class="btn-group btn-group-xs action-buttons">
<#--              <button onclick="$.acooly.framework.show('/manage/shop/shopAttrValue/show.html?id={0}',600,600);" class="btn btn-outline-info btn-xs" type="button" title="查看详情">-->
<#--                  <i class="fa fa-eye"></i>查看-->
<#--              </button>-->
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopAttrValue/edit.html',id:'{0}',entity:'shopAttrValue',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button" title="编辑属性值">
                  <i class="fa fa-pencil"></i>编辑
              </button>
              <button onclick="openAttrValueTranslationDialog('{0}')" class="btn btn-outline-primary btn-xs" type="button" title="多语言翻译">
                  <i class="fa fa-language fa-fw fa-col"></i>翻译
              </button>
              <button onclick="$.acooly.framework.remove('/manage/shop/shopAttrValue/deleteJson.html','{0}','manage_shopAttrValue_datagrid');" class="btn btn-outline-danger btn-xs" type="button" title="删除属性值">
                  <i class="fa fa-trash"></i>删除
              </button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopAttrValue_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopAttrValue/create.html',entity:'shopAttrValue',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopAttrValue/deleteJson.html','manage_shopAttrValue_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>-->
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopAttrValue_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_shopAttrValue_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopAttrValue/exportXls.html','manage_shopAttrValue_searchform','商品属性值表')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopAttrValue/exportCsv.html','manage_shopAttrValue_searchform','商品属性值表')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopAttrValue/importView.html',uploader:'manage_shopAttrValue_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>-->
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopAttrValue_searchform', 'manage_shopAttrValue_datagrid');
        });

        // 状态格式化
        function statusFormatter(value) {
            if (value == 1 || value == '1') {
                return '<span class="status-badge status-badge-enabled"><i class="fa fa-check-circle"></i> 启用</span>';
            } else {
                return '<span class="status-badge status-badge-disabled"><i class="fa fa-times-circle"></i> 禁用</span>';
            }
        }

        // ========== 多语言翻译管理 ==========
        /**
         * 打开属性值翻译管理弹窗
         * @param attrValueId 属性值ID
         */
        function openAttrValueTranslationDialog(attrValueId) {
            if (!attrValueId) {
                if (typeof $.acooly !== 'undefined' && $.acooly.messager) {
                    $.acooly.messager('错误', '属性值ID不能为空', 'danger');
                } else if (typeof $.messager !== 'undefined') {
                    $.messager.alert('错误', '属性值ID不能为空');
                } else {
                    alert('属性值ID不能为空');
                }
                return;
            }

            // 从表格中获取属性值名称
            var attrValueName = '';
            try {
                var rows = $('#manage_shopAttrValue_datagrid').datagrid('getRows');
                var row = rows.find(function(r) {
                    return r.id == attrValueId;
                });
                if (row) {
                    attrValueName = row.value || '';
                }
            } catch (e) {
                console.warn('无法获取属性值名称:', e);
            }

            // 使用 EasyUI Dialog 打开翻译管理弹窗
            $('<div></div>').dialog({
                title: '属性值多语言翻译管理' + (attrValueName ? ' - ' + attrValueName : ''),
                width: 900,
                height: 600,
                modal: true,
                maximizable: true,
                resizable: true,
                href: '/manage/shop/shopAttrValue/translationDialog.html?attrValueId=' + attrValueId,
                onOpen: function() {
                    setTimeout(function() {
                        if (typeof initTranslationDialog === 'function') {
                            initTranslationDialog();
                        }
                    }, 300);
                },
                onClose: function() {
                    $(this).dialog('destroy');
                }
            });
        }
    </script>
</div>
