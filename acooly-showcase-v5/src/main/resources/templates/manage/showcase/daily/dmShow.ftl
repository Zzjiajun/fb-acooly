<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_dmShow_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">地区：</label>
                <select name="search_EQ_region" class="form-control select2bs4" data-options="required:true">
                    <option value=""></option>
                    <#list regionList as v >
                        <option value="${v}">${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">模版编号：</label>
                <option value=""></option>

                <select name="search_EQ_serialNumber"  class="form-control select2bs4" data-options="required:true">
                    <#list collected as k,v >
                        <option value="${v}">${k}</option>
                    </#list>
                </select>
            </div>
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">添加时间：</label>-->
<#--                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">修改时间：</label>-->
<#--                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--            </div>-->
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_dmShow_searchform','manage_dmShow_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_dmShow_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/dmShow/listJson.html" toolbar="#manage_dmShow_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >id</th>
                <th field="region" formatter="contentFormatter">地区</th>
                <th field="serialNumber" formatter="contentFormatter">模版编号</th>
                <th field="remark" formatter="contentFormatter">备注</th>
                <th field="createTime" formatter="dateTimeFormatter">添加时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
                <th field="domain" formatter="copyDomain">域名地址</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_dmShow_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_dmShow_action" style="display: none;">
            <div class="btn-group btn-group-xs">
<#--              <button onclick="$.acooly.framework.show('/manage/showcase/daily/dmShow/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>-->
              <button onclick="showLink('/manage/showcase/daily/dmShow/showLink.html','{0}','{1}',800,800);" class="btn btn-success btn-xs"  href="#" ><i class="fa fa-file-o fa-lg fa-fw fa-col"></i>预览</button>
              <button onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/dmShow/edit.html',id:'{0}',entity:'dmShow',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
              <button onclick="$.acooly.framework.remove('/manage/showcase/daily/dmShow/deleteJson.html','{0}','manage_dmShow_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_dmShow_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/daily/dmShow/create.html',entity:'dmShow',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/daily/dmShow/deleteJson.html','manage_dmShow_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_dmShow_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_dmShow_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/showcase/daily/dmShow/exportXls.html','manage_dmShow_searchform','dm_show')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/showcase/daily/dmShow/exportCsv.html','manage_dmShow_searchform','dm_show')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/showcase/daily/dmShow/importView.html',uploader:'manage_dmShow_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_dmShow_searchform', 'manage_dmShow_datagrid');
        });
    </script>

    <script>
        function showLink(url,Id,name, width, height, hideSaveBtn) {
            $('#manage_accounts_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');

            var t = null;
            var w = width ? width : 500;
            var h = height ? height : 'auto';
            var tt = '落地页查看';
            tt = '<i class="fa fa-lg fa-fw fa-col fa-floppy-o" ></i>' + tt;
            var addB = '确定';
            addB = '<i class="fa fa-lg fa-fw fa-col fa-plus-circle" ></i>' + addB;
            var max = true;
            url = $.acooly.framework.buildCanonicalUrl(url, {"id":Id});


            // 构建buttons
            var saveBtn = {
                id: 'acooly-framework-add-btn',
                text: addB,
                handler: function () {
                    // $.ajax({
                    //     type: 'GET',
                    //     url: '/manage/showcase/daily/accounts/saveList.html', // 替换为你的服务器URL
                    //     data: $('#manage_tranOut_searchform').serialize(),
                    //     success: function (result) {
                    //         $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
                    //         $.acooly.divdialog.dialog('destroy');
                    //         $('#manage_accounts_datagrid').datagrid('load');
                    //     },
                    // });
                }
            };

            var closeBtn = {
                text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col" ></i>关闭',
                handler: function () {
                    // var d = $(this).closest('.window-body');
                    d.dialog('close');

                }
            };

            var buttons = buttons || [];
            // buttons.push(saveBtn);
            buttons.push(closeBtn);

            var d = null;
            $.acooly.divdialog = d = $('<div/>').dialog({
                href: url,
                title: tt,
                top: t,
                width: w,
                height: h,
                modal: true,
                /*iconCls : 'icon-save',*/
                maximizable: max,
                buttons: buttons,
                onClose: function () {
                    $(this).dialog('destroy');
                },
                onOpen: function () {
                    // 打开dialog，EASYUI渲染完成后，处理combobox的宽度
                    var that = $(this);
                    $.parser.onComplete = function () {
                        //要执行的操作
                        $.acooly.framework.extendCombobox($(that));
                        //Initialize
                        $.acooly.framework.initPlugins($(that));
                        //最后把坑爹的事件绑定解除
                        $.parser.onComplete = function () {
                        };
                    }
                }
            });
        }

        function copyDomain(value){

            return "<button class='btn btn-primary btn-xs' href='#' title='复制域名地址' onclick='copyLink("+JSON.stringify(value)+")' ><i class='fa fa-chain fa-lg fa-fw fa-col'></i>复制地址</button>";
        }


        function copyLink (value){
            // 获取要复制的文本

            // 使用 Clipboard API 将文本复制到剪贴板
            navigator.clipboard.writeText(value)
                .then(function() {
                    $.messager.alert('复制成功',value);
                })
                .catch(function(error) {
                    $.messager.show('复制失败',error);
                });
        }
    </script>
</div>
