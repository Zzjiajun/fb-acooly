<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_userGroup_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">名称：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_name"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_userGroup_searchform','manage_userGroup_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_userGroup_datagrid" class="easyui-datagrid" url="/manage/demo/usergroup/userGroup/listJson.html" toolbar="#manage_userGroup_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="asc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="name">名称</th>
                <th field="descn" formatter="contentFormatter">描述</th>
                <th field="users" formatter="contentFormatter">用户名</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_userGroup_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_userGroup_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/demo/usergroup/userGroup/edit.html',id:'{0}',entity:'userGroup',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/demo/usergroup/userGroup/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/demo/usergroup/userGroup/deleteJson.html','{0}','manage_userGroup_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_userGroup_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.showcase.usergroup.createBatch();"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>批量管理</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/demo/usergroup/userGroup/deleteJson.html','manage_userGroup_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_userGroup_searchform', 'manage_userGroup_datagrid');
        });
    </script>
</div>
