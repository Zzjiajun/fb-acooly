<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_demo_dynamicTab_view2_editform" class="form-horizontal" action="/manage/showcase/demo/dynamicTab/save2.html" method="post">
        <input name="id" type="hidden"/>
        <div class="card-body">
            <div class="form-group row">
                <label class="col-sm-3 col-form-label">昵称</label>
                <div class="col-sm-9">
                    <input type="text" name="nickname" placeholder="请输入昵称..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-3 col-form-label">个性签名</label>
                <div class="col-sm-9">
                    <input type="text" name="dailyWords" placeholder="请输入个性签名..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,64]']"/>
                </div>
            </div>
        </div>
    </form>
</div>
