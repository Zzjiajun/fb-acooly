<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_shopUsers_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createdAt" name="search_GTE_createdAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createdAt" name="search_LTE_createdAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <label class="col-form-label">更新时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updatedAt" name="search_GTE_updatedAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updatedAt" name="search_LTE_updatedAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_shopUsers_searchform','manage_shopUsers_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_shopUsers_datagrid" class="easyui-datagrid" url="/manage/shop/shopUsers/listJson.html" toolbar="#manage_shopUsers_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >用户ID</th>
                <th field="email" formatter="contentFormatter">邮箱地址</th>
                <th field="password" formatter="contentFormatter">密码（加密存储）</th>
                <th field="name" formatter="contentFormatter">用户姓名</th>
<#--                <th field="avatar" formatter="contentFormatter">头像URL</th>-->
<#--                <th field="thirdPartyId" formatter="contentFormatter">第三方平台用户ID</th>-->
<#--                <th field="provider">登录方式：GOOGLE, FACEBOOK, TIKTOK, LOCAL</th>-->
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_shopUsers_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopUsers_action" style="display: none;">
            <div class="btn-group btn-group-xs">
              <button onclick="$.acooly.framework.show('/manage/shop/shopUsers/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopUsers/edit.html',id:'{0}',entity:'shopUsers',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
              <button class="btn btn-outline-primary btn-xs" title="重置密码" onclick="changeShopUserPasswd('/manage/shop/shopUsers/changePasswd.html','{0}',400,400);" href="#"><i class="fa fa-key fa-lg fa-fw fa-col"></i>重置用户密码</button>
              <button onclick="$.acooly.framework.remove('/manage/shop/shopUsers/deleteJson.html','{0}','manage_shopUsers_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopUsers_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopUsers/create.html',entity:'shopUsers',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopUsers/deleteJson.html','manage_shopUsers_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>-->
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopUsers_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_shopUsers_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopUsers/exportXls.html','manage_shopUsers_searchform','用户表')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopUsers/exportCsv.html','manage_shopUsers_searchform','用户表')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopUsers/importView.html',uploader:'manage_shopUsers_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>-->
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopUsers_searchform', 'manage_shopUsers_datagrid');
        });



        function changeShopUserPasswd(url, id, width, height) {
            // 取消datagrid选中状态
            $('#manage_shopUsers_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');

            var w = width || 500;
            var h = height || 'auto';
            var tt = '<i class="fa fa-lg fa-fw fa-col fa-floppy-o"></i>修改密码';
            var addB = '<i class="fa fa-lg fa-fw fa-col fa-plus-circle"></i>确定';
            var max = true;

            url = $.acooly.framework.buildCanonicalUrl(url, { "id": id });

            // 创建 dialog 元素
            var d = $('<div/>');

            // 确定按钮
            var saveBtn = {
                id: 'acooly-framework-save-btn',
                text: addB,
                handler: function () {
                    var dialogRef = d; // 闭包保存 dialog 对象
                    var form = $('#manage_shopUsersChange_searchform');

                    if (form.length === 0) {
                        $.acooly.messager('提示', '未找到密码修改表单', 'danger');
                        return;
                    }

                    // 提交修改密码请求
                    $.ajax({
                        type: 'POST',
                        url: '/manage/shop/shopUsers/shopUsersCn.html',
                        data: form.serialize(),
                        success: function (result) {
                            if (result.success) {
                                $.acooly.messager('提示', result.message, 'success');
                                dialogRef.dialog('close');
                                $('#manage_shopUsers_datagrid').datagrid('reload'); // 刷新列表
                            } else {
                                $.acooly.messager('提示', result.message || '密码修改失败', 'danger');
                            }
                        },
                        error: function (xhr, status, error) {
                            $.acooly.messager('提示', '请求失败：' + error, 'danger');
                        }
                    });
                }
            };

            // 关闭按钮
            var closeBtn = {
                text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col"></i>关闭',
                handler: function () {
                    d.dialog('close');
                }
            };

            var buttons = [saveBtn, closeBtn];

            // 打开 dialog
            $.acooly.divdialog = d.dialog({
                href: url,
                title: tt,
                width: w,
                height: h,
                modal: true,
                maximizable: max,
                buttons: buttons,
                onClose: function () {
                    $(this).dialog('destroy');
                },
                onOpen: function () {
                    var that = $(this);
                    $.parser.onComplete = function () {
                        $.acooly.framework.extendCombobox($(that));
                        $.acooly.framework.initPlugins($(that));
                        $.parser.onComplete = function () {};
                    }
                }
            });
        }






    </script>
</div>
