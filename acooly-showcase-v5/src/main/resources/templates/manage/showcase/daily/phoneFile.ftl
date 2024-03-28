<div>
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden; width: 100%" align="left" >
        <div class="form-group">
            <label class="col-form-label">图片进行分析</label>
            <input type="file" name="phtFile" id="phtFile" required>
            <button class="btn btn-sm btn-success" type="button" onclick="startPhoneClick3()"><i class="fa fa-search fa-lg fa-fw fa-col"></i>图片识别</button>
        </div>
        <form id="manage_phoneFile_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="card-body">
                <div class="form-group">
                    <label class="col-form-label">电话号：</label>
                    <textarea rows="12" cols="80"  name="commentsFile" id="commentsFile" class="easyui-validatebox" ></textarea>
                </div>
                <div class="form-group">
                    <label class="col-form-label">群名字：</label>
                    <textarea rows="2" cols="80"  name="groupNumber" class="easyui-validatebox" ></textarea>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    function ajaxLoading() {
        $("<div class=\"datagrid-mask\"><iframe id=\"iframe1\" src=\"about:blank\" frameBorder=\"0\" marginHeight=\"0\" marginWidth=\"0\" style=\"position:absolute; visibility:inherit; top:0px;left:0px;width:100%; height:100%;z-index:-1; filter:alpha(opacity=0.5);\"></iframe></div>").css({ "display": "block", "z-index": "999999" }).appendTo("body");
        $("<div class=\"datagrid-mask-msg\"></div>").html("正在上传数据，请稍候。。。").appendTo("body").css({ "display": "block", "left": ($(document.body).outerWidth(true) - 190) / 2, "top": ($(window).height() - 200) / 2 });
    }
    /*去掉加载数据提示*/
    function ajaxLoadEnd() {
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
    }


    function startPhoneClick3(){
        ajaxLoading()
        var formData = new FormData();
        formData.append('file', $('#phtFile')[0].files[0]);
        $.ajax({
            type: 'POST',
            url: '/manage/showcase/daily/accounts/filePh.html', // 替换为你的服务器URL
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                ajaxLoadEnd();
                $.acooly.messager('提示', response.message, response.success ? 'success' : 'danger');
                // document.getElementById("comments").value = response.data.list;
                var arr = response.data.list.toString().split(",");
                var textarea = document.getElementById("commentsFile");
                for (var i = 0; i < arr.length; i++) {
                    textarea.value += arr[i] +","+ "\n";
                }
            },
        });
    }
</script>