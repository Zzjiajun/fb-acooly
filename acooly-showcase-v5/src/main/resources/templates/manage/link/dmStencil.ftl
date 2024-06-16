<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_dmStencil_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <label class="col-form-label">修改时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_dmStencil_searchform','manage_dmStencil_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_dmStencil_datagrid" class="easyui-datagrid" url="/manage/link/dmStencil/listJson.html" toolbar="#manage_dmStencil_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >id</th>
                <th field="pathName" formatter="contentFormatter">地址</th>
                <th field="reg" formatter="contentFormatter">备注</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_dmStencil_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_dmStencil_action" style="display: none;">
            <div class="btn-group btn-group-xs">
              <button onclick="$.acooly.framework.show('/manage/link/dmStencil/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
              <button onclick="$.acooly.framework.edit({url:'/manage/link/dmStencil/edit.html',id:'{0}',entity:'dmStencil',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
              <button onclick="$.acooly.framework.remove('/manage/link/dmStencil/deleteJson.html','{0}','manage_dmStencil_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_dmStencil_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/link/dmStencil/create.html',entity:'dmStencil',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/link/dmStencil/deleteJson.html','manage_dmStencil_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_dmStencil_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <a href="#" class="btn btn-danger" plain="true"  onclick="confirmRedis1('/manage/link/dmServer/eliminate.html','manage_dmStencil_datagrid');"><i class="fa  fa-rotate-right fa-fw fa-col"></i>刷新缓存</a>
            <div id="manage_dmStencil_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/link/dmStencil/exportXls.html','manage_dmStencil_searchform','dm_stencil')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/link/dmStencil/exportCsv.html','manage_dmStencil_searchform','dm_stencil')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/link/dmStencil/importView.html',uploader:'manage_dmStencil_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>-->
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_dmStencil_searchform', 'manage_dmStencil_datagrid');
        });



        function confirmRedis1(url, datagrid, confirmTitle, confirmMessage) {
            var title = confirmTitle ? confirmTitle : '确定';
            var message = confirmMessage ? confirmMessage : '您是否更新缓存？';
            $.messager.confirm(title, message, function (r) {
                if (r) {
                    $.ajax({
                        url: contextPath + url,
                        data: {
                            type: 'redisCil'
                        },
                        success: function (result) {
                            // if (typeof (result) == 'string')
                            //     result = eval('(' + result + ')');
                            if (result.success) {
                                $('#' + datagrid).datagrid('reload');
                            }
                            if (result.message) {
                                $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
                            }
                        }
                    });
                }
            });
        }
    </script>
</div>
