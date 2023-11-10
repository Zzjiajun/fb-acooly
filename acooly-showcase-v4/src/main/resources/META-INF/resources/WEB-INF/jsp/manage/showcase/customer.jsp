<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/manage/common/taglibs.jsp" %>
<script type="text/javascript" src="/assert/js/customer.js" charset="utf-8"></script>
<script type="text/javascript">
    $(function () {
        $.acooly.framework.registerKeydown('manage_customer_searchform', 'manage_customer_datagrid');
    });

</script>
<div id="manage_customer_layout" class="easyui-layout" data-options="fit : true,border : false">
    <!-- 主表查询条件 -->
    <form id="manage_customer_searchform" onsubmit="return false">
        <table class="tableForm" width="100%;">
            <tr>
                <td align="left">
                    <div>
                        用户名: <input type="text" class="text" size="10" name="search_LIKE_username"/>
                        性别: <select style="width:60px;height:27px;" name="search_EQ_gender" editable="false" panelHeight="auto"
                                    class="easyui-combobox">
                        <option value="">所有</option>
                        <c:forEach var="e" items="${allGenders}">
                            <option value="${e.key}" ${param.search_EQ_gender == e.key?'selected':''}>${e.value}</option>
                        </c:forEach></select>
                        姓名: <input type="text" class="text" size="10" name="search_LIKE_realName"/>
                        证件: <input type="text" class="text" size="18" name="search_LIKE_idcardNo"/>
                        手机: <input type="text" class="text" size="13" name="search_LIKE_mobileNo"/>
                        类型: <select style="width:60px;height:27px;" name="search_EQ_customerType" editable="false" panelHeight="auto"
                                    class="easyui-combobox">
                        <option value="">所有</option>
                        <c:forEach var="e" items="${allCustomerTypes}">
                            <option value="${e.key}" ${param.search_EQ_customerType == e.key?'selected':''}>${e.value}</option>
                        </c:forEach></select>
                        状态: <select style="width:60px;height:27px;" name="search_EQ_status" editable="false" panelHeight="auto"
                                    class="easyui-combobox">
                        <option value="">所有</option>
                        <c:forEach var="e" items="${allStatuss}">
                            <option value="${e.key}" ${param.search_EQ_status == e.key?'selected':''}>${e.value}</option>
                        </c:forEach></select>
                        注册时间: <input size="11" type="text" class="text" id="search_GTE_createTime" name="search_GTE_createTime"
                                     onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                        至 <input size="11" type="text" class="text" id="search_LTE_createTime" name="search_LTE_createTime"
                                onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:false"
                           onclick="$.acooly.framework.search('manage_customer_searchform','manage_customer_datagrid');"><i
                                class="fa fa-search fa-lg fa-fw fa-col"></i>查询</a>
                    </div>
                </td>
            </tr>
        </table>
    </form>
    <!-- 主表列表 -->
    <div data-options="region:'center',border:false" style="overflow: hidden;height:50%;" align="left">
        <table id="manage_customer_datagrid" class="easyui-datagrid"
               url="${pageContext.request.contextPath}/manage/showcase/customer/listJson.html" toolbar="#manage_customer_toolbar" fit="true"
               border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
               checkOnSelect="true" selectOnCheck="true" singleSelect="true"
               data-options="onClickRow:manage_customer_onClickRow">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" data-options="formatter:function(value, row, index){ return row.id }">编号</th>
                <th field="id" sum="false">ID</th>
                <th field="username">用户名</th>
                <th field="age" sortable="true" data-options="styler:function(v,r,i){ if(v < 30){ return 'background-color:#ffee00;color:red;'; } }">年龄</th>
                <th field="birthday" sortable="true" formatter="dateFormatter">生日</th>
                <th field="gender" formatter="mappingFormatter">性别</th>
                <th field="realName">姓名</th>
                <th field="idcardType" formatter="mappingFormatter">证件类型</th>
                <th field="mobileNo">手机号码</th>
                <th field="customerType" formatter="mappingFormatter">客户类型</th>
                <th field="salary" formatter="centMoneyFormatter">薪水</th>
                <th field="fee" formatter="moneyFormatter">手续费</th>
                <th field="status" formatter="mappingFormatter">状态</th>
                <th field="subject" formatter="contentFormatter">简介</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_customer_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_customer_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/showcase/customer/edit.html',id:'{0}',entity:'customer',width:800,height:420,reload:true});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/customer/show.html?id={0}',800,420);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/showcase/customer/deleteJson.html','{0}','manage_customer_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_customer_toolbar">
            <!-- 控制按钮权限：添加 -->
            <shiro:hasPermission name="customer:create">
                <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/customer/create.html',entity:'customer',width:800,height:420,reload:true})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            </shiro:hasPermission>

            <!-- 控制按钮权限: 导出 -->
            <shiro:hasPermission name="customer:export">
                <a href="#" class="easyui-menubutton" data-options="menu:'#manage_customer_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
                <div id="manage_customer_exports_menu" style="width:150px;">
                    <div onclick="$.acooly.framework.exports('/manage/showcase/customer/exportXls.html','manage_customer_searchform','客户信息表')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
                    <div onclick="$.acooly.framework.exports('/manage/showcase/customer/exportCsv.html','manage_customer_searchform','客户信息表')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
                </div>
            </shiro:hasPermission>

            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/showcase/customer/importView.html',uploader:'manage_customer_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'south',border:false" style="height:46%;">
        <div id="manage_customer_sub_tab" class="easyui-tabs" fit="true" data-options="onSelect:manage_customer_sub_tab_onSelect">
            <!-- tab1：客户基本信息管理 ------------------------------------------------------------------>
            <div title="客户基本信息" style="margin-left: 0px;">
                <table id="manage_customerBasic_datagrid" class="easyui-datagrid" fit="true" border="false" fitColumns="false"
                       toolbar="#manage_customerBasic_toolbar"
                       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
                       checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true"
                            data-options="formatter:function(value, row, index){ return row.id }">编号
                        </th>
                        <th field="id">ID</th>
                        <th field="username">用户名</th>
                        <th field="maritalStatus" formatter="mappingFormatter">婚姻状况</th>
                        <th field="childrenCount">子女情况</th>
                        <th field="educationLevel" formatter="mappingFormatter">教育背景</th>
                        <th field="incomeMonth" formatter="mappingFormatter">月收入</th>
                        <th field="socialInsurance" formatter="mappingFormatter">社会保险</th>
                        <th field="houseFund" formatter="mappingFormatter">公积金</th>
                        <th field="houseStatue" formatter="mappingFormatter">住房情况</th>
                        <th field="carStatus" formatter="mappingFormatter">是否购车</th>
                        <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                        <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
                        <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_customerBasic_action',value,row)}">动作</th>
                    </tr>
                    </thead>
                </table>
                <!-- 每行的Action动作模板 -->
                <div id="manage_customerBasic_action" style="display: none;">
                    <a onclick="$.acooly.framework.edit({url:'/manage/showcase/customerBasic/edit.html',id:'{0}',entity:'customerBasic',width:500,height:400});"
                       href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
                    <a onclick="$.acooly.framework.remove('/manage/showcase/customerBasic/deleteJson.html','{0}','manage_customerBasic_datagrid',null,null,function(){load_manage_customerBasic_datagrid();});"
                       href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
                </div>
                <!-- 表格的工具栏 -->
                <div id="manage_customerBasic_toolbar">
                    <a href="#" class="easyui-linkbutton" plain="true" onclick="manage_customerBasic_create()"><i
                            class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
                    <a href="#" class="easyui-linkbutton" plain="true" onclick="load_manage_customerBasic_datagrid()"><i
                            class="fa fa-trash-o fa-lg fa-fw fa-col"></i>刷新</a>
                </div>

            </div>

            <!-- tab2------------------------------------------------------------------>
            <div title="客户扩展信息" style="margin-left: 0px;">
                <table id="manage_customerExtend_datagrid" class="easyui-datagrid" fit="true" border="false" fitColumns="false"
                       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
                       checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true"
                            data-options="formatter:function(value, row, index){ return row.id }">编号
                        </th>
                        <th field="id">ID</th>
                        <th field="username">用户名</th>
                        <th field="updateTime" formatter="formatDate">更新时间</th>
                        <th field="createTime" formatter="formatDate">创建时间</th>
                        <th field="comments">备注</th>
                        <th field="context">扩展信息</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    /**
     * 点击客户主表格行，加载显示所有关联只tab数据
     */
    function manage_customer_onClickRow() {
        var tab = $('#manage_customer_sub_tab').tabs('getSelected');
        var index = $('#manage_customer_sub_tab').tabs('getTabIndex', tab);
        //index = $.acooly.framework.getSelectedTabIndex("manage_customer_sub_tab");
        manage_customer_selectTab(index);
    }

    function manage_customer_sub_tab_onSelect(title,index){
        manage_customer_selectTab(index);
    }


    function manage_customer_selectTab(index){
        var row = $.acooly.framework.getSelectedRow("manage_customer_datagrid");
        if(!row) return;
        if (index == 0) {
            console.info("load Basic datagrid",index);
            load_manage_customerBasic_datagrid();
        } else if (index == 1) {
            console.info("load Extend datagrid",index)
            load_manage_customerExtend_datagrid();
        }
    }


    //使用ajax查询和加载:客户基本信息
    function load_manage_customerBasic_datagrid() {
        $.acooly.framework.fireSelectRow('manage_customer_datagrid', function (row) {
            $.acooly.framework.loadGrid({
                gridId: "manage_customerBasic_datagrid",
                url: '/manage/showcase/customerBasic/listJson.html',
                ajaxData: {"search_EQ_username": row.username}
            });
        }, '请先选择操作客户数据行');
    }

    //使用ajax查询和加载:客户扩展信息
    function load_manage_customerExtend_datagrid() {
        $.acooly.framework.fireSelectRow('manage_customer_datagrid', function (row) {
            $.acooly.framework.loadGrid({
                gridId: "manage_customerExtend_datagrid",
                url: '/manage/showcase/customerExtend/listJson.html',
                ajaxData: {"search_EQ_username": row.username}
            });
        }, '请先选择操作客户数据行');

    }


    /**
     * 添加客户基本信息
     */
    function manage_customerBasic_create() {
        //判断是否选择客户主表行，如果选择，则回调传入当前选中行的数据。
        $.acooly.framework.fireSelectRow('manage_customer_datagrid', function (row) {
            $.acooly.framework.create({
                url: '/manage/showcase/customerBasic/create.html',
                entity: 'customerBasic',
                width: 500, height: 400,
                ajaxData: {'username': row.username}
            });
        }, '请先选择操作客户数据行');
    }


</script>


