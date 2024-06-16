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
                <th field="link" formatter="domainFunction1" >链接地址</th>
                <th field="domain" formatter="domainFunction">访问域名</th>
                <#--                <th field="serialNumber" formatter="contentFormatter">模版地址</th>-->
                <th field="visitsNumber"  formatter="accessNumberFunction" sortable="true" sum="true">域名访问量</th>
                <th field="clicksNumber" formatter="clickNumberFunction" sortable="true" sum="true">按钮点击数量</th>
                <th field="diversion" formatter="displayOptionTwoFunction">是否轮询</th>
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
                <button onclick="$.acooly.framework.show('/manage/showcase/daily/dmCenter/show.html?id={0}',600,600);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
                <button onclick="confirmSubmit1('/manage/showcase/daily/dmCenter/eliminate.html','{0}','manage_dmCenter_datagrid');" class="btn btn-outline-secondary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>清除记录</button>
                <button onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/dmCenter/edit.html',id:'{0}',entity:'dmCenter',width:800,height:700});" class="btn btn-outline-success btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
                <button onclick="deleteDmCenter('/manage/showcase/daily/dmCenter/deleteJson.html','{0}','manage_dmCenter_datagrid');" class="btn btn-outline-danger btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.8/clipboard.min.js"></script>
    <link href="//unpkg.com/layui@2.9.8/dist/css/layui.css" rel="stylesheet">
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_dmCenter_searchform', 'manage_dmCenter_datagrid');
        });




        // function displayOptionFunction (value,row){
        //     if (value=='1'){
        //         return "<button class='btn btn-success' onclick='openboard4("+JSON.stringify(row)+")' >落地页</button>"
        //     }else {
        //         return "<button class='btn btn-primary' onclick='openboard4("+JSON.stringify(row)+")'>表单</button>"
        //     }
        // }

        function displayOptionFunction (value, row) {
            var buttonHtml;
            if (value == '1') {
                buttonHtml = "<div style='text-align: center;'><button class='btn btn-success' onclick='openboard4("+JSON.stringify(row)+")'>落地页</button></div>";
            } else {
                buttonHtml = "<div style='text-align: center;'><button class='btn btn-primary' onclick='openboard4("+JSON.stringify(row)+")'>表单</button></div>";
            }
            return buttonHtml;
        }


        // function  domainFunction(value,row){
        //     return "<button  onclick='copyToClipboard3("+JSON.stringify(row)+")'  class='btn btn-info'>" + value+' / '+row.secondaryDomain + "</button>";
        //     // return "<button  onclick='copyToClipboard3("+JSON.stringify(row)+")'  class='btn btn-primary'>复制域名</button>";
        //     // return '<span class="btn btn-outline-info">'+value+' / '+row.secondaryDomain+'</span>'
        // }


        function domainFunction(value, row) {
            return "<div style='text-align: center;'><button onclick='copyToClipboard3("+JSON.stringify(row)+")' class='layui-btn layui-btn-radius'>" + value + ' / ' + row.secondaryDomain + "</button></div>";
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

        function domainNumberFunction(value,row){
            if(row.displayOption=='2'){
                return "<span style='color: darkred;font-weight: bold;' </span>表单无按钮</span>";
            }else {
                return "<span style='color: green;font-weight: bold;' </span>"+value+"</span>";
            }
        }

        function displayOptionTwoFunction (value){
            if (value=='1'){
                return ' <i class="fa fa-check-square fa-fw fa-col" style="color: cornflowerblue" />'
            }else {
                return '<i class="fa  fa-remove fa-fw fa-col" style="color: red"/>'
            }
        }



        function  domainFunction1(value,row){
            if (row.diversion=='1'){
                return "<button    onclick='manage_showcase_dmCenter_add()' class='btns' style=''>跳转到轮询链接</button>";
            }else {
                return "<button  onclick='copyToClipboard2("+JSON.stringify(value)+")'  class='btn btn-link'>" + value+ "</button>";
            }
            // return '<span class="btn btn-outline-info">'+value+' / '+row.secondaryDomain+'</span>'
        }



        function copyToClipboard2(text) {
            var clipboard = new ClipboardJS('.btn', {
                text: function() {
                    return text;
                }
            });

            clipboard.on('success', function(e) {
                $.messager.alert('复制成功', e.text);
                clipboard.destroy(); // 销毁 clipboard 实例
            });

            clipboard.on('error', function(e) {
                $.messager.alert('复制失败', e.action);
                // console.error('复制失败:', e.action);
                // alert('复制失败，请手动复制！');
                clipboard.destroy(); // 销毁 clipboard 实例
            });

            clipboard.onClick(event); // 手动触发复制操作
        }





        function copyToClipboard3(row) {
            var clipboard = new ClipboardJS('.btn', {
                text: function() {
                    return row.domain+'/'+row.secondaryDomain;
                }
            });

            clipboard.on('success', function(e) {
                $.messager.alert('复制成功', e.text);
                clipboard.destroy(); // 销毁 clipboard 实例
            });

            clipboard.on('error', function(e) {
                $.messager.alert('复制失败', e.action);
                // console.error('复制失败:', e.action);
                // alert('复制失败，请手动复制！');
                clipboard.destroy(); // 销毁 clipboard 实例
            });

            clipboard.onClick(event); // 手动触发复制操作
        }


        function openboard4(row){
            window.open("http://"+row.domain+'/'+row.secondaryDomain,'_blank');
        }


        function manage_showcase_dmCenter_add() {
            $('#layout_center_tabs').tabs("add", {
                title: '轮询链接',
                closable: true,
                iconCls: 'fa fa-commenting',
                href: '/manage/link/linkSrcs/index.html',
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
                            }
                            if (result.message) {
                                $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
                            }
                        }
                    });
                }
            });
        }




        function  deleteDmCenter(url, id, datagrid, confirmTitle, confirmMessage, successCallBack){
            var title = confirmTitle ? confirmTitle : '确定';
            var message = confirmMessage ? confirmMessage : '您是否要提交该操作？';
            $.messager.confirm(title, message, function (r) {
                if (r) {
                    var loadingIndex = layer.load(2, {
                        shade: [0.5, '#fff'], // 加载遮罩背景颜色和透明度
                        content: '加载中...',
                        success: function (layero) {
                            layero.find('.layui-layer-content').css({
                                'padding-top': '39px',
                                'width': '60px'
                            });
                        }
                    });
                    $.ajax({
                        url: contextPath + url,
                        data: {
                            id: id
                        },
                        success: function (result) {
                            layer.close(loadingIndex);
                            if (typeof (result) == 'string')
                                result = eval('(' + result + ')');
                            if (result.success) {
                                var className = $('#' + datagrid).attr('class');
                                if (className.indexOf('easyui-treegrid') != -1) {
                                    $('#' + datagrid).treegrid('reload');
                                } else {
                                    $('#' + datagrid).datagrid('reload');
                                }
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




        //点击按钮查看数据详情
        function clickNumberFunction(value,row){
            if(row.displayOption=='2'){
                return "<span style='color: darkred;font-weight: bold;' </span>表单无按钮</span>";
            }else {
                return "<div style='text-align: center;'><button  onclick='showClick("+JSON.stringify(row)+")'  class='btn btn-primary'>" + value+ "点击</button></div>";
            }
        }

        function accessNumberFunction(value,row){
            return "<div style='text-align: center;'><button  onclick='showAccess("+JSON.stringify(row)+")'  class='btn btn-primary'>" + value+ "点击</button></div>";
        }


        function showClick(row) {
            var url ='/manage/link/dmClick/buildClickUrl?centerId='+row.id;
           console.log(url);
            return this. showClickGet({
                url: url
            });
        }

        function showAccess(row) {
            var url ='/manage/link/dmAccess/buildAccessUrl?centerId='+row.id;
            return this.showClickGet({
                url: url
            });
        }


        function showClickGet(opts) {
            var url = opts.url;
            var width = opts.width != null ? opts.width : 1400;
            var height = opts.height != null ? opts.height : 800;
            var title = opts.title != null ? opts.title : '<i class="fa fa-file-o fa-lg fa-fw fa-col"></i>查看';
            var buttonLabel = opts.buttonLabel != null ? opts.buttonLabel : '关闭';
            var d = $('<div/>').dialog({
                href: contextPath + url,
                width: width,
                height: height,
                modal: true,
                title: title,
                buttons: [{
                    text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col"></i>关闭',
                    handler: function () {
                        d.dialog('close');
                    }
                }],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
            return d;
        }

    </script>
    <style>
        .btns {
            background: #eb94d0;
            /* 创建渐变 */
            background-image: -webkit-linear-gradient( #eb94d0, #2079b0);
            background-image: -moz-linear-gradient( #eb94d0, #2079b0);
            background-image: -ms-linear-gradient( #eb94d0, #2079b0);
            background-image: -o-linear-gradient( #eb94d0, #2079b0);
            background-image: linear-gradient(to bottom, #eb94d0, #2079b0);
            /* 给按钮添加圆角 */
            border-radius: 14px;
            text-shadow: 3px 2px 1px #9daef5;
            font-family: Arial;
            color: #fafafa;
            font-size: 12px;
            padding: 9px;
            text-decoration: none;
        }
        /* 悬停样式 */
        .btns:hover {
            background: #2079b0;
            background-image: -webkit-linear-gradient( #2079b0, #eb94d0);
            background-image: -moz-linear-gradient( #2079b0, #eb94d0);
            background-image: -ms-linear-gradient( #2079b0, #eb94d0);
            background-image: -o-linear-gradient( #2079b0, #eb94d0);
            background-image: linear-gradient(to bottom, #2079b0, #eb94d0);
            text-decoration: none;
        }
    </style>
</div>
