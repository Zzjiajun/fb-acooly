<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopCart_editform" class="form-horizontal" action="/manage/shop/shopCart/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopCart" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">user_id</label>
				<div class="col-sm-9">
					<input type="text" name="userId" placeholder="请输入user_id..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
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
