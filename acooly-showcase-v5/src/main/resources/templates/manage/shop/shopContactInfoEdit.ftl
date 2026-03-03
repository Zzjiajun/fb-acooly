<#assign jodd=JspTaglibs["http://www.springside.org.cn/jodd_form"] />
<div>
    <form id="manage_shopContactInfo_editform" class="form-horizontal"
          action="/manage/shop/shopContactInfo/<#if action=='create'>saveJson<#else>updateJson</#if>.html"
          method="post">
        <@jodd.form bean="shopContactInfo" scope="request">
            <input name="id" type="hidden"/>
            <div class="card-body">
                <#--			<div class="form-group row">-->
                <#--				<label class="col-sm-3 col-form-label">-- address, phone, email, line, whatsapp, instagram, ...</label>-->
                <#--				<div class="col-sm-9">-->
                <#--					<textarea rows="3" cols="40" placeholder="请输入-- address, phone, email, line, whatsapp, instagram, ......" name="type" class="easyui-validatebox form-control form-words" data-words="255" ></textarea>-->
                <#--				</div>-->
                <#--			</div>-->
                <#if action=='create'>
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label">名字</label>
                        <div class="col-sm-9">
                            <!-- 方式1：Select2 下拉框（带图标） -->
                            <select name="name" id="contactNameSelect" class="form-control select2bs4" data-options="required:true" required style="width: 100%;">
                                <#list contactTypes as v >
                                    <option value="${v}">${v}</option>
                                </#list>
                            </select>
                            
                            <!-- 方式2：单选按钮组（带图标）- 注释掉，如需使用请取消注释并注释掉上面的 select -->
                            <#--<div class="contact-type-radio-group">
                                <#list contactTypes as v >
                                    <label class="contact-type-radio-label">
                                        <input type="radio" name="name" value="${v}" required>
                                        <span class="contact-type-icon" data-type="${v?lower_case}"></span>
                                        <span class="contact-type-label" data-type="${v?lower_case}">${v}</span>
                                    </label>
                                </#list>
                            </div>-->
                        </div>
                    </div>
                </#if>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">实际内容</label>
                    <div class="col-sm-9">
                        <textarea rows="3" cols="40" placeholder="请输入实际内容..." name="value"
                                  class="easyui-validatebox form-control form-words" data-words="255"></textarea>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">团队</label>
                    <div class="col-sm-9">
                        <select name="teamId" class="form-control select2bs4" data-options="required:true" required>
                            <#list shopTeamMap as k,v >
                                <option value="${k}">${v}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label">是否显示</label>
                    <div class="col-sm-9">
                        <input type="radio" name="isActive" value="1" checked> 显示
                        <input type="radio" name="isActive" value="0"> 不显示
                    </div>
                </div>
            </div>
        </@jodd.form>
    </form>
</div>

<style>
    /* 联系方式类型单选按钮组样式 */
    .contact-type-radio-group {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
        margin-top: 8px;
    }
    
    .contact-type-radio-label {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 10px 16px;
        border: 2px solid #e5e7eb;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.2s;
        background: #fff;
        position: relative;
    }
    
    .contact-type-radio-label:hover {
        border-color: #6366f1 !important;
        background: #f9fafb !important;
        transform: translateY(-1px);
        box-shadow: 0 2px 4px rgba(99, 102, 241, 0.2);
    }
    
    .contact-type-radio-label input[type="radio"] {
        margin: 0;
        cursor: pointer;
    }
    
    .contact-type-radio-label.selected,
    .contact-type-radio-label input[type="radio"]:checked ~ * {
        border-color: #6366f1 !important;
        background: #eef2ff !important;
    }
    
    .contact-type-radio-label.selected .contact-type-label,
    .contact-type-radio-label input[type="radio"]:checked ~ .contact-type-label {
        font-weight: 600;
        color: #6366f1;
    }
    
    .contact-type-icon {
        font-size: 18px;
        width: 20px;
        text-align: center;
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }
    
    .contact-type-label {
        font-weight: 500;
        color: #374151;
    }
    
    /* Select2 下拉框图标样式 */
    .select2-container--bootstrap4 .select2-results__option {
        padding: 8px 12px;
    }
    
    .select2-container--bootstrap4 .select2-selection__rendered {
        padding-left: 8px;
    }
