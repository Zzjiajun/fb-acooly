<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_accounts_editform" class="form-horizontal" action="/manage/showcase/daily/accounts/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
        <@jodd.form bean="accounts" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">账户信息</label>
                    <div class="col-sm-9">
                        <input type="text" name="accountName" placeholder="请输入账户信息..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">邮箱</label>
                    <div class="col-sm-9">
                        <input type="text" name="mail" placeholder="请输入邮箱..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">持有者</label>
                    <div class="col-sm-9">
                        <select name="holder" class="form-control select2bs4" data-options="required:true">
                            <#list mapName as k,v >
                                <option value="${k}">${k}:${v}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">余额</label>
                    <div class="col-sm-9">
                        <input type="text" name="balance" placeholder="请输入余额..." onkeyup="value=value.match(/\d+\.?\d{0,2}/,'')" class="easyui-validatebox form-control"  required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">账户编号</label>
                    <div class="col-sm-9">
                        <input type="text" name="mobileNo" placeholder="请输入账户编号..." class="easyui-validatebox form-control"  required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">状态</label>
                    <div class="col-sm-9">
                        <select name="status" class="form-control select2bs4" data-options="required:true">
                            <#list statusList as k,v >
                                <option value="${k}">${v}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">备注</label>
                    <div class="col-sm-9">
                        <input type="text" name="comments"  class="easyui-validatebox form-control" />
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
    <script type="text/javascript">

    </script>
</div>
