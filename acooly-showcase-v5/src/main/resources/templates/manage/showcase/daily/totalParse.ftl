<div>
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden; width: 100%" align="left" >
        <form id="manage_parse_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">电话号：</label>
<#--                <input type="text" class="form-control form-control-sm" name="phoneNumber"/>-->
                <textarea rows="12" cols="80"  name="phoneNumber" class="easyui-validatebox" ></textarea>
            </div>
            <div class="form-group">
                <br>
                <button class="btn btn-sm btn-primary" type="button" onclick="startParseClick()"><i class="fa fa-search fa-lg fa-fw fa-col"></i>转换</button>
            </div>
            <div class="form-group">
                <br>
                <button class="btn btn-sm btn-primary" type="button" onclick="startParseClick2()"><i class="fa fa-search fa-lg fa-fw fa-col"></i>反转</button>
            </div>
        </form>
        <div data-options="region:'center',border:false">
            <div class="form-group">
                <label class="col-form-label">转换后：</label>
                <textarea rows="12" cols="20"  id="comments" class="easyui-validatebox" ></textarea>
            </div>
        </div>
    </div>
</div>
<script>
    function startParseClick(){
        $.ajax({
            type: 'POST',
            url: '/manage/showcase/daily/accounts/totalParseList.html', // 替换为你的服务器URL
            data: $('#manage_parse_searchform').serialize(),
            success: function(response) {
                $.acooly.messager('提示', response.message, response.success ? 'success' : 'danger');
                // document.getElementById("comments").value = response.data.list;
                var arr = response.data.list.toString().split(",");
                var textarea = document.getElementById("comments");
                for (var i = 0; i < arr.length; i++) {
                    textarea.value += arr[i] + "\n";
                }
            },
        });
    }
    function startParseClick2(){
        $.ajax({
            type: 'POST',
            url: '/manage/showcase/daily/accounts/totalParseList1.html', // 替换为你的服务器URL
            data: $('#manage_parse_searchform').serialize(),
            success: function(response) {
                $.acooly.messager('提示', response.message, response.success ? 'success' : 'danger');
                // document.getElementById("comments").value = response.data.list;
                var arr = response.data.list.toString().split(",");
                var textarea = document.getElementById("comments");
                for (var i = 0; i < arr.length; i++) {
                    textarea.value += arr[i] + "\n";
                }
            },
        });
    }
</script>