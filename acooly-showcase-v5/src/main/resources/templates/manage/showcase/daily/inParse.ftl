<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'center',border:false">
        <table id="manage_inParse_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/accounts/inParseList.html?accountName=${accountName}" toolbar="#manage_inParse_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="asc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="inAccount" formatter="mappingFormatter">目标账户</th>
                <th field="amount" formatter="mappingFormatter">转入的金额</th>
                <th field="accountManager" formatter="mappingFormatter">操作者</th>
                <th field="createTime" formatter="mappingFormatter">操作时间</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_inParse_action" style="display: none;">
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_inParse_toolbar">
        </div>
    </div>
</div>