<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_showcaseMember_editform" class="form-horizontal" action="/manage/showcase/member/showcaseMember/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post">
        <@jodd.form bean="showcaseMember" scope="request">
            <input name="id" type="hidden"/>
            <div>
                <ul class="nav nav-tabs" id="manage_showcaseMember_editform_tabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" id="manage_showcaseMember_editform_basic_tab" data-toggle="pill" href="#manage_showcaseMember_editform_basic" role="tab" aria-controls="manage_showcaseMember_editform_basic" aria-selected="true">基本信息</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="manage_showcaseMember_editform_profile_tab" data-toggle="pill" href="#manage_showcaseMember_editform_profile" role="tab" aria-controls="manage_showcaseMember_editform_profile" aria-selected="false">扩展信息</a>
                    </li>
                </ul>
                <div class="tab-content" id="manage_showcaseMember_editform_tabContents">
                    <div class="tab-pane fade active show" id="manage_showcaseMember_editform_basic" role="tabpanel" aria-labelledby="manage_showcaseMember_editform_basic_tab">
                        <div class="card-body">

                            <div>
                                <div class="form-group row">
                                    <label class="col-sm-2 col-form-label">用户名
                                        <a title="?远程服务器端检测用户名是否重复的easyui验证待开发" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                                    </label>
                                    <div class="col-sm-4">
                                        <input type="text" name="username" placeholder="请输入用户名..." class="easyui-validatebox form-control" data-inputmask="'alias':'account'" data-mask
                                               data-options="validType:['account','length[1,32]']" required="true"/>
                                    </div>

                                    <label class="col-sm-2 col-form-label">用户编码
                                        <a title="文本框后置按钮点击访问远程获取数据，然后回显到文本框" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                                        <a title="开发说明" target="_blank" href="https://acooly.cn/docs/core/acooly-boss-experience.html"><i class="fa fa-question-circle" aria-hidden="true"></i></a>
                                    </label>
                                    <div class="col-sm-4">
                                        <#if action=='create'>
                                            <div class="input-group">
                                                <input type="text" name="memberNo" placeholder="输入,生成编码或自动生成..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,64]']"/>
                                                <span class="input-group-append"><button name="memberNoGenerate" onclick="$.acooly.showcase.member.memberNoGenerate(this);" type="button" class="btn btn-success btn-sm btn-flat">生成</button></span>
                                            </div>
                                        <#else >
                                            <div class="col-form-content">${showcaseMember.memberNo}</div>
                                        </#if>
                                    </div>
                                </div>

                                <!-- 字段集区块1 -->
                                <fieldset class="layui-elem-field">
                                    <legend style="width: auto;font-size: 16px;">认证信息</legend>
                                    <div class="layui-field-box">
                                        <div class="form-group row">
                                            <label class="col-sm-2 col-form-label">姓名</label>
                                            <div class="col-sm-4">
                                                <input type="text" name="realName" placeholder="请输入姓名..." class="easyui-validatebox form-control" data-options="validType:['chinese','length[1,16]']" required="true"/>
                                            </div>
                                            <label class="col-sm-2 col-form-label">身份证号码
                                                <a title="身份证号码远程解析后，回写生日，年龄，性别和属相字段" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                                                <a title="开发说明" target="_blank" href="https://acooly.cn/docs/core/acooly-boss-experience.html"><i class="fa fa-question-circle" aria-hidden="true"></i></a>
                                            </label>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <input type="text" name="idcardNo" onblur="$.acooly.showcase.member.idCardVerify($(this))" placeholder="请输入身份证号码..." class="easyui-validatebox form-control" data-inputmask="'alias':'idcard'" data-mask data-options="validType:['idcard','length[1,48]']" required="true"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-2 col-form-label">生日</label>
                                            <div class="col-sm-4">
                                                <input type="text" name="birthday" placeholder="请输入生日..." class="easyui-validatebox form-control" value="<#if showcaseMember.birthday??>${showcaseMember.birthday?string('yyyy-MM-dd')}</#if>" onchange="$(this).validatebox('isValid');" onFocus="WdatePicker({readOnly:false,dateFmt:'yyyy-MM-dd'})" data-options="required:true"/>
                                            </div>
                                            <label class="col-sm-2 col-form-label">年龄</label>
                                            <div class="col-sm-4">
                                                <input type="text" name="age" placeholder="请输入年龄..." class="easyui-validatebox form-control" data-options="validType:['number[0,127]']"/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-2 col-form-label">性别</label>
                                            <div class="col-sm-4">
                                                <select name="gender" class="form-control select2bs4" data-options="required:true">
                                                    <#list allGenders as k,v>
                                                        <option value="${k}">${v}</option></#list>
                                                </select>
                                            </div>
                                            <label class="col-sm-2 col-form-label">生肖</label>
                                            <div class="col-sm-4">
                                                <select name="animal" class="form-control select2bs4">
                                                    <#list allAnimals as k,v>
                                                        <option value="${k}">${v}</option></#list>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>

                                <!-- 字段集区块2 -->
                                <fieldset class="layui-elem-field layui-field-title">
                                    <legend style="width: auto;font-size: 16px;">其他信息</legend>
                                    <div class="layui-field-box">
                                        <div class="form-group row">
                                            <label class="col-sm-2 col-form-label">手机号码</label>
                                            <div class="col-sm-4">
                                                <input type="text" name="mobileNo" placeholder="请输入手机号码..." class="easyui-validatebox form-control" data-inputmask="'alias':'mobile'" data-mask data-options="validType:['mobile','length[1,11]']"/>
                                            </div>
                                            <label class="col-sm-2 col-form-label">邮件</label>
                                            <div class="col-sm-4">
                                                <input type="text" name="mail" placeholder="请输入邮件..." class="easyui-validatebox form-control" data-inputmask="'alias':'email'" data-mask data-options="validType:['email','length[1,64]']"/>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-2 col-form-label">完成度
                                                <a title="后置标签案例；精确到小数点后两位的百分数，实则万分数，数据库存储为万分整数（实际值需除以10000）" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                                            </label>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <input type="text" name="doneRatio" placeholder="请输入完成度..." class="easyui-validatebox form-control" data-inputmask="'alias':'money','min':0,'max':100" data-mask data-options="validType:['money[0,100]']"/>
                                                    <div class="input-group-append">
                                                        <div class="input-group-text"><i class="fa fa-percent" aria-hidden="true"></i></div>
                                                    </div>
                                                </div>
                                            </div>
                                            <label class="col-sm-2 col-form-label">薪水
                                                <a title="前置标签案例，两位小数的金额，实体数据类型为Money，数据库存储单位为分。" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                                            </label>
                                            <div class="col-sm-4">
                                                <div class="input-group">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text">
                                                          <i class="fa fa-jpy"></i>
                                                        </span>
                                                    </div>
                                                    <input type="text" name="salary" placeholder="请输入薪水..." class="easyui-validatebox form-control" data-inputmask="'alias':'money','min':0,'max':999999999" data-mask data-options="validType:['money[0,999999999]']"/>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <label class="col-sm-2 col-form-label">客户类型
                                                <a title="演示下拉框注册`onchange`事件：选择`普通`则隐藏下面行的两个表单，否则显示。注意：编辑时，通过服务器端值初始化当前状态。" class="easyui-tooltip"><i class="fa fa-info-circle" aria-hidden="true"></i></a>
                                                <a title="开发说明" target="_blank" href="https://acooly.cn/docs/core/acooly-boss-experience.html"><i class="fa fa-question-circle" aria-hidden="true"></i></a>
                                            </label>
                                            <div class="col-sm-4">
                                                <select name="customerType" class="form-control select2bs4">
                                                    <#list allCustomerTypes as k,v><option value="${k}">${v}</option></#list>
                                                </select>
                                            </div>
                                            <label class="col-sm-2 col-form-label">网址</label>
                                            <div class="col-sm-4">
                                                <input type="text" name="website" placeholder="请输入网址..." class="easyui-validatebox form-control" data-inputmask="'alias':'url'" data-mask data-options="validType:['url','length[1,128]']"/>
                                            </div>
                                        </div>

                                        <div id="manage_showcaseMember_editform_customType_container" class="form-group row">
                                            <label class="col-sm-2 col-form-label">数字类型</label>
                                            <div class="col-sm-4">
                                                <select name="numStatus" class="form-control select2bs4">
                                                    <#list allNumStatuss as k,v>
                                                        <option value="${k}">${v}</option></#list>
                                                </select>
                                            </div>
                                            <label class="col-sm-2 col-form-label">推送广告</label>
                                            <div class="col-sm-4">
                                                <select name="pushAdv" class="form-control select2bs4">
                                                    <#list allPushAdvs as k,v>
                                                        <option value="${k}">${v}</option></#list>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-sm-2 col-form-label">注册渠道</label>
                                            <div class="col-sm-4">
                                                <select name="registryChannel" class="form-control select2bs4">
                                                    <#list allRegistryChannels as k,v>
                                                        <option value="${k}">${v}</option></#list>
                                                </select>
                                            </div>

                                            <label class="col-sm-2 col-form-label">状态</label>
                                            <div class="col-sm-4">
                                                <select name="status" class="form-control select2bs4" data-options="required:true">
                                                    <#list allStatuss as k,v>
                                                        <option value="${k}">${v}</option></#list>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>


                            </div>


                            <!-- 内容信息 -->
                            <div class="card">
                                <div class="card-header">
                                    <h3 class="card-title"><i class="fa fa-text-width"></i> 内容信息</h3>
                                    <div class="card-tools">
                                        <button type="button" class="btn btn-tool" data-card-widget="collapse"><i class="fa fa-minus"></i></button>
                                    </div>
                                    <div class="card-tools">
                                        <button type="button" class="btn btn-tool" data-card-widget="maximize"><i class="fa fa-expand"></i></button>
                                    </div>
                                </div>
                                <!-- /.card-header -->
                                <div class="card-body">
                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">摘要</label>
                                        <div class="col-sm-10">
                                            <input type="text" name="subject" placeholder="请输入摘要..." class="easyui-validatebox form-control" data-options="validType:['text','length[1,128]']"/>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">备注</label>
                                        <div class="col-sm-10">
                                            <textarea rows="5" cols="40" placeholder="请输入备注..." name="comments" class="easyui-validatebox form-control form-words" data-words="255"></textarea>
                                        </div>
                                    </div>
                                </div>
                                <!-- /.card-body -->
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane fade" id="manage_showcaseMember_editform_profile" role="tabpanel" aria-labelledby="manage_showcaseMember_editform_profile_tab">
                        <div class="card-body">
                            <div class="form-group row">
                                <div class="col-sm-12">
                                    <div style="color:red;">多媒体编辑器：H5批量图片上传，剪贴板粘贴图片上传，视频分享，手机视图等扩展应用。</div>
                                    <textarea id="manage_showcaseMember_editform_context" name="context" data-options="required:true" style="width:100%;height:410px;"></textarea>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
    <script>


        $(function () {
            // 初始化扩展信息多媒体编辑器
            $.acooly.showcase.member.initEditor();
            // 注册`客户类型`下拉选择的onchange事件，通过事件控制下面分部表单的显示/隐藏
            $.acooly.formObj("manage_showcaseMember_editform", "customerType").on('select2:select', function (e) {
                // 选择的option对应的数据
                let data = e.params.data;
                console.log(data);
                $.acooly.showcase.member.toggleCustomTypes(data.id);
            });

            let savedCustomerType = '${showcaseMember.customerType}';
            if (savedCustomerType) {
                $.acooly.showcase.member.toggleCustomTypes(savedCustomerType);
            }
        });

    </script>
</div>
