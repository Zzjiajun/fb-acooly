<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopCartItem_editform" class="form-horizontal" action="/manage/shop/shopCartItem/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopCartItem" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">cart_id</label>
				<div class="col-sm-9">
					<input type="text" name="cartId" placeholder="请输入cart_id..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">用户ID</label>
				<div class="col-sm-9">
					<input type="text" name="userId" placeholder="请输入用户ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">商品ID</label>
				<div class="col-sm-9">
					<input type="text" name="productId" placeholder="请输入商品ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">商品数量</label>
				<div class="col-sm-9">
					<input type="text" name="quantity" placeholder="请输入商品数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
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
