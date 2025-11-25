<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_shopOrders_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">订单编号：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_orderId"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createdAt" name="search_GTE_createdAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createdAt" name="search_LTE_createdAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <label class="col-form-label">更新时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updatedAt" name="search_GTE_updatedAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updatedAt" name="search_LTE_updatedAt" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_shopOrders_searchform','manage_shopOrders_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_shopOrders_datagrid" class="easyui-datagrid" url="/manage/shop/shopOrders/listJson.html" toolbar="#manage_shopOrders_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >订单ID</th>
                <th field="orderId">订单编号</th>
                <th field="userId" sortable="true" sum="true">用户ID</th>
                <th field="totalPrice" formatter="totalPriceFormatter" sortable="true" sum="true">订单总金额</th>
                <th field="couponId" formatter="couponIdFormatter">优惠码</th>
                <th field="shippingAddress" formatter="contentFormatter">收货地址</th>
                <th field="shippingCity">收货城市</th>
                <th field="shippingZipCode">邮政编码</th>
                <th field="shippingCountry">收货国家</th>
                <th field="contactPhone">联系电话</th>
                <th field="paymentMethod">支付方式</th>
                <th field="thirdPartyPaymentId">第三方支付ID</th>
<#--                <th field="cancelReason" formatter="contentFormatter">取消原因</th>-->
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_shopOrders_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopOrders_action" style="display: none;">
            <div class="btn-group btn-group-xs">
              <button onclick="$.acooly.framework.show('/manage/shop/shopOrders/show.html?id={0}',900,700);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopOrders/edit.html',id:'{0}',entity:'shopOrders',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
              <button onclick="$.acooly.framework.remove('/manage/shop/shopOrders/deleteJson.html','{0}','manage_shopOrders_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopOrders_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopOrders/create.html',entity:'shopOrders',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopOrders/deleteJson.html','manage_shopOrders_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopOrders_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_shopOrders_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopOrders/exportXls.html','manage_shopOrders_searchform','订单表')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopOrders/exportCsv.html','manage_shopOrders_searchform','订单表')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopOrders/importView.html',uploader:'manage_shopOrders_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopOrders_searchform', 'manage_shopOrders_datagrid');
        });

        // 渲染订单总金额 - 直接格式化显示，保留两位小数
        function totalPriceFormatter(value) {
            if (value === null || value === undefined || value === '') return '0.00';
            // 直接格式化，保留两位小数（数据库存储的是元，不是分）
            var price = parseFloat(value).toFixed(2);
            return price;
        }
        /**
         * 优惠券 ID 格式化显示（兼容 key 为数字或字符串）
         * value: couponId（可能是数字或字符串）
         */
        var couponIdToCodeMap = {
                <#list couponList as coupon>
                "${coupon.id}": "${coupon.code}",
            <#if coupon_has_next>,</#if>
            </#list>
            };

        function couponIdFormatter(value) {
            if (value === null || value === undefined || value === '') {
                return '<span style="color:#999;">未使用优惠券</span>';
            } else {
                return '<span style="color:#409EFF;">' + (couponIdToCodeMap[value] || '未知优惠券') + '</span>';
            }
        }
    </script>
</div>
