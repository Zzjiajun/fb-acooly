<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopReviews_editform" class="form-horizontal" action="/manage/shop/shopReviews/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopReviews" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">商品ID</label>
				<div class="col-sm-9">
					<select name="productId" class="form-control select2bs4">
						<option value="">请选择商品</option>
						<#list productMap as k,v >
							<option value="${k}">${v}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">用户ID</label>
				<div class="col-sm-9">
					<select name="userId" class="form-control select2bs4">
						<option value="">请选择用户</option>
						<#list userMap as k,v >
							<option value="${k}">${v}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">评分（1-5）</label>
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="rating" placeholder="请输入评分（1-5）..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>-->
<#--				</div>-->
				<div class="col-sm-9">
					<select name="rating" class="form-control select2bs4" required>
						<option value="1">1星</option>
						<option value="2">2星</option>
						<option value="3">3星</option>
						<option value="4">4星</option>
						<option value="5">5星</option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">评论内容</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入评论内容..." name="comment" class="easyui-validatebox form-control form-words" data-words="999,999,999" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">是否验证购买</label>
				<div class="col-sm-9">
					<input type="radio" name="isVerified" value="1" >否
					<input type="radio"  name="isVerified" value="0"  checked> 是
				</div>
			</div>
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">有用数量</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="helpfulCount" placeholder="请输入有用数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
<#--				</div>-->
<#--			</div>-->
        </div>
      </@jodd.form>
    </form>
</div>
