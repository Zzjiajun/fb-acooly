<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_regname_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <#--                    <div class="form-group">-->
            <#--                        <label class="col-form-label">create_time：</label>-->
            <#--                        <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
            <#--                        <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
            <#--                    </div>-->
            <#--                    <div class="form-group">-->
            <#--                        <label class="col-form-label">update_time：</label>-->
            <#--                        <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
            <#--                        <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
            <#--                    </div>-->
            <div class="form-group">
                <label class="col-form-label">持有者：</label>
                <select name="search_EQ_name" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list mapName1 as k,v >
                        <option value="${k}">${k}:${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button"
                        onclick="$.acooly.framework.search('manage_regname_searchform','manage_regname_datagrid');"><i
                            class="fa fa-search fa-lg fa-fw fa-col"></i> 查询
                </button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_regname_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/regname/listJson.html"
               toolbar="#manage_regname_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id"
               sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">id</th>
                <th field="userId" sortable="true" sum="true">用户id</th>
                <th field="name" formatter="contentFormatter">名字</th>
                <th field="regionName" formatter="contentFormatter">域名</th>
                <#--		    <th field="createTime" formatter="dateTimeFormatter">create_time</th>-->
                <#--		    <th field="updateTime" formatter="dateTimeFormatter">update_time</th>-->
                <th field="rowActions"
                    data-options="formatter:function(value, row, index){return formatAction('manage_regname_action',value,row)}">
                    动作
                </th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_regname_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/regname/edit.html',id:'{0}',entity:'regname',width:500,height:500});"
               href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/daily/regname/show.html?id={0}',500,500);" href="#"
               title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/showcase/daily/regname/deleteJson.html','{0}','manage_regname_datagrid');"
               href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_regname_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.create({url:'/manage/showcase/daily/regname/create.html',entity:'regname',width:500,height:500})"><i
                        class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.removes('/manage/showcase/daily/regname/deleteJson.html','manage_regname_datagrid')"><i
                        class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_regname_exports_menu'"><i
                        class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
            <div id="manage_regname_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/showcase/daily/regname/exportXls.html','manage_regname_searchform','dm_regname')">
                    <i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel
                </div>
                <div onclick="$.acooly.framework.exports('/manage/showcase/daily/regname/exportCsv.html','manage_regname_searchform','dm_regname')">
                    <i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV
                </div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.imports({url:'/manage/showcase/daily/regname/importView.html',uploader:'manage_regname_import_uploader_file'});"><i
                        class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_regname_searchform', 'manage_regname_datagrid');
        });
    </script>
</div>
