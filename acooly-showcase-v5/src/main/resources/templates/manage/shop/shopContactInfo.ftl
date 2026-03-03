<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<style>
    /* 🎨 整体样式优化 */
    .contact-info-container {
        background: #f5f7fa;
        min-height: 100vh;
    }

    .team-link-btn {
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 6px 12px;
        font-size: 12px;
        font-weight: 500;
        cursor: pointer;
        transition: 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        max-width: 100%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .team-link-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
    }

    .team-link-btn i {
        font-size: 14px;
    }

    /* 🔍 搜索表单样式 */
    #manage_shopContactInfo_searchform {
        background: #ffffff;
        border-radius: 12px;
        padding: 20px;
        margin: 15px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
        border: 1px solid #e5e7eb;
    }

    #manage_shopContactInfo_searchform .form-group {
        margin-right: 20px;
        margin-bottom: 10px;
    }

    #manage_shopContactInfo_searchform label {
        color: #4b5563;
        font-weight: 500;
        font-size: 14px;
        margin-right: 8px;
        min-width: 80px;
    }

    #manage_shopContactInfo_searchform .form-control {
        border: 1px solid #d1d5db;
        border-radius: 8px;
        padding: 8px 12px;
        font-size: 14px;
        transition: all 0.2s;
    }

    #manage_shopContactInfo_searchform .form-control:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
        outline: none;
    }

    #manage_shopContactInfo_searchform .btn-primary {
        background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
        border: none;
        border-radius: 8px;
        padding: 8px 20px;
        font-weight: 500;
        box-shadow: 0 2px 4px rgba(99, 102, 241, 0.3);
        transition: all 0.2s;
    }

    #manage_shopContactInfo_searchform .btn-primary:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 8px rgba(99, 102, 241, 0.4);
    }

    /* 📋 EasyUI Layout Center Region - 确保不裁剪分页栏 */
    .easyui-layout .layout-panel-center {
        overflow: visible !important;
    }
    
    .easyui-layout .layout-panel-center .layout-body {
        overflow: visible !important;
    }
    
    /* 📋 表格容器 */
    .datagrid-container {
        margin: 15px;
        background: #ffffff;
        border-radius: 12px;
        overflow: visible !important; /* 强制 visible，确保分页栏可见 */
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
        border: 1px solid #e5e7eb;
        padding-bottom: 5px !important; /* 减少底部空白，只保留少量间距 */
        position: relative; /* 确保定位上下文 */
    }
    
    /* 📊 表格样式 */
    #manage_shopContactInfo_datagrid {
        border: none !important;
        border-radius: 12px;
    }
    
    /* 确保 datagrid 容器不会裁剪分页栏 */
    #manage_shopContactInfo_datagrid .datagrid-view,
    #manage_shopContactInfo_datagrid .datagrid-view2 {
        overflow: visible !important;
    }
    
    /* 确保 datagrid 主体容器不会裁剪分页栏 */
    #manage_shopContactInfo_datagrid .datagrid-wrap {
        overflow: visible !important;
    }
    
    /* 确保 datagrid 表格容器有足够空间 */
    #manage_shopContactInfo_datagrid .datagrid-body {
        overflow: auto !important;
    }
    
    /* 📄 分页栏样式 - 确保显示和定位（使用多种选择器确保覆盖） */
    #manage_shopContactInfo_datagrid .datagrid-pager,
    #manage_shopContactInfo_datagrid + .pagination,
    .datagrid-container .pagination,
    .datagrid-container .datagrid-pager {
        background: #f9fafb !important;
        border-top: 1px solid #e5e7eb !important;
        padding: 10px 12px !important;
        border-radius: 0 0 12px 12px;
        margin: 0 !important;
        display: block !important;
        visibility: visible !important;
        opacity: 1 !important;
        position: relative !important;
        z-index: 10 !important;
        height: auto !important;
        min-height: 40px !important;
        overflow: visible !important;
    }
    
    /* 确保分页栏的父容器也不裁剪 */
    #manage_shopContactInfo_datagrid .datagrid-view .datagrid-ft,
    #manage_shopContactInfo_datagrid .datagrid-ft {
        overflow: visible !important;
        display: block !important;
    }
    
    /* EasyUI 分页栏通用样式 */
    .pagination,
    .datagrid-pager {
        display: block !important;
        visibility: visible !important;
        opacity: 1 !important;
    }
    
    #manage_shopContactInfo_datagrid .datagrid-pager .pagination-info,
    #manage_shopContactInfo_datagrid .datagrid-pager .pagination-btn,
    #manage_shopContactInfo_datagrid .datagrid-pager .pagination-num {
        color: #374151 !important;
        font-size: 13px !important;
    }
    
    #manage_shopContactInfo_datagrid .datagrid-pager .pagination-btn:hover,
    #manage_shopContactInfo_datagrid .datagrid-pager .pagination-num:hover {
        background: #eef2ff !important;
        color: #6366f1 !important;
    }
    
    #manage_shopContactInfo_datagrid .datagrid-pager .pagination-num-selected {
        background: #6366f1 !important;
        color: #ffffff !important;
    }

    /* 🛠️ 工具栏样式 */
    #manage_shopContactInfo_toolbar {
        background: #f9fafb;
        border-bottom: 1px solid #e5e7eb;
        padding: 12px 16px;
        border-radius: 12px 12px 0 0;
    }

    #manage_shopContactInfo_toolbar .easyui-linkbutton {
        background: #ffffff;
        border: 1px solid #e5e7eb;
        color: #374151;
        border-radius: 8px;
        padding: 8px 16px;
        font-weight: 500;
        transition: all 0.2s;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    }

    #manage_shopContactInfo_toolbar .easyui-linkbutton:hover {
        background: #eef2ff;
        border-color: #6366f1;
        color: #6366f1;
        transform: translateY(-1px);
        box-shadow: 0 2px 4px rgba(99, 102, 241, 0.2);
    }


    #manage_shopContactInfo_datagrid .datagrid-header {
        background: #f9fafb !important;
        border-bottom: 2px solid #e5e7eb;
    }

    #manage_shopContactInfo_datagrid .datagrid-header th {
        background: #f9fafb !important;
        font-weight: 600;
        color: #374151;
        font-size: 13px;
        padding: 12px 8px;
        border-right: 1px solid #e5e7eb;
    }

    #manage_shopContactInfo_datagrid .datagrid-header th:last-child {
        border-right: none;
    }

    #manage_shopContactInfo_datagrid .datagrid-body td {
        border-bottom: 1px solid #f3f4f6;
        padding: 12px 8px;
        font-size: 14px;
        color: #4b5563;
    }

    #manage_shopContactInfo_datagrid .datagrid-row {
        transition: all 0.2s;
    }

    #manage_shopContactInfo_datagrid .datagrid-row:hover {
        background: #f9fafc !important;
    }

    #manage_shopContactInfo_datagrid .datagrid-row-selected {
        background: #eef2ff !important;
    }

    /* 🎯 操作按钮样式 */
    #manage_shopContactInfo_action .btn-group {
        display: flex;
        gap: 4px;
        align-items: center;
        flex-wrap: nowrap;
        white-space: nowrap;
    }

    #manage_shopContactInfo_action .btn {
        border-radius: 6px;
        padding: 4px 8px;
        font-size: 12px;
        transition: all 0.2s;
        border: 1px solid transparent;
        white-space: nowrap;
        flex-shrink: 0;
    }

    #manage_shopContactInfo_action .btn-outline-primary {
        background: #ffffff;
        border-color: #d1d5db;
        color: #4b5563;
    }

    #manage_shopContactInfo_action .btn-outline-primary:hover {
        background: #6366f1;
        border-color: #6366f1;
        color: #ffffff;
        transform: translateY(-1px);
        box-shadow: 0 2px 4px rgba(99, 102, 241, 0.3);
    }

    #manage_shopContactInfo_action .btn-link {
        min-width: auto;
        padding: 4px 6px !important;
    }

    #manage_shopContactInfo_action .fa-arrow-circle-up,
    #manage_shopContactInfo_action .fa-arrow-circle-down {
        color: #6366f1;
        font-size: 16px;
        transition: all 0.2s;
    }

    #manage_shopContactInfo_action .fa-arrow-circle-up:hover,
    #manage_shopContactInfo_action .fa-arrow-circle-down:hover {
        color: #8b5cf6;
        transform: scale(1.1);
    }

    /* 确保操作列内容不换行 */
    #manage_shopContactInfo_datagrid .datagrid-cell[field="rowActions"] {
        white-space: nowrap;
        overflow: visible;
    }

    /* 🏷️ 状态徽章 */
    .status-badge {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 500;
    }

    .status-badge-active {
        background: #d1fae5;
        color: #065f46;
        border: 1px solid #a7f3d0;
    }

    .status-badge-inactive {
        background: #fee2e2;
        color: #991b1b;
        border: 1px solid #fecaca;
    }

    /* 📱 响应式优化 */
    @media (max-width: 768px) {
        #manage_shopContactInfo_searchform .form-group {
            margin-right: 10px;
            margin-bottom: 15px;
        }
    }
