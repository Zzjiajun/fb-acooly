<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    /* 🌿 全局 */
    body, .shop-team-container {
        background: #f5f6f8;
        font-family: "Inter", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
        color: #2d3748;
    }

    /* 🌈 搜索栏（优化高度和分层） */
    .search-panel {
        background: #fafafa;
        padding: 8px 14px;
        margin-bottom: 8px;
        border-radius: 12px;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        gap: 10px;
        border-bottom: 2px solid #e5e7eb;
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
        padding: 4px 8px;
        font-size: 13px;
        transition: border-color 0.2s;
    }

    .search-panel .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.15);
    }

    .search-btn {
        background: linear-gradient(135deg, #6366f1, #8b5cf6);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 5px 12px;
        font-size: 13px;
        font-weight: 500;
        transition: 0.3s;
    }

    .search-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 10px rgba(99, 102, 241, 0.3);
    }

    /* 🧭 工具栏 */
    #manage_shopTeam_toolbar {
        background: #fff;
        border-radius: 12px;
        padding: 10px 16px;
        margin-bottom: 12px;
        box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);
        display: flex;
        align-items: center;
        gap: 10px;
    }

    #manage_shopTeam_toolbar .easyui-linkbutton {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 8px;
        padding: 6px 12px;
        transition: 0.2s;
    }

    #manage_shopTeam_toolbar .easyui-linkbutton:hover {
        background: #eef2ff;
        border-color: #6366f1;
        color: #6366f1;
    }

    /* 📋 表格 */
    #manage_shopTeam_datagrid {
        background: #fff;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
    }

    .datagrid-header-row {
        background: #f9fafb !important;
    }

    #manage_shopTeam_datagrid .datagrid-header th {
        font-weight: 600;
        color: #4b5563;
        font-size: 13px;
        border-bottom: 1px solid #e5e7eb;
    }

    #manage_shopTeam_datagrid .datagrid-body td {
        border-bottom: 1px solid #f3f4f6;
        padding: 12px 8px;
        vertical-align: middle;
    }

    #manage_shopTeam_datagrid .datagrid-row:hover {
        background: #f9fafc !important;
        transform: scale(1.001);
    }

    /* 🔗 团队分享链接按钮 */
    .team-redirect-btn {
        background: linear-gradient(135deg, #0cf37b, rgb(29, 55, 164));
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 6px 12px;
        font-size: 12px;
        font-weight: 500;
        cursor: pointer;
        transition: 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        max-width: 100%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .team-redirect-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
    }

    .team-redirect-btn i {
        font-size: 14px;
    }


    /* 🔗 团队分享链接按钮 */
    .team-link-btn {
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 6px 12px;
        font-size: 12px;
        font-weight: 500;
        cursor: pointer;
        transition: 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        max-width: 100%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .team-link-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
    }

    .team-link-btn i {
        font-size: 14px;
    }

    /* 👥 团队成员标签 */
    .member-tag {
        display: inline-block;
        background: linear-gradient(135deg, #f093fb, #f5576c);
        color: #fff;
        padding: 4px 10px;
        border-radius: 999px;
        font-size: 12px;
        font-weight: 500;
        margin: 2px 4px 2px 0;
        box-shadow: 0 2px 4px rgba(245, 87, 108, 0.2);
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
</style>
<div class="easyui-layout shop-team-container" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:10px 10px 0 10px;">
        <div class="search-panel">
            <form id="manage_shopTeam_searchform" class="form-inline ac-form-search" onsubmit="return false">
                <div class="form-group">
                    <label class="col-form-label">创建时间：</label>
                    <input type="text" class="form-control form-control-sm" id="search_GTE_createTime"
                           name="search_GTE_createTime" placeholder="开始日期"
                           onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                    <span class="mr-1 ml-1" style="margin: 0 8px;">至</span>
                    <input type="text" class="form-control form-control-sm" id="search_LTE_createTime"
                           name="search_LTE_createTime" placeholder="结束日期"
                           onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                </div>
                <div class="form-group">
                    <label class="col-form-label">修改时间：</label>
                    <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime"
                           name="search_GTE_updateTime" placeholder="开始日期"
                           onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                    <span class="mr-1 ml-1" style="margin: 0 8px;">至</span>
                    <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime"
                           name="search_LTE_updateTime" placeholder="结束日期"
                           onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                </div>
                <div class="form-group">
                    <button class="btn btn-sm search-btn" type="button"
                            onclick="$.acooly.framework.search('manage_shopTeam_searchform','manage_shopTeam_datagrid');">
                        <i class="fa fa-search fa-fw"></i> 查询
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false" style="padding: 0 10px 10px 10px;">
        <table id="manage_shopTeam_datagrid" class="easyui-datagrid" url="/manage/shop/shopTeam/listJson.html"
               toolbar="#manage_shopTeam_toolbar" fit="true" border="false" fitColumns="true"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id"
               sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true" rownumbers="false">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter" width="40">选择</th>
                <th field="id" sortable="true" width="60">ID</th>
                <th field="teamName" formatter="teamNameFormatter" width="50">团队名称</th>
                <th field="teamLink" formatter="teamLinkFormatter" width="240">团队A网站分享链接</th>
                <th field="teamRegisterLink" formatter="teamRegisterLinkFormatter" width="240">团队注册分享链接</th>
                <th field="memberNames" formatter="memberNamesFormatter" width="200">团队成员</th>
                <th field="createTime" formatter="dateTimeFormatter" width="100" sortable="true">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter" width="100" sortable="true">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" width="200"
                    data-options="formatter:function(value, row, index){return formatAction('manage_shopTeam_action',value,row)}">
                    操作
                </th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopTeam_action" style="display: none;">
            <div class="btn-group btn-group-xs action-buttons">
                <#--              <button onclick="$.acooly.framework.show('/manage/shop/shopTeam/show.html?id={0}',600,600);" class="btn btn-outline-info btn-xs" type="button" title="查看详情">-->
                <#--                  <i class="fa fa-eye"></i>查看-->
                <#--              </button>-->
                <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopTeam/edit.html',id:'{0}',entity:'shopTeam',width:600,height:500});"
                        class="btn btn-outline-primary btn-xs" type="button" title="编辑团队">
                    <i class="fa fa-pencil"></i>编辑
                </button>
                <#if isAdmin>
                    <button onclick="$.acooly.framework.remove('/manage/shop/shopTeam/deleteJson.html','{0}','manage_shopTeam_datagrid');"
                            class="btn btn-outline-danger btn-xs" type="button" title="删除团队">
                        <i class="fa fa-trash"></i>删除
                    </button>
                </#if>
            </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopTeam_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.create({url:'/manage/shop/shopTeam/create.html',entity:'shopTeam',width:600,height:500})">
                <i class="fa fa-plus-circle fa-fw"></i>添加团队
            </a>
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.removes('/manage/shop/shopTeam/deleteJson.html','manage_shopTeam_datagrid')">
                <i class="fa fa-trash fa-fw"></i>批量删除
            </a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopTeam_exports_menu'">
                <i class="fa fa-cloud-download fa-fw"></i>批量导出
            </a>
            <div id="manage_shopTeam_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/shop/shopTeam/exportXls.html','manage_shopTeam_searchform','shop_team')">
                    <i class="fa fa-file-excel-o fa-lg fa-fw"></i>Excel
                </div>
                <div onclick="$.acooly.framework.exports('/manage/shop/shopTeam/exportCsv.html','manage_shopTeam_searchform','shop_team')">
                    <i class="fa fa-file-text-o fa-lg fa-fw"></i>CSV
                </div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.imports({url:'/manage/shop/shopTeam/importView.html',uploader:'manage_shopTeam_import_uploader_file'});">
                <i class="fa fa-cloud-upload fa-fw"></i>批量导入
            </a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopTeam_searchform', 'manage_shopTeam_datagrid');
        });

        // 格式化团队分享链接，添加复制按钮
        function teamLinkFormatter(value, row) {
            if (!value) return '<span style="color:#9ca3af;">暂无链接</span>';
            // 截断过长的链接显示
            var displayText = value.length > 40 ? value.substring(0, 40) + '...' : value;
            return "<div style='text-align: center;'><button onclick='copyTeamLink(" + JSON.stringify(value) + ")' " +
                "title='点击复制：' + value + ' 到剪贴板' class='team-link-btn'>" +
                "<i class='fa fa-copy'></i> " + displayText + "</button></div>";
        }

        function teamRegisterLinkFormatter(value, row) {
            if (!value) return '<span style="color:#9ca3af;">暂无链接</span>';
            // 截断过长的链接显示
            var displayText = value.length > 40 ? value.substring(0, 40) + '...' : value;
            return "<div style='text-align: center;'><button onclick='copyTeamLink(" + JSON.stringify(value) + ")' " +
                "title='点击复制：' + value + ' 到剪贴板' class='team-redirect-btn'>" +
                "<i class='fa fa-copy'></i> " + displayText + "</button></div>";
        }

        // 格式化团队名称
        function teamNameFormatter(value) {
            if (!value) return '<span style="color:#9ca3af;">--</span>';
            return '<span class="team-name" title="' + value + '" style="font-weight: 500; color: #374151;">' + value + '</span>';
        }

        // 格式化团队成员
        function memberNamesFormatter(value) {
            if (!value) return '<span style="color:#9ca3af;">暂无成员</span>';
            var members = value.split(', ');
            var html = '<div style="display: flex; flex-wrap: wrap; gap: 4px;">';
            members.forEach(function (member) {
                html += '<span class="member-tag">' + member + '</span>';
            });
            html += '</div>';
            return html;
        }

        // 复制团队分享链接到剪贴板
        function copyTeamLink(value) {
            if (value) {
                navigator.clipboard.writeText(value).then(() => {
                    $.messager.alert('提示', '复制成功：' + value, 'info');
                }).catch(err => {
                    // 降级方案：使用传统方法
                    var textArea = document.createElement("textarea");
                    textArea.value = value;
                    textArea.style.position = "fixed";
                    textArea.style.left = "-999999px";
                    textArea.style.top = "-999999px";
                    document.body.appendChild(textArea);
                    textArea.focus();
                    textArea.select();
                    try {
                        var successful = document.execCommand('copy');
                        if (successful) {
                            $.messager.alert('提示', '复制成功：' + value, 'info');
                        } else {
                            $.messager.alert('错误', '复制失败，请手动复制', 'error');
                        }
                    } catch (err) {
                        $.messager.alert('错误', '复制失败：' + err, 'error');
                    }
                    document.body.removeChild(textArea);
                });
            } else {
                $.messager.alert('提示', '团队分享链接为空', 'warning');
            }
        }
    </script>
</div>
