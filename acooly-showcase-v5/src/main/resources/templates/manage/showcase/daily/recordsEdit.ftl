<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_records_editform" class="form-horizontal"
          action="/manage/showcase/daily/records/<#if action=='create'>saveJson<#else>updateJson</#if>.html"
          method="post">
        <@jodd.form bean="records" scope="request">
            <input name="id" type="hidden"/>
            <input name="userName" type="hidden" value=${userName} />
            <div class="card-body card-blue"  id="cardCreate">
                <#list mapQuery as k,v>
                    <label class="col-sm-12 " style="text-align: center;">账户名称：${k}</label>
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label" >今日花销</label>
                        <div class="col-sm-6">
                            <input type="text"  placeholder="请输入今日花销..." onblur="updateInput${k}()" onkeyup="value=value.match(/\d+\.?\d{0,2}/,'')"
                                   required class="easyui-validatebox form-control" id="${k}_1" name=${k}   />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label">充值金额</label>
                        <div class="col-sm-6">
                            <input type="text" required placeholder="请输入充值金额..." onblur="updateInput${k}_1()"
                                   onkeyup="value=value.match(/\d+\.?\d{0,2}/,'')"
                                   class="easyui-validatebox form-control" id="${k}_2" name=${k}recharge  />
                        </div>
                    </div>
                    <div class="form-group row" >
                        <label class="col-sm-3 col-form-label">最后余额</label>
                        <div class="col-sm-6">
                        <input type="text" disabled="disabled" onkeyup="value=value.match(/\d+\.?\d{0,2}/,'')"
                               required class="easyui-validatebox form-control" id="${k}_3" value=${v} />
                        </div>
                    </div>
                </#list>
            </div>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">记录时间</label>
                    <div class="col-sm-6">
                        <input name="buildTime" type="text"
                               class="easyui-datebox form-control" required="required"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">地区</label>
                    <div class="col-sm-6">
                        <select name="region" class="form-control select2bs4" data-options="required:true">
                            <#list regionList as v >
                                <option value="${v}">${v}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">业绩</label>
                    <div class="col-sm-6">
                        <input type="text" required name="grades" placeholder="请输入业绩..."
                               class="easyui-validatebox form-control"/>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
    <script type="text/javascript">
        <#list mapQuery as k,v>
        function updateInput${k}() {
            var input1 = document.getElementById("${k}_1");
            var input2 = document.getElementById("${k}_2");
            var input3 = document.getElementById("${k}_3");
            input3.value = (Number(${v})-Number(input1.value)+Number(input2.value))
            input3.value = input3.value.match(/\d+\.?\d{0,2}/)[0];
        }
        function updateInput${k}_1() {
            var input1 = document.getElementById("${k}_1");
            var input2 = document.getElementById("${k}_2");
            var input3 = document.getElementById("${k}_3");
            input3.value = Number(input2.value)+Number(${v})-Number(input1.value)
            input3.value = input3.value.match(/\d+\.?\d{0,2}/)[0];
        }
        </#list>

        // var action=$("#manage_records_editform").attr("action");
        // if (action=='/manage/showcase/daily/records/updateJson.html'){
        //
        // }
    </script>
</div>
