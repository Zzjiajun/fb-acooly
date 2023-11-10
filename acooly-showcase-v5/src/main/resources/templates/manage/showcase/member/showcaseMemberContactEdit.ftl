<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<script type="text/javascript" src="/manage/assert/plugin/area/area.js" charset="utf-8"></script>
<div>
    <form id="manage_showcaseMemberContact_editform" class="form-horizontal" action="/manage/showcase/member/showcaseMemberContact/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
        <@jodd.form bean="showcaseMemberContact" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">用户编码</label>
                    <div class="col-sm-9 col-form-content">
                        <#if action=='create'>${RequestParameters.memberNo}<#else>${showcaseMemberContact.memberNo}</#if>
                        <input type="hidden" name="memberNo" value="<#if action=='create'>${RequestParameters.memberNo}<#else>${showcaseMemberContact.memberNo}</#if>"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">联系人</label>
                    <div class="col-sm-9">
                        <input type="text" name="username" placeholder="请输入联系人名..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">手机号码</label>
                    <div class="col-sm-9">
                        <input type="text" name="mobileNo" placeholder="请输入手机号码..." class="easyui-validatebox form-control" data-inputmask="'alias':'mobile'" data-mask data-options="validType:['mobile','length[1,16]']" required="true"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">区域</label>
                    <div class="col-sm-9 row">
                        <div class="col-md-4"><select id="s_province" name="province" class="form-control select2bs4"></select></div>
                        <div class="col-md-4"><select id="s_city" name="city" class="form-control select2bs4"></select></div>
                        <div class="col-md-4"><select id="s_county" name="district" class="form-control select2bs4"></select></div>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">地址</label>
                    <div class="col-sm-9">
                        <input type="text" name="address" placeholder="请输入地址..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,128]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">邮政编码</label>
                    <div class="col-sm-9">
                        <input type="text" name="zip" placeholder="请输入邮政编码..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,16]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">邮箱</label>
                    <div class="col-sm-9">
                        <input type="text" name="email" placeholder="请输入邮箱..." class="easyui-validatebox form-control" data-inputmask="'alias':'email'" data-mask data-options="validType:['email','length[1,128]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">QQ账号</label>
                    <div class="col-sm-9">
                        <input type="text" name="qq" placeholder="请输入QQ账号..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,16]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">旺旺账号</label>
                    <div class="col-sm-9">
                        <input type="text" name="wangwang" placeholder="请输入旺旺账号..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">微信账号</label>
                    <div class="col-sm-9">
                        <input type="text" name="wechat" placeholder="请输入微信账号..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">脸书账号</label>
                    <div class="col-sm-9">
                        <input type="text" name="facebeek" placeholder="请输入脸书账号..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">谷歌账号</label>
                    <div class="col-sm-9">
                        <input type="text" name="google" placeholder="请输入谷歌账号..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">公司名称</label>
                    <div class="col-sm-9">
                        <input type="text" name="companyName" placeholder="请输入公司名..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">公司电话</label>
                    <div class="col-sm-9">
                        <input type="text" name="phoneNo" placeholder="请输入联系电话..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,32]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">职业</label>
                    <div class="col-sm-9">
                        <input type="text" name="career" placeholder="请输入职业..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,16]']"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">备注</label>
                    <div class="col-sm-9">
                        <textarea rows="3" cols="40" placeholder="请输入备注..." name="comments" class="easyui-validatebox form-control"></textarea>
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
    <script type="text/javascript">

        $(function () {
            _init_area();
            initAreaValue();
        });

        function initAreaValue() {
            <#if showcaseMemberContact != null>
            $.acooly.formVal("manage_showcaseMemberContact_editform", "province", '${showcaseMemberContact.province}');
            change(1);
            $.acooly.formVal("manage_showcaseMemberContact_editform", "city", '${showcaseMemberContact.city}');
            change(2);
            $.acooly.formVal("manage_showcaseMemberContact_editform", "district", '${showcaseMemberContact.district}');
            </#if>
        }
    </script>
</div>
