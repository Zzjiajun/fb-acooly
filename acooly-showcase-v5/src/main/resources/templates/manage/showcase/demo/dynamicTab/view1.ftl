<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_demo_dynamicTab_view1_editform" class="form-horizontal" action="/manage/showcase/demo/dynamicTab/save1.html" method="post">
        <input name="id" type="hidden"/>
        <div class="card-body">
            <div class="form-group row">
                <label class="col-sm-3 col-form-label">文件标题</label>
                <div class="col-sm-9">
                    <input type="text" name="fileTitle" placeholder="请输入文件标题..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,45]']"/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-3 col-form-label">文件名</label>
                <div class="col-sm-9">
                    <input type="text" name="fileName" placeholder="请输入文件名..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,45]']"/>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-3 col-form-label">备注</label>
                <div class="col-sm-9">
                    <input type="text" name="comments" placeholder="请输入备注..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,128]']"/>
                </div>
            </div>
        </div>
    </form>
</div>