</style>

<script type="text/javascript">
    $(function() {
        // 联系方式图标映射表（复用列表页面的逻辑）
        var contactIconMap = {
            'address': { icon: 'fa-map-marker', color: '#e74c3c', label: '地址' },
            'phone': { icon: 'fa-phone', color: '#27ae60', label: '电话' },
            'email': { icon: 'fa-envelope', color: '#3498db', label: '邮箱' },
            'line': { icon: 'fa-comment', color: '#00c300', label: 'Line' },
            'facebook': { icon: 'fa-facebook', color: '#1877f2', label: 'Facebook' },
            'whatsapp': { icon: 'fa-whatsapp', color: '#25d366', label: 'WhatsApp' },
            'instagram': { icon: 'fa-instagram', color: '#e4405f', label: 'Instagram' },
            'twitter': { icon: 'fa-twitter', color: '#1da1f2', label: 'Twitter' },
            'telegram': { icon: 'fa-paper-plane', color: '#0088cc', label: 'Telegram' },
            'tiktok': { icon: 'fa-video-camera', color: '#000000', label: 'TikTok' }
        };
        
        // 获取图标配置的函数
        function getContactIconConfig(value) {
            if (!value) return null;
            var nameLower = value.toLowerCase().trim();
            return contactIconMap[nameLower] || null;
        }
        
        // 方式1：初始化 Select2 下拉框（带图标）
        $('#contactNameSelect').select2({
            theme: 'bootstrap4',
            width: '100%',
            placeholder: '请选择联系方式类型',
            allowClear: false,
            // 自定义下拉选项显示格式（带图标）
            templateResult: function(option) {
                if (!option.id) {
                    return option.text;
                }
                var config = getContactIconConfig(option.id);
                if (config) {
                    var $result = $('<div style="display: flex; align-items: center; gap: 8px;">' +
                        '<i class="fa ' + config.icon + '" style="color: ' + config.color + '; font-size: 16px; width: 20px; text-align: center;"></i>' +
                        '<span style="font-weight: 500; color: #374151;">' + config.label + '</span>' +
                        '</div>');
                    return $result;
                }
                return option.text;
            },
            // 自定义选中项显示格式（带图标）
            templateSelection: function(option) {
                if (!option.id) {
                    return option.text;
                }
                var config = getContactIconConfig(option.id);
                if (config) {
                    var $selection = $('<div style="display: flex; align-items: center; gap: 8px;">' +
                        '<i class="fa ' + config.icon + '" style="color: ' + config.color + '; font-size: 16px; width: 20px; text-align: center;"></i>' +
                        '<span style="font-weight: 500; color: #374151;">' + config.label + '</span>' +
                        '</div>');
                    return $selection;
                }
                return option.text;
            }
        });
        
        // 方式2：初始化单选按钮组图标显示
        function initContactTypeRadioIcons() {
            $('.contact-type-icon').each(function() {
                var type = $(this).data('type');
                var config = getContactIconConfig(type);
                if (config) {
                    $(this).html('<i class="fa ' + config.icon + '" style="color: ' + config.color + ';"></i>');
                }
            });
            
            $('.contact-type-label').each(function() {
                var type = $(this).data('type');
                var config = getContactIconConfig(type);
                if (config) {
                    $(this).text(config.label);
                }
            });
            
            // 监听单选按钮变化，更新样式
            $('.contact-type-radio-group input[type="radio"]').on('change', function() {
                $('.contact-type-radio-label').removeClass('selected');
                $(this).closest('.contact-type-radio-label').addClass('selected');
            });
            
            // 设置初始选中状态样式
            $('.contact-type-radio-group input[type="radio"]:checked').closest('.contact-type-radio-label').addClass('selected');
        }
        
        // 如果使用单选按钮组，取消下面的注释
        // initContactTypeRadioIcons();
    });
</script>
