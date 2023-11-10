<div>
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden; width: 100%">
        <form id="manage_tranOut_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <input name="id" id="Id" type="hidden" value="${accountsId}"/>
            <div class="card-body card-blue">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">持有者：</label>
                    <div class="col-sm-6">
                        <select id="firstCombobox1" style="width: 70%" name="accountManager"
                                class="form-control select2bs4"
                                data-options="required:true">
                            <option></option>
                            <#list listHolder as listHolder >
                                <option value="${listHolder}">${listHolder}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">账户：</label>
                    <div class="col-sm-6">
                        <select id="dropdown" style="width: 70%" name="inAccount" class="form-control select2bs4"
                                data-options="required:true">
                            <option></option>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">转出余额：</label>
                    <div class="col-sm-6">
                        <input type="text" style="width: 70%" id="roll" onkeyup="value=value.match(/\d+\.?\d{0,2}/,'')" name="amount"
                               onblur="updateInputTest()" class="easyui-validatebox form-control"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">账户余额：</label>
                    <div class="col-sm-6">
                        <input type="text" style="width: 70%" disabled="disabled" id="balance" class="easyui-validatebox form-control"
                               onkeyup="value=value.match(/\d+\.?\d{0,2}/,'')"
                               value=${balance} />
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    var dataTest = [];
    $('#firstCombobox1').change(function () {
        <#list mapHolder as k,v >
        if ($('#firstCombobox1').val() == '${k}') {
            dataTest.length = 0;
            <#list v as item >
            dataTest.push('${item}');
            </#list>
        } else {
        }
        </#list>
        $('#dropdown').empty();
        $(function () {
            $.each(dataTest, function () {
                $('#dropdown').append("<option value=" + this + ">" + this + "</option>");
            });
        })
    })
    $('#dropdown').change(function () {
        console.log($('#dropdown').val());
    })


    function updateInputTest() {
        var input1 = document.getElementById("roll");
        var input3 = document.getElementById("balance");
        input3.value = Number(${balance}) - Number(input1.value)
    }
</script>