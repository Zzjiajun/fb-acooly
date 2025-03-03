<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_board_editform" class="form-horizontal" action="/manage/link/board/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="board" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">管理员名字</label>
				<div class="col-sm-9">
					<select name="manageName" class="form-control select2bs4" data-options="required:true" required>
						<#list usernameList as v >
							<option value="${v}">${v}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">字段</label>
				<div class="col-sm-9">
					<div id="manage_resource1_form_icon_container">
						<div class="resource_icons">
							<a href="javascript:;" onclick="$('#board_icons_font').toggle()">
								<div class="header">
									<span>管理员姓名</span>
									<div style="float: right;margin-right: 5px;"><i class="fa fa-chevron-down"></i></div>
								</div>
							</a>
							<div id="board_icons_font" style="display: flex; flex-wrap: wrap; max-width: 100%;">
								<#list usernameList1 as v>
									<span class="iconSpan" style="flex: 0 0 50%; box-sizing: border-box; padding: 5px;">
                            <input class="icon-elm" type="checkbox" name="attachedName" value="${v}"/>
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
