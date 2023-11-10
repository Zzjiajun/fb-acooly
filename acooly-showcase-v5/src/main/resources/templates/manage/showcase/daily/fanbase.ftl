<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_fanbase_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">账户：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_groupName"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_fanbase_searchform','manage_fanbase_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_fanbase_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/fanbase/listJson.html" toolbar="#manage_fanbase_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="asc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="groupId" formatter="mappingFormatter">群编号</th>
                <th field="groupName" formatter="mappingFormatter">群名</th>
                <th field="groupQuantity" formatter="mappingFormatter">粉丝数</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_fanbase_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_fanbase_action" style="display: none;">
            <a  onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/fanbase/edit.html',id:'{0}',entity:'fanbase',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/daily/fanbase/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a  onclick="$.acooly.framework.remove('/manage/showcase/daily/fanbase/deleteJson.html','{0}','manage_fanbase_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_fanbase_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/daily/fanbase/create.html',maximizable:true,entity:'fanbase',width:800,height:600})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/daily/fanbase/deleteJson.html','manage_fanbase_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_fanbase_searchform', 'manage_fanbase_datagrid');
        });
    </script>
</div>
