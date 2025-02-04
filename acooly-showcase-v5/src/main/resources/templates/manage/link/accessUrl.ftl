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
            <div class="form-group">
                <button class="btn btn-sm btn-primary" type="button" onclick="$.acooly.framework.search('manage_accessUrl_searchform', 'manage_accessUrl_datagrid');">
                    <i class="fa fa-search fa-fw fa-col"></i> 查询
                </button>
            </div>
        </form>
    </div>

    <div data-options="region:'center', border: false">
        <section class="content" style="min-height: 10px; padding-top: 5px">
            <div class="row">
                <section class="col-lg-12 connectedSortable">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title">
                                <i class="fas fa-chart-pie mr-1"></i>
                                Sales
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
                            <div class="tab-content p-0">
                                <!-- Morris chart - Sales -->
                                <div class="chart tab-pane active" id="revenue-chart" style="position: relative; height: 500px;">
                                    <table  id="manage_accessUrl_datagrid" class="easyui-datagrid"
                                           url="/manage/link/dmAccess/listAccessUrl?centerId=${k}" fit="true" border="false" fitColumns="false"
                                           pagination="true" idField="id" pageSize="100" pageList="[10, 20, 30, 40, 50,100,1000]" sortName="id" sortOrder="desc" checkOnSelect="true" selectOnCheck="true" singleSelect="true">
                                        <thead>
                                        <tr>
                                            <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
                                            <th field="id" sortable="true">id</th>
                                            <th field="createTime" formatter="dateTimeFormatter">访问时间</th>
                                            <th field="ip" formatter="contentFormatter">IP地址</th>
                                            <th field="region" formatter="contentFormatter">访客地区</th>
                                            <th field="accessPath" formatter="contentFormatter">访问路径</th>
                                            <th field="accessDevice" formatter="clickDeviceFormatterFunction">访问设备</th>
                                            <th field="visitorType" formatter="clickTypeFormatterFunction">访客类型</th>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
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
<script type="text/javascript">
    $(function () {
        $.acooly.framework.initPage('manage_accessUrl_searchform', 'manage_accessUrl_datagrid');

        var chart = echarts.init(document.getElementById('chartContainer'));

        var chartOptions = {
            title: {
                text: '每日 IP 和访问次数数'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['IP', '访问次数']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: []
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: 'IP',
                    type: 'line',
                    data: []
                },
                {
                    name: '访问次数',
                    type: 'line',
                    data: []
                }
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

            var ipCountData = dates.map(function (date) {
                return ipCounts[date].size;
            });

            var totalRowsData = dates.map(function (date) {
                return totalRows[date];
            });

            chart.setOption({
                xAxis: {
                    data: dates
                },
                series: [
                    {
                        name: 'IP',
                        data: ipCountData
                    },
                    {
                        name: '访问次数',
                        data: totalRowsData
                    }
                ]
            });
        }

        $('#manage_accessUrl_datagrid').datagrid({
            onLoadSuccess: function (data) {
                updateChart(data.rows);
            }
        });
    });

    function clickDeviceFormatterFunction(value) {
        if (value == '1') {
            return '<i class="fa fa-desktop fa-fw fa-col" style="color: firebrick; align-items: center;"></i>';
        } else {
            return '<i class="fa fa-mobile-phone fa-fw fa-col" style="color: cornflowerblue; align-items: center;"></i>';
        }
    }

    function clickTypeFormatterFunction(value) {
        if (value == '1') {
            return '旧点击访客<i class="fa fa-user-times fa-fw fa-col" style="color: red; align-items: center;" title="旧访客"></i>';
        } else {
            return '新点击访客<i class="fa fa-user-plus fa-fw fa-col" style="color: green; align-items: center;" title="新访客"></i>';
        }
    }
</script>
