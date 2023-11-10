<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_showcaseMemberFile_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">文件ID：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_objectId"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">用户编码：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_memberNo"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">用户名：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_username"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">文件标题：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_fileTitle"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">文件名：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_fileName"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">文件扩展名：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_fileExt"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">文件类型：</label>
                <select name="search_EQ_fileType" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allFileTypes as k,v>
                    <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">修改时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_showcaseMemberFile_searchform','manage_showcaseMemberFile_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_showcaseMemberFile_datagrid" class="easyui-datagrid" url="/manage/showcase/member/showcaseMemberFile/listJson.html" toolbar="#manage_showcaseMemberFile_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="objectId">文件ID</th>
                <th field="memberNo">用户编码</th>
                <th field="username">用户名</th>
                <th field="fileTitle">文件标题</th>
                <th field="fileName">文件名</th>
                <th field="filePath" formatter="fileFormatter">文件路径</th>
                <th field="fileExt">文件扩展名</th>
                <th field="fileType" formatter="mappingFormatter">文件类型</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
                <th field="comments" formatter="contentFormatter">备注</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_showcaseMemberFile_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_showcaseMemberFile_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/showcase/member/showcaseMemberFile/edit.html',id:'{0}',entity:'showcaseMemberFile',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/member/showcaseMemberFile/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/showcase/member/showcaseMemberFile/deleteJson.html','{0}','manage_showcaseMemberFile_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_showcaseMemberFile_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.showcase.member.file.add()"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/member/showcaseMemberFile/deleteJson.html','manage_showcaseMemberFile_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_showcaseMemberFile_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
            <div id="manage_showcaseMemberFile_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/showcase/member/showcaseMemberFile/exportXls.html','manage_showcaseMemberFile_searchform','会员附件')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
                <div onclick="$.acooly.framework.exports('/manage/showcase/member/showcaseMemberFile/exportCsv.html','manage_showcaseMemberFile_searchform','会员附件')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/showcase/member/showcaseMemberFile/importView.html',uploader:'manage_showcaseMemberFile_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_showcaseMemberFile_searchform', 'manage_showcaseMemberFile_datagrid');
        });
    </script>
</div>
