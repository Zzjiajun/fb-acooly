<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopOrderItems_editform" class="form-horizontal" action="/manage/shop/shopOrderItems/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopOrderItems" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">order_id</label>
				<div class="col-sm-9">
					<input type="text" name="orderId" placeholder="请输入order_id..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">product_id</label>
				<div class="col-sm-9">
					<input type="text" name="productId" placeholder="请输入product_id..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">product_name</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入product_name..." name="productName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">quantity</label>
				<div class="col-sm-9">
					<input type="text" name="quantity" placeholder="请输入quantity..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">price</label>
				<div class="col-sm-9">
					<input type="text" name="price" placeholder="请输入price..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,12]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">total_price</label>
				<div class="col-sm-9">
					<input type="text" name="totalPrice" placeholder="请输入total_price..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,12]']"/>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
