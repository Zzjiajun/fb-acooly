<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopProductFeatures_editform" class="form-horizontal" action="/manage/shop/shopProductFeatures/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopProductFeatures" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">商品ID</label>
				<div class="col-sm-9">
					<input type="text" name="productId" placeholder="请输入商品ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">特性名称</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入特性名称..." name="feature" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">特性值</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入特性值..." name="featureValue" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">排序顺序</label>
				<div class="col-sm-9">
					<input type="text" name="sortOrder" placeholder="请输入排序顺序..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">创建时间</label>
				<div class="col-sm-9">
					<input type="text" name="createdAt" placeholder="请输入创建时间..." class="easyui-validatebox form-control" value="<#if shopProductFeatures.createdAt??>${shopProductFeatures.createdAt?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
