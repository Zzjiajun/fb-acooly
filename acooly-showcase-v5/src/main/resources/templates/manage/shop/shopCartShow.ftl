<style>
	/* 购物车基本信息卡片 */
	.cart-info-card {
		background: #fff;
		border-radius: 8px;
		box-shadow: 0 2px 8px rgba(0,0,0,0.1);
		margin-bottom: 20px;
	}
	.cart-info-header {
		background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
		color: #fff;
		padding: 15px 20px;
		border-radius: 8px 8px 0 0;
		font-size: 16px;
		font-weight: 600;
	}
	.cart-info-body {
		padding: 20px;
	}
	.cart-info-item {
		display: flex;
		padding: 10px 0;
		border-bottom: 1px solid #f0f0f0;
	}
	.cart-info-item:last-child {
		border-bottom: none;
	}
	.cart-info-label {
		width: 140px;
		color: #666;
		font-weight: 500;
		flex-shrink: 0;
	}
	.cart-info-value {
		flex: 1;
		color: #333;
	}

	/* 购物车商品明细区域 */
	.cart-items-section {
		margin-top: 30px;
	}
	.cart-items-header {
		background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
		color: #fff;
		padding: 15px 20px;
		border-radius: 8px 8px 0 0;
		font-size: 16px;
		font-weight: 600;
		margin-bottom: 0;
	}
	.cart-items-body {
		padding: 20px;
		background: #fff;
		border-radius: 0 0 8px 8px;
		box-shadow: 0 2px 8px rgba(0,0,0,0.1);
	}
	.cart-items-empty {
		text-align: center;
		padding: 40px 20px;
		color: #999;
	}
	.cart-items-empty i {
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

<!-- 购物车基本信息卡片 -->
<div class="cart-info-card">
	<div class="cart-info-header">
		<i class="fa fa-shopping-cart"></i> 购物车基本信息
	</div>
	<div class="cart-info-body">
<#--		<div class="cart-info-item">-->
<#--			<div class="cart-info-label">购物车ID:</div>-->
<#--			<div class="cart-info-value">${shopCart.id!''}</div>-->
<#--		</div>-->
<#--		<div class="cart-info-item">-->
<#--			<div class="cart-info-label">用户ID:</div>-->
<#--			<div class="cart-info-value">${shopCart.userId!''}</div>-->
<#--		</div>-->
		<div class="cart-info-item">
			<div class="cart-info-label">购物车总金额:</div>
			<div class="cart-info-value" style="color: #f5576c; font-weight: 600; font-size: 16px;">
				$${shopCart.totalPrice}
			</div>
		</div>
	</div>
</div>

<!-- 购物车商品明细 -->
<div class="cart-items-section">
	<div class="cart-items-header">
		<i class="fa fa-list"></i> 购物车商品明细
	</div>
	<div class="cart-items-body">
		<#if shopCartItemDtos?? && shopCartItemDtos?size gt 0>
			<table id="cartItems_datagrid" style="width:100%;height:400px;"></table>
		<#else>
			<div class="cart-items-empty">
				<i class="fa fa-shopping-bag"></i>
				<div>购物车暂无商品</div>
			</div>
		</#if>
	</div>
</div>

<script type="text/javascript">
	$(function () {
		<#if shopCartItemDtos?? && shopCartItemDtos?size gt 0>

		// 通过 ?js_string 防止注入
		var cartItemsData = [
			<#list shopCartItemDtos as item>
			{
				id: ${item.id!0},
				cartId: ${item.cartId!0},
				userId: ${item.userId!0},
				productName: "${(item.productName!'')?js_string}",
				quantity: ${item.quantity!0},
				price: ${item.price!0},
				totalPrice: ${item.totalPrice!0},
				productImage: "${(item.productImage!'')?js_string}",
				createTime: "${item.createTime?string('yyyy-MM-dd HH:mm:ss')!''}"
			}<#if item_has_next>,</#if>
			</#list>
		];

		// 初始化 datagrid
		$('#cartItems_datagrid').datagrid({
			border: false,
			fitColumns: false,
			pagination: false,
			singleSelect: true,
			rownumbers: false,
			data: cartItemsData,
			columns: [[
				{field: 'productImage', title: '商品图片', width: 120, align: 'center', formatter: cartImageFormatter},
				{field: 'productName', title: '商品名称', width: 200, formatter: cartProductNameFormatter},
				{field: 'quantity', title: '数量', width: 80, align: 'center', formatter: quantityFormatter},
				{field: 'price', title: '单价', width: 100, align: 'right', formatter: cartPriceFormatter},
				{field: 'totalPrice', title: '小计', width: 120, align: 'right', formatter: cartTotalPriceFormatter},
				{field: 'createTime', title: '添加时间', width: 150, align: 'center'}
			]]
		});

		</#if>
	});

	// 商品图片格式化
	function cartImageFormatter(value) {
		// if (!value || value === '') {
		// 	return '<span style="color:#9ca3af;font-size:12px;"><i class="fa fa-image"></i> 暂无图片</span>';
		// }
		return '<img src="' + value + '" class="product-image"/>';
	}

	// 商品名称格式化
	function cartProductNameFormatter(value, row) {
		// if (!value || value === '') {
		// 	return '<span style="color:#9ca3af;">商品ID: ' + (row.productId || '--') + '</span>';
		// }
		return '<span title="' + value + '">' + value + '</span>';
	}

	// 数量格式化
	function quantityFormatter(value) {
		if (!value || value == 0) {
			return '<span style="color:#9ca3af;">0</span>';
		}
		return '<span style="color:#333;font-weight:500;">' + value + '</span>';
	}

	// 单价格式化
	function cartPriceFormatter(value) {
		if (!value || value == 0) {
			return '<span style="color:#9ca3af;">0.00</span>';
		}
		var price = parseFloat(value).toFixed(2);
		return '<span style="color:#333;font-weight:500;">$' + price + '</span>';
	}

	// 小计格式化
	function cartTotalPriceFormatter(value) {
		if (!value || value == 0) {
			return '<span style="color:#9ca3af;">0.00</span>';
		}
		var totalPrice = parseFloat(value).toFixed(2);
		return '<span style="color:#f5576c;font-weight:600;font-size:14px;">$' + totalPrice + '</span>';
	}
</script>