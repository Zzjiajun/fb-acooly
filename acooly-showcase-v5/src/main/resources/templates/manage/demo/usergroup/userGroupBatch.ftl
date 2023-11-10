<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <style>
        .usergroup {
        }

        .usergroup-row {
            margin-top: 8px;
            padding: 5px 0;
            border-radius: 3px;
            border-bottom: 1px dotted #bbb;
        }
    </style>
    <form id="manage_userGroup_editform" class="form-horizontal" action="/manage/demo/usergroup/userGroup/batchSave.html" method="post">
        <@jodd.form bean="userGroup" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <div class="col-sm-12 usergroup">
                        <div class="row usergroup-toolbar">
                            <div class="col-sm-12">
                                <button type="button" class="btn btn-primary btn-sm" onclick="$.acooly.showcase.usergroup.rowAdd();"><i class="fa fa-plus fa-lg fa-fw fa-col"></i>添加行</button>
                                <button type="button" class="btn btn-primary btn-sm" onclick="$.acooly.showcase.usergroup.rowDeleteAll()"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i>清除所有</button>
                            </div>
                        </div>

                        <div class="usergroup-header">
                            <div class="row" style="margin-top: 5px;">
                                <div class="col-sm-3">名称</div>
                                <div class="col-sm-8">说明</div>
                                <div class="col-sm-1"></div>
                            </div>
                        </div>
                        <div id="manage_userGroup_row_container" class="usergroup-content"></div>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>

    <!-- 动态分配行 -->
    <script type="text/html" id="manage_userGroup_row_template">
        <div id="manage_userGroup_row<%=i%>" class="row usergroup-row unpaid-row" data-index="<%=i%>">
            <input type="hidden" name="id_<%=i%>" id="manage_userGroup_id<%=i%>">
            <div class="col-sm-3"><input type="text" name="name_<%=i%>" placeholder="请输入名称..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,45]']" required="true"/></div>
            <div class="col-sm-8">
                <select name="users_<%=i%>" id="manage_userGroup_user<%=i%>" class="form-control select2bs4"
                        data-placeholder="选择客户经理（多选）..."
                        multiple="true">
                    <#list users as user>
                        <option value="${user.username}">${user.pinyin}:${user.realName}</option></#list>
                </select>
            </div>
            <div class="col-sm-1">
                <button type="button" class="btn btn-primary btn-sm" onclick="$.acooly.showcase.usergroup.rowDelete('<%=i%>')"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></button>
            </div>
        </div>
    </script>

    <script>
        $(function () {
            $.acooly.showcase.usergroup.load();
        });
    </script>

</div>
