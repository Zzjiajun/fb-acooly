<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_showcaseMemberContact_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">用户编码：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_memberNo"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">联系人：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_username"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">手机号码：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_mobileNo"/>
            </div>

            <div class="form-group">
                <label class="col-form-label">省：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_province"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">市：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_city"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">区：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_district"/>
            </div>

            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_showcaseMemberContact_searchform','manage_showcaseMemberContact_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_showcaseMemberContact_datagrid" class="easyui-datagrid" url="/manage/showcase/member/showcaseMemberContact/listJson.html" toolbar="#manage_showcaseMemberContact_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="username">联系人</th>
                <th field="mobileNo">手机号码</th>
                <th field="email" formatter="contentFormatter">邮箱</th>
                <th field="province">省</th>
                <th field="city">市</th>
                <th field="district">区</th>
                <th field="companyName">公司名称</th>
                <th field="phoneNo">公司电话</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_showcaseMemberContact_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_showcaseMemberContact_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/showcase/member/showcaseMemberContact/edit.html',id:'{0}',entity:'showcaseMemberContact',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/member/showcaseMemberContact/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/showcase/member/showcaseMemberContact/deleteJson.html','{0}','manage_showcaseMemberContact_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_showcaseMemberContact_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/member/showcaseMemberContact/create.html',entity:'showcaseMemberContact',width:500,height:500})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/member/showcaseMemberContact/deleteJson.html','manage_showcaseMemberContact_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_showcaseMemberContact_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
            <div id="manage_showcaseMemberContact_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/showcase/member/showcaseMemberContact/exportXls.html','manage_showcaseMemberContact_searchform','会员联系信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
                <div onclick="$.acooly.framework.exports('/manage/showcase/member/showcaseMemberContact/exportCsv.html','manage_showcaseMemberContact_searchform','会员联系信息')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/showcase/member/showcaseMemberContact/importView.html',uploader:'manage_showcaseMemberContact_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_showcaseMemberContact_searchform', 'manage_showcaseMemberContact_datagrid');
        });
    </script>
</div>
