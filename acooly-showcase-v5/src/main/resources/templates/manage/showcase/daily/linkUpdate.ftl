<div>
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden; width: 100%" align="left" >
        <br>
        <form id="manage_linkUp_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">选择域名：</label>
                <select name="regname1" id="regname1" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list domainList1 as domainList >
                        <option value="${domainList}">${domainList}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">选择二级域名：</label>
                <select name="domain1" id="domain1" class="form-control select2bs4" data-options="required:true">
                    <option value=""></option>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">旧链接：</label>
                <textarea rows="1" cols="30"  name="oldLink" class="easyui-validatebox form-control" required ></textarea>

            </div>
            <div class="form-group">
                <label class="col-form-label">新连接：</label>
                <textarea rows="1" cols="30"  name="newLink" class="easyui-validatebox form-control" required ></textarea>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="startLinkClick3()"><i class="fa fa-search fa-lg fa-fw fa-col"></i>修改</button>
            </div>
        </form>
    </div>
</div>
<script>

    var dataTest2 = [];

    $('#regname1').change(function () {
        <#list mapDomain1 as k,v >
        if ($('#regname1').val() == '${k}') {
            dataTest2.length = 0;
            <#list v as item >
            dataTest2.push('${item}');
            </#list>
        } else {
        }
        </#list>
        $('#domain1').empty();
        $(function () {
            $.each(dataTest2, function () {
                $('#domain1').append("<option value=" + this + ">" + this + "</option>");
            });
        })
    })

    $('#domain1').change(function () {
        console.log($('#domain1').val());
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
    function startLinkClick3(){
        ajaxLoading();
        $.ajax({
            url: '/manage/showcase/daily/accounts/fileUpdate',
            type: 'POST',
            data: $('#manage_linkUp_searchform').serialize(),
            success: function(result){
                ajaxLoadEnd();
                $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger');
            }
        });
    }
</script>