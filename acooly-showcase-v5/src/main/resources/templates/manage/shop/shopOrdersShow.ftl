<style>
    .order-info-card {
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        margin-bottom: 20px;
    }
    .order-info-header {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
        padding: 15px 20px;
        border-radius: 8px 8px 0 0;
        font-size: 16px;
        font-weight: 600;
    }
    .order-info-body {
        padding: 20px;
    }
    .order-info-item {
        display: flex;
        padding: 10px 0;
        border-bottom: 1px solid #f0f0f0;
    }
    .order-info-item:last-child {
        border-bottom: none;
    }
    .order-info-label {
        width: 140px;
        color: #666;
        font-weight: 500;
        flex-shrink: 0;
    }
    .order-info-value {
        flex: 1;
        color: #333;
    }
    .order-items-section {
        margin-top: 30px;
    }
    .order-items-header {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        color: #fff;
        padding: 15px 20px;
        border-radius: 8px 8px 0 0;
        font-size: 16px;
        font-weight: 600;
        margin-bottom: 0;
    }
    .order-items-body {
        padding: 20px;
        background: #fff;
        border-radius: 0 0 8px 8px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    .order-items-empty {
        text-align: center;
        padding: 40px 20px;
        color: #999;
    }
    .order-items-empty i {
        font-size: 48px;
        margin-bottom: 15px;
        display: block;
    }

    /* 商品图片样式 */
    .product-image {
        max-width: 60px;
        max-height: 60px;
        border-radius: 4px;
        object-fit: cover;
    }
</style>

<div class="order-info-card">
    <div class="order-info-header">
        <i class="fa fa-file-text-o"></i> 订单基本信息
    </div>
    <div class="order-info-body">
        <div class="order-info-item">
            <div class="order-info-label">订单ID:</div>
            <div class="order-info-value">${shopOrders.id!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">订单编号:</div>
            <div class="order-info-value">${shopOrders.orderId!''}</div>
        </div>
<#--        <div class="order-info-item">-->
<#--            <div class="order-info-label">用户ID:</div>-->
<#--            <div class="order-info-value">${shopOrders.userId!''}</div>-->
<#--        </div>-->
        <div class="order-info-item">
            <div class="order-info-label">订单总金额:</div>
            <div class="order-info-value" style="color: #f5576c; font-weight: 600; font-size: 16px;">
                $${(shopOrders.totalPrice?string('0.00'))!''}
            </div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">订单状态:</div>
            <div class="order-info-value">
                <span class="badge badge-info">${shopOrders.status!''}</span>
            </div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">支付方式:</div>
            <div class="order-info-value">${shopOrders.paymentMethod!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">收货地址:</div>
            <div class="order-info-value">${shopOrders.shippingAddress!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">收货城市:</div>
            <div class="order-info-value">${shopOrders.shippingCity!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">邮政编码:</div>
            <div class="order-info-value">${shopOrders.shippingZipCode!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">收货国家:</div>
            <div class="order-info-value">${shopOrders.shippingCountry!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">联系电话:</div>
            <div class="order-info-value">${shopOrders.contactPhone!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">创建时间:</div>
            <div class="order-info-value">${(shopOrders.createTime?string('yyyy-MM-dd HH:mm:ss'))!''}</div>
        </div>
        <div class="order-info-item">
            <div class="order-info-label">更新时间:</div>
            <div class="order-info-value">${(shopOrders.updateTime?string('yyyy-MM-dd HH:mm:ss'))!''}</div>
        </div>
    </div>
</div>

<!-- 订单商品明细 -->
<div class="order-items-section" style="margin-top: 30px;">
    <div class="order-items-header">
        <i class="fa fa-shopping-cart"></i> 订单商品明细
    </div>

    <div class="order-items-body" style="padding: 15px; background: #fff; border-radius: 0 0 8px 8px;">
        <#if orderItems?? && (orderItems?size > 0)>
            <table id="orderItems_datagrid" class="easyui-datagrid"
                   style="width:100%;height:400px;"
                   fit="false" border="false" fitColumns="false"
                   pagination="false" singleSelect="true" rownumbers="false">
                <thead>
                <tr>
                    <th data-options="field:'productImage',width:200,align:'center',formatter:imageFormatter">商品图片</th>
                    <th data-options="field:'productName',width:100,formatter:productNameFormatter">商品名称</th>
                    <th data-options="field:'productId',width:100,align:'center'">商品ID</th>
                    <th data-options="field:'quantity',width:80,align:'center'">数量</th>
                    <th data-options="field:'price',width:100,align:'right',formatter:priceFormatter">单价</th>
                    <th data-options="field:'totalPrice',width:120,align:'right',formatter:totalPriceFormatter">小计</th>
                </tr>
                </thead>
            </table>
        <#else>
            <div class="order-items-empty">
                <i class="fa fa-shopping-bag"></i>
                <div>该订单暂无商品明细</div>
            </div>
        </#if>
    </div>
</div>


<script type="text/javascript">
    $(function() {
        <#if orderItems ?? && (orderItems?size > 0)>
        var orderItemsData = [
            <#list orderItems as item>
            {
                id: ${item.id!0},
                productId: ${item.productId!0},
                productName: "${(item.productName!'')?js_string}",
                quantity: ${item.quantity!0},
                price: "${item.price!''}",
                totalPrice: "${item.totalPrice!''}",
                productImage: "${item.productImage}"
            }<#if item_has_next>,</#if>
            </#list>
        ];

        //  使用 loadData 加载数据（更稳）
        $('#orderItems_datagrid').datagrid('loadData', orderItemsData);
        </#if>
    });
    // 商品名称格式化
    function productNameFormatter(value, row) {
        if (!value) return '<span style="color:#9ca3af;">--</span>';
        return '<span title="' + value + '">' + value + '</span>';
    }

    // 单价格式化
    function priceFormatter(value) {
        if (!value || value == 0 || value == '') return '<span style="color:#9ca3af;">0.00</span>';
        var price = parseFloat(value).toFixed(2);
        return '<span style="color:#333;font-weight:500;">$' + price + '</span>';
    }

    // 总价格式化
    function totalPriceFormatter(value) {
        if (!value || value == 0 || value == '') return '<span style="color:#9ca3af;">0.00</span>';
        var totalPrice = parseFloat(value).toFixed(2);
        return '<span style="color:#f5576c;font-weight:600;font-size:14px;">$' + totalPrice + '</span>';
    }

// 商品图片格式化（预留）
    function imageFormatter(value) {
        if (!value || value === '') {
            return '<span style="color:#9ca3af;font-size:12px;"><i class="fa fa-image"></i> 暂无图片</span>';
        }
        return '<img src="' + value + '" class="product-image"/>';
    }
</script>
