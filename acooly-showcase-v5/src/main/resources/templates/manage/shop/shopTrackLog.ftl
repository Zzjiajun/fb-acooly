<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_shopTrackLog_searchform" class="form-inline ac-form-search" onsubmit="return false">
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">访客ID（浏览器唯一标识，localStorage）：</label>-->
<#--                <input type="text" class="form-control form-control-sm" name="search_EQ_visitorId"/>-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">会话ID（单次访问会话）：</label>-->
<#--                <input type="text" class="form-control form-control-sm" name="search_EQ_sessionId"/>-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">页面类型：home/category/product/other：</label>-->
<#--                <input type="text" class="form-control form-control-sm" name="search_EQ_pageType"/>-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">语言代码：zh_CN/en_US/it_IT/ja_JP/ko_KR/fr_FR：</label>-->
<#--                <input type="text" class="form-control form-control-sm" name="search_EQ_locale"/>-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">IP地址：</label>-->
<#--                <input type="text" class="form-control form-control-sm" name="search_EQ_ipAddress"/>-->
<#--            </div>-->
<#--            <div class="form-group">-->
<#--                <label class="col-form-label">访问日期（用于分区和统计，格式：YYYY-MM-DD）：</label>-->
<#--                <input type="text" class="form-control form-control-sm" id="search_GTE_visitDate" name="search_GTE_visitDate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_visitDate" name="search_LTE_visitDate" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />-->
<#--            </div>-->
            <div class="form-group">
                <label class="col-form-label">创建时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <label class="col-form-label">update_time：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_updateTime" name="search_GTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span> <input type="text" class="form-control form-control-sm" id="search_LTE_updateTime" name="search_LTE_updateTime" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'})" />
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_shopTrackLog_searchform','manage_shopTrackLog_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
    </form>
    </div>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <table id="manage_shopTrackLog_datagrid" class="easyui-datagrid" url="/manage/shop/shopTrackLog/listJson.html" toolbar="#manage_shopTrackLog_toolbar" fit="true" border="false" fitColumns="false"
                pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="id" sortable="true" width="80">ID</th>
                <th field="visitDate" formatter="dateFormatter" sortable="true" width="120">访问日期</th>
                <th field="pageType" formatter="pageTypeFormatter" width="100">页面类型</th>
                <th field="pageKey" formatter="pageKeyFormatter" width="200">页面</th>
                <th field="productId" formatter="productIdFormatter" sortable="true" width="100">商品ID</th>
                <th field="stayDuration" formatter="durationFormatter" sortable="true" width="100">停留时间</th>
                <th field="ipAddress" formatter="ipFormatter" width="120">IP地址</th>
                <th field="userAgent" formatter="userAgentFormatter" width="200">UA信息</th>
                <th field="source" formatter="contentFormatter" width="100">来源渠道</th>
                <th field="deviceType" formatter="deviceFormatter" width="150">设备信息</th>
                <th field="browser" formatter="browserFormatter" width="150">浏览器</th>
                <th field="locale" formatter="localeFormatter" width="100">语言</th>
                <th field="referer" formatter="refererFormatter" width="150">来源</th>
                <th field="visitorId" formatter="visitorIdFormatter" width="150">访客ID</th>
                <th field="sessionId" formatter="sessionIdFormatter" width="150">会话ID</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_shopTrackLog_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_shopTrackLog_action" style="display: none;">
            <div class="btn-group btn-group-xs">
              <button onclick="$.acooly.framework.show('/manage/shop/shopTrackLog/show.html?id={0}',500,500);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
              <button onclick="$.acooly.framework.edit({url:'/manage/shop/shopTrackLog/edit.html',id:'{0}',entity:'shopTrackLog',width:500,height:500});" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
              <button onclick="$.acooly.framework.remove('/manage/shop/shopTrackLog/deleteJson.html','{0}','manage_shopTrackLog_datagrid');" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
          </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_shopTrackLog_toolbar">
            <a href="#" class="easyui-linkbutton" plain="true" onclick="clearStatsCache()" style="background-color: #f56c6c; color: white;"><i class="fa fa-trash-o fa-fw fa-col"></i>清理缓存</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/shop/shopTrackLog/create.html',entity:'shopTrackLog',width:500,height:500})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/shop/shopTrackLog/deleteJson.html','manage_shopTrackLog_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
            <a href="#" class="easyui-menubutton" data-options="menu:'#manage_shopTrackLog_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
            <div id="manage_shopTrackLog_exports_menu" style="width:150px;">
              <div onclick="$.acooly.framework.exports('/manage/shop/shopTrackLog/exportXls.html','manage_shopTrackLog_searchform','访问行为日志表（按日期分区，建议按月归档）')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
              <div onclick="$.acooly.framework.exports('/manage/shop/shopTrackLog/exportCsv.html','manage_shopTrackLog_searchform','访问行为日志表（按日期分区，建议按月归档）')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
            </div>
            <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.imports({url:'/manage/shop/shopTrackLog/importView.html',uploader:'manage_shopTrackLog_import_uploader_file'});"><i class="fa fa-cloud-upload fa-fw fa-col"></i>批量导入</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_shopTrackLog_searchform', 'manage_shopTrackLog_datagrid');
        });
        
        // 清理统计缓存
        function clearStatsCache() {
            $.messager.confirm('确认', '确定要清理所有统计缓存吗？清理后需要重新计算统计数据。', function(r){
                if (r){
                    $.ajax({
                        url: '/manage/shop/shopTrackStats/clearCacheJson.html',
                        type: 'POST',
                        data: {},
                        success: function(result) {
                            if (result.success) {
                                var message = (result.data && result.data.message) ? result.data.message : '缓存清理完成';
                                $.messager.show({
                                    title: '成功',
                                    msg: message,
                                    timeout: 3000,
                                    showType: 'slide'
                                });
                            } else {
                                $.messager.alert('错误', result.message || '缓存清理失败', 'error');
                            }
                        },
                        error: function() {
                            $.messager.alert('错误', '请求失败', 'error');
                        }
                    });
                }
            });
        }
        
        // 格式化停留时间
        function durationFormatter(value, row, index) {
            if (!value) return '<span style="color:#999;">-</span>';
            var seconds = Math.floor(value / 1000);
            var minutes = Math.floor(seconds / 60);
            var hours = Math.floor(minutes / 60);
            
            if (hours > 0) {
                return '<span style="color:#67c23a;font-weight:bold;">' + hours + 'h ' + (minutes % 60) + 'm</span>';
            } else if (minutes > 0) {
                return '<span style="color:#409eff;">' + minutes + 'm ' + (seconds % 60) + 's</span>';
            } else if (seconds > 0) {
                return '<span style="color:#909399;">' + seconds + 's</span>';
            } else {
                return '<span style="color:#c0c4cc;">' + value + 'ms</span>';
            }
        }
        
        // 格式化页面类型
        function pageTypeFormatter(value, row, index) {
            if (!value) return '-';
            var typeMap = {
                'home': '<span class="label label-primary">首页</span>',
                'category': '<span class="label label-success">分类</span>',
                'product': '<span class="label label-warning">商品</span>',
                'other': '<span class="label label-default">其他</span>'
            };
            return typeMap[value] || '<span class="label label-info">' + value + '</span>';
        }
        
        // 格式化页面标识
        function pageKeyFormatter(value, row, index) {
            if (!value) return '-';
            var displayValue = value.length > 30 ? value.substring(0, 30) + '...' : value;
            return '<span title="' + value + '" style="cursor:pointer;color:#409eff;">' + displayValue + '</span>';
        }
        
        // 格式化商品ID
        function productIdFormatter(value, row, index) {
            if (!value) return '<span style="color:#c0c4cc;">-</span>';
            return '<span style="color:#67c23a;font-weight:bold;">#' + value + '</span>';
        }
        
        // 格式化IP地址
        function ipFormatter(value, row, index) {
            if (!value) return '-';
            return '<span style="font-family:monospace;color:#409eff;">' + value + '</span>';
        }
        
        // 格式化设备信息（参考ustat.com样式）
        function deviceFormatter(value, row, index) {
            if (!value) return '<span style="color:#c0c4cc;">-</span>';
            
            try {
                // 尝试解析JSON
                var device = typeof value === 'string' ? JSON.parse(value) : value;
                if (device && typeof device === 'object') {
                    var parts = [];
                    // 设备类型图标和名称
                    if (device.typeEn) {
                        var typeIcon = '';
                        var typeName = '';
                        if (device.typeEn === 'desktop') {
                            typeIcon = '<i class="fa fa-desktop" style="color:#409eff;margin-right:4px;"></i>';
                            typeName = device.type || '桌面设备';
                        } else if (device.typeEn === 'mobile') {
                            typeIcon = '<i class="fa fa-mobile" style="color:#67c23a;margin-right:4px;"></i>';
                            typeName = device.type || '移动设备';
                        } else if (device.typeEn === 'tablet') {
                            typeIcon = '<i class="fa fa-tablet" style="color:#e6a23c;margin-right:4px;"></i>';
                            typeName = device.type || '平板设备';
                        } else {
                            typeIcon = '<i class="fa fa-laptop" style="color:#909399;margin-right:4px;"></i>';
                            typeName = device.type || device.typeEn;
                        }
                        parts.push(typeIcon + '<span style="color:#303133;">' + typeName + '</span>');
                    }
                    // 操作系统
                    if (device.os) {
                        var osText = device.os;
                        if (device.osVersion) {
                            osText += ' ' + device.osVersion;
                        }
                        parts.push('<span style="color:#606266;font-size:12px;">' + osText + '</span>');
                    }
                    // 品牌和型号（如果有）
                    if (device.brand && device.model) {
                        parts.push('<span style="color:#909399;font-size:12px;">' + device.brand + ' ' + device.model + '</span>');
                    }
                    return parts.length > 0 ? parts.join('<br/>') : value;
                }
            } catch (e) {
                // 如果不是JSON，尝试解析旧格式
                if (value.indexOf('类型：') >= 0) {
                    var lines = value.split('\n');
                    var result = [];
                    lines.forEach(function(line) {
                        if (line.indexOf('类型：') >= 0) {
                            var type = line.replace('类型：', '').trim();
                            var icon = type.indexOf('桌面') >= 0 ? '<i class="fa fa-desktop" style="color:#409eff;margin-right:4px;"></i>' : 
                                      type.indexOf('移动') >= 0 ? '<i class="fa fa-mobile" style="color:#67c23a;margin-right:4px;"></i>' : 
                                      '<i class="fa fa-laptop" style="color:#909399;margin-right:4px;"></i>';
                            result.push(icon + '<span style="color:#303133;">' + type + '</span>');
                        } else if (line.indexOf('操作系统：') >= 0) {
                            result.push('<span style="color:#606266;font-size:12px;">' + line.replace('操作系统：', '').trim() + '</span>');
                        }
                    });
                    return result.length > 0 ? result.join('<br/>') : value;
                }
            }
            
            // 简单文本显示
            return '<span style="color:#606266;">' + (value.length > 20 ? value.substring(0, 20) + '...' : value) + '</span>';
        }
        
        // 格式化浏览器信息（参考ustat.com样式）
        function browserFormatter(value, row, index) {
            if (!value) return '<span style="color:#c0c4cc;">-</span>';
            
            try {
                // 尝试解析JSON
                var browser = typeof value === 'string' ? JSON.parse(value) : value;
                if (browser && typeof browser === 'object') {
                    var parts = [];
                    if (browser.name) {
                        // 浏览器图标
                        var browserIcon = '';
                        var browserColor = '#409eff';
                        if (browser.name === 'Chrome') {
                            browserIcon = '<i class="fa fa-chrome" style="color:#4285f4;margin-right:4px;"></i>';
                            browserColor = '#4285f4';
                        } else if (browser.name === 'Firefox') {
                            browserIcon = '<i class="fa fa-firefox" style="color:#ff7139;margin-right:4px;"></i>';
                            browserColor = '#ff7139';
                        } else if (browser.name === 'Safari') {
                            browserIcon = '<i class="fa fa-safari" style="color:#1e88e5;margin-right:4px;"></i>';
                            browserColor = '#1e88e5';
                        } else if (browser.name === 'Edge') {
                            browserIcon = '<i class="fa fa-edge" style="color:#0078d4;margin-right:4px;"></i>';
                            browserColor = '#0078d4';
                        } else {
                            browserIcon = '<i class="fa fa-globe" style="color:#909399;margin-right:4px;"></i>';
                        }
                        parts.push(browserIcon + '<span style="color:#303133;font-weight:500;">' + browser.name + '</span>');
                    }
                    if (browser.version) {
                        parts.push('<span style="color:#606266;font-size:12px;">v' + browser.version + '</span>');
                    }
                    return parts.length > 0 ? parts.join(' ') : value;
                }
            } catch (e) {
                // 如果不是JSON，尝试解析旧格式
                if (value.indexOf('名称：') >= 0) {
                    var lines = value.split('\n');
                    var result = [];
                    lines.forEach(function(line) {
                        if (line.indexOf('名称：') >= 0) {
                            var name = line.replace('名称：', '').trim();
                            var icon = '<i class="fa fa-globe" style="color:#409eff;margin-right:4px;"></i>';
                            result.push(icon + '<span style="color:#303133;font-weight:500;">' + name + '</span>');
                        } else if (line.indexOf('版本：') >= 0) {
                            result.push('<span style="color:#606266;font-size:12px;">v' + line.replace('版本：', '').trim() + '</span>');
                        }
                    });
                    return result.length > 0 ? result.join(' ') : value;
                }
            }
            
            // 简单文本显示
            return '<span style="color:#606266;">' + (value.length > 20 ? value.substring(0, 20) + '...' : value) + '</span>';
        }
        
        // 格式化语言
        function localeFormatter(value, row, index) {
            if (!value) return '-';
            var localeMap = {
                'zh_CN': '🇨🇳 中文',
                'en_US': '🇺🇸 English',
                'it_IT': '🇮🇹 Italiano',
                'ja_JP': '🇯🇵 日本語',
                'ko_KR': '🇰🇷 한국어',
                'fr_FR': '🇫🇷 Français'
            };
            return localeMap[value] || value;
        }
        
        // 格式化来源页面
        function refererFormatter(value, row, index) {
            if (!value || value === '/') return '<span style="color:#c0c4cc;">直接访问</span>';
            var displayValue = value.length > 25 ? value.substring(0, 25) + '...' : value;
            return '<span title="' + value + '" style="color:#909399;">' + displayValue + '</span>';
        }
        
        // 格式化访客ID
        function visitorIdFormatter(value, row, index) {
            if (!value) return '-';
            var displayValue = value.length > 20 ? value.substring(0, 20) + '...' : value;
            return '<span title="' + value + '" style="font-family:monospace;font-size:11px;color:#909399;">' + displayValue + '</span>';
        }
        
        // 格式化会话ID
        function sessionIdFormatter(value, row, index) {
            if (!value) return '-';
            var displayValue = value.length > 20 ? value.substring(0, 20) + '...' : value;
            return '<span title="' + value + '" style="font-family:monospace;font-size:11px;color:#909399;">' + displayValue + '</span>';
        }
        
        // 格式化UA信息
        function userAgentFormatter(value, row, index) {
            if (!value) return '<span style="color:#999;">-</span>';
            var displayValue = value.length > 40 ? value.substring(0, 40) + '...' : value;
            return '<span title="' + value + '" style="font-size:11px;color:#c0c4cc;font-family:monospace;">' + displayValue + '</span>';
        }
    </script>
    <style>
        .label {
            display: inline-block;
            padding: 3px 8px;
            font-size: 12px;
            font-weight: 500;
            line-height: 1;
            color: #fff;
            text-align: center;
            white-space: nowrap;
            vertical-align: baseline;
            border-radius: 3px;
        }
        .label-primary { background-color: #409eff; }
        .label-success { background-color: #67c23a; }
        .label-warning { background-color: #e6a23c; }
        .label-info { background-color: #909399; }
        .label-default { background-color: #c0c4cc; }
    </style>
</div>
