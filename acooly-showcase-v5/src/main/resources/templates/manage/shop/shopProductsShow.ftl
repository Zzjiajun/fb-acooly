<style>
	.product-show-container {
		background: #fff;
		border-radius: 12px;
		padding: 30px;
		box-shadow: 0 2px 8px rgba(0,0,0,0.08);
	}
	.product-image-wrapper {
		text-align: center;
		margin-bottom: 20px;
		padding: 10px;
		background: #fafafa;
		border-radius: 12px;
	}
	.product-image-wrapper img {
		max-width: 100%;
		max-height: 300px;
		width: auto;
		height: auto;
		border-radius: 12px;
		box-shadow: 0 4px 12px rgba(0,0,0,0.15);
		object-fit: contain;
	}
	.product-info-item {
		margin-bottom: 20px;
		padding-bottom: 20px;
		border-bottom: 1px solid #f0f0f0;
	}
	.product-info-item:last-child {
		border-bottom: none;
	}
	.product-info-label {
		font-weight: 600;
		color: #606266;
		font-size: 14px;
		margin-bottom: 8px;
		display: flex;
		align-items: center;
	}
	.product-info-label i {
		margin-right: 8px;
		color: #409eff;
	}
	.product-info-value {
		color: #303133;
		font-size: 15px;
		line-height: 1.6;
		word-break: break-word;
	}
	.product-name {
		font-size: 24px;
		font-weight: 600;
		color: #303133;
		line-height: 1.5;
	}
	.product-serial {
		font-family: 'Courier New', monospace;
		font-size: 16px;
		color: #409eff;
		background: #f0f7ff;
		padding: 8px 16px;
		border-radius: 6px;
		display: inline-block;
	}
	.product-brand {
		font-size: 16px;
		color: #67c23a;
		font-weight: 500;
	}
	.product-description {
		color: #606266;
		line-height: 1.8;
		white-space: pre-wrap;
	}
</style>
<div class="product-show-container">
	<!-- 商品信息 -->
	<div>
		<!-- 商品名称 -->
		<div class="product-info-item">
			<div class="product-info-label">
				<i class="fa fa-tag"></i> 商品名称
			</div>
			<div class="product-info-value product-name">
				${shopProducts.name!''}
			</div>
		</div>
		
		<!-- 商品编号 -->
		<div class="product-info-item">
			<div class="product-info-label">
				<i class="fa fa-barcode"></i> 商品编号
			</div>
			<div class="product-info-value">
				<span class="product-serial">${shopProducts.serialNumber!''}</span>
			</div>
		</div>

		<!-- 商品图片 -->
		<div class="product-image-wrapper">
			<#if shopProducts.imageUrl?? && shopProducts.imageUrl != ''>
				<#assign imageUrl = shopProducts.imageUrl>
				<#if !imageUrl?starts_with("http://") && !imageUrl?starts_with("https://")>
					<#assign imageUrl = "https://ltbotstk.com" + imageUrl>
				</#if>
				<img src="${imageUrl}" alt="${shopProducts.name!''}" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'400\' height=\'300\'%3E%3Crect fill=\'%23f0f0f0\' width=\'400\' height=\'300\'/%3E%3Ctext x=\'50%25\' y=\'50%25\' text-anchor=\'middle\' dy=\'.3em\' fill=\'%23999\'%3E暂无图片%3C/text%3E%3C/svg%3E';" />
			<#else>
				<div style="width:100%;height:300px;background:#f5f7fa;border-radius:12px;display:flex;align-items:center;justify-content:center;color:#909399;">
					<i class="fa fa-image" style="font-size:48px;"></i>
				</div>
			</#if>
		</div>
		
		<!-- 品牌 -->
		<div class="product-info-item">
			<div class="product-info-label">
				<i class="fa fa-certificate"></i> 品牌
			</div>
			<div class="product-info-value">
				<#if shopProducts.brandId?? && brandMap??>
					<#assign brandName = brandMap[shopProducts.brandId?string]!''>
					<#if brandName != ''>
						<span class="product-brand"><i class="fa fa-tag"></i> ${brandName}</span>
					<#else>
						<span style="color:#909399;">未设置品牌</span>
					</#if>
				<#else>
					<span style="color:#909399;">未设置品牌</span>
				</#if>
			</div>
		</div>
		
		<!-- 商品描述 -->
<#--		<div class="product-info-item">-->
<#--			<div class="product-info-label">-->
<#--				<i class="fa fa-file-text-o"></i> 商品描述-->
<#--			</div>-->
<#--			<div class="product-info-value product-description">-->
<#--				${shopProducts.description!''}-->
<#--			</div>-->
<#--		</div>-->
	</div>
</div>
