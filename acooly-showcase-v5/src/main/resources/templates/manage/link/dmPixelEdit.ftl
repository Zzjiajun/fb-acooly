<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmPixel_editform" class="form-horizontal" action="/manage/link/dmPixel/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmPixel" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">domain</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入domain..." name="domain" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
			<div id="pixelIdDm" class="form-group row">
				<label class="col-sm-3 col-form-label">pixel_id</label>
				<div class="col-sm-9">
					<textarea rows="3" cols="40" placeholder="请输入pixel_id..." name="pixelId" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>



	<script>
		var formElement1 = document.getElementById("manage_dmPixel_editform");
		if (formElement1.getAttribute("action")==='/manage/link/dmPixel/updateJson.html'){
			var pixelIdDm = document.getElementById('pixelIdDm');
			div.style.display = 'none';
		}
	</script>
</div>
