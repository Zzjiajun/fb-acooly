<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmObserverPermission_editform" class="form-horizontal" action="/manage/link/dmObserverPermission/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="dmObserverPermission" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">观察者用户ID</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="userId" placeholder="请输入观察者用户ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">dmCenter记录ID</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="dmCenterId" placeholder="请输入dmCenter记录ID..." class="easyui-validatebox form-control" data-options="validType:['number[0,2147483646]'],required:true"/>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">授权时间</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="grantTime" placeholder="请输入授权时间..." class="easyui-validatebox form-control" value="<#if dmObserverPermission.grantTime??>${dmObserverPermission.grantTime?string('yyyy-MM-dd HH:mm:ss')}</#if>" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" onblur="$(this).validatebox('isValid');" data-options="required:true" />-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">授权人</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="grantBy" placeholder="请输入授权人..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,50]']" required="true"/>-->
<#--				</div>-->
<#--			</div>-->
<#--			<div class="form-group row">-->
<#--				<label class="col-sm-3 col-form-label">状态：1=有效，0=无效</label>-->
<#--				<div class="col-sm-9">-->
<#--					<input type="text" name="status" placeholder="请输入状态：1=有效，0=无效..." class="easyui-validatebox form-control" data-options="validType:['number[0,127]']"/>-->
<#--				</div>-->
<#--			</div>-->

            <div class="form-group row">
                <label class="col-sm-3 col-form-label">选择观察者：</label>
                <div class="col-sm-9">
                    <select name="userId" class="form-control select2bs4">
                        <#list allObservers as k,v >
                            <option value="${k}">${v}</option>
                        </#list>
                    </select>
                </div>
            </div>
        </div>
      </@jodd.form>
    </form>
</div>
