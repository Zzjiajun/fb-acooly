<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopOrders_editform" class="form-horizontal" action="/manage/shop/shopOrders/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopOrders" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<!-- 支付状态选择 -->
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">支付状态</label>
				<div class="col-sm-9">
					<select name="status" id="statusSelect" class="form-control select2bs4" required>
						<option value="">请选择支付状态</option>
						<option value="PENDING" <#if shopOrders.status?? && shopOrders.status == "PENDING">selected</#if>>待支付</option>
						<option value="PROCESSING" <#if shopOrders.status?? && shopOrders.status == "PROCESSING">selected</#if>>处理中（已支付，正在准备发货）</option>
						<option value="SHIPPED" <#if shopOrders.status?? && shopOrders.status == "SHIPPED">selected</#if>>已发货</option>
						<option value="DELIVERED" <#if shopOrders.status?? && shopOrders.status == "DELIVERED">selected</#if>>已送达</option>
						<option value="CANCELLED" <#if shopOrders.status?? && shopOrders.status == "CANCELLED">selected</#if>>已取消</option>
						<option value="FAILED" <#if shopOrders.status?? && shopOrders.status == "FAILED">selected</#if>>支付失败</option>
<#--						<option value="REFUNDED" <#if shopOrders.status?? && shopOrders.status == "REFUNDED">selected</#if>>已退款</option>-->
					</select>
					<span style="color: #6c757d; font-size: 12px; display: block; margin-top: 8px;">
						<i class="fa fa-info-circle"></i> 请根据订单实际状态选择对应的支付状态
					</span>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">物流单号</label>
				<div class="col-sm-9">
					<input type="text" name="logisticsId" placeholder="请输入物流单号" class="easyui-validatebox form-control"  data-options="validType:['text','length[1,50]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">收货地址</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入收货地址..." name="shippingAddress" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">收货城市</label>
				<div class="col-sm-9">
					<input type="text" name="shippingCity" placeholder="请输入收货城市..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,100]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">邮政编码</label>
				<div class="col-sm-9">
					<input type="text" name="shippingZipCode" placeholder="请输入邮政编码..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,20]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">收货国家</label>
				<div class="col-sm-9">
					<input type="text" name="shippingCountry" placeholder="请输入收货国家..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,100]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">联系电话</label>
				<div class="col-sm-9">
					<input type="text" name="contactPhone" placeholder="请输入联系电话..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,20]']"/>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
<style>
    /* 编辑页面样式优化 */
    .card-body {
        padding: 20px;
    }
    
    .form-group {
        margin-bottom: 20px;
    }
    
    .form-control {
        border-radius: 8px;
        border: 1px solid #d1d5db;
        padding: 8px 12px;
        transition: all 0.3s;
    }
    
    .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
        outline: none;
    }
    
    .select2bs4 {
        width: 100% !important;
    }
    
    .select2-container--bootstrap4 .select2-selection {
        border-radius: 8px;
        border: 1px solid #d1d5db;
        min-height: 38px;
    }
    
    .select2-container--bootstrap4 .select2-selection:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
    }
</style>
<script>
    $(document).ready(function() {
        // 初始化select2
        $('.select2bs4').select2({
            theme: 'bootstrap4',
            placeholder: "请选择",
            allowClear: true,
            width: '100%',
            templateResult: formatStatusOption,
            templateSelection: formatStatusSelection
        });
        
        // 格式化下拉选项显示（带图标）
        function formatStatusOption(option) {
            if (!option.id) {
                return option.text;
            }
            
            var statusMap = {
                'PENDING': { text: '待支付', icon: 'fa-clock-o', color: '#f59e0b' },
                'PROCESSING': { text: '处理中（已支付，正在准备发货）', icon: 'fa-cog', color: '#2563eb' },
                'SHIPPED': { text: '已发货', icon: 'fa-truck', color: '#7c3aed' },
                'DELIVERED': { text: '已送达', icon: 'fa-check-circle', color: '#059669' },
                'CANCELLED': { text: '已取消', icon: 'fa-times-circle', color: '#4b5563' },
                'FAILED': { text: '支付失败', icon: 'fa-exclamation-triangle', color: '#dc2626' },
                'REFUNDED': { text: '已退款', icon: 'fa-undo', color: '#ea580c' }
            };
            
            var status = statusMap[option.id] || { text: option.text, icon: 'fa-question-circle', color: '#6b7280' };
            
            var $option = $(
                '<span><i class="fa ' + status.icon + '" style="color: ' + status.color + '; margin-right: 6px;"></i>' + 
                status.text + '</span>'
            );
            return $option;
        }
        
        // 格式化选中项显示（带图标）
        function formatStatusSelection(option) {
            if (!option.id) {
                return option.text;
            }
            
            var statusMap = {
                'PENDING': { text: '待支付', icon: 'fa-clock-o', color: '#f59e0b' },
                'PROCESSING': { text: '处理中', icon: 'fa-cog', color: '#2563eb' },
                'SHIPPED': { text: '已发货', icon: 'fa-truck', color: '#7c3aed' },
                'DELIVERED': { text: '已送达', icon: 'fa-check-circle', color: '#059669' },
                'CANCELLED': { text: '已取消', icon: 'fa-times-circle', color: '#4b5563' },
                'FAILED': { text: '支付失败', icon: 'fa-exclamation-triangle', color: '#dc2626' },
                'REFUNDED': { text: '已退款', icon: 'fa-undo', color: '#ea580c' }
            };
            
            var status = statusMap[option.id] || { text: option.text, icon: 'fa-question-circle', color: '#6b7280' };
            
            var $selection = $(
                '<span><i class="fa ' + status.icon + '" style="color: ' + status.color + '; margin-right: 6px;"></i>' + 
                status.text + '</span>'
            );
            return $selection;
        }
    });
</script>
