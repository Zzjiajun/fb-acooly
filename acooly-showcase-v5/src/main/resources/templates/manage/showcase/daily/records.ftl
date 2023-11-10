<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<#--<style>-->
<#--    /* 方法1：设置textarea合适的宽高 */-->
<#--    #jsonTextarea {-->
<#--        float: left;-->
<#--        margin-right: 20px;-->
<#--        width: 40%;-->
<#--        height: 70vh;-->
<#--        outline: none;-->
<#--        padding: 5px;-->
<#--    }-->

<#--    /* 方法2：自定义高亮样式 */-->
<#--    #jsonPre {-->
<#--        float: left;-->
<#--        width: 40%;-->
<#--        height: 70vh;-->
<#--        outline: 1px solid #ccc;-->
<#--        padding: 5px;-->
<#--        overflow: scroll;-->
<#--    }-->

<#--    .string {-->
<#--        color: green;-->
<#--    }-->

<#--    .number {-->
<#--        color: darkorange;-->
<#--    }-->

<#--    .boolean {-->
<#--        color: blue;-->
<#--    }-->

<#--    .null {-->
<#--        color: magenta;-->
<#--    }-->

<#--    .key {-->
<#--        color: red;-->
<#--    }-->
<#--</style>-->
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_records_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">用户：</label>
                <select name="search_EQ_userName" class="form-control select2bs4" data-options="required:true">
                    <option></option>
                    <#list mapName as k,v >
                        <option value="${k}">${k}:${v}</option>
                    </#list>
                </select>
            </div>
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
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_records_searchform','manage_records_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_records_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/records/listJson.html" toolbar="#manage_records_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="asc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="userName" formatter="mappingFormatter">用户</th>
                <th field="accountInformation" formatter="parse2">账户花销详情</th>
                <th field="recharge" formatter="parse2">账户充值详情</th>
                <th field="spending" >今日总花销</th>
                <th field="grades" formatter="mappingFormatter">业绩</th>
                <th field="region" formatter="mappingFormatter">地区</th>
                <th field="buildTime" formatter="dateTimeFormatter">生效时间</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_records_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_records_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/records/edit.html',id:'{0}',entity:'records',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/showcase/daily/records/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/showcase/daily/records/deleteJson.html','{0}','manage_records_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_records_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/daily/records/create.html',maximizable:true,entity:'records',width:800,height:600})"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>添加</a>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.showcase.usergroup.createBatch();"><i class="fa fa-plus-circle fa-lg fa-fw fa-col"></i>批量管理</a>-->
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/daily/records/deleteJson.html','manage_records_datagrid')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>批量删除</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_records_searchform', 'manage_records_datagrid');
        });

        function parse2(value) {
            return  value.toString().replace(/,/g,'<br>');

            // 设置缩进为2个空格
            // value = JSON.stringify(JSON.parse(value), null, 2);
            // value = value

            // value = value.replace(/,/g,'<br>');
            // return value.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
            //     var cls = 'number';
            //     if (/^"/.test(match)) {
            //         if (/:$/.test(match)) {
            //             cls = 'key';
            //         } else {
            //             cls = 'string';
            //         }
            //     } else if (/true|false/.test(match)) {
            //         cls = 'boolean';
            //     } else if (/null/.test(match)) {
            //         cls = 'null';
            //     }
            //
            //     return '<span class="' + cls + '">' + match.replace(/^.|.$/g, '') + '</span>';
        }
    </script>
</div>
