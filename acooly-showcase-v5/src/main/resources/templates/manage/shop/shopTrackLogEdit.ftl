<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopTrackLog_editform" class="form-horizontal" action="/manage/shop/shopTrackLog/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopTrackLog" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">访客ID（浏览器唯一标识，localStorage）</label>
				<div class="col-sm-9">
					<input type="text" name="visitorId" placeholder="请输入访客ID（浏览器唯一标识，localStorage）..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,64]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">会话ID（单次访问会话）</label>
				<div class="col-sm-9">
					<input type="text" name="sessionId" placeholder="请输入会话ID（单次访问会话）..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,64]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">页面类型：home/category/product/other</label>
				<div class="col-sm-9">
					<input type="text" name="pageType" placeholder="请输入页面类型：home/category/product/other..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,20]']" required="true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">页面标识：URL或页面唯一Key</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入页面标识：URL或页面唯一Key..." name="pageKey" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">商品ID（商品页时必填，其他页面为NULL）</label>
				<div class="col-sm-9">
					<input type="text" name="productId" placeholder="请输入商品ID（商品页时必填，其他页面为NULL）..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">停留时间（毫秒）</label>
				<div class="col-sm-9">
					<input type="text" name="stayDuration" placeholder="请输入停留时间（毫秒）..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]'],required:true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">语言代码：zh_CN/en_US/it_IT/ja_JP/ko_KR/fr_FR</label>
				<div class="col-sm-9">
					<input type="text" name="locale" placeholder="请输入语言代码：zh_CN/en_US/it_IT/ja_JP/ko_KR/fr_FR..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,10]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">浏览器UA</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入浏览器UA..." name="userAgent" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">IP地址</label>
				<div class="col-sm-9">
					<input type="text" name="ipAddress" placeholder="请输入IP地址..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,50]']"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">来源页面</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入来源页面..." name="referer" class="easyui-validatebox form-control form-words" data-words="500" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">设备类型：desktop/mobile/tablet</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入设备类型：desktop/mobile/tablet..." name="deviceType" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">浏览器：Chrome/Firefox/Safari</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入浏览器：Chrome/Firefox/Safari..." name="browser" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">来源</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入来源..." name="source" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">访问日期（用于分区和统计，格式：YYYY-MM-DD）</label>
				<div class="col-sm-9">
					<input type="text" name="visitDate" placeholder="请输入访问日期（用于分区和统计，格式：YYYY-MM-DD）..." class="easyui-validatebox form-control" value="<#if shopTrackLog.visitDate??>${shopTrackLog.visitDate?string('yyyy-MM-dd')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" onblur="$(this).validatebox('isValid');"  />
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
