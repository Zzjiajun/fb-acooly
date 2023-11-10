<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_showcaseMemberFile_editform" class="form-horizontal" action="/manage/showcase/member/showcaseMemberFile/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" enctype="multipart/form-data">
        <@jodd.form bean="showcaseMemberFile" scope="request">
            <input name="id" type="hidden"/>
            <input name="memberNo" value="${member.memberNo}" type="hidden"/>

            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">用户编码</label>
                    <div class="col-sm-9 col-form-content">${member.memberNo}</div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">用户名</label>
                    <div class="col-sm-9 col-form-content">${member.username}</div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">文件标题</label>
                    <div class="col-sm-9">
                        <input type="text" name="fileTitle" placeholder="请输入文件标题..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,45]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">文件名</label>
                    <div class="col-sm-9">
                        <input type="text" name="fileName" placeholder="请输入文件名..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,45]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">文件路径</label>
                    <div class="col-sm-9">
                        <#if showcaseMemberFile != null && showcaseMemberFile.filePath != ''>
                            <div class="row col-form-content">
                                <div class="col-sm-6"><a href="javascript:;" onclick="$.acooly.file.play('${serverRoot}${showcaseMemberFile.filePath}');">查看</a></div>
                                <div class="col-sm-6" style="text-align: right;"><a href="javascript:;" onclick="$('#manage_showcaseMemberFile_filePath_container').toggle();">重新上传</a></div>
                            </div>
                        </#if>
                        <div class="custom-file" id="manage_showcaseMemberFile_filePath_container" <#if showcaseMemberFile != null && showcaseMemberFile.filePath != ''>style="display: none;"</#if>>
                            <input type="file" name="filePath_uploadFile" class="custom-file-input">
                            <label class="custom-file-label">请选择上传的文件</label>
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">文件扩展名</label>
                    <div class="col-sm-9">
                        <input type="text" name="fileExt" placeholder="请输入文件扩展名..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,16]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">文件类型</label>
                    <div class="col-sm-9">
                        <select name="fileType" class="form-control select2bs4" data-options="required:true">
                            <#list allFileTypes as k,v>
                                <option value="${k}">${v}</option></#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">备注</label>
                    <div class="col-sm-9">
                        <input type="text" name="comments" placeholder="请输入备注..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,128]']"/>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
</div>
