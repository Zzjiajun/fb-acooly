<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopProductAttr_editform" class="form-horizontal" action="/manage/shop/shopProductAttr/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopProductAttr" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">商品ID</label>
				<div class="col-sm-9">
					<input type="text" name="productId" placeholder="请输入商品ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">属性值ID</label>
				<div class="col-sm-9">
					<input type="text" name="attrValueId" placeholder="请输入属性值ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
