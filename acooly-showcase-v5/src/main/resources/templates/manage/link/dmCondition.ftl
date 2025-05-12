<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_dmCondition_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">账号名：</label>
                <select name="search_EQ_userName" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list map1Name as k,v >
                        <option value="${k}">${k}:${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">访问域名：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_accessAddress"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">国家：</label>
                <select name="search_EQ_ipCountry" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list ipMap as k,v >
                        <option value="${v}">${k}</option>
                    </#list>
                </select>
            </div>
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
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_dmCondition_searchform','manage_dmCondition_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_dmCondition_datagrid" class="easyui-datagrid" url="/manage/link/dmCondition/listJson.html" toolbar="#manage_dmCondition_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >id</th>
                <th field="userName" formatter="contentFormatter">用户名</th>
                <th field="accessAddress" style="width: 25%"  formatter="contidFunction">访问域名</th>
                <th field="isIp" formatter="statusFunction">IP防护</th>
                <th field="ipCountry" formatter="isIpFormatter">IP国家</th>
                <th field="timeZone" formatter="statusFunction">是否添加时区</th>
                <th field="timeContinent" formatter="conFormatter">时区洲</th>
                <th field="isChinese" formatter="statusFunction">设备中文语言</th>
<#--                <th field="language" formatter="contentFormatter">设备语言</th>-->
                <th field="isMobile" formatter="statusFunction">移动设备</th>
                <th field="isSpecificDevice" formatter="statusFunction">特定设备低端机</th>
                <th field="isVpn" formatter="statusFunction">VPN和代理检测</th>
                <th field="ipWhite" formatter="statusFunction">白名单开关</th>
                <th field="ipLimits" formatter="statusFunction">ip点击限制</th>
                <th field="isRobot" formatter="statusFunction">爬虫机器人</th>
                <th field="isIdentify" formatter="statusFunction">无法识别设备</th>
                <th field="iosVersion" formatter="contentFormatter">ios系统版本</th>
                <th field="andVersion" formatter="contentFormatter">安卓系统</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_dmCondition_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_dmCondition_action" style="display: none;">
            <div class="btn-group btn-group-xs">
<#--              <button onclick="$.acooly.framework.show('/manage/link/dmCondition/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>-->
              <button id="myCondition_edit" onclick="$.acooly.framework.edit({url:'/manage/link/dmCondition/edit.html',id:'{0}',entity:'dmCondition',width:700,height:600});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
<#--              <button onclick="$.acooly.framework.remove('/manage/link/dmCondition/deleteJson.html','{0}','manage_dmCondition_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>-->
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_dmCondition_toolbar">
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/link/dmCondition/create.html',entity:'dmCondition',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>-->
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/link/dmCondition/deleteJson.html','manage_dmCondition_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>-->
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_dmCondition_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_dmCondition_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/link/dmCondition/exportXls.html','manage_dmCondition_searchform','dm_condition')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/link/dmCondition/exportCsv.html','manage_dmCondition_searchform','dm_condition')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/link/dmCondition/importView.html',uploader:'manage_dmCondition_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>-->
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_dmCondition_searchform', 'manage_dmCondition_datagrid');
        });


        // $(document).ready(function() {
        //     $('#myCondition_edit').hide();
        //
        //     // 发送AJAX请求到后端接口
        //     $.ajax({
        //         url: '/manage/showcase/daily/records/centerPermissions.html',
        //         type: 'GET',
        //         dataType: 'json',
        //         success: function(data) {
        //             // 根据返回的数据控制<a>标签的显示
        //             if (data.length>0) {
        //                 $('#myCondition_edit').hide();
        //             } else {
        //                 $('#myCondition_edit').show();
        //             }
        //         },
        //         error: function() {
        //             console.log('请求失败');
        //         },
        //     });
        // });

        function statusFunction (value) {
            if (value == 1) {
                return '<span class="fa fa-check-square fa-fw fa-col" style="color: green">开启</span>';
            }else {
                return '<span class="fa  fa-remove fa-fw fa-col" style="color: red">关闭</span>';
            }
        }

        var contMap = new Map();
        <#list conMap as k,v>
        contMap.set('${v}', '${k}');
        console.log('${v}', '${k}')
        </#list>

        function conFormatter (value) {
            return contMap.get(value);
        }

        var iptMap = new Map();
        <#list ipMap as k,v>
        iptMap.set('${v}', '${k}');
        console.log('${v}', '${k}')
        </#list>

        function isIpFormatter (value) {
            return iptMap.get(value);
        }



        function contidFunction(value){
           return "<button   class='btn btn-link expandable'>" + value+ "</button>";

        }
    </script>
</div>
