<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
<#--        <form id="manage_permissions_searchform" class="form-inline ac-form-search" onsubmit="return false">-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">账户：</label>-->
<#--                <input type="text" class="form-control form-control-sm" name="search_EQ_accountName"/>-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_permissions_searchform','manage_permissions_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>-->
<#--            </div>-->
<#--        </form>-->
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_permissions_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/permissions/userPermissionsList.html" toolbar="#manage_permissions_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="asc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" width="10%">ID</th>
                <th field="username" formatter="mappingFormatter"  width="10%">登录名称</th>
                <th field="realName" formatter="mappingFormatter"  width="10%">姓名</th>
                <th field="pinyin" formatter="mappingFormatter"  width="10%">拼音</th>
                <th field="status" formatter="statusFormatters"  width="10%">有无权限</th>
<#--                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_permissions_action',value,row)}">权限控制</th>-->
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_permissions_action" style="display: none;">
<#--            <button class="btn btn-outline-primary" onclick="addPermissions('{0}','manage_permissions_datagrid')" >添加权限</button>-->
<#--            <button class="btn btn-outline-danger" onclick="deletePermissions('{0}','manage_permissions_datagrid')">删除权限</button>-->
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_permissions_toolbar">
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/daily/permissions/create.html',maximizable:true,entity:'permissions',width:800,height:600})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>-->
            <#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.showcase.usergroup.createBatch();"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>批量管理</a>-->
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/daily/permissions/deleteJson.html','manage_permissions_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>-->
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_permissions_searchform', 'manage_permissions_datagrid');
        });
        function statusFormatters(value,row){
            if (value=='1'){
                return '<button class="btn btn-outline-danger" onclick="deletePermissions('+row.id+')">删除权限</button>'
            }else {
                return '<button class="btn btn-outline-primary" onclick="addPermissions('+row.id+')" >添加权限</button>'
            }
        }
        function addPermissions( id, datagrid, confirmTitle, confirmMessage) {
            var title = confirmTitle ? confirmTitle : '确定';
            var message = confirmMessage ? confirmMessage : '您是否要提交该操作？';
            $.messager.confirm(title, message, function (r) {
                if (r) {
                    $.ajax({
                        url: '/manage/showcase/daily/permissions/addPermissions',
                        data: {id: id},
                        Type: "POST",
                        dataType: "json",
                        success: function (result) {
                            $('#manage_permissions_datagrid').datagrid('reload')
                            $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
                        }
                    });
                }
            });
        }
        function deletePermissions( id, datagrid, confirmTitle, confirmMessage) {
            var title = confirmTitle ? confirmTitle : '确定';
            var message = confirmMessage ? confirmMessage : '您是否要提交该操作？';
            $.messager.confirm(title, message, function (r) {
                if (r) {
                    $.ajax({
                        url: '/manage/showcase/daily/permissions/deletePermissions',
                        data: {id: id},
                        Type: "POST",
                        dataType: "json",
                        success: function (result) {
                            $('#manage_permissions_datagrid').datagrid('reload');
                            $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
                        }
                    });
                }
            });
        }
    </script>
</div>
