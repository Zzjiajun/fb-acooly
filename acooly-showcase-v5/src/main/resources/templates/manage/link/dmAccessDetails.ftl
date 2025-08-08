<div class="card-body">
    <dl class="row">
        <dt class="col-sm-3">失败明细:</dt>
        <dd class="col-sm-9">
            <pre id="detailsContainerAccess" style="padding-left: 18px; color: #d9534f; background: #fff3f3; border-left: 3px solid #d9534f; border-radius: 4px; min-height: 28px; margin-bottom: 0;">${dmAccess.details}</pre>
        </dd>
    </dl>
</div>
<script>
    $(function() {
        try {
            const deviceContainer = $('#detailsContainerAccess');
            const deviceContent = deviceContainer.text();
            if (deviceContent) {
                const items = deviceContent.split(',').map(item => item.trim()).filter(Boolean);
                deviceContainer.empty();
                items.forEach(function(item) {
                    deviceContainer.append(
                        '<li style="margin-bottom:2px;list-style:none;"><i class="fa fa-times-circle" style="color:#d9534f;margin-right:4px;"></i>' +
                        $('<div>').text(item).html() + '</li>'
                    );
                });
            } else {
                deviceContainer.append('<li style="color:#aaa;">无失败明细</li>');
            }
        } catch (error) {
            console.error('格式化失败明细时出错:', error);
        }
    });
</script>