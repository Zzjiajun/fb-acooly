<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_dmCenter_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">地区：</label>
                <select name="search_EQ_region" class="form-control select2bs4" data-options="required:true">
                    <option></option>
                    <#list regionList as v >
                        <option value="${v}">${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">账号名：</label>
                <select name="search_EQ_userName" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list mapName as k,v >
                        <option value="${k}">${k}:${v}</option>
                    </#list>
                </select>
            </div>

<#--            <div class="form-group">-->
<#--                <label class="col-form-label">创建时间：</label>-->
<#--                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">修改时间：</label>-->
<#--                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--            </div>-->
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_dmCenter_searchform','manage_dmCenter_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_dmCenter_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/dmCenter/listJson.html" toolbar="#manage_dmCenter_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >id</th>
                <th field="userName" formatter="contentFormatter">用户名</th>
                <th field="region" formatter="contentFormatter">地区</th>
                <th field="displayOption" formatter="displayOptionFunction">广告类型</th>
                <th field="pixel" formatter="contentFormatter">像素Id</th>
                <th field="link" >链接地址</th>
                <th field="domain" formatter="domainFunction">访问域名</th>
<#--                <th field="serialNumber" formatter="contentFormatter">模版地址</th>-->
                <th field="visitsNumber" sortable="true" sum="true">域名访问量</th>
                <th field="clicksNumber" sortable="true" sum="true">按钮点击数量</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_dmCenter_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_dmCenter_action" style="display: none;">
            <div class="btn-group btn-group-xs">
              <button onclick="$.acooly.framework.show('/manage/showcase/daily/dmCenter/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
              <button onclick="confirmSubmit1('/manage/showcase/daily/dmCenter/eliminate.html','{0}','manage_dmCenter_datagrid');" class="btn btn-outline-secondary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>清除记录</button>
              <button onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/dmCenter/edit.html',id:'{0}',entity:'dmCenter',width:800,height:700});" class="btn btn-outline-success btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
              <button onclick="$.acooly.framework.remove('/manage/showcase/daily/dmCenter/deleteJson.html','{0}','manage_dmCenter_datagrid');" class="btn btn-outline-danger btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_dmCenter_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/daily/dmCenter/create.html',entity:'dmCenter',width:800,height:700})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/daily/dmCenter/deleteJson.html','manage_dmCenter_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>-->
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_dmCenter_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_dmCenter_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/showcase/daily/dmCenter/exportXls.html','manage_dmCenter_searchform','dm_center')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/showcase/daily/dmCenter/exportCsv.html','manage_dmCenter_searchform','dm_center')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/showcase/daily/dmCenter/importView.html',uploader:'manage_dmCenter_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>-->
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_dmCenter_searchform', 'manage_dmCenter_datagrid');
        });




        function displayOptionFunction (value){
            if (value=='1'){
                return '<span class="btn btn-success" >落地页</span>'
            }else {
                return '<span class="btn btn-primary" >表单</button>'
            }
        }

        function  domainFunction(value,row){
            return "<button  onclick='domainClick("+JSON.stringify(row)+")'  class='btn btn-outline-info'>" + value+' / '+row.secondaryDomain + "</button>";
            // return '<span class="btn btn-outline-info">'+value+' / '+row.secondaryDomain+'</span>'
        }
        function domainClick(row){
            navigator.clipboard.writeText(row.domain+'/'+row.secondaryDomain)
                .then(function() {
                    $.messager.alert('复制成功',row.domain+'/'+row.secondaryDomain);
                })
                .catch(function(error) {
                    $.messager.show('复制失败',error);
                });
        }




         function confirmSubmit1(url, id, datagrid, confirmTitle, confirmMessage, successCallBack) {
            var title = confirmTitle ? confirmTitle : '确定';
            var message = confirmMessage ? confirmMessage : '您是否清除记录？';
            $.messager.confirm(title, message, function (r) {
                if (r) {
                    $.ajax({
                        url: contextPath + url,
                        data: {
                            id: id
                        },
                        success: function (result) {
                            // if (typeof (result) == 'string')
                            //     result = eval('(' + result + ')');
                            if (result.success) {
                                $('#' + datagrid).datagrid('reload');
                                if (successCallBack) {
                                    successCallBack.call(this);
                                }
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
