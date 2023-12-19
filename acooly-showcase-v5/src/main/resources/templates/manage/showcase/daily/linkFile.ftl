<div>
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden; width: 100%" align="left" >
        <br>
        <form id="manage_link_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">选择域名：</label>
                <select name="regname" id="regname" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list domainList as domainList >
                        <option value="${domainList}">${domainList}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label class="col-form-label">选择二级域名：</label>
                <select name="domain" id="domain" class="form-control select2bs4" data-options="required:true">
                    <option value=""></option>
                </select>
            </div>


            <div class="form-group">
                <input type="file" name="uploadFile" id="uploadFile" required>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="startLinkClick1()"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 上传</button>
            </div>
        </form>
    </div>
</div>
<script>
    var dataTest1 = [];

    $('#regname').change(function () {
        <#list mapDomain as k,v >
        if ($('#regname').val() == '${k}') {
            dataTest1.length = 0;
            <#list v as item >
            dataTest1.push('${item}');
            </#list>
        } else {
        }
        </#list>
        $('#domain').empty();
        $(function () {
            $.each(dataTest1, function () {
                $('#domain').append("<option value=" + this + ">" + this + "</option>");
            });
        })
    })

    $('#domain').change(function () {
        console.log($('#domain').val());
    })









    function ajaxLoading() {
        $("<div class=\"datagrid-mask\"><iframe id=\"iframe1\" src=\"about:blank\" frameBorder=\"0\" marginHeight=\"0\" marginWidth=\"0\" style=\"position:absolute; visibility:inherit; top:0px;left:0px;width:100%; height:100%;z-index:-1; filter:alpha(opacity=0.5);\"></iframe></div>").css({ "display": "block", "z-index": "999999" }).appendTo("body");
        $("<div class=\"datagrid-mask-msg\"></div>").html("正在上传数据，请稍候。。。").appendTo("body").css({ "display": "block", "left": ($(document.body).outerWidth(true) - 190) / 2, "top": ($(window).height() - 200) / 2 });
    }
    /*去掉加载数据提示*/
    function ajaxLoadEnd() {
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
    }

    $(document).ready(function(){
        $('.easyui-filebox').filebox();
    });
    function startLinkClick1(){
        ajaxLoading();
        var formData = new FormData();
        formData.append('file', $('#uploadFile')[0].files[0]);
        formData.append('domain', $('#domain').val());
        formData.append('regname', $('#regname').val());
        $.ajax({
            url: '/manage/showcase/daily/accounts/file1',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(result){
                ajaxLoadEnd();
                $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
            }
        });
    }
</script>