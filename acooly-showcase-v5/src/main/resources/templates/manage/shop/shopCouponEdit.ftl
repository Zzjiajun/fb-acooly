<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopCoupon_editform" class="form-horizontal" action="/manage/shop/shopCoupon/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopCoupon" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">优惠方式</label>
				<div class="col-sm-9">
					<select name="discountType"  class="form-control select2bs4" data-options="required:true">
						<option value="percent">打折</option>
<#--						<option value="EXPIRED">过期</option>-->
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">优惠力度</label>
				<div class="col-sm-9">
					<select name="discountValue"  class="form-control select2bs4" data-options="required:true">
						<option value="0.95">9.5折</option>
						<option value="0.90">9折</option>
						<option value="0.80">8折</option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">券可被全局使用的总次数</label>
				<div class="col-sm-9">
					<input type="text" name="totalUses" placeholder="请输入券可被全局使用的总次数，0 表示不限..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">已被使用次数</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="usedCount" placeholder="请输入已被使用次数..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
<#--				</div>-->
<#--			</div>-->
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">单用户使用次数</label>
				<div class="col-sm-9">
					<input type="text" name="perUserLimit" placeholder="请输入每个用户最多使用次数，0 表示不限..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">状态</label>
				<div class="col-sm-9">
					<select name="status"  class="form-control select2bs4" data-options="required:true">
						<option value="ACTIVE">启用</option>
						<option value="EXPIRED">过期</option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">有效期开始</label>
				<div class="col-sm-9">
					<input type="text" name="validFrom" placeholder="请输入有效期开始..." class="easyui-validatebox form-control" value="<#if shopCoupon.validFrom??>${shopCoupon.validFrom?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">有效期结束</label>
				<div class="col-sm-9">
					<input type="text" name="validTo" placeholder="请输入有效期结束..." class="easyui-validatebox form-control" value="<#if shopCoupon.validTo??>${shopCoupon.validTo?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
