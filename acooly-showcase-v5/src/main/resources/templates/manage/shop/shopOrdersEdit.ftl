<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopOrders_editform" class="form-horizontal" action="/manage/shop/shopOrders/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopOrders" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">订单编号</label>
				<div class="col-sm-9">
					<input type="text" name="orderId" placeholder="请输入订单编号..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,100]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">用户ID</label>
				<div class="col-sm-9">
					<input type="text" name="userId" placeholder="请输入用户ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">订单总金额</label>
				<div class="col-sm-9">
					<input type="text" name="totalPrice" placeholder="请输入订单总金额..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">订单状态：PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED</label>
				<div class="col-sm-9">
					<input type="text" name="status" placeholder="请输入订单状态：PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,50]']" required="true"/>
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
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">支付方式：CREDIT_CARD, PAYPAL, STRIPE, ALIPAY, WECHAT_PAY, BANK_TRANSFER</label>
				<div class="col-sm-9">
					<input type="text" name="paymentMethod" placeholder="请输入支付方式：CREDIT_CARD, PAYPAL, STRIPE, ALIPAY, WECHAT_PAY, BANK_TRANSFER..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,50]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">取消原因</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入取消原因..." name="cancelReason" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">创建时间</label>
				<div class="col-sm-9">
					<input type="text" name="createdAt" placeholder="请输入创建时间..." class="easyui-validatebox form-control" value="<#if shopOrders.createdAt??>${shopOrders.createdAt?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">更新时间</label>
				<div class="col-sm-9">
					<input type="text" name="updatedAt" placeholder="请输入更新时间..." class="easyui-validatebox form-control" value="<#if shopOrders.updatedAt??>${shopOrders.updatedAt?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
