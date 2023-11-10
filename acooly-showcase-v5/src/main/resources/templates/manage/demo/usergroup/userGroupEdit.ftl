<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_userGroup_editform" class="form-horizontal" action="/manage/demo/usergroup/userGroup/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
        <@jodd.form bean="userGroup" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">名称</label>
                    <div class="col-sm-9">
                        <input type="text" name="name" placeholder="请输入名称..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,45]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">描述</label>
                    <div class="col-sm-9">
                        <textarea rows="3" cols="40" placeholder="请输入描述..." name="descn" class="easyui-validatebox form-control"></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">用户名</label>
                    <div class="col-sm-9">
                        <textarea rows="3" cols="40" placeholder="请输入用户名..." name="users" class="easyui-validatebox form-control"></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">备注</label>
                    <div class="col-sm-9">
                        <textarea rows="3" cols="40" placeholder="请输入备注..." name="comments" class="easyui-validatebox form-control"></textarea>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
</div>
