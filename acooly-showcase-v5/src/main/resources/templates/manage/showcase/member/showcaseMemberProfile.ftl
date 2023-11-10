<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_showcaseMemberProfile_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">用户名：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_username"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">手机认证：</label>
                <select name="search_EQ_mobileNoStatus" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allMobileNoStatuss as k,v>
                    <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">实名认证：</label>
                <select name="search_EQ_realNameStatus" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allRealNameStatuss as k,v>
                    <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">客户经理：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_manager"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">经纪人：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_broker"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_showcaseMemberProfile_searchform','manage_showcaseMemberProfile_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_showcaseMemberProfile_datagrid" class="easyui-datagrid" url="/manage/showcase/member/showcaseMemberProfile/listJson.html" toolbar="#manage_showcaseMemberProfile_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="profilePhoto" formatter="$.acooly.showcase.member.profile.photoFormatter">头像</th>
                <th field="username">用户名</th>
                <th field="nickname">昵称</th>
                <th field="emailStatus" formatter="mappingFormatter">邮箱认证</th>
                <th field="mobileNoStatus" formatter="mappingFormatter">手机认证</th>
                <th field="realNameStatus" formatter="mappingFormatter">实名认证</th>
                <th field="smsSendStatus" formatter="mappingFormatter">发送短信</th>
                <th field="broker">经纪人</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_showcaseMemberProfile_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_showcaseMemberProfile_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/showcase/member/showcaseMemberProfile/edit.html',id:'{0}',entity:'showcaseMemberProfile',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/member/showcaseMemberProfile/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/showcase/member/showcaseMemberProfile/deleteJson.html','{0}','manage_showcaseMemberProfile_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_showcaseMemberProfile_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/member/showcaseMemberProfile/create.html',entity:'showcaseMemberProfile',width:500,height:500})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/member/showcaseMemberProfile/deleteJson.html','manage_showcaseMemberProfile_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_showcaseMemberProfile_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
            <div id="manage_showcaseMemberProfile_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/showcase/member/showcaseMemberProfile/exportXls.html','manage_showcaseMemberProfile_searchform','会员配置信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_showcaseMemberProfile_searchform', 'manage_showcaseMemberProfile_datagrid');
        });
    </script>
</div>
