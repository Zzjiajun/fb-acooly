<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopReviews_editform" class="form-horizontal" action="/manage/shop/shopReviews/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopReviews" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">商品ID</label>
				<div class="col-sm-9">
					<input type="text" name="productId" placeholder="请输入商品ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">用户ID</label>
				<div class="col-sm-9">
					<input type="text" name="userId" placeholder="请输入用户ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">评分（1-5）</label>
				<div class="col-sm-9">
					<input type="text" name="rating" placeholder="请输入评分（1-5）..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
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
					<input type="text" name="isVerified" placeholder="请输入是否验证购买..." class="easyui-validatebox form-control" data-options="validType:['number[0,127]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">有用数量</label>
				<div class="col-sm-9">
					<input type="text" name="helpfulCount" placeholder="请输入有用数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">创建时间</label>
				<div class="col-sm-9">
					<input type="text" name="createdAt" placeholder="请输入创建时间..." class="easyui-validatebox form-control" value="<#if shopReviews.createdAt??>${shopReviews.createdAt?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
