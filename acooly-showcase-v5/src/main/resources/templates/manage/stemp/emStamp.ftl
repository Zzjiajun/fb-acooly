<style>
    .iconSpan {
        display: inline-block;
        margin: 5px;
        padding: 5px;
        width: 180px;
        border: 1px dashed #ddd;
        border-radius: 3px;
    }

    .iconSpan .icon-elm {
        vertical-align: middle;
        display: inline-block;
        font-size: 12px;
    }

    .resource_icons .header {
        height: 35px;
        line-height: 35px;
        vertical-align: middle;
        padding: 2px 10px;
        margin: 0 10px 5px 0;
        display: block;
        background-color: #eeeeee;
        border-bottom: 1px solid #dddddd;
    }

</style>
<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'north'" style="overflow: hidden; height: 33px; padding-left: 2px;">
        <div id="manage_stampOne_toolbar" style="margin-top: 1px;">
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.system.resource.treeToggle(this)">-->
<#--                <i class="fa fa-minus-square-o fa-fw fa-col"></i>全部折叠</a>-->
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="createStamp()">
                <i class="fa fa-plus-circle fa-fw fa-col"></i>添加资源表</a>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.system.resource.formCreate(true)">-->
<#--                <i class="fa fa-plus-circle fa-fw fa-col"></i>添加资源表</a>-->
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.system.resource.loadTree()">-->
<#--                <i class="fa fa-refresh fa-fw fa-col"></i>刷新</a>-->
        </div>
    </div>
    <!-- 菜单树 -->
    <div data-options="region:'west',border:true,split:true" id="manage_resource1_tree_panel" style="width:300px;padding-left: 10px;" align="left">
        <div id="manage_resource1_tree" class="ztree"></div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit : true,border : false">
            <!-- 查询条件 -->
            <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
                <form id="manage_emSumdata_searchform" class="form-inline ac-form-search" onsubmit="return false">
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
                        <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_emSumdata_searchform','manage_emSumdata_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
                    </div>
                </form>
            </div>

            <!-- 列表和工具栏 -->
            <div data-options="region:'center',border:false">
                <table id="manage_emSumdata_datagrid" class="easyui-datagrid"  toolbar="#manage_emSumdata_toolbar" fit="true" border="false" fitColumns="false"
                       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                        <th field="id" sortable="true" >id</th>
                        <th field="groupName" formatter="contentFormatter">群名</th>
                        <th field="business" formatter="contentFormatter">业务</th>
                        <th field="country" formatter="contentFormatter">国家</th>
                        <th field="remark" formatter="contentFormatter">备注</th>
                        <th field="phone" formatter="contentFormatter">电话</th>
                        <th field="share" formatter="contentFormatter">股民</th>
                        <th field="intent" formatter="contentFormatter">意向</th>
                        <th field="name" formatter="contentFormatter">姓名</th>
                        <th field="email" formatter="contentFormatter">邮箱</th>
                        <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                        <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
                    </tr>
                    </thead>
                    <thead frozen="true">
                    <tr>
                        <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_emSumdata_action',value,row)}">动作</th>
                    </tr>
                    </thead>
                </table>
                <!-- 每行的Action动作模板 -->
                <div id="manage_emSumdata_action" style="display: none;">
                    <div class="btn-group btn-group-xs">
                        <button onclick="$.acooly.framework.show('/manage/stemp/emSumdata/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
                        <button onclick="editWithGather('{0}');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
                        <button onclick="$.acooly.framework.remove('/manage/stemp/emSumdata/deleteJson.html','{0}','manage_emSumdata_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
                    </div>
                </div>
                <!-- 表格的工具栏 -->
                <div id="manage_emSumdata_toolbar">
                    <a href="#" class="easyui-linkbutton" plain="true" onclick="createWithGather()"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
                    <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/stemp/emSumdata/deleteJson.html','manage_emSumdata_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
                    <a href="#" class="easyui-menubutton" data-options="menu:'#manage_emSumdata_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
                    <div id="manage_emSumdata_exports_menu" style="width:150px;">
<#--                        <div onclick="$.acooly.framework.exports('/manage/stemp/emSumdata/exportXls.html','manage_emSumdata_searchform','em_sumdata')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>-->
                        <div onclick="exportWithStampId()"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
