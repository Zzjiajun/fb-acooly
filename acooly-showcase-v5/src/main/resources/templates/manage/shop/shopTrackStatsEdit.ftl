<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopTrackStats_editform" class="form-horizontal" action="/manage/shop/shopTrackStats/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopTrackStats" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">统计类型：daily_site/daily_page/daily_product</label>
				<div class="col-sm-9">
					<input type="text" name="statType" placeholder="请输入统计类型：daily_site/daily_page/daily_product..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,20]']" required="true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">统计日期（格式：YYYY-MM-DD）</label>
				<div class="col-sm-9">
					<input type="text" name="statDate" placeholder="请输入统计日期（格式：YYYY-MM-DD）..." class="easyui-validatebox form-control" value="<#if shopTrackStats.statDate??>${shopTrackStats.statDate?string('yyyy-MM-dd')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" onblur="$(this).validatebox('isValid');" data-options="required:true" />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">实体ID（商品ID，stat_type=daily_product时使用）</label>
				<div class="col-sm-9">
					<input type="text" name="entityId" placeholder="请输入实体ID（商品ID，stat_type=daily_product时使用）..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">实体标识（页面Key，stat_type=daily_page时使用）</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入实体标识（页面Key，stat_type=daily_page时使用）..." name="entityKey" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">语言代码（可为空表示全语言统计）</label>
				<div class="col-sm-9">
					<input type="text" name="locale" placeholder="请输入语言代码（可为空表示全语言统计）..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,10]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">页面浏览量（Page View）</label>
				<div class="col-sm-9">
					<input type="text" name="pv" placeholder="请输入页面浏览量（Page View）..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">独立访客数（Unique Visitor）</label>
				<div class="col-sm-9">
					<input type="text" name="uv" placeholder="请输入独立访客数（Unique Visitor）..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">总停留时间（毫秒）</label>
				<div class="col-sm-9">
					<input type="text" name="totalDuration" placeholder="请输入总停留时间（毫秒）..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">平均停留时间（毫秒）</label>
				<div class="col-sm-9">
					<input type="text" name="avgDuration" placeholder="请输入平均停留时间（毫秒）..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">最大停留时间（毫秒）</label>
				<div class="col-sm-9">
					<input type="text" name="maxDuration" placeholder="请输入最大停留时间（毫秒）..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">最小停留时间（毫秒）</label>
				<div class="col-sm-9">
					<input type="text" name="minDuration" placeholder="请输入最小停留时间（毫秒）..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">跳出率（百分比，0-100）</label>
				<div class="col-sm-9">
					<input type="text" name="bounceRate" placeholder="请输入跳出率（百分比，0-100）..." class="easyui-validatebox form-control" data-options="validType:['number[0,9999]']"/>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
