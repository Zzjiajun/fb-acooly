<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    /* 使用Bootstrap 5的徽章样式 - 放大版本 */
    .badge {
        padding: 0.6em 1.2em; /* 增加内边距 */
        font-size: 12px; /* 增加字体大小 */
        font-weight: 400; /* 加粗文字 */
        border-radius: 0.2rem; /* 增加圆角 */
        transition: all 0.2s ease;
        min-width: 60px; /* 设置最小宽度 */
        text-align: center;
    }

    .badge-success {
        background-color: #28a745;
        color: white;
        border: 1px solid #218838; /* 增加边框宽度 */
        box-shadow: 0 2px 4px rgba(40, 167, 69, 0.3); /* 添加阴影 */
    }

    .badge-warning {
        background-color: #ffc107;
        color: #212529;
        border: 2px solid #e0a800;
        box-shadow: 0 2px 4px rgba(255, 193, 7, 0.3);
    }

    .badge-danger {
        background-color: #dc3545;
        color: white;
        border: 2px solid #c82333;
        box-shadow: 0 2px 4px rgba(220, 53, 69, 0.3);
    }

    .badge-secondary {
        background-color: #6c757d;
        color: white;
        border: 2px solid #545b62;
        box-shadow: 0 2px 4px rgba(108, 117, 125, 0.3);
    }

    /* 悬停效果 */
    .badge:hover {
        transform: scale(1.08); /* 增加缩放比例 */
        box-shadow: 0 4px 12px rgba(0,0,0,0.15); /* 增加阴影强度 */
        z-index: 1; /* 提高层级 */
    }

    /* 响应式调整 */
    @media (max-width: 768px) {
        .badge {
            padding: 0.4em 0.8em; /* 移动端缩小内边距 */
            font-size: 12px; /* 移动端缩小字体 */
            min-width: 60px; /* 移动端缩小最小宽度 */
        }
    }
</style>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_shopCoupon_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">优惠券名称:</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_code"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">状态</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_status"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">有效期开始：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_validFrom" name="search_GTE_validFrom" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_validFrom" name="search_LTE_validFrom" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <label class="col-form-label">有效期结束：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_validTo" name="search_GTE_validTo" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_validTo" name="search_LTE_validTo" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_shopCoupon_searchform','manage_shopCoupon_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_shopCoupon_datagrid" class="easyui-datagrid" url="/manage/shop/shopCoupon/listJson.html" toolbar="#manage_shopCoupon_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" >id</th>
                <th field="code" formatter="CodeFunction">优惠劵码</th>
                <th field="discountType" formatter="discountTypeFunction">优惠方式</th>
                <th field="discountValue" formatter="discountValueFunction">优惠力度</th>
                <th field="totalUses" formatter="totalUsesFunction">全局使用总次数</th>
                <th field="usedCount" sortable="true" sum="true">已被使用次数</th>
                <th field="perUserLimit" formatter="totalUsesFunction">单用户的次数</th>
                <th field="validFrom" formatter="dateTimeFormatter">有效期开始</th>
                <th field="validTo" formatter="dateTimeFormatter">有效期结束</th>
                <th field="createdBy">创建用户</th>
                <th field="status" formatter="statusOptionFunction">状态</th>
                <th field="paySuccessCount" formatter="payAmountWithIconFormatter">支付成功总金额</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
<#--                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>-->
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_shopCoupon_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopCoupon_action" style="display: none;">
            <div class="btn-group btn-group-xs">
<#--              <button onclick="$.acooly.framework.show('/manage/shop/shopCoupon/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>-->
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopCoupon/edit.html',id:'{0}',entity:'shopCoupon',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
<#--              <button onclick="$.acooly.framework.remove('/manage/shop/shopCoupon/deleteJson.html','{0}','manage_shopCoupon_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>-->
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopCoupon_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopCoupon/create.html',entity:'shopCoupon',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopCoupon/deleteJson.html','manage_shopCoupon_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>-->
<#--            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopCoupon_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>-->
<#--            <div id="manage_shopCoupon_exports_menu" style="width:150px;">-->
<#--              <div onclick="$.acooly.framework.exports('/manage/shop/shopCoupon/exportXls.html','manage_shopCoupon_searchform','shop_coupon')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>-->
<#--              <div onclick="$.acooly.framework.exports('/manage/shop/shopCoupon/exportCsv.html','manage_shopCoupon_searchform','shop_coupon')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>-->
<#--            </div>-->
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopCoupon/importView.html',uploader:'manage_shopCoupon_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>-->
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopCoupon_searchform', 'manage_shopCoupon_datagrid');
        });

        function CodeFunction(value, row) {
            return "<div style='text-align: center;'><button onclick='copyToCode("+JSON.stringify(row)+")' " +
                "title='复制优惠券码到剪贴板'  class='layui-btn layui-btn-radius'>" + value  + "</button></div>";
        }

        function copyToCode(row) {
            navigator.clipboard.writeText(row.code).then(() => {
                $.messager.alert('提示', '复制成功：' + row.code);
            }).catch(err => {
                $.messager.alert('错误', '复制失败：' + err);
            });
        }

        function statusOptionFunction(value, row) {
            if (!value) return '';
            var cls = '';
            var text = '';

            switch(value) {
                case 'ACTIVE':
                    cls = 'badge badge-success';
                    text = '启用';
                    break;
                case 'INACTIVE':
                    cls = 'badge badge-secondary';
                    text = '停用';
                    break;
                case 'EXPIRED':
                    cls = 'badge badge-danger';
                    text = '过期';
                    break;
                default:
                    cls = 'badge badge-warning';
                    text = '未知';
            }

            return "<span class='" + cls + "'>" + text + "</span>";
        }


        function discountTypeFunction(value) {
            if (!value) return '';
            var cls = '';
            var text = '';

            switch(value) {
                case 'percent':
                    cls = 'badge badge-success';
                    text = '打折';
                    break;
                case 'amount':
                    cls = 'badge badge-info';
                    text = '满减';
                    break;
                default:
                    cls = 'badge badge-secondary';
                    text = '其他';
            }

            return "<span class='" + cls + "'>" + text + "</span>";
        }

        function discountValueFunction(value, row) {
            if (!value) return '';
            if (row.discountType === 'percent') {
                return "<span class='badge badge-success'>" + value*100 + "%</span>";
            }
            return value;
        }

        function totalUsesFunction(value, row) {
            if (value =="0"){
                return "<span class='badge badge-success'>无限制</span>";
            }else{
                return "<span class='badge badge-success'>" + value + " 次</span>";
            }
        }

        function payAmountWithIconFormatter(value, row, index) {
            if (!value) return '<span class="text-muted">$0.00</span>';
            // 根据金额大小显示不同颜色
            var amount = parseFloat(value);
            var colorClass = '';
            if (amount >= 10000) {
                colorClass = 'text-success font-weight-bold';
            } else if (amount >= 5000) {
                colorClass = 'text-primary font-weight-bold';
            } else if (amount >= 1000) {
                colorClass = 'text-info';
            } else {
                colorClass = 'text-secondary';
            }
            return '<i class="fa fa-dollar-sign text-success mr-1"></i>' +
                '<span class="' + colorClass + '">' + amount.toFixed(2) + '</span>';
        }
    </script>
</div>
