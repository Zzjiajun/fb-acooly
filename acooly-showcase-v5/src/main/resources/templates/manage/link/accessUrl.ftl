<div class="easyui-layout" data-options="fit: true, border: false">
    <!-- 查询条件 -->
    <div data-options="region:'north', border: false" style="padding: 5px; overflow: hidden;">
        <form id="manage_accessUrl_searchform" class="form-inline ac-form-search" onsubmit="return false">
            <div class="form-group" style="display: none">
                <input type="text" class="form-control form-control-sm" name="search_EQ_centerId" value='${k}' />
            </div>
            <div class="form-group">
                <label class="col-form-label">访问时间：</label>
                <input type="text" class="form-control form-control-sm" id="search_GTE_createTime" name="search_GTE_createTime" onFocus="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd'})" />
                <span class="mr-1 ml-1">至</span>
                <input type="text" class="form-control form-control-sm" id="search_LTE_createTime" name="search_LTE_createTime" onFocus="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd'})" />
            </div>
            <br>
            <div class="form-group">
                <label class="col-form-label">是否通过</label>
                <select name="search_EQ_passed" class="form-control select2bs4" data-options="required:true">
                    <option value="">全部</option>
                    <option value="0">通过</option>
                    <option value="1">未通过</option>
                </select>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_accessUrl_searchform', 'manage_accessUrl_datagrid');">
                    <i class="fa fa-search fa-fw fa-col"></i> 查询
                </button>
            </div>
            <div class="form-group">
                <button class="btn btn-sm btn-success" type="button" onclick="exportsAccessUrl('/manage/link/dmAccess/exportXls.html','manage_accessUrl_searchform','dm_access','${k}')">
                    <i class="fa fa-file-excel-o fa-fw fa-col"></i> 导出Excel记录表
                </button>
            </div>
            <div class="form-group">
                <label class="col-form-label">显示时区：</label>
                <select id="timezoneSelector" class="form-control select2bs4" style="width: 160px;">
                    <option value="Asia/Shanghai">中国上海</option>
                    <option value="Europe/Berlin">德国柏林</option>
                    <option value="America/New_York">纽约</option>
                </select>
            </div>
        </form>
    </div>

    <style>
      /* 轻量风格与紧凑表格 */
      .kpi-wrap { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 8px; margin: 4px 0 6px; }
      .kpi-card { background: #fff; border: 1px solid #eef0f3; border-radius: 8px; padding: 8px 10px; box-shadow: 0 1px 2px rgba(0,0,0,.03); transition: box-shadow .2s ease, transform .2s ease; }
      .kpi-card:hover { box-shadow: 0 2px 6px rgba(0,0,0,.06); transform: translateY(-1px); }
      .kpi-title { color: #6b7280; font-size: 11px; margin-bottom: 4px; }
      .kpi-value { color: #0f172a; font-size: 18px; font-weight: 700; letter-spacing: .2px; }
      .kpi-sub { color: #9ca3af; font-size: 11px; }

      .badge { display: inline-block; padding: 2px 6px; border-radius: 10px; font-size: 11px; line-height: 1.4; }
      .badge-success { color: #065f46; background: #d1fae5; border: 1px solid #a7f3d0; }
      .badge-danger  { color: #991b1b; background: #fee2e2; border: 1px solid #fecaca; }
      .badge-info    { color: #1e40af; background: #dbeafe; border: 1px solid #bfdbfe; }
      .badge-primary { color: #1d4ed8; background: #e0ecff; border: 1px solid #c7dbff; }
      .badge-muted   { color: #e80808; background: #f3f4f6; border: 1px solid #e5e7eb; }

      .ellipsis { max-width: 240px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; display: inline-block; vertical-align: bottom; }

      /* EasyUI 表格紧凑化与悬浮 */
      .datagrid-row { font-size: 12px; }
      .datagrid-header .datagrid-cell, .datagrid-body .datagrid-cell { padding: 6px 8px; }
      .datagrid-row-over { background: #f7fafc !important; }
      .datagrid-row-selected { background: #eef2ff !important; }
    </style>

    <div data-options="region:'center', border: false">
        <section class="content" style="min-height: 10px; padding-top: 5px">
            <div class="row">
                <section class="col-lg-12 connectedSortable">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title">
                                <i class="fas fa-chart-pie mr-1"></i>
                                访问统计
                            </h3>
                            <div class="card-tools">
                                <ul class="nav nav-pills dropdown-menu-lg-right">
                                    <li class="nav-item">
                                        <a class="nav-link active" href="#revenue-chart" data-toggle="tab">数据表格</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="#sales-chart" data-toggle="tab">数据折线图</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="card-body">
                            <!-- KPI 概览 -->
                            <div class="kpi-wrap">
                              <div class="kpi-card">
                                <div class="kpi-title">访问总次数</div>
                                <div id="kpiTotalRows" class="kpi-value">-</div>
                                <div class="kpi-sub">当前筛选范围</div>
                              </div>
                              <div class="kpi-card">
                                <div class="kpi-title">独立 IP</div>
                                <div id="kpiUniqueIps" class="kpi-value">-</div>
                                <div class="kpi-sub">按 IP 去重</div>
                              </div>
                              <div class="kpi-card">
                                <div class="kpi-title">通过率</div>
                                <div id="kpiPassRate" class="kpi-value">-</div>
                                <div class="kpi-sub">通过/总次数</div>
                              </div>
                              <div class="kpi-card">
                                <div class="kpi-title">访问地区（Top3）</div>
                                <div id="kpiTopRegions" class="kpi-value" style="font-size:14px; font-weight:600; line-height:1.4;">-</div>
                                <div class="kpi-sub">按地区计数</div>
                              </div>
                              <div class="kpi-card">
                                <div class="kpi-title">机型（Top3）</div>
                                <div id="kpiTopModels" class="kpi-value" style="font-size:14px; font-weight:600; line-height:1.4;">-</div>
                                <div class="kpi-sub">按机型计数</div>
                              </div>
                              <#if !isCurrentUserObserverAccess>
                              <div class="kpi-card">
                                 <div class="kpi-title">来源（Top3）</div>
                                 <div id="kpiTopSources" class="kpi-value" style="font-size:14px; font-weight:600; line-height:1.4;">-</div>
                                 <div class="kpi-sub">按来源计数</div>
                              </div>
                              </#if>

                            </div>

                            <div class="tab-content p-0">
                                <!-- 数据表格 -->
                                <div class="chart tab-pane active" id="revenue-chart" style="position: relative; height: 500px;">
                                    <table  id="manage_accessUrl_datagrid" class="easyui-datagrid"
                                           url="/manage/link/dmAccess/listAccessUrl?centerId=${k}"  fit="true" border="false" fitColumns="false"
                                           pagination="true" idField="id" pageSize="100" pageList="[10, 20, 30, 40, 50,100,1000]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                                        <thead>
                                        <tr>
                                            <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                                            <th field="id" sortable="true">id</th>
                                            <th field="createTime" formatter="dateTimeOneFormatter">访问时间</th>
                                            <th field="region" formatter="contentFormatter">访客地区</th>
                                            <th field="ip" formatter="deviceDetailsFormatter">IP</th>
                                            <th field="continent" formatter="timeZoneFormatter">时区</th>
                                            <th field="accessPath" formatter="contentFormatter">访问路径</th>
                                            <th field="accessDevice" formatter="clickDeviceFormatterFunction">访问设备</th>
                                            <th field="models" formatter="contentFormatter">机型</th>
                                            <#if !isCurrentUserObserverAccess>
                                                <th field="source" formatter="contentFormatter">来源</th>
                                            </#if>
                                            <th field="visitorType" formatter="clickTypeFormatterFunction">访客类型</th>
                                            <th field="language" formatter="contentFormatter">语言</th>
                                            <th field="passed" formatter="displayPassedFunction">是否通过</th>
                                            <#if !isCurrentUserObserverAccess>
                                                <th field="deviceDetails"  formatter="showDetails">设备和客户端详情</th>
                                            </#if>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                                <!-- 折线图 -->
                                <div class="chart tab-pane" id="sales-chart" style="position: relative; height: 500px; width: 100%">
                                    <div id="chartContainer" style="width: 1200px; height: 500px;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </section>
    </div>
</div>
<script>
    dayjs.extend(dayjs_plugin_utc);
    dayjs.extend(dayjs_plugin_timezone);
</script>
<script type="text/javascript">
    $(function () {
        $.acooly.framework.initPage('manage_accessUrl_searchform', 'manage_accessUrl_datagrid');

        var chart = echarts.init(document.getElementById('chartContainer'));

        var chartOptions = {
            color: ['#4f46e5', '#10b981'],
            title: { text: '每日 IP 和访问次数数' },
            tooltip: { trigger: 'axis', backgroundColor: 'rgba(17,24,39,.95)', borderWidth: 0, textStyle: { color: '#e5e7eb' } },
            legend: { data: ['IP', '访问次数'] },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: { type: 'category', boundaryGap: false, axisLine: { lineStyle: { color: '#e5e7eb' } }, axisLabel: { color: '#6b7280' }, data: [] },
            yAxis: { type: 'value', axisLine: { lineStyle: { color: '#e5e7eb' } }, axisLabel: { color: '#6b7280' }, splitLine: { lineStyle: { color: '#f3f4f6' } } },
            series: [
                { name: 'IP', type: 'line', smooth: true, symbolSize: 6, areaStyle: { color: 'rgba(79,70,229,.08)' }, data: [] },
                { name: '访问次数', type: 'line', smooth: true, symbolSize: 6, areaStyle: { color: 'rgba(16,185,129,.08)' }, data: [] }
            ]
        };

        chart.setOption(chartOptions);

        function updateChart(data) {
            var dates = [];
            var ipCounts = {};
            var totalRows = {};

            data.forEach(function (item) {
                var date = item.createTime.split(' ')[0];
                if (!dates.includes(date)) {
                    dates.push(date);
                    ipCounts[date] = new Set();
                    totalRows[date] = 0;
                }
                ipCounts[date].add(item.ip);
                totalRows[date]++;
            });

            // 按日期升序排序
            dates.sort();

            var ipCountData = dates.map(function (date) { return ipCounts[date].size; });
            var totalRowsData = dates.map(function (date) { return totalRows[date]; });

            chart.setOption({
                xAxis: { data: dates },
                series: [ { name: 'IP', data: ipCountData }, { name: '访问次数', data: totalRowsData } ]
            });
        }

        function updateKpis(rows) {
            var total = rows.length;
            var ipSet = new Set();
            var pass = 0;
            var regionCount = {};
            var modelCount = {};
            var sourceCount = {};
            rows.forEach(function (r) {
                if (r.ip) ipSet.add(r.ip);
                if (String(r.passed) === '0') pass++;
                if (r.region) regionCount[r.region] = (regionCount[r.region] || 0) + 1;
                if (r.models) modelCount[r.models] = (modelCount[r.models] || 0) + 1;
                if (r.source) sourceCount[r.source] = (sourceCount[r.source] || 0) + 1;
            });
            var uniqueIps = ipSet.size;
            var passRate = total > 0 ? Math.round(pass * 1000 / total) / 10 + '%' : '-';
            $('#kpiTotalRows').text(total);
            $('#kpiUniqueIps').text(uniqueIps);
            $('#kpiPassRate').text(passRate);

            function top3(obj){
                var arr = Object.keys(obj).map(function(k){ return { key: k, val: obj[k] }; });
                arr.sort(function(a,b){ return b.val - a.val; });
                return arr.slice(0,3).map(function(it){ return it.key + '(' + it.val + ')'; }).join(' / ') || '-';
            }
            $('#kpiTopRegions').text(top3(regionCount));
            $('#kpiTopModels').text(top3(modelCount));
            $('#kpiTopSources').text(top3(sourceCount));
        }

        $('#manage_accessUrl_datagrid').datagrid({
            onLoadSuccess: function (data) {
                updateChart(data.rows);
                updateKpis(data.rows);
                enableDatagridColumnDrag('#manage_accessUrl_datagrid');
            }
        });
    });

    window.currentTimezone = 'Asia/Shanghai';
    $('#timezoneSelector').on('change', function() {
        window.currentTimezone = $(this).val();
        $('#manage_accessUrl_datagrid').datagrid('reload');
    });

    function dateTimeOneFormatter(value, row, index) {
        if (!value) return '';
        // value 例：'2024-05-01 08:00:00'，假设为服务器上海时区
        // 先解析为 dayjs 对象，再转为选中时区
        return dayjs.tz(value, 'Asia/Shanghai').tz(currentTimezone).format('YYYY-MM-DD HH:mm:ss');
    }


    // 简版：为 EasyUI datagrid 启用列拖拽（仅普通列，跳过复选框列；作用于非冻结区 view2）
    function enableDatagridColumnDrag(gridSelector) {
        var $grid = $(gridSelector);
        var $panel = $grid.datagrid('getPanel');
        if (!$panel || $panel.length === 0) { return; }

        var $headerRow = $panel.find('div.datagrid-view2 .datagrid-header .datagrid-header-row');
        if ($headerRow.length === 0) { return; }

        // 解绑旧事件，避免重复绑定
        $headerRow.find('td[field]').each(function(){
            this.ondragstart = null;
            this.ondragover = null;
            this.ondrop = null;
            this.ondragend = null;
            this.draggable = false;
        });

        var dragState = { fromField: null, toField: null };

        function moveColumnDom(fromField, toField) {
            if (!fromField || !toField || fromField === toField) { return; }
            var $view2 = $panel.find('div.datagrid-view2');

            // 移动表头单元
            var $fromTh = $headerRow.find('td[field="' + fromField + '"]');
            var $toTh = $headerRow.find('td[field="' + toField + '"]');
            if ($fromTh.length === 0 || $toTh.length === 0) { return; }

            var fromIndex = $fromTh.index();
            var toIndex = $toTh.index();

            if (fromIndex < toIndex) {
                $toTh.after($fromTh);
            } else {
                $toTh.before($fromTh);
            }

            // 同步移动每一行对应单元格
            $view2.find('.datagrid-body tr.datagrid-row').each(function(){
                var $row = $(this);
                var $fromTd = $row.find('td[field="' + fromField + '"]');
                var $toTd = $row.find('td[field="' + toField + '"]');
                if ($fromTd.length === 0 || $toTd.length === 0) { return; }
                var fIdx = $fromTd.index();
                var tIdx = $toTd.index();
                if (fIdx < tIdx) {
                    $toTd.after($fromTd);
                } else {
                    $toTd.before($fromTd);
                }
            });
        }

        // 设置可拖拽
        $headerRow.find('td[field]').each(function(){
            var $th = $(this);
            var field = $th.attr('field');
            if (!field || field === 'showCheckboxWithId') { return; }

            this.draggable = true;
            this.ondragstart = function (e) {
                dragState.fromField = field;
                try { e.dataTransfer.setData('text/plain', field); } catch (err) {}
                // 提示样式
                $th.addClass('ac-col-dragging');
            };
            this.ondragover = function (e) { e.preventDefault(); };
            this.ondrop = function (e) {
                e.preventDefault();
                dragState.toField = field;
                if (dragState.fromField && dragState.toField) {
                    moveColumnDom(dragState.fromField, dragState.toField);
                }
                dragState.fromField = null;
                dragState.toField = null;
            };
            this.ondragend = function () { $headerRow.find('.ac-col-dragging').removeClass('ac-col-dragging'); };
        });
    }


    function clickDeviceFormatterFunction(value) {
        if (value == '1') {
            return '<i class="fa fa-desktop fa-fw fa-col" style="color: firebrick; align-items: center;"></i>';
        } else {
            return '<i class="fa fa-mobile-phone fa-fw fa-col" style="color: cornflowerblue; align-items: center;"></i>';
        }
    }

    function clickTypeFormatterFunction(value) {
        if (value == '1') {
            return '<span class="badge badge-muted">旧访客</span>';
        } else {
            return '<span class="badge badge-primary">新访客</span>';
        }
    }

    function displayPassedFunction(value, row) {
        if (value === '0') {
            return '<span class="badge badge-success">通过</span>';
        } else {
            return '<span class="badge badge-danger" style="margin-right:6px;">失败</span>' +
                '<button onclick="showTrollsDetailsList(' +row.id+ ')" class="btn btn-outline-danger btn-xs" type="button">' +
                '<i class="fa fa-info fa-fw fa-col"></i>详情</button>';
        }
    }


    function deviceDetailsFormatter(value, row) {
        var ip = value || '';
        var isp = row.ipDetails || '';
        return '<div  style="font-size: 11px;" title="' + ip + '">Ip：' + ip + '</div>'
            + '<div class="ellipsis" style="color:#8b8f98; font-size:11px; margin-top:2px;" title="' + isp + '">运营商：' + isp + '</div>';
    }

    function timeZoneFormatter(value, row) {
        return '<div style="font-size: 11px;" >设备时区：' + (value || '') + '</div>'
            + '<div style="color:#8b8f98; font-size:11px;">Ip时区：' + (row.ipTime || '') + '</div>';
    }
    function exportsAccessUrl(url, searchForm, fileName, centerId){
        var queryParams = $.acooly.framework.afterQueryParams[searchForm];
        if (isEmptyObject(queryParams)) {
            queryParams = serializeObject($('#' + searchForm));
        }
        if (fileName) {
            $(queryParams).attr('exportFileName', fileName);
        }
        if (centerId) {
            $(queryParams).attr('centerId', centerId);
        }

        $.acooly.framework.createAndSubmitForm(url, queryParams);
    }

    function showAccessDetails(value) {
        if (!value) { return ''; }
        if (typeof value === 'string') {
            try { value = JSON.parse(value); } catch (e) { return value; }
        }
        if (Array.isArray(value)) {
            let html = '<div style="max-height: 100px; overflow-y: auto;">';
            value.forEach((item) => { html += `<div>${item}</div>`; });
            html += '</div>'; return html;
        }
        if (typeof value === 'object') {
            let html = '<div style="max-height: 100px; overflow-y: auto;">';
            Object.entries(value).forEach(([key, val]) => { html += `<div>${key}: ${val}</div>`; });
            html += '</div>'; return html;
        }
        return value;
    }

    function showDetails(value,row) {
        return '<button onclick="showDetailsList(' +row.id+ ')" class="btn btn-outline-primary btn-xs" type="button"><i class="fa fa-info fa-fw fa-col"></i>查看</button>';
    }

    function showDetailsList(id) {
        var url ='/manage/link/dmAccess/showAccessUrl.html?id='+id;
        $.acooly.framework.show(url,500,500);
    }

    function showTrollsDetailsList(id) {
        var url ='/manage/link/dmAccess/showDetailsAccessUrl.html?id='+id;
        $.acooly.framework.show(url,500,500);
    }
</script>
