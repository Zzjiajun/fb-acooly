<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">设备详情:</dt>
		<dd style="color: red; word-wrap: break-word;" class="col-sm-9">
			<pre id="deviceDetailsContainer">${dmAccess.deviceDetails}</pre>
		</dd>
		<dt class="col-sm-3">客户端详情:</dt>
		<dd style="color: red; word-wrap: break-word;" class="col-sm-9">
			<pre id="clientDetailsContainer">${dmAccess.clientDetails}</pre>
		</dd>
	</dl>
</div>
<script>
	$(function() {
		// 格式化设备详情
		try {
			const deviceContainer = $('#deviceDetailsContainer');
			const deviceContent = deviceContainer.text();
			// console.log('原始设备详情内容:', deviceContent);
			
			// 直接处理字符串
			const formattedDeviceContent = deviceContent
				.split(',')
				.map(item => item.trim())
				.join('\n');
			deviceContainer.text(formattedDeviceContent);
		} catch (error) {
			console.error('格式化设备详情时出错:', error);
		}

		// 格式化客户端详情
		try {
			const clientContainer = $('#clientDetailsContainer');
			const clientContent = clientContainer.text();
			// console.log('原始客户端详情内容:', clientContent);
			
			// 直接处理字符串
			const formattedClientContent = clientContent
				.split(',')
				.map(item => item.trim())
				.join('\n');
			clientContainer.text(formattedClientContent);
		} catch (error) {
			console.error('格式化客户端详情时出错:', error);
		}
	});
</script>
