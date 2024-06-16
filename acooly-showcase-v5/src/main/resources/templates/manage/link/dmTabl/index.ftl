<div class="easyui-layout" data-options="fit : true,border : false">
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_clickUrl_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group" style="display: none">
                <input type="text" class="form-control form-control-sm" name="search_EQ_centerId" value='${k}' />
            </div>
            <div class="form-group">
                <label class="col-form-label">访问时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_clickUrl_searchform','manage_clickUrl_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <div data-options="region:'center',border:false">
        <table id="manage_clickUrl_datagrid" class="easyui-datagrid"
               url="/manage/link/dmClick/listClickUrl?centerId=${k}" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="createTime" formatter="dateTimeFormatter">点击时间</th>
                <#--        <th field="centerId" sortable="true" sum="true">数据中心id</th>-->
                <th field="ip" formatter="contentFormatter">Ip</th>
                <th field="region" formatter="contentFormatter">地区</th>
                <th field="clickDevice" formatter="clickDeviceFormatterFunction">点击设备</th>
                <th field="clickType" formatter="clickTypeFormatterFunction">点击类型</th>
                <#--        <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>-->
            </tr>
            </thead>
        </table>
    </div>


    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_clickUrl_searchform', 'manage_clickUrl_datagrid');
        });
    </script>
    <script>
        function clickDeviceFormatterFunction(value) {
            if (value == '1') {
                //居中显示
                return ' <i class="fa fa-desktop fa-fw fa-col"  style=" color: firebrick; align-items: center; "  />'
            } else {
                return '<i class="fa  fa-mobile-phone fa-fw fa-col" style=" color: cornflowerblue;align-items: center;" />'
            }
        }

        function clickTypeFormatterFunction(value) {
            if (value == '1') {
                return '旧点击访客<i class="fa fa-user-times fa-fw fa-col" style=" color: red; align-items: center;"  title="旧访客"/>'
            } else {
                return '新点击访客<i class="fa  fa-user-plus fa-fw fa-col" style=" color: green ;align-items: center;" title="新访客"/>'
            }
        }

    </script>
</div>