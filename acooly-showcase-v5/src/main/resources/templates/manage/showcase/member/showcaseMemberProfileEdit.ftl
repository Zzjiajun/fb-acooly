<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_showcaseMemberProfile_editform" class="form-horizontal" action="/manage/showcase/member/showcaseMemberProfile/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" enctype="multipart/form-data">
        <@jodd.form bean="showcaseMemberProfile" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">用户</label>
                    <div class="col-sm-9 col-form-content">
                        <#if action=='create' && RequestParameters.memberNo == ''>
                            <select name="memberNo" class="form-control select2bs4" required="true">
                                <#list members as m>
                                    <option value="${m.memberNo}">${m.memberNo} - ${m.username}</option></#list>
                            </select>
                        <#else>
                            <#if action=='create'>${RequestParameters.memberNo}<#else>${showcaseMemberProfile.username}</#if>
                            <input type="hidden" name="memberNo" value="<#if action=='create'>${RequestParameters.memberNo}<#else>${showcaseMemberProfile.memberNo}</#if>"/>
                        </#if>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">昵称</label>
                    <div class="col-sm-9">
                        <input type="text" name="nickname" placeholder="请输入昵称..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">
                        爱好
                        <a title="如果需要在列表中回显复选框value（例如：game）对应的中文(游戏)" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                    </label>
                    <div class="col-sm-9 col-form-content">
                        <div class="icheck-primary d-inline">
                            <input type="checkbox" name="dailyWords" value="game" <#if showcaseMemberProfile.dailyWords?index_of("game") != -1>checked</#if> id="manage_showcaseMemberProfile_editform_dailyWords_1">
                            <label for="manage_showcaseMemberProfile_editform_dailyWords_1">游戏</label>
                        </div>
                        <div class="icheck-danger d-inline">
                            <input type="checkbox" name="dailyWords" value="music" <#if showcaseMemberProfile.dailyWords?index_of("music") != -1>checked</#if> id="manage_showcaseMemberProfile_editform_dailyWords_2">
                            <label for="manage_showcaseMemberProfile_editform_dailyWords_2">音乐</label>
                        </div>
                        <div class="icheck-warning d-inline">
                            <input type="checkbox" name="dailyWords" value="read" <#if showcaseMemberProfile.dailyWords?index_of("read") != -1>checked</#if> id="manage_showcaseMemberProfile_editform_dailyWords_3">
                            <label for="manage_showcaseMemberProfile_editform_dailyWords_3">看书</label>
                        </div>
                        <div class="icheck-success d-inline">
                            <input type="checkbox" name="dailyWords" value="sport" <#if showcaseMemberProfile.dailyWords?index_of("sport") != -1>checked</#if> id="manage_showcaseMemberProfile_editform_dailyWords_4">
                            <label for="manage_showcaseMemberProfile_editform_dailyWords_4">运动</label>
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">客户经理</label>
                    <div class="col-sm-9">
                        <select name="manager" class="form-control select2bs4"
                                data-placeholder="选择客户经理（多选）..."
                                multiple="true">
                            <#list users as user><option value="${user.username}">${user.pinyin}:${user.realName}</option></#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">经纪人</label>
                    <div class="col-sm-9">
                        <select name="broker" class="form-control select2bs4" data-placeholder="请选择经纪人...">
                            <#list users as user>
                                <option value="${user.username}">${user.pinyin}:${user.realName}</option></#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">头像</label>
                    <div class="col-sm-9">
                        <#if showcaseMemberProfile != null && showcaseMemberProfile.profilePhoto != ''>
                            <div class="row col-form-content">
                                <div class="col-sm-6"><a href="javascript:;" onclick="$.acooly.file.play('${serverRoot}${showcaseMemberProfile.profilePhoto}');">查看</a></div>
                                <div class="col-sm-6" style="text-align: right;"><a href="javascript:;" onclick="$('#manage_showcaseMemberProfile_profilePhoto_container').toggle();">重新上传</a></div>
                            </div>
                        </#if>
                        <div class="custom-file" id="manage_showcaseMemberProfile_profilePhoto_container" <#if showcaseMemberProfile != null && showcaseMemberProfile.profilePhoto != ''>style="display: none;"</#if>>
                            <input type="file" name="profilePhoto_uploadFile" class="custom-file-input">
                            <label class="custom-file-label">请选择上传的文件</label>
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">邮箱认证</label>
                    <div class="col-sm-9 col-form-content">
                        <#list allEmailStatuss as k,v>
                            <div class="icheck-primary d-inline">
                                <input type="radio" id="manage_showcaseMemberProfile_editform_emailStatus_${k}" value="${k}" name="emailStatus">
                                <label for="manage_showcaseMemberProfile_editform_emailStatus_${k}">${v}</label>
                            </div>
                        </#list>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">手机认证</label>
                    <div class="col-sm-9">
                        <select name="mobileNoStatus" class="form-control select2bs4">
                            <#list allMobileNoStatuss as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">实名认证</label>
                    <div class="col-sm-9">
                        <select name="realNameStatus" class="form-control select2bs4">
                            <#list allRealNameStatuss as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">发送短信</label>
                    <div class="col-sm-9">
                        <select name="smsSendStatus" class="form-control select2bs4">
                            <#list allSmsSendStatuss as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
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
    <script>

        $(function () {
            // 设置初始值
            $.acooly.formVal("manage_showcaseMemberProfile_editform", "manager", '${showcaseMemberProfile.manager}');
            // 注册事件
            $.acooly.formObj("manage_showcaseMemberProfile_editform", "manager").on('select2:select', function (e) {
                let data = e.params.data;
                console.log(data);
            });
        });

    </script>
</div>
