<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_dmCenter_editform" class="form-horizontal" action="/manage/showcase/daily/dmCenter/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
        <@jodd.form bean="dmCenter" scope="request">
            <input name="id" type="hidden" />
            <div class="card-body">
                <#--			<div class="form-group row">-->
                <#--				<label class="col-sm-3 col-form-label">用户名</label>-->
                <#--				<div class="col-sm-9">-->
                <#--					<textarea rows="3" cols="40" placeholder="请输入用户名..." name="userName" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
                <#--				</div>-->
                <#--			</div>-->
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">地区</label>
                    <div class="col-sm-9">
                        <select name="region" class="form-control select2bs4" data-options="required:true">
                            <#list regionList as v >
                                <option value="${v}">${v}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">像素代码</label>
                    <div class="col-sm-9">
                        <textarea rows="3"   id="inputTextArea"  cols="40" oninput="formatInput()"  placeholder="请输入像素代码..." name="pixel" class="easyui-validatebox form-control" ></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">链接地址</label>
                    <div class="col-sm-9">
                        <textarea rows="2" cols="40" placeholder="请输入链接地址..." name="link" class="easyui-validatebox form-control form-words" data-words="255" data-options="required:true"></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">主域名</label>
                    <div class="col-sm-9">
                        <select name="domain" id="domainTest" class="form-control select2bs4" data-options="required:true">
                            <option value="">全部</option>
                            <#list domainList as domainList >
                                <option value="${domainList}">${domainList}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">二级域名</label>
                    <div class="col-sm-9">
                        <select name="secondaryDomain" id="secondaryDomain" class="form-control select2bs4" data-options="required:true">
                            <option value=""></option>
                        </select>

                    </div>
                </div>
                <#--			<div class="form-group row">-->
                <#--				<label class="col-sm-3 col-form-label">域名访问量</label>-->
                <#--				<div class="col-sm-9">-->
                <#--					<input type="text" name="visitsNumber" placeholder="请输入域名访问量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
                <#--				</div>-->
                <#--			</div>-->
                <#--			<div class="form-group row">-->
                <#--				<label class="col-sm-3 col-form-label">按钮点击数量</label>-->
                <#--				<div class="col-sm-9">-->
                <#--					<input type="text" name="clicksNumber" placeholder="请输入按钮点击数量..." class="easyui-validatebox form-control" data-options="validType:['number[0,999999999]']"/>-->
                <#--				</div>-->
                <#--			</div>-->
            </div>
        </@jodd.form>
    </form>
    <script>
        function formatInput() {
            var inputTextArea = document.getElementById('inputTextArea');
            var inputValue = inputTextArea.value.replace(/\D/g, ''); // 移除非数字字符
            var formattedValue = '';
            for (var i = 0; i < inputValue.length; i++) {
                formattedValue += inputValue[i];
                if ((i + 1) % 15 === 0) { // 每15个数字添加换行符
                    formattedValue += ","+'\n';
                }
            }
            inputTextArea.value = formattedValue;
        }



        var dataTest2 = [];

        $('#domainTest').change(function () {
            <#list mapDomain as k,v >
            if ($('#domainTest').val() == '${k}') {
                dataTest2.length = 0;
                <#list v as item >
                dataTest2.push('${item}');
                </#list>
            } else {
            }
            </#list>
            $('#secondaryDomain').empty();
            $(function () {
                $.each(dataTest2, function () {
                    $('#secondaryDomain').append("<option value=" + this + ">" + this + "</option>");
                });
            })
        })
    </script>
</div>
