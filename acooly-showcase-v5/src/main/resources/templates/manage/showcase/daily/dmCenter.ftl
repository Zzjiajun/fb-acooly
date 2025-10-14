<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 查询条件 -->
    <div data-options="region:'north',border:false" style="padding:5px; overflow: hidden;">
        <form id="manage_dmCenter_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group">
                <label class="col-form-label">地区：</label>
                <select name="search_EQ_region" class="form-control select2bs4" data-options="required:true">
                    <option></option>
                    <#list regionList as v >
                        <option value="${v}">${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">账号名：</label>
                <select name="search_EQ_userName" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <#list mapName as k,v >
                        <option value="${k}">${k}:${v}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label class="col-form-label">主域名：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_domain"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">二级域名：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_secondaryDomain"/>
            </div>
            <div class="form-group">
                <label class="col-form-label">备注：</label>
                <input type="text" class="form-control form-control-sm" name="search_LIKE_remark"/>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_dmCenter_searchform','manage_dmCenter_datagrid');"><i class="fa fa-search fa-fw fa-col"></i> 查询</button>
            </div>
        </form>
    </div>

    <style>
      /* KPI + 轻量风格 */
      .kpi-wrap { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 14px; margin: 6px 10px 10px; }
      .kpi-card { background: #fff; border: 1px solid #eef0f3; border-radius: 8px; padding: 10px 14px; box-shadow: 0 1px 2px rgba(0,0,0,.04); }
      .kpi-title { color: #6b7280; font-size: 14px; margin-bottom: 6px; }
      .kpi-value { color: #111827; font-size: 22px; font-weight: 700; letter-spacing: .3px; }
      .kpi-sub { color: #6b7280; font-size: 14px; }

      /* 徽章 */
      .badge { display: inline-block; padding: 3px 8px; border-radius: 14px; font-size: 14px; line-height: 1.4; }
      .badge-success { color: #065f46; background: #d1fae5; border: 1px solid #a7f3d0; }
      .badge-danger  { color: #991b1b; background: #fee2e2; border: 1px solid #fecaca; }
      .badge-primary { color: #1d4ed8; background: #e0ecff; border: 1px solid #c7dbff; }
      .badge-muted   { color: #6b7280; background: #f3f4f6; border: 1px solid #e5e7eb; }

      /* 表格紧凑与悬浮 */
      .datagrid-row { font-size: 14px; }
      .datagrid-header .datagrid-cell, .datagrid-body .datagrid-cell { padding: 6px 8px; }
      .datagrid-row-over { background: #f7fafc !important; }
      .datagrid-row-selected { background: #eef2ff !important; }
      /* 粘性表头与浅色分隔 */
      .datagrid-view .datagrid-header { position: sticky; top: 0; z-index: 10; background: #ffffff; border-bottom: 1px solid #eef0f3; box-shadow: 0 6px 10px -8px rgba(0,0,0,0.12); }
      .datagrid-header-row .datagrid-cell { border-right: 1px solid #f3f4f6; }
      .datagrid-header-row .datagrid-cell:last-child { border-right: none; }
      .datagrid-body .datagrid-row { border-bottom: 1px solid #f7f7f8; }
      /* 表头字号加强 */
      .datagrid-header .datagrid-cell, .datagrid-header .datagrid-cell span { font-size: 14px; font-weight: 600; color: #111827; }

      /* 更现代的链接按钮 */
      .btns { background: #ffffff; border: 1px solid #e5e7eb; color: #374151; border-radius: 14px; padding: 6px 10px; font-size: 14px; box-shadow: 0 1px 1px rgba(0,0,0,.03); }
      .btns:hover { background: #f9fafb; border-color: #d1d5db; color: #111827; }
      /* 轮询链接按钮（渐变描边 + 轻微动效） */
      .loop-link-btn { display: inline-flex; align-items: center; gap: 6px; padding: 6px 14px; border-radius: 9999px; font-size: 14px; color: #0f172a; background: linear-gradient(#fff,#fff) padding-box, linear-gradient(90deg,#60a5fa,#34d399,#f59e0b) border-box; border: 1px solid transparent; box-shadow: 0 1px 2px rgba(0,0,0,.06); cursor: pointer; transition: transform .15s ease, box-shadow .15s ease; }
      .loop-link-btn i { color: #2563eb; }
      .loop-link-btn:hover { transform: translateY(-1px); box-shadow: 0 2px 6px rgba(0,0,0,.08); }
      .expandable { width: 420px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; text-align: left; transition: width .2s ease; }
      .ellipsis { max-width: 260px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; display: inline-block; vertical-align: bottom; }

      /* 标签化内容放大 */
      .tag-lg { font-size: 14px; padding: 3px 10px; border-radius: 14px; }
      .tag-lg--gray { background:#f3f4f6; color:#374151; }
      .tag-lg--blue { background:#eef2ff; color:#1f2937; }
      .tag-lg--muted { background:#f5f5f5; color:#374151; }

      /* 指标大按钮（更易点击） */
      .metric-btn { display: inline-flex; align-items: center; gap: 6px; padding: 6px 14px; border-radius: 999px; font-size: 14px; line-height: 1; border: 1px solid #d1d5db; background: #f8fafc; color: #0f172a; cursor: pointer; user-select: none; transition: all .15s ease; }
      .metric-btn i { font-size: 14px; }
      .metric-btn:hover { background: #eef2ff; border-color: #c7d2fe; box-shadow: 0 1px 3px rgba(0,0,0,.06); transform: translateY(-1px); }
      .metric-btn--blue { background: #eef2ff; border-color: #c7d2fe; color: #1d4ed8; }
      .metric-btn--green { background: #ecfdf5; border-color: #a7f3d0; color: #047857; }
      .metric-btn--orange { background: #fff7ed; border-color: #fed7aa; color: #c2410c; }
      .metric-btn--red { background: #fee2e2; border-color: #fecaca; color: #991b1b; }
      /* 统一色系等级（蓝色系，降低杂色） */
      .metric-level-low { background:#eff6ff !important; border-color:#dbeafe !important; color:#1d4ed8 !important; }
      .metric-level-mid { background:#dbeafe !important; border-color:#bfdbfe !important; color:#1e40af !important; }
      .metric-level-high { background:#bfdbfe !important; border-color:#93c5fd !important; color:#1e3a8a !important; }

      /* 访问量强调 Chip（带迷你进度条） */
      .metric-chip { display: inline-flex; align-items: center; gap: 8px; padding: 6px 10px; border-radius: 999px; font-size: 14px; border: 1px solid #e5e7eb; background: #ffffff; color: #0f172a; cursor: pointer; user-select: none; box-shadow: 0 1px 2px rgba(0,0,0,.04); }
      .metric-chip b { font-weight: 700; letter-spacing: .2px; }
      .metric-chip__track { width: 74px; height: 6px; background: #e5e7eb; border-radius: 999px; overflow: hidden; }
      .metric-chip__fill { height: 100%; display: block; }
      .metric-chip--low { border-color: #a7f3d0; background: #ecfdf5; color: #047857; }
      .metric-chip--mid { border-color: #fde68a; background: #fef9c3; color: #92400e; }
      .metric-chip--high { border-color: #fecaca; background: #fee2e2; color: #991b1b; }
    </style>

    <!-- 列表和工具栏 -->
    <div data-options="region:'center',border:false">
        <!-- KPI 概览（已注释，避免影响表格功能）14px
        <div class="kpi-wrap">
            <div class="kpi-card">
                <div class="kpi-title">记录总数</div>
                <div id="kpiTotalCenters" class="kpi-value">-</div>
                <div class="kpi-sub">当前筛选范围</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">总访问量</div>
                <div id="kpiTotalVisits" class="kpi-value">-</div>
                <div class="kpi-sub">域名访问量合计</div>
            </div>
            <div class="kpi-card">
                <div class="kpi-title">总按钮点击</div>
                <div id="kpiTotalClicks" class="kpi-value">-</div>
                <div class="kpi-sub">按钮点击数量合计</div>
            </div>
        </div>
        -->

        <table id="manage_dmCenter_datagrid" class="easyui-datagrid" url="/manage/showcase/daily/dmCenter/listJson.html" toolbar="#manage_dmCenter_toolbar" fit="true" border="false" fitColumns="false"
               border="true" pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="false">
            <thead>
            <tr>
                <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                <th field="protect" style="width: 5%" formatter="showProtect">防护</th>
                <th field="userName" formatter="userTagFormatter">用户名</th>
                <th field="region" formatter="regionTagFormatter">地区</th>
                <th field="displayOption" formatter="displayOptionFunction">广告类型</th>
                 <th field="pixel" formatter="pixelTagFormatter">像素Id</th>
                 <th field="remark" formatter="remarkTagFormatter">备注</th>
                 <th field="link" formatter="domainFunction1" >链接地址</th>
                <th field="domain" formatter="domainFunction">访问域名</th>
                <th field="visitsNumber"  formatter="accessNumberFunction" sortable="true" sum="true">域名访问量</th>
                <th field="trolls"  formatter="tollsNumberFunction" >跳转水军群数量</th>
                <th field="clicksNumber" formatter="clickNumberFunction" sortable="true" sum="true">按钮点击数量</th>
                <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
                <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
            </tr>
            </thead>
            <thead frozen="true">
            <tr>
                <th field="rowActions" data-options="formatter:function(value, row, index){return formatAction('manage_dmCenter_action',value,row)}">动作</th>
            </tr>
            </thead>
        </table>
        <!-- 每行的Action动作模板 -->
        <div id="manage_dmCenter_action" style="display: none;">
            <div class="btn-group btn-group-xs">
                <button onclick="editDmCondition('{0}');" class="btn btn-success btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑规则</button>
                <#if !isCurrentUserObserver>
                    <button onclick="$.acooly.framework.show('/manage/showcase/daily/dmCenter/show.html?id={0}',600,600);" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>
                    <button onclick="$.acooly.framework.edit({url:'/manage/link/dmObserverPermission/editCenter.html',id:'{0}',entity:'dmObserverPermission',width:500,height:500});" class="btn btn-info btn-xs" type="button"><i class="fa fa-users fa-fw fa-col"></i>分配观察者</button>
                    <button id="myCenter_edit1" onclick="confirmSubmit1('/manage/showcase/daily/dmCenter/eliminate.html','{0}','manage_dmCenter_datagrid');" class="btn btn-outline-secondary btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>清除记录</button>
                    <button id="myCenter_edit2" onclick="$.acooly.framework.edit({url:'/manage/showcase/daily/dmCenter/edit.html',id:'{0}',entity:'dmCenter',width:800,height:700});" class="btn btn-outline-success btn-xs" type="button"><i class="fa fa-pencil fa-fw fa-col"></i>编辑</button>
                    <button id="myCenter_edit3" onclick="deleteDmCenter('/manage/showcase/daily/dmCenter/deleteJson.html','{0}','manage_dmCenter_datagrid');" class="btn btn-outline-danger btn-xs" type="button"><i class="fa fa-trash fa-fw fa-col"></i>删除</button>
                </#if>
            </div>
        </div>
        <!-- 表格的工具栏 -->
        <div id="manage_dmCenter_toolbar">
            <#if !isCurrentUserObserver>
                <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.create({url:'/manage/showcase/daily/dmCenter/create.html',entity:'dmCenter',width:800,height:700})"><i class="fa fa-plus-circle fa-fw fa-col"></i>添加</a>
                <a href="#" class="easyui-linkbutton" plain="true" onclick="$.acooly.framework.removes('/manage/showcase/daily/dmCenter/deleteJson.html','manage_dmCenter_datagrid')"><i class="fa fa-trash fa-fw fa-col"></i>批量删除</a>
                <a href="#" class="easyui-menubutton" data-options="menu:'#manage_dmCenter_exports_menu'"><i class="fa fa-cloud-download fa-fw fa-col"></i>批量导出</a>
                <div id="manage_dmCenter_exports_menu" style="width:150px;">
                    <div onclick="$.acooly.framework.exports('/manage/showcase/daily/dmCenter/exportXls.html','manage_dmCenter_searchform','dm_center')"><i class="fa fa-file-excel-o fa-lg fa-fw fa-col"></i>Excel</div>
                    <div onclick="$.acooly.framework.exports('/manage/showcase/daily/dmCenter/exportCsv.html','manage_dmCenter_searchform','dm_center')"><i class="fa fa-file-text-o fa-lg fa-fw fa-col"></i>CSV</div>
                </div>
                <a id="myCenter_edit4" href="#" class="btn btn-outline-danger" plain="true" onclick="confirmSubmit1('/manage/showcase/daily/dmCenter/eliminateAll.html','1','manage_dmCenter_datagrid')"><i class="fa fa-plus-circle fa-fw fa-col"></i>清除全部记录</a>
            </#if>
        </div>
    </div>

    <script type="text/javascript">
        $(function () {
            $.acooly.framework.initPage('manage_dmCenter_searchform', 'manage_dmCenter_datagrid');
            // $('#manage_dmCenter_datagrid').datagrid({ onLoadSuccess: function(data){ updateKpis(data.rows || []); } });
        });

        // function updateKpis(rows){
        //     var total = rows.length;
        //     var totalVisits = 0;
        //     var totalClicks = 0;
        //     rows.forEach(function(r){
        //         var v = parseInt(r.visitsNumber, 10); if (!isNaN(v)) totalVisits += v;
        //         var c = parseInt(r.clicksNumber, 10); if (!isNaN(c)) totalClicks += c;
        //     });
        //     $('#kpiTotalCenters').text(total);
        //     $('#kpiTotalVisits').text(totalVisits);
        //     $('#kpiTotalClicks').text(totalClicks);
        // }

        // 编辑规则函数 - 添加回调刷新功能
        function editDmCondition(id) {
            var dialog = $('<div/>').dialog({
                href: contextPath + '/manage/link/dmCondition/editCenter.html?id=' + id,
                width: 700,
                height: 720,
                modal: true,
                title: '<i class="fa fa-pencil fa-lg fa-fw fa-col"></i>编辑规则',
                buttons: [{
                    text: '<i class="fa fa-save fa-lg fa-fw fa-col"></i>保存',
                    handler: function () {
                        var form = dialog.find('#manage_dmCondition_editform');
                        $.ajax({
                            url: contextPath + form.attr('action'),
                            data: form.serialize(),
                            type: 'POST',
                            dataType: 'json',
                            success: function (result) {
                                if (result.success) {
                                    dialog.dialog('close');
                                    $.acooly.framework.search('manage_dmCenter_searchform', 'manage_dmCenter_datagrid');
                                    $.acooly.messager('提示', '保存成功！', 'success');
                                } else {
                                    $.acooly.messager('错误', result.message || '保存失败！', 'danger');
                                }
                            },
                            error: function () { $.acooly.messager('错误', '网络错误，保存失败！', 'danger'); }
                        });
                    }
                }, {
                    text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col"></i>取消',
                    handler: function () { dialog.dialog('close'); }
                }],
                onClose: function () { $(this).dialog('destroy'); }
            });
        }

        function displayOptionFunction (value, row) {
            var isLanding = (value == '1');
            var text = isLanding ? '落地页' : '表单';
            var cls = isLanding ? 'metric-btn metric-btn--green' : 'metric-btn metric-btn--blue';
            return "<div style='text-align:center;'><span class='"+cls+"' onclick='openboard4("+JSON.stringify(row)+")'>"+ text +"</span></div>";
        }

        function domainFunction(value, row) {
            return "<div style='text-align: center;'><button onclick='copyToClipboard3("+JSON.stringify(row)+")' " +
                "title='" + value + " / " + row.secondaryDomain + "'  class='layui-btn layui-btn-radius'>" + value + ' / ' + row.secondaryDomain + "</button></div>";
        }

        function domainClick(row){
            navigator.clipboard.writeText(row.domain+'/'+row.secondaryDomain)
                .then(function() { $.messager.alert('复制成功',row.domain+'/'+row.secondaryDomain); })
                .catch(function(error) { $.messager.show('复制失败',error); });
        }

        function domainNumberFunction(value,row){
            if(row.displayOption== '2'){
                return "<span class='badge badge-danger'>表单无按钮</span>";
            }else {
                return "<span class='badge badge-success'>"+value+"</span>";
            }
        }

        // 用户名灰底圆角标签
        function userTagFormatter(value){
            if (!value) return '';
            var safe = $('<div>').text(value).html();
            return '<span class="tag-lg tag-lg--gray">'+safe+'</span>';
        }
        // 地区灰底圆角标签
        function regionTagFormatter(value){
            if (!value) return '';
            var safe = $('<div>').text(value).html();
            return '<span class="tag-lg tag-lg--blue">'+safe+'</span>';
        }
        // 像素ID 标签
        function pixelTagFormatter(value){
            if (value == null) return '';
            var safe = $('<div>').text(value).html();
            return '<span class="tag-lg tag-lg--muted">'+safe+'</span>';
        }
        // 备注 标签（长文本截断+title）
        function remarkTagFormatter(value){
            if (!value) return '';
            var safe = $('<div>').text(value).html();
            return '<span title="'+safe+'" class="tag-lg tag-lg--muted" style="display:inline-block;max-width:260px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">'+safe+'</span>';
        }

        function displayOptionTwoFunction (value){
            if (value=='1'){
                return ' <i class="fa fa-check-square fa-fw fa-col" style="color: cornflowerblue" />'
            }else {
                return '<i class="fa  fa-remove fa-fw fa-col" style="color: red"/>'
            }
        }

        function  domainFunction1(value,row){
            if (row.diversion=='1'){
                return "<button onclick='manage_showcase_dmCenter_add()' class='loop-link-btn'><i class='fa fa-random'></i> 跳转到轮询链接</button>";
            }else {
                return "<button onclick='copyToClipboard2("+JSON.stringify(value)+")' class='btn btn-link expandable'>" + value+ "</button>";
            }
        }

        function copyToClipboard2(text) {
            var clipboard = new ClipboardJS('.btn', { text: function() { return text; } });
            clipboard.on('success', function(e) { $.messager.alert('复制成功', e.text); clipboard.destroy(); });
            clipboard.on('error', function(e) { $.messager.alert('复制失败', e.action); clipboard.destroy(); });
            clipboard.onClick(event);
        }

        function showProtect(value,row){
            if(row.protect=='0'){
                return "<span class='badge badge-success'>防护</span>";
            }else {
                return "<span class='badge badge-danger'>无防护</span>";
            }
        }

        function copyToClipboard3(row) {
            var clipboard = new ClipboardJS('.btn', { text: function() { return 'https://'+row.domain+'/'+row.secondaryDomain; } });
            clipboard.on('success', function(e) { $.messager.alert('复制成功', e.text); clipboard.destroy(); });
            clipboard.on('error', function(e) { $.messager.alert('复制失败', e.action); clipboard.destroy(); });
            clipboard.onClick(event);
        }

        function openboard4(row){ window.open("http://"+row.domain+'/'+row.secondaryDomain,'_blank'); }

        function manage_showcase_dmCenter_add() {
            $('#layout_center_tabs').tabs("add", { title: '轮询链接', closable: true, iconCls: 'fa fa-commenting', href: '/manage/link/linkSrcs/index.html' });
        }

        function confirmSubmit1(url, id, datagrid, confirmTitle, confirmMessage, successCallBack) {
            var title = confirmTitle ? confirmTitle : '确定';
            var message = confirmMessage ? confirmMessage : '您是否清除记录？';
            $.messager.confirm(title, message, function (r) {
                if (r) {
                    $.ajax({
                        url: contextPath + url,
                        data: { id: id },
                        success: function (result) {
                            if (result.success) { $('#' + datagrid).datagrid('reload'); }
                            if (result.message) { $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger'); }
                        }
                    });
                }
            });
        }

        function  deleteDmCenter(url, id, datagrid, confirmTitle, confirmMessage, successCallBack){
            var title = confirmTitle ? confirmTitle : '确定';
            var message = confirmMessage ? confirmMessage : '您是否要提交该操作？';
            $.messager.confirm(title, message, function (r) {
                if (r) {
                    var loadingIndex = layer.load(2, { shade: [0.5, '#fff'], content: '加载中...', success: function (layero) { layero.find('.layui-layer-content').css({ 'padding-top': '39px','width': '60px' }); } });
                    $.ajax({
                        url: contextPath + url,
                        data: { id: id },
                        success: function (result) {
                            layer.close(loadingIndex);
                            if (typeof (result) == 'string') result = eval('(' + result + ')');
                            if (result.success) {
                                var className = $('#' + datagrid).attr('class');
                                if (className.indexOf('easyui-treegrid') != -1) { $('#' + datagrid).treegrid('reload'); } else { $('#' + datagrid).datagrid('reload'); }
                                if (successCallBack) { successCallBack.call(this); }
                            }
                            if (result.message) { $.acooly.messager('提示', result.message, result.success ? 'success' : 'danger'); }
                        }
                    });
                }
            });
        }

        //点击按钮查看数据详情（使用更小的按钮样式）
        function clickNumberFunction(value,row){
            if(row.displayOption== '2'){
                return "<span class='badge badge-danger'>表单无按钮</span>";
            }else {
                var num = (value == null ? 0 : value);
                var levelCls = 'metric-level-low';
                if (num >= 50) levelCls = 'metric-level-high';
                else if (num >= 20) levelCls = 'metric-level-mid';
                return "<div style='text-align:center;'><span class='metric-btn "+levelCls+"' onclick='showClick("+JSON.stringify(row)+")'><i class='fa fa-mouse-pointer'></i> 点击 <b>" + num + "</b> 次</span></div>";
            }
        }

        function accessNumberFunction(value,row){
            var num = (value == null ? 0 : value);
            var levelCls = 'metric-level-low';
            if (num >= 100) levelCls = 'metric-level-high';
            else if (num >= 50) levelCls = 'metric-level-mid';
            var flame = (num >= 100) ? " <i class='fa fa-fire' style='color:#ef4444;'></i>" : "";
            return "<div style='text-align:center;'><span class='metric-btn "+levelCls+"' onclick='showAccess("+JSON.stringify(row)+")'><i class='fa fa-line-chart'></i> 访问 <b>" + num + "</b> 次"+ flame +"</span></div>";
        }

        function tollsNumberFunction(value,row){
            var num = (value == null ? 0 : value);
            var levelCls = 'metric-level-low';
            if (num >= 50) levelCls = 'metric-level-high';
            else if (num >= 20) levelCls = 'metric-level-mid';
            return "<div style='text-align:center;'><span class='metric-btn "+levelCls+"' onclick='showTrolls("+JSON.stringify(row)+")'><i class='fa fa-users'></i> 水军 <b>" + num + "</b> 组</span></div>";
        }

        function showClick(row) {
            var url ='/manage/link/dmClick/buildClickUrl?centerId='+row.id; return this. showClickGet({ url: url });
        }
        function showAccess(row) {
            var url ='/manage/link/dmAccess/buildAccessUrl?centerId='+row.id; return this.showClickGet({ url: url });
        }
        function showTrolls(row) {
            var url ='/manage/link/dmTrolls/buildTrollsUrl?centerId='+row.id; return this.showClickGet({ url: url });
        }

        function showClickGet(opts) {
            var url = opts.url; var width = opts.width != null ? opts.width : 1600; var height = opts.height != null ? opts.height : 850; var title = opts.title != null ? opts.title : '<i class="fa fa-file-o fa-lg fa-fw fa-col"></i>查看';
            var d = $('<div/>').dialog({ href: contextPath + url, width: width, height: height, modal: true, title: title, buttons: [{ text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col"></i>关闭', handler: function () { d.dialog('close'); } }], onClose: function () { $(this).dialog('destroy'); } });
            return d;
        }

        // 按钮悬浮伸缩
        document.addEventListener('DOMContentLoaded', function() {
            const expandableButtons = document.querySelectorAll('.expandable');
            expandableButtons.forEach(button => {
                button.addEventListener('mouseenter', function() { this.style.width = '520px'; });
                button.addEventListener('mouseleave', function() { this.style.width = '420px'; });
            });
        });

    </script>
</div>
