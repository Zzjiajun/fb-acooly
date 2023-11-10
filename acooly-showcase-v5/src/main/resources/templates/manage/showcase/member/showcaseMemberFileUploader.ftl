<div class="form-horizontal">
    <div class="card-body">
        <div class="form-group row">
            <div id="manage_showcaseMemberFile_uploader_message" style="color: red;"></div>
        </div>
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">用户编码</label>
            <div class="col-sm-9 col-form-content">${member.memberNo}</div>
        </div>
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">文件</label>
            <div class="col-sm-9">
                <input type="hidden" id="manage_showcaseMemberFile_uploader_memberNo" value="${member.memberNo}">
                <input type="file" name="manage_showcaseMemberFile_uploader_file" id="manage_showcaseMemberFile_uploader_file"/>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.createUploadify({
                /** 上传导入的URL */
                url: '/manage/showcase/member/showcaseMemberFile/upload.html?_csrf=${_csrf.token}&splitKey=v',
                /** 导入操作消息容器 */
                messager: 'manage_showcaseMemberFile_uploader_message',
                /** 上传导入文件表单ID */
                uploader: 'manage_showcaseMemberFile_uploader_file',
                jsessionid: '${Session.id}',
                formData: {memberNo: $('#manage_showcaseMemberFile_uploader_memberNo').val()}
            });
        });
    </script>
</div>
