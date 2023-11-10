<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_phone_editform" class="form-horizontal"
          action="/manage/showcase/daily/phone/<#if action=='create'>saveJson<#else>updateJson</#if>.html"
          method="post">
        <@jodd.form bean="phone" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">群号</label>
                    <div class="col-sm-9">
                        <input type="text" name="groupName" placeholder="请输入群号..."
                               class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"
                               required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">手机名</label>
                    <div class="col-sm-9">
                        <input type="text" name="phoneNumber" placeholder="请输入群名..." class="easyui-validatebox form-control"
                               required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">重复数</label>
                    <div class="col-sm-9">
                        <input type="text" name="repeatCount" placeholder="请输入粉丝数..." class="easyui-validatebox form-control"
                               required="true"/>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
    <script type="text/javascript">

    </script>
</div>
