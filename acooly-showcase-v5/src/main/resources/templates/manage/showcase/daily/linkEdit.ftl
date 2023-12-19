<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_link_editform" class="form-horizontal" action="/manage/showcase/daily/link/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="link" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">一级域名</label>
				<div class="col-sm-9">
					<select name="regionName" class="form-control select2bs4" data-options="required:true">
						<#list collect as collect >
							<option value="${collect}">${collect}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">地区</label>
				<div class="col-sm-9">
					<select name="regional" class="form-control select2bs4" data-options="required:true">
						<#list list1 as collect >
							<option value="${collect}">${collect}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">二级域名或更长的域名</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40"  placeholder="三级域名创建时写为：xxx/xxxx" name="domain" class="easyui-validatebox form-control" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>