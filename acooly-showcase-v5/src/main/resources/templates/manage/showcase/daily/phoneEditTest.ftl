<div>
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden; width: 100%" align="left">
        <form id="manage_phoneTest_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-form-label">电话号：</label>
                    <div class="col-sm-9">
                        <textarea rows="8" cols="80" name="phoneNumber" class="easyui-validatebox"></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-form-label">群名字：</label>
                    <div class="col-sm-9">
                        <textarea rows="4" cols="80" name="groupNumber" class="easyui-validatebox"></textarea>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    // function startParseClick(){
    //     $.ajax({
    //         type: 'POST',
    //         url: '/manage/showcase/daily/accounts/totalParseList.html', // 替换为你的服务器URL
    //         data: $('#manage_parse_searchform').serialize(),
    //         success: function(response) {
    //             // document.getElementById("comments").value = response.data.list;
    //             var arr = response.data.list.toString().split(",");
    //             var textarea = document.getElementById("comments");
    //             for (var i = 0; i < arr.length; i++) {
    //                 textarea.value += arr[i] + "\n";
    //             }
    //         },
    //     });
    // }
</script>