<#--                        <div onclick="$.acooly.framework.exports('/manage/stemp/emSumdata/exportCsv.html','manage_emSumdata_searchform','em_sumdata')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>-->
                    </div>
                    <a href="#" class="easyui-linkbutton" plain="true"
                       onclick="handleImport()">
                        <i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入
                    </a>
                </div>
            </div>
            <script type="text/javascript">
                $(function () {
                    $.acooly.framework.initPage('manage_emSumdata_searchform', 'manage_emSumdata_datagrid');
                });
            </script>
        </div>
    </div>

    <script type="text/javascript">
        // 确保 resource1 已经定义
        if (typeof $.acooly.system.resource1 === 'undefined') {
            $.acooly.system.resource1 = {};
        }

        // 定义 zTreeSetting
        $.acooly.system.resource1.zTreeSetting = {
            view: {
                showLine: true,
                showIcon: true,
                addHoverDom: function (treeId, treeNode) {
                    let aObj = $("#" + treeNode.tId + "_a");
                    // 添加子
                    // if (treeNode.isParent && $("#manage_resource_add_Btn_" + treeNode.id).length == 0) {
                    //     let html = "<a id='manage_resource_add_Btn_" + treeNode.id + "' href='javascript:;' onclick='$.acooly.system.resource.formCreate();return false;' style='margin:0 0 0 5px;padding-top: 2px;'><i class=\"fa fa-plus-circle\"></i></a>"
                    //     aObj.after(html)
                    // }
                    // 删除
                    if (!treeNode.children || treeNode.children.length == 0) {
                        if ($("#manage_resource1_delete_Btn_" + treeNode.id).length > 0)
                            return;
                        let html = "<a  id='manage_resource1_delete_Btn_" + treeNode.id + "' href='javascript:;' onclick='deleteStamp(" + treeNode.id + ");return false;' style='margin:0 0 0 5px;padding-top: 2px;color:red;'><i class=\"fa fa-minus-circle\"></i></a>"
                        aObj.append(html)
                    }
                },
                removeHoverDom: function (treeId, treeNode) {
                    // if ($("#manage_resource1_add_Btn_" + treeNode.id).length > 0)
                    //     $("#manage_resource1_add_Btn_" + treeNode.id).unbind().remove();
                    if (!treeNode.children || treeNode.children.length == 0)
                        $("#manage_resource1_delete_Btn_" + treeNode.id).unbind().remove();
                },
                fontCss: function (treeId, treeNode) {
                    return treeNode.isParent ? {
                        color: 'black'
                    } : {
                        color: 'gray'
                    };
                }
            },
            edit: {
                enable: false,
                showRemoveBtn: false,
                showRenameBtn: false,
                draggable: false // 取消节点可移动功能
            },
            callback: {
                // 点击节点事件
                // 点击节点事件
                onClick: function (event, treeId, treeNode, clickFlag) {
                    var parentId = treeNode.parentId ? treeNode.parentId : treeNode.id;
                    // 动态设置数据表格的URL
                    updateDatagridUrlAndReload(parentId, treeNode.gather);
                },
                onDrop: function (event, treeId, treeNodes, targetNode, moveType) {
                    // 如果需要处理拖放事件，可以在这里添加逻辑
                }
            }
        };


        // 更新数据表格URL并刷新
        function updateDatagridUrlAndReload(parentId, gather) {
            $('#manage_emSumdata_datagrid').datagrid('options').url = '/manage/stemp/emSumdata/listJson.html?search_EQ_stampId=' + parentId
                + '&search_EQ_isDelete=0';
            $('#manage_emSumdata_datagrid').datagrid('reload');

            // 获取gather字段值
            var gatherFields = gather ? gather.split(',') : [];

            // 定义需要根据gather显示的列
            var columnsToToggle = ['groupName', 'business', 'country', 'remark', 'phone', 'share', 'intent','name','email'];

            // 隐藏所有列
            $.each(columnsToToggle, function (index, field) {
                $('#manage_emSumdata_datagrid').datagrid('hideColumn', field);
            });

            // 显示根据gather字段指定的列
            $.each(gatherFields, function (index, field) {
                $('#manage_emSumdata_datagrid').datagrid('showColumn', field);
            });
        }



        //添加按钮
        function createWithGather() {
            var zTree = $.fn.zTree.getZTreeObj("manage_resource1_tree");
            var selectedNodes = zTree.getSelectedNodes();
            if (selectedNodes.length > 0) {
                var selectedNode = selectedNodes[0];
                var gather = selectedNode.gather || '';
                var stampId = selectedNode.parentId ? selectedNode.parentId : selectedNode.id;
                var url = '/manage/stemp/emSumdata/create.html';
                if (gather) {
                    url += '?gather=' + gather;
                }
                if (stampId) {
                    if (gather) {
                        url += '&stampId=' + stampId;
                    } else {
                        url += '?stampId=' + stampId;
                    }
                }
                $.acooly.framework.create({
                    url: url,
                    entity: 'emSumdata',
                    width: 500,
                    height: 500
                });
            } else {
                $.acooly.messager("提示", "请先选择一个类型表", 'warning');
            }
        }


        function handleImport() {
            // 获取当前选中的树节点
            const zTree = $.fn.zTree.getZTreeObj("manage_resource1_tree");
            const nodes = zTree.getSelectedNodes();

            if (nodes.length === 0) {
                $.acooly.messager("提示", "请先选择资源分类", "warning");
                return;
            }

            var stampId = nodes[0].id;
            let url = `/manage/stemp/emSumdata/importView.html`;
            url += '?stampId=' + stampId;

            $.acooly.framework.imports({
                url: url,
                uploader: 'manage_emSumdata_import_uploader_file'
            });
        }

        //编辑按钮
        function editWithGather(id) {
            var zTree = $.fn.zTree.getZTreeObj("manage_resource1_tree");
            var selectedNodes = zTree.getSelectedNodes();
            if (selectedNodes.length > 0) {
                var selectedNode = selectedNodes[0];
                var gather = selectedNode.gather || '';
                $.acooly.framework.edit({
                    url: '/manage/stemp/emSumdata/edit.html?' + (gather ? 'gather=' + gather : ''),
                    id: id,
                    entity: 'emSumdata',
                    width: 500,
                    height: 500
                });
            } else {
                $.acooly.messager("提示", "请先选择一个父节点", 'warning');
            }
        }


        /**
         * 加载树
         *
         * @param defaultNode
         */
        $.acooly.system.resource1.loadTree = function (defaultNode) {
            $.ajax({
                url: '/manage/stemp/emStamp/listJson.html', // 假设这个 URL 返回只包含父节点的数据
                success: function (data, status) {
                    if (data.success) {
                        // 初始化树结构，只使用父节点数据
                        $.fn.zTree.init($("#manage_resource1_tree"), $.acooly.system.resource1.zTreeSetting, data.rows);
                        var zTree = $.fn.zTree.getZTreeObj("manage_resource1_tree");
                        // 展开所有节点
                        zTree.expandAll(true);
                        // 选择默认节点

                        // 如果没有指定默认节点，选择第一个父节点
                        if (!defaultNode) {
                            defaultNode = zTree.getNodes()[0];
                        }

                        // 选择默认节点
                        if (defaultNode) {
                            zTree.selectNode(defaultNode, true);
                            updateDatagridUrlAndReload(defaultNode.id, defaultNode.gather);
                        }
                    } else {
                        console.error("Failed to load tree data:", data.message);
                    }
                },
                error: function (xhr, status, error) {
                    console.error("AJAX Error: " + status + error);
                }
            });
        };

        $(function () {
            // 调用加载树的方法
            $.acooly.system.resource1.loadTree();
            // $.acooly.system.resource.init();
            $.acooly.framework.initPage('manage_emFileds_searchform', 'manage_emFileds_datagrid');
        });

        //删除表方法
        function deleteStamp(id) {
            $.messager.confirm("确认", "你确认删除该资源？	", function (r) {
                if (!r)
                    return;
                $.ajax({
                    url: '/manage/stemp/emStamp/deleteJson.html?id=' + id,
                    success: function (result, status) {
                        if (typeof (result) == 'string') {
                            result = eval('(' + result + ')');
                        }
                        if (result.success) {
                            var zTree = $.fn.zTree.getZTreeObj("manage_resource1_tree");
                            var node = zTree.getNodeByParam("id", id, null);
                            zTree.removeNode(node);
                        }
                        if (result.message)
                            $.acooly.messager("提示", result.message, result.success ? 'success' : 'danger');
                    }
                });
            })
        }

        //导出表方法
        function exportWithStampId() {
            var zTree = $.fn.zTree.getZTreeObj("manage_resource1_tree");
            var selectedNodes = zTree.getSelectedNodes();
            if (selectedNodes.length > 0) {
                var selectedNode = selectedNodes[0];
                var stampId = selectedNode.parentId ? selectedNode.parentId : selectedNode.id;
                var stampName = selectedNode.name;
                // 创建自定义参数对象
                const extraParams = {
                    search_EQ_stampId: stampId
                    // 可以添加其他参数
                }
                // 合并表单参数和额外参数
                const formParams = serializeObject($('#manage_emSumdata_searchform'));
                const queryParams = $.extend({}, formParams, extraParams);

                // 调用导出方法
                exportsResource(
                    '/manage/stemp/emSumdata/exportXls.html',
                    queryParams,  // 直接传递参数对象
                    stampName
                );
            } else {
                $.acooly.messager("提示", "请先选择一个父节点", 'warning');
            }
        }


        function exportsResource (url, queryParams, fileName, confirmTitle, confirmMessage) {
            if (isEmptyObject(queryParams)) {
                queryParams = serializeObject($('#' + searchForm));
            }
            if (fileName) {
                $(queryParams).attr('exportFileName', fileName);
            }

            $.acooly.framework.createAndSubmitForm(url, queryParams);
        }


        //添加表方法
        function createStamp() {
            createStampThree({url:'/manage/stemp/emStamp/create.html',entity:'emStamp',width:800,height:600})
            // 监听表单关闭事件

        }


        function createStampThree(opts){
            if (opts.entity) {
                createStampTwo(opts.url, "manage_" + opts.entity + "_editform", "manage_" + opts.entity + "_datagrid", opts.title, opts.top, opts.width, opts.height, opts.addButton, opts.reload, opts.maximizable, opts.onSubmit, opts.onSuccess, (opts.onFail || opts.onFailure), (opts.onCloseWindow || opts.onClose), opts.ajaxData, opts.buttons, opts.hideSaveBtn);
            } else {
                createStampTwo(opts.url, opts.form, opts.datagrid, opts.title, opts.top, opts.width, opts.height, opts.addButton, opts.reload, opts.maximizable, opts.onSubmit, opts.onSuccess, (opts.onFail || opts.onFailure), (opts.onCloseWindow || opts.onClose), opts.ajaxData, opts.buttons, opts.hideSaveBtn);
            }
        }

        function createStampTwo(url, form, datagrid, title, top, width, height,
                                 addButton, reload, maximizable, onSubmit, onSuccess, onFailure, onClose,
                                 ajaxData, buttons, hideSaveBtn){
            $('#' + datagrid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');

            var t = top ? top : null;
            var w = width ? width : 500;
            var h = height ? height : 'auto';
            var tt = title ? title : '添加';
            tt = '<i class="fa fa-lg fa-fw fa-col fa-floppy-o" ></i>' + tt;
            var addB = addButton ? addButton : '增加';
            addB = '<i class="fa fa-lg fa-fw fa-col fa-plus-circle" ></i>' + addB;
            var max = maximizable ? maximizable : false;
            url = $.acooly.framework.buildCanonicalUrl(url, ajaxData);

            // 构建buttons
            var saveBtn = {
                id: 'acooly-framework-add-btn',
                text: addB,
                handler: function () {
                    ajaxSubmitHandlerSave("create", $(this), form, datagrid, reload, onSubmit, onSuccess, onFailure);
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
            if (!hideSaveBtn) {
                buttons.push(saveBtn);
            }
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
                    if (onClose) {
                        onClose.call(d);
                    }
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

        function  ajaxSubmitHandlerSave(type, thisObject, form, datagrid, reload, onSubmit, onSuccess, onFailure){
            var d = $(thisObject).closest('.window-body');
            $('#' + form).ajaxSubmit({
                beforeSerialize: function (formData, options) {
                    // 编辑页面beforeSubmit方法
                    try {
                        var _beforeSubmit = eval(form + "_beforeSubmit");
                        if (typeof (_beforeSubmit) == "function") {
                            _beforeSubmit.call(this, formData);
                        }
                    } catch (e) {
                    }
                },
                beforeSubmit: function (formData, jqForm, options) {
                    // 如果有回调函数则处理回调函数的验证
                    if (onSubmit && !onSubmit.call(this, arguments)) {
                        return false;
                    }
                    // 编辑页面onSubmit方法
                    try {
                        var defaultFunc = eval(form + "_onSubmit");
                        if (defaultFunc && typeof (defaultFunc) == "function" && !defaultFunc.call(this, arguments)) {
                            return false;
                        }
                    } catch (e) {
                        // ig
                    }
                    // 默认easy-ui-validator验证
                    var result = $('#' + form).form('validate');
                    $(thisObject).linkbutton(result ? 'disable' : 'enable');
                    return result;
                },
                success: function (result, statusText) {
                    $(thisObject).linkbutton('enable');
                    if (typeof (result) == 'string') {
                        result = eval('(' + result + ')');
                    }
                    try {
                        if (result.success) {

                            // 如果有回调函数则处理回调函数
                            if (onSuccess) {
                                onSuccess.call(d, result);
                            }

                            if (type == 'create') {
                                $.acooly.framework.onSaveSuccess(d, datagrid, result, reload);
                            } else {
                                $.acooly.framework.onUpdateSuccess(d, datagrid, result, reload);
                            }

                            $.acooly.divdialog.dialog('destroy');
                            $.acooly.system.resource1.loadTree();
                            //
                        } else {
                            if (onFailure) {
                                onFailure.call(d, result);
                            }
                        }
                        if (result.message) {
                            $.acooly.messager("提示", result.message, result.success ? 'success' : 'danger');
                        }
                    } catch (e) {
                        $.acooly.messager('错误', e, 'danger');
                    }

                },
                error: function (XmlHttpRequest, textStatus, errorThrown) {
                    $(thisObject).linkbutton('enable');
                    $.acooly.messager('错误', errorThrown, 'danger');
                }
            });
        }


    </script>

</div>
