<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    .datagrid-editable .select2-container--bootstrap4 .select2-selection--single {
        height: calc(1.8125rem + 2px) !important;
        line-height: calc(1.8125rem + 2px) !important;
    }

    .datagrid-editable .select2-container--bootstrap4 .select2-selection--single .select2-selection__rendered {
        line-height: calc(1.8125rem + 2px) !important;
    }
</style>

<script>
    // 1、第一步
    $.showcase.roweditor.initMapping();
</script>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;" align="left">
        <form id="manage_roweditor_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">用户名：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_username"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">姓名：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_realName"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">手机号码：</label>
                <input type="text" class="form-control form-control-sm" name="search_EQ_mobileNo"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">状态：</label>
                <select name="search_EQ_status" class="form-control input-sm select2bs4">
                    <option value="">所有</option><#list allStatuss as k,v>
                    <option value="${k}">${v}</option></#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_roweditor_searchform','manage_roweditor_datagrid');"><i class="fa fa-search fa-lg fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false" class="ac-form-search">
        <table id="manage_roweditor_datagrid" class="easyui-datagrid" url="/manage/demo/roweditor/roweditor/listJson.html" toolbar="#manage_roweditor_toolbar" fit="true" border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true"
               data-options="onDblClickCell:$.showcase.roweditor.onClickCell, onEndEdit:$.showcase.roweditor.onEndEdit">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true">ID</th>
                <th field="memberNo" data-options="
                    editor:{
                        type: 'validatebox',
                        options:{
                            validType: ['text','length[1,32]'],
                            required: true
                        }
                    }">会员编码
                </th>
                <th field="username" data-options="editor:{ type:'acoolyText',
                        options:{
                            validType:'[\'account\',\'length[8,16]\']',
                            required: true,
                            placeholder:'用户名',
                            inputmask:'\'alias\':\'account\''
                        }
                }">用户名
                </th>
                <th field="realName" style="width: 60px;" data-options="editor:{ type:'acoolyText',
                        options:{ validType:'[\'chinese\',\'length[2,10]\']',
                            placeholder:'姓名...',
                            required: true
                        }
                }">姓名
                </th>
                <th field="idcardNo" style="width: 200px;" data-options="editor:{
                        type:'acoolyText',
                        options:{
                            validType:'[\'idcard\',\'length[18,18]\']',
                            required: true,
                            placeholder:'请输入身份证号码...',
                            inputmask:'\'alias\':\'idcard\'',
                            events:{
                                blur: function(obj){
<#--                                    $.showcase.roweditor.cellVal('age',33);-->
                                   $.showcase.roweditor.parseIdCard(obj);
                                },
                                change : function(){ console.info('event:onChange');}
                            }
                        }
                }">身份证号码
                </th>
                <th field="age" sortable="true" sum="true" data-options="editor:{
                        type:'acoolyText',
                        options:{
                            validType:'[\'number[1,120]\']',
                            placeholder:'年龄...',
                        }
                }">年龄
                </th>
                <th field="birthday" style="width: 120px;" formatter="dateFormatter" data-options="editor:{
                        type:'datebox',options:{required:true}
                }">生日
                </th>
                <th field="gender" style="width: 100px;" formatter="mappingFormatter" data-options="editor:{
                            type:'combobox',
                            options:{ valueField:'key',textField:'value',
                               method:'post', url:'/manage/demo/roweditor/roweditor/data.html?name=Gender',
                               required:true
                            }
                    }">性别
                </th>
                <th field="animal" style="width: 100px;" formatter="mappingFormatter" data-options="editor:{
                            type:'select2',
                            options:{
                                data:$.showcase.roweditor.mapping['allAnimals']
                            }
                        }">生肖
                </th>
                <th field="mobileNo" data-options="
                    styler: function(value,row,index){
                      return 'padding:0 10px 0 0;';
                    },
                    editor:{
                        type: 'validatebox',
                        options:{ validType:['mobile','length[1,11]']
                    }
                    }">手机号码
                </th>
                <th field="registryChannel" formatter="mappingFormatter" data-options="editor:{
                            type:'select2',
                            options:{
                               data:$.showcase.roweditor.mapping['allRegistryChannels']
                            }
                        }">注册渠道
                </th>
                <th field="mail">邮件</th>
                <th field="website" formatter="contentFormatter">网址</th>
                <th field="doneRatio" formatter="percentFormatter" data-options="editor:{type:'numberbox'}" sortable="true" sum="true">完成度</th>
                <th field="salary" formatter="centMoneyFormatter" data-options="editor:{type:'numberbox',options:{precision:2}}" sortable="true" sum="true">薪水</th>
                <th field="status" style="width: 80px;" formatter="mappingFormatter" data-options="
                    editor:{ type:'select2',
                        options:{data:$.showcase.roweditor.mapping['allStatuss'] }
                    }">状态
                </th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">更新时间</th>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_roweditor_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>

        <!-- 每行的Action动作模板 -->
        <div id="manage_roweditor_action" style="display: none;">
            <a onclick="$.acooly.framework.edit({url:'/manage/demo/roweditor/roweditor/edit.html',id:'{0}',entity:'roweditor',width:500,height:500});" href="#" title="编辑"><i class="fa fa-pencil fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.show('/manage/demo/roweditor/roweditor/show.html?id={0}',500,500);" href="#" title="查看"><i class="fa fa-file-o fa-lg fa-fw fa-col"></i></a>
            <a onclick="$.acooly.framework.remove('/manage/demo/roweditor/roweditor/deleteJson.html','{0}','manage_roweditor_datagrid');" href="#" title="删除"><i class="fa fa-trash-o fa-lg fa-fw fa-col"></i></a>
        </div>

        <!-- 表格的工具栏 -->
        <div id="manage_roweditor_toolbar">
            <a href="#" id="manage_roweditor_toolbar_insert" class="easyui-linkbutton" plain="true" onclick="$.showcase.roweditor.insertFirst()"><i class="fa fa-plus-square-o fa-lg fa-fw fa-col"></i>插入</a>
            <a href="#" id="manage_roweditor_toolbar_accept" class="easyui-linkbutton" plain="true" onclick="$.showcase.roweditor.accept()"><i class="fa fa-floppy-o fa-lg fa-fw fa-col"></i>保存</a>
            <a href="#" id="manage_roweditor_toolbar_reject" class="easyui-linkbutton" plain="true" onclick="$.showcase.roweditor.reject()"><i class="fa fa-arrow-circle-o-left fa-lg fa-fw fa-col"></i>取消</a>
            <a href="#" id="manage_roweditor_toolbar_delete" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/demo/roweditor/roweditor/deleteJson.html','manage_roweditor_datagrid')"><i class="fa fa-trash fa-lg fa-fw fa-col"></i>删除</a>
            <a href="#" id="manage_roweditor_toolbar_create" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/demo/roweditor/roweditor/create.html',entity:'roweditor',width:500,height:500})"><i class="fa fa-plus-square fa-lg fa-fw fa-col"></i>添加</a>

            <a href="#" id="manage_roweditor_toolbar_question" class="easyui-linkbutton easyui-tooltip" plain="true" title="双击行进入编辑模式"><i class="fa fa-question-circle fa-lg fa-fw fa-col"></i></a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_roweditor_searchform', 'manage_roweditor_datagrid');
        });
    </script>
</div>
