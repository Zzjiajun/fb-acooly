<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_accounts_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">账户：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_accountName"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">持有者：</label>
                <select name="search_EQ_holder" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list mapName as k,v >
                        <option value="${k}">${k}:${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_accounts_searchform','manage_accounts_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_accounts_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/accounts/listJson.html" toolbar="#manage_accounts_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="asc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="accountName" formatter="mappingFormatter">账户信息</th>
                <th field="mail" formatter="mappingFormatter">邮箱</th>
                <th field="holder" formatter="mappingFormatter">持有者</th>
                <th field="balance" formatter="mappingFormatter">余额</th>
                <th field="mobileNo" formatter="mappingFormatter">账户编号</th>
                <th field="status" formatter="statusFormatters">状态</th>
                <th field="createTime" formatter="dateTimeFormatter">接户时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
                <th field="deprecatedTime" formatter="dateTimeFormatter">弃用时间</th>
                <th field="comments">备注</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_accounts_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_accounts_action" style="display: none;">
            <a id="myLink" onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/accounts/edit.html',id:'{0}',entity:'accounts',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="createAccounts('/manage/showcase/daily/accounts/transferOut.html','{0}','{1}',500,500);"  href="#" title="转出"><i class="fa  fa-line-chart fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/daily/accounts/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a id="myLink1" onclick="$.acooly.framework.remove('/manage/showcase/daily/accounts/deleteJson.html','{0}','manage_accounts_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_accounts_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/daily/accounts/create.html',maximizable:true,entity:'accounts',width:800,height:600})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_showcaseMember_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
            <div id="manage_showcaseMember_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/showcase/daily/accounts/exportXls.html','manage_accounts_searchform','信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
                <div onclick="$.acooly.framework.exports('/manage/showcase/daily/accounts/exportCsv.html','manage_accounts_searchform','信息')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/daily/accounts/deleteJson.html','manage_accounts_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_accounts_searchform', 'manage_accounts_datagrid');
        });
        function statusFormatters(value){
            if (value=='1'){
                return '<span class="btn btn-success">有效</span>'
            }else {
                return '<span class="btn btn-danger">弃用</span>'
            }
        }

        $(document).ready(function() {
            $('#myLink').hide();
            $('#myLink1').hide();
            // 发送AJAX请求到后端接口
            $.ajax({
                url: '/manage/showcase/daily/records/totalPermissions.html',
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    // 根据返回的数据控制<a>标签的显示
                    if (data.length>0) {
                        $('#myLink').show();
                        $('#myLink1').show();
                    } else {
                        $('#myLink').hide();
                        $('#myLink1').hide();
                    }
                },
                error: function() {
                    console.log('请求失败');
                },
            });
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



        function createAccounts(url,Id,name, width, height, hideSaveBtn) {
            $('#manage_accounts_datagrid').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');

            var t = null;
            var w = width ? width : 500;
            var h = height ? height : 'auto';
            var tt = '转账';
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
                    $.ajax({
                        type: 'GET',
                        url: '/manage/showcase/daily/accounts/saveList.html', // 替换为你的服务器URL
                        data: $('#manage_tranOut_searchform').serialize(),
                        success: function (result) {
                            $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
                            $.acooly.divdialog.dialog('destroy');
                            $('#manage_accounts_datagrid').datagrid('load');
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
