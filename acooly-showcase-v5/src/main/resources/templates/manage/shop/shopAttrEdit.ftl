<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<style>
    .attr-edit-container {
        background: #f5f6f8;
        padding: 20px;
    }
    
    .form-card {
        background: #fff;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    }
    
    .form-group label {
        color: #374151;
        font-weight: 600;
        font-size: 14px;
    }
    
    .form-control {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 8px 12px;
        transition: all 0.2s;
    }
    
    .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99,102,241,0.15);
    }
</style>
<div class="attr-edit-container">
    <div class="form-card">
    <form id="manage_shopAttr_editform" class="form-horizontal" action="/manage/shop/shopAttr/<#if action=='create'>saveJson<#else>updateJson</#if>.html" method="post" >
		<@jodd.form bean="shopAttr" scope="request">
        <input name="id" type="hidden" />
		<div class="card-body">
			<div class="form-group row">
                        <label class="col-sm-3 col-form-label">属性名称</label>
				<div class="col-sm-9">
                            <input type="text" name="name" placeholder="请输入属性名称（如：性别、风格、材质）..." class="easyui-validatebox form-control"  data-options="validType:['text','length[1,50]']" required="true"/>
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">状态</label>
				<div class="col-sm-9">
                            <select name="status" class="form-control select2bs4">
                                <option value="1" <#if shopAttr.status?? && shopAttr.status == 1>selected</#if>>启用</option>
                                <option value="0" <#if shopAttr.status?? && (shopAttr.status == 0 || !shopAttr.status??)>selected</#if>>禁用</option>
                            </select>
				</div>
			</div>
        </div>
      </@jodd.form>
    </form>
</div>
</div>
<script>
    $(function () {
        // 初始化select2
        $('.select2bs4').select2({
            theme: 'bootstrap4',
            placeholder: "请选择",
            allowClear: true
        });
    });
</script>
