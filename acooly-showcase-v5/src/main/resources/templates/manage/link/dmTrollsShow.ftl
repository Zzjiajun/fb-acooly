<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">失败明细:</dt>
		<dd style="color: red; word-wrap: break-word;" class="col-sm-9">
			<pre id="detailsContainer">${dmTrolls.details}</pre>
		</dd>
	</dl>
</div>
<script>
	$(function() {
		try {
			const deviceContainer = $('#detailsContainer');
			const deviceContent = deviceContainer.text();
			// console.log('原始设备详情内容:', deviceContent);

			// 直接处理字符串
			const formattedDeviceContent = deviceContent
					.split(',')
					.map(item => item.trim())
					.join('\n');
			deviceContainer.text(formattedDeviceContent);
		} catch (error) {
			console.error('格式化JSON时出错:', error);
		}
	});
</script>
