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
                <th field="teamName" formatter="contentFormatter" width="120">团队名称</th>
                <th field="orderId">订单编号</th>
                <th field="userId" sortable="true" sum="true">用户ID</th>
                <th field="status" formatter="statusFormatter" width="120">支付状态</th>
                <th field="logisticsId" formatter="logisticsIdFormatter" width="150">物流单号</th>
                <th field="totalPrice" formatter="totalPriceFormatter" sortable="true" sum="true">订单总金额</th>
                <th field="couponId" formatter="couponIdFormatter">优惠码</th>
                <th field="shippingAddress" formatter="contentFormatter">收货地址</th>
                <th field="shippingCity">收货城市</th>
                <th field="shippingZipCode">邮政编码</th>
                <th field="shippingCountry">收货国家</th>
                <th field="contactPhone">联系电话</th>
                <th field="paymentMethod">支付方式</th>
                <th field="thirdPartyPaymentId">第三方支付ID</th>
<#--            <th field="cancelReason" formatter="contentFormatter">取消原因</th>-->
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
<#--              <button onclick="$.acooly.framework.remove('/manage/shop/shopOrders/deleteJson.html','{0}','manage_shopOrders_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>-->
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopOrders_toolbar">
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopOrders/create.html',entity:'shopOrders',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>-->
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopOrders/deleteJson.html','manage_shopOrders_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>-->
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopOrders_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_shopOrders_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopOrders/exportXls.html','manage_shopOrders_searchform','订单表')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopOrders/exportCsv.html','manage_shopOrders_searchform','订单表')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
<#--            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopOrders/importView.html',uploader:'manage_shopOrders_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>-->
        </div>
    </div>
    <style>
        /* 支付状态样式 */
        .status-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
            text-align: center;
            min-width: 70px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .status-pending {
            background: linear-gradient(135deg, #fbbf24, #f59e0b);
            color: #fff;
        }
        
        .status-processing {
            background: linear-gradient(135deg, #3b82f6, #2563eb);
            color: #fff;
        }
        
        .status-shipped {
            background: linear-gradient(135deg, #8b5cf6, #7c3aed);
            color: #fff;
        }
        
        .status-delivered {
            background: linear-gradient(135deg, #10b981, #059669);
            color: #fff;
        }
        
        .status-cancelled {
            background: linear-gradient(135deg, #6b7280, #4b5563);
            color: #fff;
        }
        
        .status-failed {
            background: linear-gradient(135deg, #ef4444, #dc2626);
            color: #fff;
        }
        
        .status-refunded {
            background: linear-gradient(135deg, #f97316, #ea580c);
            color: #fff;
        }
        
        /* 物流单号样式 */
        .logistics-number {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 6px 12px;
            background: linear-gradient(135deg, #f0f4ff, #e0e7ff);
            border: 1px solid #c7d2fe;
            border-radius: 8px;
            font-family: 'Courier New', monospace;
            font-size: 13px;
            font-weight: 600;
            color: #4f46e5;
            cursor: pointer;
            transition: all 0.3s;
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        
        .logistics-number:hover {
            background: linear-gradient(135deg, #e0e7ff, #c7d2fe);
            border-color: #818cf8;
            transform: translateY(-1px);
            box-shadow: 0 2px 8px rgba(79, 70, 229, 0.2);
        }
        
        .logistics-number .copy-icon {
            opacity: 0.6;
            font-size: 11px;
            transition: opacity 0.3s;
        }
        
        .logistics-number:hover .copy-icon {
            opacity: 1;
        }
        
        .logistics-empty {
            color: #9ca3af;
            font-size: 12px;
            font-style: italic;
        }
    </style>
    <script type="text/javascript">
        // 渲染订单总金额 - 直接格式化显示，保留两位小数
        function totalPriceFormatter(value) {
            if (value === null || value === undefined || value === '') return '0.00';
            // 直接格式化，保留两位小数（数据库存储的是元，不是分）
            var price = parseFloat(value).toFixed(2);
            return price;
        }
        
        /**
         * 支付状态格式化显示
         * value: 状态值（PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED, FAILED, REFUNDED）
         */
        function statusFormatter(value) {
            if (!value) return '<span class="status-badge status-pending">未知</span>';
            
            var statusMap = {
                'PENDING': { text: '待支付', icon: 'fa-clock-o', class: 'status-pending' },
                'PROCESSING': { text: '处理中', icon: 'fa-cog', class: 'status-processing' },
                'SHIPPED': { text: '已发货', icon: 'fa-truck', class: 'status-shipped' },
                'DELIVERED': { text: '已送达', icon: 'fa-check-circle', class: 'status-delivered' },
                'CANCELLED': { text: '已取消', icon: 'fa-times-circle', class: 'status-cancelled' },
                'FAILED': { text: '支付失败', icon: 'fa-exclamation-triangle', class: 'status-failed' },
                'REFUNDED': { text: '已退款', icon: 'fa-undo', class: 'status-refunded' }
            };
            
            var status = statusMap[value.toUpperCase()] || { text: value, icon: 'fa-question-circle', class: 'status-pending' };
            
            return '<span class="status-badge ' + status.class + '">' +
                   '<i class="fa ' + status.icon + ' fa-fw"></i> ' + status.text +
                   '</span>';
        }
        
        /**
         * 优惠券 ID 格式化显示（兼容 key 为数字或字符串）
         * value: couponId（可能是数字或字符串）
         */
        var couponIdToCodeMap = {
            <#if couponList?? && (couponList?size > 0)>
                <#list couponList as coupon>
                "${coupon.id}": "${coupon.code}"<#if coupon_has_next>,</#if>
                </#list>
            </#if>
        };

        function couponIdFormatter(value) {
            if (value === null || value === undefined || value === '') {
                return '<span style="color:#999;">未使用优惠券</span>';
            } else {
                return '<span style="color:#409EFF;">' + (couponIdToCodeMap[value] || '未知优惠券') + '</span>';
            }
        }
        
        /**
         * 物流单号格式化显示
         * value: 物流单号
         * row: 行数据
         */
        function logisticsIdFormatter(value, row) {
            if (!value || value.trim() === '') {
                return '<span class="logistics-empty"><i class="fa fa-minus-circle"></i> 暂无物流单号</span>';
            }
            
            var logisticsId = value.trim();
            var uniqueId = 'logistics_' + (row.id || Math.random().toString(36).substr(2, 9));
            
            return '<span class="logistics-number" onclick="copyLogisticsNumber(\'' + logisticsId + '\', \'' + uniqueId + '\')" ' +
                   'id="' + uniqueId + '" title="点击复制物流单号：' + logisticsId + '">' +
                   '<i class="fa fa-truck" style="color: #6366f1;"></i>' +
                   '<span class="logistics-text">' + logisticsId + '</span>' +
                   '<i class="fa fa-copy copy-icon" style="margin-left: 4px;"></i>' +
                   '</span>';
        }
        
        /**
         * 复制物流单号到剪贴板
         */
        function copyLogisticsNumber(logisticsId, elementId) {
            if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard.writeText(logisticsId).then(function() {
                    // 显示成功提示
                    var element = document.getElementById(elementId);
                    if (element) {
                        var originalHtml = element.innerHTML;
                        element.innerHTML = '<i class="fa fa-check" style="color: #10b981;"></i> <span>已复制</span>';
                        element.style.background = 'linear-gradient(135deg, #d1fae5, #a7f3d0)';
                        element.style.borderColor = '#10b981';
                        
                        setTimeout(function() {
                            element.innerHTML = originalHtml;
                            element.style.background = '';
                            element.style.borderColor = '';
                        }, 1500);
                    }
                    
                    // 使用框架的消息提示
                    if (typeof $.acooly !== 'undefined' && $.acooly.messager) {
                        $.acooly.messager.success('物流单号已复制：' + logisticsId);
                    } else if (typeof $.messager !== 'undefined') {
                        $.messager.show({
                            title: '成功',
                            msg: '物流单号已复制：' + logisticsId,
                            timeout: 2000,
                            showType: 'slide'
                        });
                    }
                }).catch(function(err) {
                    console.error('复制失败:', err);
                    if (typeof $.acooly !== 'undefined' && $.acooly.messager) {
                        $.acooly.messager.error('复制失败，请手动复制');
                    }
                });
            } else {
                // 降级方案：使用传统方法
                var textArea = document.createElement('textarea');
                textArea.value = logisticsId;
                textArea.style.position = 'fixed';
                textArea.style.opacity = '0';
                document.body.appendChild(textArea);
                textArea.select();
                
                try {
                    var successful = document.execCommand('copy');
                    if (successful) {
                        if (typeof $.acooly !== 'undefined' && $.acooly.messager) {
                            $.acooly.messager.success('物流单号已复制：' + logisticsId);
                        } else if (typeof $.messager !== 'undefined') {
                            $.messager.show({
                                title: '成功',
                                msg: '物流单号已复制：' + logisticsId,
                                timeout: 2000,
                                showType: 'slide'
                            });
                        }
                    } else {
                        if (typeof $.acooly !== 'undefined' && $.acooly.messager) {
                            $.acooly.messager.error('复制失败，请手动复制');
                        }
                    }
                } catch (err) {
                    console.error('复制失败:', err);
                    if (typeof $.acooly !== 'undefined' && $.acooly.messager) {
                        $.acooly.messager.error('复制失败，请手动复制');
                    }
                }
                
                document.body.removeChild(textArea);
            }
        }
        
        $(function () {
            $.acooly.framework.initPage('manage_shopOrders_searchform', 'manage_shopOrders_datagrid');
        });
    </script>
</div>
