<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopSubCategories_editform" class="form-horizontal" action="/manage/shop/shopSubCategories/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopSubCategories" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">父级分类ID</label>
				<div class="col-sm-9">
					<select name="parentId" id="domainTest" class="form-control select2bs4" data-options="required:true" required>
						<option value=" "> </option>
						<#list parentMap as k,v >
							<option value="${k}">${v}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">子级分类名称</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入子级分类名称..." name="name" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">子级分类标识</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入子级分类标识..." name="slug" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">子级分类描述</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入子级分类描述..." name="description" class="easyui-validatebox form-control form-words" data-words="999,999,999" ></textarea>
				</div>
			</div>
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">排序顺序</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="sortOrder" placeholder="请输入排序顺序..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
<#--				</div>-->
<#--			</div>-->
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">是否启用</label>
				<div class="col-sm-9">
					<input type="radio"  name="isActive" value="0"  checked> 否
					<input type="radio" name="isActive" value="1" > 是
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">是否显示</label>
				<div class="col-sm-9">
					<input type="radio"  name="isShow" value="0"  checked> 否
					<input type="radio" name="isShow" value="1" > 是
				</div>
			</div>
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">分类图标URL</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入分类图标URL..." name="icon" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">分类图片URL</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入分类图片URL..." name="image" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">SEO标题</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入SEO标题..." name="seoTitle" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">SEO关键词</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入SEO关键词..." name="seoKeywords" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">SEO描述</label>-->
<#--				<div class="col-sm-9">-->
<#--					<textarea rows="3" cols="40" placeholder="请输入SEO描述..." name="seoDescription" class="easyui-validatebox form-control form-words" data-words="999,999,999" ></textarea>-->
<#--				</div>-->
<#--			</div>-->
        </div>
      </@jodd.form>
    </form>
</div>
