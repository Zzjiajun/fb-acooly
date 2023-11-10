<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_phone_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">粉丝群：</label>
                <select name="search_EQ_groupName" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list groupName as  mapName>
                        <option value="${mapName}">${mapName}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button"
                        onclick="$.acooly.framework.search('manage_phone_searchform','manage_phone_datagrid');"><i
                            class="fa fa-search fa-lg fa-fw fa-col"></i> 查询
                </button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_phone_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/phone/listJson.html"
               toolbar="#manage_phone_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id"
               sortOrder="asc" checkOnSelect="true" selectOnCheck="true" singleSelect="false">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="groupName" formatter="mappingFormatter">群名</th>
                <th field="phoneNumber" formatter="mappingFormatter">电话号</th>
                <th field="repeatCount" formatter="mappingFormatter">出现数</th>
                <th field="rowActions"
                    data-options="formatter:function(value, row, index){return formatAction('manage_phone_action',value,row)}">
                    动作
                </th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_phone_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/phone/edit.html',id:'{0}',entity:'phone',width:500,height:500});"
               href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/daily/phone/show.html?id={0}',500,500);" href="#"
               title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/showcase/daily/phone/deleteJson.html','{0}','manage_phone_datagrid');"
               href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_phone_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="createPhone('/manage/showcase/daily/phone/createTest.html',true,800,600)"><i
                        class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_showcaseMember_exports_menu'"><i
                        class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
            <div id="manage_showcaseMember_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/showcase/daily/phone/exportXls.html','manage_phone_searchform','信息')">
                    <i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel
                </div>
                <div onclick="$.acooly.framework.exports('/manage/showcase/daily/phone/exportCsv.html','manage_phone_searchform','信息')">
                    <i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV
                </div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.removes('/manage/showcase/daily/phone/deleteJson.html','manage_phone_datagrid')"><i
                        class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_phone_searchform', 'manage_phone_datagrid');
        });

        function ajaxLoading() {
            $("<div class=\"datagrid-mask\"><iframe id=\"iframe1\" src=\"about:blank\" frameBorder=\"0\" marginHeight=\"0\" marginWidth=\"0\" style=\"position:absolute; visibility:inherit; top:0px;left:0px;width:100%; height:100%;z-index:-1; filter:alpha(opacity=0.5);\"></iframe></div>").css({ "display": "block", "z-index": "999999" }).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("正在查询数据，请稍候。。。").appendTo("body").css({ "display": "block", "left": ($(document.body).outerWidth(true) - 190) / 2, "top": ($(window).height() - 200) / 2 });
        }
        /*去掉加载数据提示*/
        function ajaxLoadEnd() {
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }


        function createPhone(url, maxs, width, height, hideSaveBtn) {
            $('#manage_phone_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');

            var t = null;
            var w = width ? width : 500;
            var h = height ? height : 'auto';
            var tt = '添加';
            tt = '<i class="fa fa-lg fa-fw fa-col fa-floppy-o" ></i>' + tt;
            var addB = '增加';
            addB = '<i class="fa fa-lg fa-fw fa-col fa-plus-circle" ></i>' + addB;
            var max = true;
            url = $.acooly.framework.buildCanonicalUrl(url, null);

            // 构建buttons
            var saveBtn = {
                id: 'acooly-framework-add-btn',
                text: addB,
                handler: function () {
                    ajaxLoading();
                    $.ajax({
                        type: 'POST',
                        url: '/manage/showcase/daily/phone/phoneParse.html', // 替换为你的服务器URL
                        data: $('#manage_phoneTest_searchform').serialize(),
                        success: function (result) {
                            ajaxLoadEnd();
                            $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
                            $.acooly.divdialog.dialog('destroy');
                            $('#manage_phone_datagrid').datagrid('load');
                        },
                    });
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
            buttons.push(saveBtn);
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
    </script>
</div>