</style>

<div class="easyui-layout contact-info-container" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:0; overflow: hidden;">
        <form id="manage_shopContactInfo_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">团队名称</label>
                <select name="search_EQ_teamId" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list shopTeamMap as k,v >
                        <option value="${k}">${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime"
                       name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
                       placeholder="开始日期"/>
                <span class="mr-1 ml-1" style="color: #9ca3af;">至</span>
                <input type="text" class="form-control form-control-sm" id="search_LTE_createTime"
                       name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
                       placeholder="结束日期"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">更新时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime"
                       name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
                       placeholder="开始日期"/>
                <span class="mr-1 ml-1" style="color: #9ca3af;">至</span>
                <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime"
                       name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})"
                       placeholder="结束日期"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button"
                        onclick="$.acooly.framework.search('manage_shopContactInfo_searchform','manage_shopContactInfo_datagrid');">
                    <i class="fa fa-search fa-fw"></i> 查询
                </button>
            </div>
        </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false" class="datagrid-container" style="overflow: visible !important;">
        <table id="manage_shopContactInfo_datagrid" class="easyui-datagrid"
               url="/manage/shop/shopContactInfo/listJson.html" toolbar="#manage_shopContactInfo_toolbar" fit="true"
               border="false" fitColumns="false"
               pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="sortTime"
               sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter" width="50">编号</th>
                <th field="id" sortable="true" width="80">ID</th>
                <th field="name" formatter="contactNameIconFormatter" width="120">名字</th>
                <th field="value" formatter="teamNameLinkFormatter" width="300">实际内容</th>
                <#if isAdmin>
                    <th field="sortTime" sortable="true" width="40">顺序</th>
                </#if>
                <th field="teamName" formatter="contentFormatter" width="120">团队名称</th>
                <th field="isActive" sortable="true" width="100" formatter="activeStatusFormatter">是否显示</th>
                <th field="createTime" formatter="dateTimeFormatter" width="160">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter" width="160">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" width="240"
                    data-options="formatter:function(value, row, index){return formatAction('manage_shopContactInfo_action',value,row)}">
                    操作
                </th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopContactInfo_action" style="display: none;">
            <div class="btn-group btn-group-xs">
                <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopContactInfo/edit.html',id:'{0}',entity:'shopContactInfo',width:500,height:500});"
                        class="btn btn-outline-primary btn-xs" type="button" title="编辑">
                    <i class="fa fa-pencil fa-fw"></i> 编辑
                </button>
                <#if isAdmin>
                    <button onclick="$.acooly.framework.move('/manage/shop/shopContactInfo/upJson.html','{0}','manage_shopContactInfo_datagrid');"
                            href="#" title="上移" class="btn btn-link btn-xs"
                            style="background: none; border: none; padding: 4px 6px; color: #6366f1;">
                        <i class="fa fa-arrow-circle-up fa-lg"></i>
                    </button>
                    <button onclick="$.acooly.framework.move('/manage/shop/shopContactInfo/downJson.html','{0}','manage_shopContactInfo_datagrid');"
                            href="#" title="下移" class="btn btn-link btn-xs"
                            style="background: none; border: none; padding: 4px 6px; color: #6366f1;">
                        <i class="fa fa-arrow-circle-down fa-lg"></i>
                    </button>
                </#if>
                <button onclick="$.acooly.framework.remove('/manage/shop/shopContactInfo/deleteJson.html','{0}','manage_shopContactInfo_datagrid');"
                        class="btn btn-outline-primary btn-xs" type="button" title="删除">
                    <i class="fa fa-trash fa-fw"></i> 删除
                </button>
            </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopContactInfo_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true"
               onclick="$.acooly.framework.create({url:'/manage/shop/shopContactInfo/create.html',entity:'shopContactInfo',width:500,height:500})">
                <i class="fa fa-plus-circle fa-fw"></i> 添加联系方式
            </a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopContactInfo_searchform', 'manage_shopContactInfo_datagrid');
            
            // 确保分页栏显示 - 使用多种方式查找和显示分页栏
            function ensurePagerVisible() {
                var datagrid = $('#manage_shopContactInfo_datagrid');
                if (!datagrid.length) return;
                
                try {
                    // 方式1：通过 datagrid API 获取分页栏
                    var pager = null;
                    try {
                        pager = datagrid.datagrid('getPager');
                        if (pager && pager.length) {
                            console.log('✅ 通过 getPager 找到分页栏，高度:', pager.outerHeight());
                        }
                    } catch(e) {
                        console.log('无法通过 getPager 获取分页栏:', e);
                    }
                    
                    // 方式2：通过选择器查找分页栏（多种可能的选择器）
                    var pagerSelectors = [
                        '#manage_shopContactInfo_datagrid .datagrid-pager',
                        '#manage_shopContactInfo_datagrid + .pagination',
                        '.datagrid-container .pagination',
                        '.datagrid-container .datagrid-pager',
                        '#manage_shopContactInfo_datagrid .pagination',
                        '.datagrid-pager',
                        '.pagination',
                        '[class*="pager"]',
                        '[class*="pagination"]'
                    ];
                    
                    var foundPagers = [];
                    for (var i = 0; i < pagerSelectors.length; i++) {
                        var $pager = $(pagerSelectors[i]);
                        if ($pager.length > 0) {
                            foundPagers.push({selector: pagerSelectors[i], element: $pager});
                            console.log('找到分页栏，选择器:', pagerSelectors[i], '数量:', $pager.length, '高度:', $pager.outerHeight());
                        }
                    }
                    
                    // 使用找到的分页栏（优先使用 getPager，否则使用第一个找到的）
                    var targetPager = (pager && pager.length) ? pager : (foundPagers.length > 0 ? foundPagers[0].element : null);
                    
                    if (targetPager && targetPager.length) {
                        // 强制显示分页栏 - 使用多种方式确保生效
                        targetPager.css({
                            'display': 'block',
                            'visibility': 'visible',
                            'opacity': '1',
                            'height': 'auto',
                            'overflow': 'visible',
                            'min-height': '40px'
                        });
                        
                        // 直接设置 style 属性（最高优先级）
                        var pagerElement = targetPager[0];
                        if (pagerElement) {
                            pagerElement.style.setProperty('display', 'block', 'important');
                            pagerElement.style.setProperty('visibility', 'visible', 'important');
                            pagerElement.style.setProperty('opacity', '1', 'important');
                            pagerElement.style.setProperty('height', 'auto', 'important');
                            pagerElement.style.setProperty('overflow', 'visible', 'important');
                        }
                        
                        // 确保分页栏的所有父容器都不隐藏
                        var parents = targetPager.parents();
                        parents.each(function() {
                            var $parent = $(this);
                            var overflow = $parent.css('overflow');
                            var display = $parent.css('display');
                            if (overflow === 'hidden') {
                                $parent.css('overflow', 'visible');
                                $parent[0].style.setProperty('overflow', 'visible', 'important');
                            }
                            // 检查是否是 datagrid-container
                            if ($parent.hasClass('datagrid-container')) {
                                $parent.css('padding-bottom', '70px');
                            }
                        });
                        
                        // 检查分页栏是否真的可见
                        var pagerHeight = targetPager.outerHeight();
                        var pagerDisplay = targetPager.css('display');
                        var pagerVisibility = targetPager.css('visibility');
                        var pagerOffset = targetPager.offset();
                        
                        console.log('分页栏最终状态 - 高度:', pagerHeight, 'display:', pagerDisplay, 'visibility:', pagerVisibility, '位置:', pagerOffset);
                        
                        if (pagerHeight > 0 && pagerDisplay !== 'none' && pagerVisibility !== 'hidden') {
                            console.log('✅ 分页栏已成功显示，高度:', pagerHeight);
                        } else {
                            console.warn('⚠️ 分页栏可能仍被隐藏，尝试最后修复...');
                        }
                    } else {
                        console.warn('⚠️ 未找到分页栏元素');
                        // 尝试通过检查 DOM 结构查找
                        console.log('检查 datagrid DOM 结构...');
                        var datagridEl = datagrid[0];
                        if (datagridEl) {
                            console.log('datagrid 元素:', datagridEl);
                            console.log('datagrid 父元素:', datagridEl.parentElement);
                            console.log('datagrid 兄弟元素:', Array.from(datagridEl.parentElement.children));
                            // 查找所有可能的分页元素
                            var allElements = datagridEl.parentElement.querySelectorAll('*');
                            for (var j = 0; j < allElements.length; j++) {
                                var el = allElements[j];
                                var className = el.className || '';
                                if (className.indexOf('pager') >= 0 || className.indexOf('pagination') >= 0) {
                                    console.log('找到可能的分页元素:', el, '类名:', className);
                                }
                            }
                        }
                    }
                } catch(e) {
                    console.error('设置分页栏显示时出错:', e);
                }
            }
            
            // 延迟执行，确保 datagrid 已初始化
            setTimeout(ensurePagerVisible, 300);
            setTimeout(ensurePagerVisible, 800);
            setTimeout(ensurePagerVisible, 1500);
            
            // 监听 datagrid 加载完成事件和分页变化事件
            var datagridEl = $('#manage_shopContactInfo_datagrid');
            var originalOnLoadSuccess = datagridEl.datagrid('options').onLoadSuccess;
            datagridEl.datagrid({
                onLoadSuccess: function(data) {
                    if (originalOnLoadSuccess) {
                        originalOnLoadSuccess.call(this, data);
                    }
                    setTimeout(ensurePagerVisible, 100);
                }
            });
            
            // 监听窗口大小变化，重新检查分页栏
            $(window).on('resize', function() {
                setTimeout(ensurePagerVisible, 200);
            });
            
            // 移除多余的空白 - 调整容器高度
            setTimeout(function() {
                try {
                    var container = $('.datagrid-container');
                    var pager = datagridEl.datagrid('getPager');
                    if (pager && pager.length) {
                        var pagerHeight = pager.outerHeight() || 40;
                        // 移除多余的 padding-bottom
                        container.css('padding-bottom', Math.max(10, pagerHeight + 5) + 'px');
                    }
                } catch(e) {
                    console.error('调整容器高度时出错:', e);
                }
            }, 1500);
        });

        // 状态格式化函数
        function activeStatusFormatter(value) {
            if (value == 1 || value == '1') {
                return '<span class="status-badge status-badge-active">显示</span>';
            } else {
                return '<span class="status-badge status-badge-inactive">隐藏</span>';
            }
        }

        // 联系方式名称图标格式化函数
        function contactNameIconFormatter(value) {
            if (!value) return '<span style="color:#9ca3af;">--</span>';
            
            // 转换为小写以便匹配（不区分大小写）
            var nameLower = value.toLowerCase().trim();
            
            // 图标映射表（兼容 Font Awesome 4.7.0）
            var iconMap = {
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
            
            // 查找匹配的图标配置
            var config = iconMap[nameLower];
            
            if (config) {
                return '<div style="display: flex; align-items: center; gap: 8px;">' +
                       '<i class="fa ' + config.icon + '" style="color: ' + config.color + '; font-size: 16px; width: 20px; text-align: center;"></i>' +
                       '<span style="font-weight: 500; color: #374151;">' + config.label + '</span>' +
                       '</div>';
            } else {
                // 如果没有匹配到，显示原始值
                return '<span style="font-weight: 500; color: #374151;">' + value + '</span>';
            }
        }

        // 格式化团队分享链接，添加复制按钮
        function teamNameLinkFormatter(value, row) {
            if (!value) return '<span style="color:#9ca3af;">暂无链接</span>';
            // 截断过长的链接显示
            var displayText = value.length > 40 ? value.substring(0, 40) + '...' : value;
            return "<div style='text-align: center;'><button onclick='copyTeamLink(" + JSON.stringify(value) + ")' " +
                "title='点击复制：' + value + ' 到剪贴板' class='team-link-btn'>" +
                "<i class='fa fa-copy'></i> " + displayText + "</button></div>";
        }

        // 复制团队分享链接到剪贴板
        function copyTeamLink(value) {
            if (value) {
                navigator.clipboard.writeText(value).then(() => {
                    $.messager.alert('提示', '复制成功：' + value, 'info');
                }).catch(err => {
                    // 降级方案：使用传统方法
                    var textArea = document.createElement("textarea");
                    textArea.value = value;
                    textArea.style.position = "fixed";
                    textArea.style.left = "-999999px";
                    textArea.style.top = "-999999px";
                    document.body.appendChild(textArea);
                    textArea.focus();
                    textArea.select();
                    try {
                        var successful = document.execCommand('copy');
                        if (successful) {
                            $.messager.alert('提示', '复制成功：' + value, 'info');
                        } else {
                            $.messager.alert('错误', '复制失败，请手动复制', 'error');
                        }
                    } catch (err) {
                        $.messager.alert('错误', '复制失败：' + err, 'error');
                    }
                    document.body.removeChild(textArea);
                });
            } else {
                $.messager.alert('提示', '团队分享链接为空', 'warning');
            }
        }
    </script>
</div>
