<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="overflow: hidden;" align="left">
        <form id="manage_user1_searchform" class="form-inline ac-form-search" onsubmit="return false"
              style="padding-left: 5px;">
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">姓名：</label>-->
<#--                <input type="text" class="form-control form-control-sm" name="search_LIKE_realName"/>-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <button class="btn btn-sm btn-primary" type="button"-->
<#--                        onclick="$.acooly.framework.search('manage_user_searchform','manage_user_datagrid');"><i-->
<#--                            class="fa fa-search fa-fw fa-col"></i> 查询-->
<#--                </button>-->
<#--            </div>-->
        </form>
    </div>
    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_use1_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/accounts/userTotalList.html"
               toolbar="#manage_user1_toolbar" fit="true" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[5, 10, 20, 30, 40, 50 ]" sortName="id"
               sortOrder="desc"
               checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="userId" checkbox="true"
                    data-options="formatter:function(value, row, index){ return row.id }">编号
                </th>
                <th field="username">用户</th>
                <th field="realName">姓名</th>
                <th field="spending">充值</th>
                <th field="email">E-mail</th>
            </tr>
            </thead>
        </table>
        <div id="manage_user1_action" style="display: none;">
        </div>
        <div id="manage_user1_toolbar">
        </div>
    </div>
    <div data-options="region:'south',border:false" style="height:50%;">
        <div id="manage_records1_sub_tab" class="easyui-tabs" fit="true" >
            <div title="账户信息" style="margin-left: 0px;">
                <table id="manage_records1_datagrid" class="easyui-datagrid" fit="true" border="false" fitColumns="false"
                       toolbar="#manage_records1_toolbar"
                       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
                       checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true"
                            data-options="formatter:function(value, row, index){ return row.id }">编号
                        </th>
                        <th field="accountName" formatter="mappingFormatter">账户信息</th>
                        <th field="balance" formatter="mappingFormatter">余额</th>
                        <th field="spending" formatter="mappingFormatter">花费</th>
                        <th field="recharge" formatter="mappingFormatter">充值</th>
                        <th field="inAmount">转入金额</th>
                        <th field="outStr" formatter="outFormatter"></th>
                        <th field="outAmount" >转出金额</th>
                        <th field="inStr" formatter="inFormatter"></th>
                        <th field="status" formatter="statusFormatters">状态</th>
                        <th field="createTime" formatter="dateTimeFormatter">接户时间</th>
                        <th field="deprecatedTime" formatter="dateTimeFormatter">弃用时间</th>
<#--                        <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_records1_action',value,row)}">动作</th>-->
                    </thead>
                </table>
                <!-- 每行的Action动作模板 -->
                <div id="manage_records1_action" style="display: none;">
<#--                    <a onclick="showOut('/manage/showcase/daily/transactionss/outParse.html?id=',800,600,'{0}');" href="#" title="转入明细"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>-->
                </div>
                <!-- 表格的工具栏 -->
                <div id="manage_records1_toolbar">
                </div>

            </div>
            <div title="地区总信息" style="margin-left: 0px;">
                <div data-options="region:'north',border:false" style="overflow: hidden;" align="left">
                    <form id="manage_user1_searchform" class="form-inline ac-form-search" onsubmit="return false"
                          style="padding-left: 5px;">
                        <div class="form-group">
                            <label class="col-form-label">创建时间：</label>
                            <input type="text" class="form-control form-control-sm" id="createTimeGTE" name="createTimeGTE" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                            <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="createTimeLTE" name="createTimeLTE" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                        </div>
                        <div class="form-group">
                            <button class="btn btn-sm btn-primary" type="button"
                                    onclick="startTimeClick()"><i
                                        class="fa fa-search fa-fw fa-col"></i> 查询
                            </button>
                        </div>
                    </form>
                </div>
                <table id="manage_accounts1_datagrid" class="easyui-datagrid" fit="true" border="false" fitColumns="false"
                       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
                       checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true"
                            data-options="formatter:function(value, row, index){ return row.id }">编号
                        </th>
                        <th field="region" formatter="mappingFormatter">地区</th>
                        <th field="spending" formatter="mappingFormatter">花费</th>
                        <th field="recharge" formatter="mappingFormatter">充值</th>
                        <th field="grades" formatter="mappingFormatter">业绩</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    /**
     * 页面加载完成后执行
     */
    $(function () {
        //注册按键回车直接提交查询
        $.acooly.framework.initPage('manage_user1_searchform', 'manage_user1_datagrid');
    });

    $("#manage_use1_datagrid").datagrid({
        onClickRow: function (index, row) {
            console.log("xxxxxxxxxxx");
            $.acooly.framework.loadGrid({
                gridId: "manage_records1_datagrid",
                url: '/manage/showcase/daily/records/totalForm.html',
                ajaxData: {"userName": row.username}
            });
            $.acooly.framework.loadGrid({
                gridId: "manage_accounts1_datagrid",
                url: '/manage/showcase/daily/records/totalPerformance.html',
                ajaxData: {"userName": row.username}
            });
        }
    })
    function statusFormatters(value){
        if (value=='1'){
            return '<span class="btn btn-success">有效</span>'
        }else {
            return '<span class="btn btn-danger">弃用</span>'
        }
    }
    function startTimeClick(){
        // $.ajax({
        //     type: 'POST',
        //     url: '/manage/showcase/daily/records/totalPerformance.html', // 替换为你的服务器URL
        //     data: $('#manage_user1_searchform').serialize(),
        //     success: function(response) {
        //
        //     },
        // });
        //获取当前选中的额值
        var row = $("#manage_use1_datagrid").datagrid("getSelections")[0];
        $.acooly.framework.loadGrid({
            gridId: "manage_accounts1_datagrid",
            url: '/manage/showcase/daily/records/totalPerformance.html',
            ajaxData: {"createTimeGTE": $('#createTimeGTE').val(),
                "userName": row.username,"timeSelection": "true",
                "createTimeLTE": $('#createTimeLTE').val()}
        });
    }
    function outFormatter(value,row){
        return "<a href='#' title='转入信息' onclick='showOut("+JSON.stringify(row)+")' ><i class='fa fa-arrow-circle-o-down fa-lg fa-fw fa-col'></i></a>";
    }
    function showOut(value){
        var url="/manage/showcase/daily/accounts/outParse.html?accountName="+value["accountName"];
        return $.acooly.framework.get({
            url: url,
            width: 800,
            height: 600
        });
    }
    function inFormatter(value,row){
        return "<a href='#' title='转出信息' onclick='showIn("+JSON.stringify(row)+")' ><i class='fa fa-arrow-circle-o-up fa-lg fa-fw fa-col'></i></a>";
    }
    function showIn(value){
        var url="/manage/showcase/daily/accounts/inParse.html?accountName="+value["accountName"];
        return $.acooly.framework.get({
            url: url,
            width: 800,
            height: 600
        });
    }

</script>