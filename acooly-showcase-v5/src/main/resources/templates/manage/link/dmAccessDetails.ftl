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
    overflow-y: auto;
    word-break: break-word;
  }
  .details-box-danger {
    background: #fff7f7;
    border-color: #fecaca;
  }
  .details-list { list-style: none; margin: 0; padding: 0; }
  .details-list li { margin-bottom: 2px; display: flex; align-items: flex-start; }
  .details-icon { color: #d9534f; margin-right: 6px; line-height: 1.4; }
</style>
<div class="card-body">
    <dl class="row">
        <dt class="col-sm-3">失败明细:</dt>
        <dd class="col-sm-9">
            <ul id="detailsContainerAccess" class="details-list details-box details-box-danger"></ul>
        </dd>
    </dl>
</div>
<script>
    $(function() {
        function toLines(rawText) {
            const text = (rawText || '').trim();
            if (!text) return [];
            // JSON 数组/对象
            try {
                const first = text[0];
                const last = text[text.length - 1];
                const looksLikeJson = (first === '{' && last === '}') || (first === '[' && last === ']');
                if (looksLikeJson) {
                    const parsed = JSON.parse(text);
                    if (Array.isArray(parsed)) {
                        return parsed.map(String);
                    }
                    if (parsed && typeof parsed === 'object') {
                        return Object.keys(parsed).map(k => k + ': ' + String(parsed[k]));
                    }
                }
            } catch (e) { /* ignore */ }
            // querystring a=1&b=2
            if (text.indexOf('&') > -1 && text.indexOf('=') > -1) {
                return text.split('&').map(s => s.trim()).filter(Boolean);
            }
            // 逗号分隔
            if (text.indexOf(',') > -1) {
                return text.split(',').map(s => s.trim()).filter(Boolean);
            }
            return [text];
        }

        var raw = '${dmAccess.details?js_string}';
        var lines = toLines(raw);
        var $list = $('#detailsContainerAccess');
        if (lines.length === 0) {
            $list.append('<li style="color:#999;">无失败明细</li>');
            return;
        }
        lines.forEach(function(line) {
            var safe = $('<div>').text(line).html();
            $list.append('<li><i class="fa fa-times-circle details-icon"></i><span>' + safe + '</span></li>');
        });
    });
</script>