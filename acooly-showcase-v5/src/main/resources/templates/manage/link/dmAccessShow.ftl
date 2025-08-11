<style>
  .details-box {
    background: #ffffff;
    color: #333333;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    padding: 8px 10px;
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
    font-size: 12px;
    line-height: 1.6;
    max-height: 260px;
    overflow: auto;
    white-space: pre-wrap;
    word-break: break-word;
  }
</style>
<div class="card-body">
	<dl class="row">
		<dt class="col-sm-3">设备详情:</dt>
		<dd class="col-sm-9">
			<pre id="deviceDetailsContainer" class="details-box">${dmAccess.deviceDetails}</pre>
		</dd>
		<dt class="col-sm-3">客户端详情:</dt>
		<dd class="col-sm-9">
			<pre id="clientDetailsContainer" class="details-box">${dmAccess.clientDetails}</pre>
		</dd>
		<dt class="col-sm-3">虚拟机详情:</dt>
		<dd class="col-sm-9">
			<pre id="virtualDetailsContainer" class="details-box">${dmAccess.virtualDetails}</pre>
		</dd>
	</dl>
</div>
<script>
	$(function() {
    function tryFormatContent(rawText) {
      const text = (rawText || '').trim();
      if (!text) return '';
      // 优先尝试 JSON 美化
      try {
        const first = text[0];
        const last = text[text.length - 1];
        const looksLikeJson = (first === '{' && last === '}') || (first === '[' && last === ']');
        if (looksLikeJson) {
          return JSON.stringify(JSON.parse(text), null, 2);
        }
      } catch (e) { /* 忽略，走降级逻辑 */ }

      // 其次尝试 querystring 风格 a=1&b=2
      if (text.indexOf('&') > -1 && text.indexOf('=') > -1) {
        return text.split('&').map(item => item.trim()).join('\n');
      }

      // 最后按逗号分隔
      if (text.indexOf(',') > -1) {
        return text.split(',').map(item => item.trim()).join('\n');
      }

      return text;
    }

    ['#deviceDetailsContainer', '#clientDetailsContainer', '#virtualDetailsContainer'].forEach(sel => {
      const $el = $(sel);
      $el.text(tryFormatContent($el.text()));
    });
	});
</script>
