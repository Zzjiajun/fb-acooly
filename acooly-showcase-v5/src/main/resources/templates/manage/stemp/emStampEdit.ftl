<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_emStamp_editform" class="form-horizontal" action="/manage/stemp/emStamp/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="emStamp" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-1 col-form-label">表名</label>
				<div class="col-sm-11">
					<input type="text" rows="3" cols="40" placeholder="请输入类型数据名..." name="name" class="easyui-validatebox form-control form-words" data-words="255" />
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-1 col-form-label">字段</label>
				<div class="col-sm-11">
					<div id="manage_resource1_form_icon_container">
						<div class="resource_icons">
							<a href="javascript:;" onclick="$('#emStamp_icons_font').toggle()">
								<div class="header">
									<span>字段名称</span>
									<div style="float: right;margin-right: 5px;"><i class="fa fa-chevron-down"></i></div>
								</div>
							</a>
							<div id="emStamp_icons_font">
								<#list nameMap as k,v>
									<span class="iconSpan">
                                            <input class="icon-elm" type="checkbox" name="gather" value="${k}"/>
                                            <span class="icon-elm">${v}</span>  <!-- 显示文字 -->
                                        </span>
								</#list>
							</div>
						</div>
					</div>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
