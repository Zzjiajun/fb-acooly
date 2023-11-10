<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_showcaseMember_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class=""></div>
            <div class="form-group">
                <label class="col-form-label">用户名：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_username"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">身份证号码：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_idcardNo"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">手机号码：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_mobileNo"/>
            </div>

            <div class="form-group">
                <label class="col-form-label">客户类型：</label>
                <select name="search_EQ_customerType" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allCustomerTypes as k,v>
                    <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">状态：</label>
                <select name="search_EQ_status" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allStatuss as k,v>
                    <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">注册时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">修改时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_showcaseMember_searchform','manage_showcaseMember_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_showcaseMember_datagrid" class="easyui-datagrid" url="/manage/showcase/member/showcaseMember/listJson.html" toolbar="#manage_showcaseMember_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true"
               data-options="onClickRow:$.acooly.showcase.member.tab.mainDatagridOnClick">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="memberNo">会员编码</th>
                <th field="username">用户名</th>
                <th field="realName">姓名</th>
                <th field="idcardNo">身份证号码</th>
                <th field="mobileNo">手机号码</th>
                <th field="customerType" formatter="mappingFormatter">客户类型</th>
                <th field="doneRatio" formatter="percentFormatter" sortable="true" sum="true">完成度</th>
                <th field="salary" formatter="moneyFormatter" sortable="true" sum="true">薪水</th>
                <th field="registryChannel" formatter="mappingFormatter">注册渠道</th>
                <th field="numStatus" formatter="mappingFormatter">数字类型</th>
                <th field="status" formatter="mappingFormatter">状态</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_showcaseMember_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_showcaseMember_action" style="display: none;">
            <button class="btn btn-xs btn-primary" onclick="$.acooly.framework.edit({url:'/manage/showcase/member/showcaseMember/edit.html',id:'{0}',maximizable:true,entity:'showcaseMember',width:800,height:600});"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
            <button class="btn btn-xs btn-info" onclick="$.acooly.framework.show('/manage/showcase/member/showcaseMember/show.html?id={0}',800,600);"><i class="fa fa-file-o fa-fw fa-col"></i>查看</button>
            <button class="btn btn-xs btn-danger" onclick="$.acooly.framework.remove('/manage/showcase/member/showcaseMember/deleteJson.html','{0}','manage_showcaseMember_datagrid');"><i class="fa fa-trash-o fa-fw fa-col"></i>删除</button>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_showcaseMember_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/member/showcaseMember/create.html',maximizable:true,entity:'showcaseMember',width:800,height:600})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/member/showcaseMember/deleteJson.html','manage_showcaseMember_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_showcaseMember_exports_menu'"><i class="fa fa-arrow-circle-o-down fa-lg fa-fw fa-col"></i>批量导出</a>
            <div id="manage_showcaseMember_exports_menu" style="width:150px;">
                <div onclick="$.acooly.framework.exports('/manage/showcase/member/showcaseMember/exportXls.html','manage_showcaseMember_searchform','会员信息')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
                <div onclick="$.acooly.framework.exports('/manage/showcase/member/showcaseMember/exportCsv.html','manage_showcaseMember_searchform','会员信息')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/showcase/member/showcaseMember/importView.html',uploader:'manage_showcaseMember_import_uploader_file'});"><i class="fa fa-arrow-circle-o-up fa-lg fa-fw fa-col"></i>批量导入</a>-->

            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/demo/dynamicTab/index.html',maximizable:true,entity:'showcaseMember',width:800,height:600})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>Demo:动态Tab</a>
        </div>
    </div>

    <div data-options="region:'south',border:false" style="height:40%;">
        <div id="manage_showcaseMember_tabs" class="easyui-tabs" data-options="onSelect:$.acooly.showcase.member.tab.onSelect" fit="true">
            <!-- tab1：会员附件 -->
            <div title="会员附件" style="margin-left: 0px;">
                <table id="manage_showcaseMemberFile_datagrid" class="easyui-datagrid" toolbar="#manage_showcaseMemberFile_toolbar" fit="true" border="false" fitColumns="false"
                       pagination="true" idField="id" pageSize="10" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                        <th field="id" sortable="true">ID</th>
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
                    <a onclick="$.acooly.framework.show('/manage/showcase/member/showcaseMemberFile/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
                    <a onclick="$.acooly.framework.remove('/manage/showcase/member/showcaseMemberFile/deleteJson.html','{0}','manage_showcaseMemberFile_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
                </div>

                <!-- 表格的工具栏 -->
                <div id="manage_showcaseMemberFile_toolbar">
                    <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.showcase.member.file.upload()"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>批量上传</a>
                    <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/member/showcaseMemberFile/deleteJson.html','manage_showcaseMemberFile_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>删除</a>
                    <a href="#" class="easyui-linkbutton easyui-tooltip" plain="true" title="Tab1：文件相关操作，子列表采用分页模式，请注意：`$.acooly.showcase.member.file.upload()`方法"><i class="fa fa-question-circle-o fa-lg fa-fw fa-col"></i></a>
                </div>


            </div>
            <!-- tab2：会员联系信息 -->
            <div title="会员联系人" style="margin-left: 0px;">

                <table id="manage_showcaseMemberContact_datagrid" class="easyui-datagrid" toolbar="#manage_showcaseMemberContact_toolbar" fit="true" border="false" fitColumns="false"
                       sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                    <thead>
                    <tr>
                        <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                        <th field="id" sortable="true">ID</th>
                        <th field="memberNo">会员编码</th>
                        <th field="username">联系人</th>
                        <th field="mobileNo">手机号码</th>
                        <th field="email">邮箱</th>
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
                    <a onclick="$.acooly.framework.edit({url:'/manage/showcase/member/showcaseMemberContact/edit.html',id:'{0}',entity:'showcaseMemberContact',reload:true,onSuccess:function(){$.acooly.showcase.member.contact.loadList();},width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
                    <a onclick="$.acooly.framework.show('/manage/showcase/member/showcaseMemberContact/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
                    <a onclick="$.acooly.framework.remove('/manage/showcase/member/showcaseMemberContact/deleteJson.html','{0}','manage_showcaseMemberContact_datagrid',null,null,function(){$.acooly.showcase.member.contact.loadList();});" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
                </div>

                <!-- 表格的工具栏 -->
                <div id="manage_showcaseMemberContact_toolbar" class="row">
                    <form class="col-md-6 form-inline ac-form-search" id="manage_showcaseMemberContact_searchform">
                        <div class="form-group">
                            <label class="col-form-label">联系人：</label>
                            <input type="text" class="form-control form-control-sm" name="search_LIKE_username"/>
                        </div>
                        <div class="form-group">
                            <label class="col-form-label">手机号码：</label>
                            <input type="text" class="form-control form-control-sm" name="search_EQ_mobileNo"/>
                        </div>
                        <div class="form-group">
                            <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.showcase.member.contact.loadList();"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
                            <a title="1、子列表采用非分页全加载；<br>2、支持工具栏条件查询" class="easyui-tooltip ml-2"><i class="fa fa-2x fa-question-circle-o fa-lg fa-fw fa-col"></i></a>
                        </div>
                    </form>
                    <div class="col-md-6" style="line-height: 43px; text-align: right;">
                        <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.showcase.member.contact.add();"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i> 添加</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    $(function () {
        $.acooly.framework.initPage('manage_showcaseMember_searchform', 'manage_showcaseMember_datagrid');
    });
</script>
</div>
