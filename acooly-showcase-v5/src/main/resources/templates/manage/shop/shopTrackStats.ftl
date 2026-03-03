<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div class="easyui-layout" data-options="fit : true,border : false">
    <!-- 统计大屏视图 -->
    <div data-options="region:'center',border:false">
        <div id="statsDashboard" style="width:100%;height:100%;overflow:auto;background:#f5f7fa;padding:20px;">
            <!-- 头部 -->
            <div style="height:70px;display:flex;align-items:center;justify-content:space-between;margin-bottom:20px;background:#fff;border-radius:8px;padding:0 30px;box-shadow:0 2px 8px rgba(0,0,0,0.08);">
                <div style="font-size:24px;font-weight:600;color:#303133;">
                    <i class="fa fa-chart-line"></i> 访问统计大屏
                </div>
                <div style="display:flex;align-items:center;gap:15px;flex:1;justify-content:center;">
                    <!-- 时间选择器 -->
                    <div style="display:flex;align-items:center;gap:8px;background:#f5f7fa;border-radius:4px;padding:4px;">
                        <button class="date-range-btn" data-range="today" onclick="selectDateRange('today')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">今日</button>
                        <button class="date-range-btn" data-range="yesterday" onclick="selectDateRange('yesterday')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">昨日</button>
                        <button class="date-range-btn" data-range="thisWeek" onclick="selectDateRange('thisWeek')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">本周</button>
                        <button class="date-range-btn" data-range="lastWeek" onclick="selectDateRange('lastWeek')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">上周</button>
                        <button class="date-range-btn" data-range="thisMonth" onclick="selectDateRange('thisMonth')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">本月</button>
                        <button class="date-range-btn" data-range="lastMonth" onclick="selectDateRange('lastMonth')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">上月</button>
                        <button class="date-range-btn" data-range="last7Days" onclick="selectDateRange('last7Days')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">近7天</button>
                        <button class="date-range-btn" data-range="last30Days" onclick="selectDateRange('last30Days')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">近30天</button>
                        <button class="date-range-btn" data-range="custom" onclick="selectDateRange('custom')" style="padding:6px 12px;border:none;background:transparent;border-radius:4px;cursor:pointer;font-size:13px;color:#606266;">自定义</button>
                    </div>
                    <div id="customDateRange" style="display:none;align-items:center;gap:8px;">
                        <input type="date" id="dashboardStartDate" style="background:#fff;border:1px solid #dcdfe6;border-radius:4px;padding:6px 10px;color:#606266;font-size:13px;outline:none;width:130px;" />
                        <span style="color:#909399;">至</span>
                        <input type="date" id="dashboardEndDate" style="background:#fff;border:1px solid #dcdfe6;border-radius:4px;padding:6px 10px;color:#606266;font-size:13px;outline:none;width:130px;" />
                        <button onclick="confirmCustomDateRange()" style="background:#67c23a;border:none;border-radius:4px;padding:6px 15px;color:#fff;cursor:pointer;font-size:13px;outline:none;"><i class="fa fa-check"></i> 确定</button>
            </div>
                    <div id="dateRangeDisplay" style="color:#606266;font-size:13px;min-width:200px;text-align:center;"></div>
                    <button onclick="loadDashboardStats()" style="background:#409eff;border:none;border-radius:4px;padding:8px 20px;color:#fff;cursor:pointer;font-size:14px;outline:none;"><i class="fa fa-sync-alt"></i> 刷新</button>
            </div>
                <div id="dashboardCurrentTime" style="font-size:16px;color:#909399;"></div>
            </div>
            
            <!-- 统计卡片区 -->
            <div style="display:flex;gap:20px;margin-bottom:20px;flex-wrap:wrap;">
                <div class="stat-card" style="flex:1;min-width:200px;border-radius:8px;padding:25px;display:flex;flex-direction:column;align-items:center;background:#fff;box-shadow:0 2px 8px rgba(0,0,0,0.08);border-left:4px solid #409eff;position:relative;">
                    <div style="font-size:32px;margin-bottom:12px;color:#409eff;"><i class="fa fa-eye" style="text-shadow:0 2px 4px rgba(64,158,255,0.3);"></i></div>
                    <div id="dashboardTotalPv" style="font-size:28px;font-weight:600;margin-bottom:8px;color:#303133;">-</div>
                    <div style="font-size:14px;color:#909399;">总访问量 (PV)</div>
                </div>
                <div class="stat-card" style="flex:1;min-width:200px;border-radius:8px;padding:25px;display:flex;flex-direction:column;align-items:center;background:#fff;box-shadow:0 2px 8px rgba(0,0,0,0.08);border-left:4px solid #67c23a;position:relative;">
                    <div style="font-size:32px;margin-bottom:12px;color:#67c23a;"><i class="fa fa-users" style="text-shadow:0 2px 4px rgba(103,194,58,0.3);"></i></div>
                    <div id="dashboardTotalUv" style="font-size:28px;font-weight:600;margin-bottom:8px;color:#303133;">-</div>
                    <div style="font-size:14px;color:#909399;">独立访客 (UV)</div>
                </div>
                <div class="stat-card" style="flex:1;min-width:200px;border-radius:8px;padding:25px;display:flex;flex-direction:column;align-items:center;background:#fff;box-shadow:0 2px 8px rgba(0,0,0,0.08);border-left:4px solid #e6a23c;position:relative;">
                    <div style="font-size:32px;margin-bottom:12px;color:#e6a23c;"><i class="fa fa-hourglass" style="text-shadow:0 2px 4px rgba(230,162,60,0.3);"></i></div>
                    <div id="dashboardAvgDuration" style="font-size:28px;font-weight:600;margin-bottom:8px;color:#303133;">-</div>
                    <div style="font-size:14px;color:#909399;">平均停留时间</div>
                </div>
                <div class="stat-card" style="flex:1;min-width:200px;border-radius:8px;padding:25px;display:flex;flex-direction:column;align-items:center;background:#fff;box-shadow:0 2px 8px rgba(0,0,0,0.08);border-left:4px solid #909399;position:relative;">
                    <div style="font-size:32px;margin-bottom:12px;color:#909399;"><i class="fa fa-shopping-cart" style="text-shadow:0 2px 4px rgba(144,147,153,0.3);"></i></div>
                    <div id="dashboardProductVisitRate" style="font-size:28px;font-weight:600;margin-bottom:8px;color:#303133;">-</div>
                    <div style="font-size:14px;color:#909399;">商品访问率</div>
                </div>
                <div class="stat-card" style="flex:1;min-width:200px;border-radius:8px;padding:25px;display:flex;flex-direction:column;align-items:center;background:#fff;box-shadow:0 2px 8px rgba(0,0,0,0.08);border-left:4px solid #f56c6c;position:relative;">
                    <div style="font-size:32px;margin-bottom:12px;color:#f56c6c;"><i class="fa fa-server" style="text-shadow:0 2px 4px rgba(245,108,108,0.3);"></i></div>
                    <div id="dashboardTotalIp" style="font-size:28px;font-weight:600;margin-bottom:8px;color:#303133;">-</div>
                    <div style="font-size:14px;color:#909399;">独立IP数</div>
                </div>
                <div class="stat-card" style="flex:1;min-width:200px;border-radius:8px;padding:25px;display:flex;flex-direction:column;align-items:center;background:#fff;box-shadow:0 2px 8px rgba(0,0,0,0.08);border-left:4px solid #9c27b0;position:relative;">
                    <div style="font-size:32px;margin-bottom:12px;color:#9c27b0;"><i class="fa fa-external-link" style="text-shadow:0 2px 4px rgba(156,39,176,0.3);"></i></div>
                    <div id="dashboardBounceRate" style="font-size:28px;font-weight:600;margin-bottom:8px;color:#303133;">-</div>
                    <div style="font-size:14px;color:#909399;">跳出率</div>
                </div>
            </div>
            
            <!-- 主体区 -->
            <div style="display:flex;gap:20px;margin-bottom:20px;">
                <div style="flex:2;">
                    <div style="background:#fff;border-radius:8px;padding:20px;box-shadow:0 2px 8px rgba(0,0,0,0.08);height:400px;position:relative;">
                        <div style="font-size:16px;font-weight:600;margin-bottom:15px;color:#303133;display:flex;justify-content:space-between;align-items:center;">
                            <span><i class="fa fa-pie-chart" style="color:#409eff;"></i> 页面类型分布</span>
                            <button onclick="showChartModal('pageTypeChart', '页面类型分布')" style="background:#409eff;border:none;border-radius:4px;padding:4px 12px;color:#fff;cursor:pointer;font-size:12px;"><i class="fa fa-expand"></i> 放大</button>
                        </div>
                        <div id="pageTypeChart" style="width:100%;height:360px;"></div>
                    </div>
                </div>
                <div style="flex:1;display:flex;flex-direction:column;gap:20px;">
                    <div style="background:#fff;border-radius:8px;padding:20px;box-shadow:0 2px 8px rgba(0,0,0,0.08);height:190px;overflow-y:auto;position:relative;">
                        <div style="font-size:16px;font-weight:600;margin-bottom:15px;color:#303133;display:flex;justify-content:space-between;align-items:center;">
                            <span><i class="fa fa-list-alt" style="color:#67c23a;"></i> 页面类型统计</span>
                            <button onclick="showListModal('pageTypeList', '页面类型统计')" style="background:#67c23a;border:none;border-radius:4px;padding:4px 12px;color:#fff;cursor:pointer;font-size:12px;"><i class="fa fa-expand"></i> 放大</button>
                        </div>
                        <div id="pageTypeList"></div>
                    </div>
                    <div style="background:#fff;border-radius:8px;padding:20px;box-shadow:0 2px 8px rgba(0,0,0,0.08);height:190px;overflow-y:auto;position:relative;">
                        <div style="font-size:16px;font-weight:600;margin-bottom:15px;color:#303133;display:flex;justify-content:space-between;align-items:center;">
                            <span><i class="fa fa-map-marker" style="color:#e6a23c;"></i> IP分布 TOP10</span>
                            <button onclick="showListModal('ipList', 'IP分布 TOP10')" style="background:#e6a23c;border:none;border-radius:4px;padding:4px 12px;color:#fff;cursor:pointer;font-size:12px;"><i class="fa fa-expand"></i> 放大</button>
                        </div>
                        <div id="ipList"></div>
                    </div>
            </div>
            </div>
            
            <!-- 底部区 -->
            <div style="display:flex;gap:20px;margin-bottom:20px;">
                <div style="flex:1;background:#fff;border-radius:8px;padding:20px;box-shadow:0 2px 8px rgba(0,0,0,0.08);height:350px;position:relative;">
                    <div style="font-size:16px;font-weight:600;margin-bottom:15px;color:#303133;display:flex;justify-content:space-between;align-items:center;">
                        <span><i class="fa fa-bar-chart" style="color:#409eff;"></i> 页面访问排行</span>
                        <button onclick="showChartModal('pageRankChart', '页面访问排行')" style="background:#409eff;border:none;border-radius:4px;padding:4px 12px;color:#fff;cursor:pointer;font-size:12px;"><i class="fa fa-expand"></i> 放大</button>
                    </div>
                    <div id="pageRankChart" style="width:100%;height:300px;"></div>
                </div>
                <div style="flex:1;background:#fff;border-radius:8px;padding:20px;box-shadow:0 2px 8px rgba(0,0,0,0.08);height:350px;position:relative;">
                    <div style="font-size:16px;font-weight:600;margin-bottom:15px;color:#303133;display:flex;justify-content:space-between;align-items:center;">
                        <span><i class="fa fa-shopping-bag" style="color:#67c23a;"></i> 商品浏览排行</span>
                        <button onclick="showChartModal('productRankChart', '商品浏览排行')" style="background:#67c23a;border:none;border-radius:4px;padding:4px 12px;color:#fff;cursor:pointer;font-size:12px;"><i class="fa fa-expand"></i> 放大</button>
                    </div>
                    <div id="productRankChart" style="width:100%;height:300px;"></div>
                </div>
    </div>

            <!-- 停留时间分析 -->
            <div style="background:#fff;border-radius:8px;padding:20px;box-shadow:0 2px 8px rgba(0,0,0,0.08);height:350px;position:relative;">
                <div style="font-size:16px;font-weight:600;margin-bottom:15px;color:#303133;display:flex;justify-content:space-between;align-items:center;">
                    <span><i class="fa fa-hourglass-half" style="color:#9c27b0;"></i> 停留时间分析</span>
                    <button onclick="showChartModal('durationChart', '停留时间分析')" style="background:#9c27b0;border:none;border-radius:4px;padding:4px 12px;color:#fff;cursor:pointer;font-size:12px;"><i class="fa fa-expand"></i> 放大</button>
          </div>
                <div id="durationChart" style="width:100%;height:300px;"></div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
    <script type="text/javascript">
        $(function () {
            // 初始化大屏
            initDashboard();
        });
        
        // 当前选择的日期范围（使用window对象避免重复声明）
        if (typeof window.dashboardCurrentDateRange === 'undefined') {
            window.dashboardCurrentDateRange = 'today'; // 默认显示今天
            window.dashboardCurrentStartDate = '';
            window.dashboardCurrentEndDate = '';
            window.dashboardIsLoading = false; // 加载状态标志
        }
        
        // 初始化大屏
        function initDashboard() {
            updateDashboardTime();
            // setInterval(updateDashboardTime, 1000);
            // 默认选择今天
            selectDateRange('today');
            // 每30秒自动刷新（只在非加载状态时刷新）
            setInterval(function() {
                if (!window.dashboardIsLoading) {
                    loadDashboardStats();
                }
            }, 3000000000000);
        }
        
        // 更新时间
        function updateDashboardTime() {
            const now = new Date();
            const timeStr = now.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
            $('#dashboardCurrentTime').text(timeStr);
        }
        
        // 选择日期范围
        function selectDateRange(range) {
            window.dashboardCurrentDateRange = range;
            const today = new Date();
            const yesterday = new Date(today);
            yesterday.setDate(yesterday.getDate() - 1);
            
            // 更新按钮样式
            $('.date-range-btn').css({
                'background': 'transparent',
                'color': '#606266',
                'font-weight': 'normal'
            });
            $('.date-range-btn[data-range="' + range + '"]').css({
                'background': '#409eff',
                'color': '#fff',
                'font-weight': 'bold'
            });
            
            let startDate, endDate;
            const formatDate = (d) => {
                const year = d.getFullYear();
                const month = String(d.getMonth() + 1).padStart(2, '0');
                const day = String(d.getDate()).padStart(2, '0');
                return year + '-' + month + '-' + day;
            };
            
            switch(range) {
                case 'today':
                    startDate = endDate = formatDate(today);
                    break;
                case 'yesterday':
                    startDate = endDate = formatDate(yesterday);
                    break;
                case 'thisWeek':
                    const thisWeekStart = new Date(today);
                    thisWeekStart.setDate(today.getDate() - today.getDay());
                    startDate = formatDate(thisWeekStart);
                    endDate = formatDate(today);
                    break;
                case 'lastWeek':
                    const lastWeekEnd = new Date(today);
                    lastWeekEnd.setDate(today.getDate() - today.getDay() - 1);
                    const lastWeekStart = new Date(lastWeekEnd);
                    lastWeekStart.setDate(lastWeekEnd.getDate() - 6);
                    startDate = formatDate(lastWeekStart);
                    endDate = formatDate(lastWeekEnd);
                    break;
                case 'thisMonth':
                    startDate = formatDate(new Date(today.getFullYear(), today.getMonth(), 1));
                    endDate = formatDate(today);
                    break;
                case 'lastMonth':
                    const lastMonthEnd = new Date(today.getFullYear(), today.getMonth(), 0);
                    const lastMonthStart = new Date(today.getFullYear(), today.getMonth() - 1, 1);
                    startDate = formatDate(lastMonthStart);
                    endDate = formatDate(lastMonthEnd);
                    break;
                case 'last7Days':
                    const last7DaysStart = new Date(today);
                    last7DaysStart.setDate(today.getDate() - 6);
                    startDate = formatDate(last7DaysStart);
                    endDate = formatDate(today);
                    break;
                case 'last30Days':
                    const last30DaysStart = new Date(today);
                    last30DaysStart.setDate(today.getDate() - 29);
                    startDate = formatDate(last30DaysStart);
                    endDate = formatDate(today);
                    break;
                case 'custom':
                    $('#customDateRange').show();
                    $('#dashboardStartDate').val(window.dashboardCurrentStartDate || '');
                    $('#dashboardEndDate').val(window.dashboardCurrentEndDate || '');
                    // 如果已有日期，显示并可以立即查询
                    if (window.dashboardCurrentStartDate && window.dashboardCurrentEndDate) {
                        $('#dateRangeDisplay').text(window.dashboardCurrentStartDate + ' ~ ' + window.dashboardCurrentEndDate);
                        loadDashboardStats();
                    }
                    return;
                default:
                    startDate = endDate = formatDate(yesterday);
            }
            
            $('#customDateRange').hide();
            window.dashboardCurrentStartDate = startDate;
            window.dashboardCurrentEndDate = endDate;
            
            // 更新显示
            if (startDate === endDate) {
                $('#dateRangeDisplay').text(startDate);
            } else {
                $('#dateRangeDisplay').text(startDate + ' ~ ' + endDate);
            }
            
            // 加载数据
            loadDashboardStats();
        }
        
        // 确认自定义日期范围
        function confirmCustomDateRange() {
            const startDate = $('#dashboardStartDate').val();
            const endDate = $('#dashboardEndDate').val();
            if (startDate && endDate) {
                if (new Date(startDate) > new Date(endDate)) {
                    alert('开始日期不能大于结束日期！');
                    return;
                }
                window.dashboardCurrentDateRange = 'custom';
                window.dashboardCurrentStartDate = startDate;
                window.dashboardCurrentEndDate = endDate;
                $('#dateRangeDisplay').text(startDate + ' ~ ' + endDate);
                // 更新按钮样式
                $('.date-range-btn').css({
                    'background': 'transparent',
                    'color': '#606266',
                    'font-weight': 'normal'
                });
                $('.date-range-btn[data-range="custom"]').css({
                    'background': '#409eff',
                    'color': '#fff',
                    'font-weight': 'bold'
                });
                loadDashboardStats();
            } else {
                alert('请选择开始日期和结束日期！');
            }
        }
        
        // 自定义日期范围变化（可选：自动触发，但建议使用确定按钮）
        // $('#dashboardStartDate, #dashboardEndDate').on('change', function() {
        //     const startDate = $('#dashboardStartDate').val();
        //     const endDate = $('#dashboardEndDate').val();
        //     if (startDate && endDate) {
        //         confirmCustomDateRange();
        //     }
        // });
        
        // 清空所有图表（刷新前调用）
        function clearAllCharts() {
            // 清空商品排行图表
            const productChartDom = document.getElementById('productRankChart');
            if (productChartDom) {
                let chart = echarts.getInstanceByDom(productChartDom);
                if (chart) {
                    chart.clear();
                }
            }
            
            // 清空页面访问排行图表
            const pageChartDom = document.getElementById('pageRankChart');
            if (pageChartDom) {
                let chart = echarts.getInstanceByDom(pageChartDom);
                if (chart) {
                    chart.clear();
                }
            }
            
            // 清空停留时间分析图表
            const durationChartDom = document.getElementById('durationChart');
            if (durationChartDom) {
                let chart = echarts.getInstanceByDom(durationChartDom);
                if (chart) {
                    chart.clear();
                }
            }
            
            // 清空页面类型分布图表
            const pageTypeChartDom = document.getElementById('pageTypeChart');
            if (pageTypeChartDom) {
                let chart = echarts.getInstanceByDom(pageTypeChartDom);
                if (chart) {
                    chart.clear();
                }
            }
            
            // 清空列表数据
            $('#pageTypeList').html('');
            $('#ipList').html('');
            
            // 清空全局数据
            window.productRankingFullData = [];
        }
        
        // 加载统计数据
        function loadDashboardStats() {
            if (window.dashboardIsLoading) {
                return; // 如果正在加载，则跳过
            }
            
            window.dashboardIsLoading = true;
            
            // 刷新前先清空所有图表
            clearAllCharts();
            
            const date = (window.dashboardCurrentStartDate === window.dashboardCurrentEndDate) ? window.dashboardCurrentStartDate : null;
            const startDate = window.dashboardCurrentStartDate;
            const endDate = window.dashboardCurrentEndDate;
            
            const requestData = {};
            if (date) {
                requestData.date = date;
            } else {
                requestData.startDate = startDate;
                requestData.endDate = endDate;
            }
            
            // 加载网站统计
            $.ajax({
                url: '/manage/shop/shopTrackStats/siteStatsJson.html',
                type: 'GET',
                data: requestData,
                success: function(result) {
                    if (result.success && result.data && result.data.stats) {
                        updateDashboardSiteStats(result.data.stats);
                    } else {
                        // 即使没有数据也要清空停留时间分析图表
                        updateDurationChart(null);
                    }
                    window.dashboardIsLoading = false;
                },
                error: function() {
                    console.error('加载网站统计失败');
                    updateDurationChart(null);
                    window.dashboardIsLoading = false;
                }
            });
            
            // 加载页面统计
            $.ajax({
                url: '/manage/shop/shopTrackStats/pageStatsJson.html',
                type: 'GET',
                data: requestData,
                success: function(result) {
                    // 无论是否有数据都调用更新函数，确保清空旧数据
                    const pageData = (result.success && result.rows) ? result.rows : [];
                    updateDashboardPageStats(pageData);
                },
                error: function() {
                    console.error('加载页面统计失败');
                    updateDashboardPageStats([]);
                }
            });
            
            // 加载IP统计
            $.ajax({
                url: '/manage/shop/shopTrackStats/ipStatsJson.html',
                type: 'GET',
                data: Object.assign({limit: 10}, requestData),
                success: function(result) {
                    if (result.success && result.rows) {
                        updateDashboardIpStats(result.rows);
                    } else {
                        updateDashboardIpStats([]);
                    }
                },
                error: function() {
                    console.error('加载IP统计失败');
                    updateDashboardIpStats([]);
                }
            });
            
            // 加载商品排行（小屏显示10条，保存完整数据用于放大）
            $.ajax({
                url: '/manage/shop/shopTrackStats/productRankingJson.html',
                type: 'GET',
                data: Object.assign({limit: 100, orderBy: 'pv'}, requestData), // 获取更多数据，用于放大显示
                success: function(result) {
                    console.debug('商品排行查询结果:', result);
                    // 检查多种可能的数据结构
                    let rows = null;
                    if (result.rows) {
                        rows = result.rows;
                    } else if (result.data && result.data.rows) {
                        rows = result.data.rows;
                    } else if (Array.isArray(result.data)) {
                        rows = result.data;
                    } else if (Array.isArray(result)) {
                        rows = result;
                    }
                    
                    // 无论是否有数据都调用更新函数，确保清空旧数据
                    if (result.success !== false && rows && rows.length > 0) {
                        // 保存完整数据到全局变量，用于放大显示
                        window.productRankingFullData = rows;
                        // 小屏只显示前10条
                        const displayData = rows.slice(0, 10);
                        updateDashboardProductRanking(displayData, rows);
                    } else {
                        window.productRankingFullData = [];
                        updateDashboardProductRanking([], []);
                    }
                },
                error: function(xhr, status, error) {
                    window.productRankingFullData = [];
                    console.error('加载商品排行失败:', error, xhr.responseText);
                    updateDashboardProductRanking([], []);
                }
            });
        }
        
        // 显示图表弹窗
        function showChartModal(chartId, title) {
            const chartDom = document.getElementById(chartId);
            if (!chartDom) return;
            
            const chart = echarts.getInstanceByDom(chartDom);
            if (!chart) return;
            
            // 获取当前图表配置
            let option = chart.getOption();
            
            // 如果是商品排行，使用完整数据
            if (chartId === 'productRankChart' && window.productRankingFullData && window.productRankingFullData.length > 0) {
                const fullData = window.productRankingFullData;
                const productNames = fullData.map(item => {
                    const name = item.productName || ('商品ID: ' + (item.productId || '未知'));
                    return name; // 放大时显示完整名称
                });
                const pvData = fullData.map(item => item.pv || 0);
                
                option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: { type: 'shadow' },
                        formatter: function(params) {
                            const item = fullData[params[0].dataIndex];
                            const fullName = item.productName || ('商品ID: ' + (item.productId || '未知'));
                            return '<div style="text-align:left;">' +
                                   '<div style="font-weight:600;margin-bottom:8px;color:#303133;font-size:14px;">' + fullName + '</div>' +
                                   '<div style="color:#606266;font-size:13px;line-height:1.8;">' +
                                   '访问量: <span style="color:#409eff;font-weight:600;">' + (item.pv || 0) + '</span><br/>' +
                                   '独立访客: <span style="color:#67c23a;font-weight:600;">' + (item.uv || 0) + '</span><br/>' +
                                   '平均停留: <span style="color:#e6a23c;font-weight:600;">' + formatDuration(item.avgDuration || 0) + '</span>' +
                                   '</div></div>';
                        }
                    },
                    grid: {
                        left: '8%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                        axisLabel: { color: '#606266' }
                    },
                    yAxis: {
                        type: 'category',
                        data: productNames,
                        triggerEvent: true, // 启用Y轴标签的点击事件
                        axisLabel: { 
                            color: '#606266',
                            fontSize: 12,
                            interval: 0, // 强制显示所有标签
                            formatter: function(value) {
                                // 放大时也优化显示，如果名称太长可以换行或截断
                                if (value.length > 40) {
                                    return value.substring(0, 40) + '...';
                                }
                                return value;
                            },
                            rich: {
                                clickable: {
                                    color: '#409eff',
                                    cursor: 'pointer',
                                    textDecoration: 'underline'
                                }
                            }
                        }
                    },
                    series: [{
                        name: '访问量',
                        type: 'bar',
                        data: pvData,
                        itemStyle: {
                            color: '#67c23a'
                        },
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(103, 194, 58, 0.5)'
                            }
                        }
                    }]
                };
            }
            
            // 创建弹窗
            const modalHtml = '<div id="chartModal" style="position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.7);z-index:10000;display:flex;align-items:center;justify-content:center;">' +
                '<div style="background:#fff;border-radius:8px;padding:30px;width:90%;max-width:1200px;max-height:90%;position:relative;box-shadow:0 4px 20px rgba(0,0,0,0.3);">' +
                '<div style="font-size:20px;font-weight:600;margin-bottom:20px;color:#303133;display:flex;justify-content:space-between;align-items:center;">' +
                '<span>' + title + (chartId === 'productRankChart' && window.productRankingFullData ? ' (共' + window.productRankingFullData.length + '条)' : '') + '</span>' +
                '<button onclick="closeChartModal()" style="background:#f56c6c;border:none;border-radius:4px;padding:6px 15px;color:#fff;cursor:pointer;font-size:14px;"><i class="fa fa-times"></i> 关闭</button>' +
                '</div>' +
                '<div id="modalChartContainer" style="width:100%;height:600px;"></div>' +
                '</div>' +
                '</div>';
            
            $('body').append(modalHtml);
            
            // 在弹窗中初始化图表
            const modalChart = echarts.init(document.getElementById('modalChartContainer'));
            modalChart.setOption(option);
            
            // 如果是商品排行，添加点击事件：点击Y轴标签（商品名称）时跳转
            if (chartId === 'productRankChart' && window.productRankingFullData && window.productRankingFullData.length > 0) {
                const fullData = window.productRankingFullData;
                const fullProductNames = fullData.map(item => {
                    return item.productName || ('商品ID: ' + (item.productId || '未知'));
                });
                
                modalChart.off('click');
                modalChart.on('click', function(params) {
                    // 判断是否点击的是Y轴标签
                    if (params.componentType === 'yAxis' && params.value !== undefined) {
                        // 通过商品名称找到对应的数据
                        const clickedName = params.value;
                        // 查找匹配的商品（考虑截断的情况）
                        let clickedIndex = -1;
                        for (let i = 0; i < fullProductNames.length; i++) {
                            const fullName = fullProductNames[i];
                            const displayName = fullName.length > 40 ? fullName.substring(0, 40) + '...' : fullName;
                            // 匹配显示名称或完整名称
                            if (displayName === clickedName || fullName === clickedName) {
                                clickedIndex = i;
                                break;
                            }
                        }
                        
                        if (clickedIndex !== -1 && fullData[clickedIndex]) {
                            const productId = fullData[clickedIndex].productId;
                            if (productId) {
                                // 使用框架方法打开商品详情页（模仿 shopProtal.ftl 的方式）
                                $.acooly.framework.show('/manage/shop/shopProducts/show.html?id=' + productId, 600, 600);
                            }
                        }
                    }
                });
            }
            
            // 窗口大小改变时调整图表
            const resizeHandler = function() {
                modalChart.resize();
            };
            window.addEventListener('resize', resizeHandler);
            
            // 保存resize handler，关闭时移除
            $('#chartModal').data('resizeHandler', resizeHandler);
        }
        
        // 显示列表弹窗
        function showListModal(listId, title) {
            const listHtml = $('#' + listId).html();
            
            const modalHtml = '<div id="listModal" style="position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.7);z-index:10000;display:flex;align-items:center;justify-content:center;">' +
                '<div style="background:#fff;border-radius:8px;padding:30px;width:90%;max-width:800px;max-height:90%;position:relative;box-shadow:0 4px 20px rgba(0,0,0,0.3);display:flex;flex-direction:column;">' +
                '<div style="font-size:20px;font-weight:600;margin-bottom:20px;color:#303133;display:flex;justify-content:space-between;align-items:center;">' +
                '<span>' + title + '</span>' +
                '<button onclick="closeListModal()" style="background:#f56c6c;border:none;border-radius:4px;padding:6px 15px;color:#fff;cursor:pointer;font-size:14px;"><i class="fa fa-times"></i> 关闭</button>' +
                '</div>' +
                '<div id="modalListContainer" style="flex:1;overflow-y:auto;padding:10px;">' + listHtml + '</div>' +
                '</div>' +
                '</div>';
            
            $('body').append(modalHtml);
        }
        
        // 关闭图表弹窗
        function closeChartModal() {
            const resizeHandler = $('#chartModal').data('resizeHandler');
            if (resizeHandler) {
                window.removeEventListener('resize', resizeHandler);
            }
            $('#chartModal').remove();
        }
        
        // 关闭列表弹窗
        function closeListModal() {
            $('#listModal').remove();
        }
        
        // 点击背景关闭弹窗
        $(document).on('click', '#chartModal, #listModal', function(e) {
            if (e.target === this) {
                $(this).remove();
            }
        });
        
        // 更新网站统计
        function updateDashboardSiteStats(data) {
            $('#dashboardTotalPv').text(formatNumber(data.totalPv || 0));
            $('#dashboardTotalUv').text(formatNumber(data.totalUv || 0));
            $('#dashboardAvgDuration').text(formatDuration(data.avgDuration || 0));
            $('#dashboardProductVisitRate').text((data.productVisitRate || 0).toFixed(1) + '%');
            $('#dashboardTotalIp').text(formatNumber(data.totalIp || 0));
            $('#dashboardBounceRate').text((data.bounceRate || 0).toFixed(1) + '%');
            
            // 更新页面类型分布图表
            if (data.pageTypeStats && typeof echarts !== 'undefined') {
                updatePageTypeChart(data.pageTypeStats);
                updatePageTypeList(data.pageTypeStats);
                updateDurationChart(data.pageTypeStats);
            }
        }
        
        // 更新页面类型分布图表
        function updatePageTypeChart(pageTypeStats) {
            if (typeof echarts === 'undefined') return;
            const chartDom = document.getElementById('pageTypeChart');
            if (!chartDom) return;
            let chart = echarts.getInstanceByDom(chartDom);
            if (!chart) chart = echarts.init(chartDom);
            
            // 先清空图表
            chart.clear();
            
            if (!pageTypeStats || Object.keys(pageTypeStats).length === 0) {
                chart.setOption({
                    title: {
                        text: ' ',
                        left: 'center',
                        top: 'middle',
                        textStyle: { color: '#909399', fontSize: 14 }
                    }
                });
                return;
            }
            
            const data = [];
            
            for (const [key, value] of Object.entries(pageTypeStats)) {
                data.push({
                    value: value.pv || 0,
                    name: key
                });
            }
            
            const option = {
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b}: {c} ({d}%)'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    textStyle: { color: '#606266' }
                },
                series: [{
                    name: '页面类型',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 4,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: true,
                        color: '#606266'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: 16,
                            fontWeight: 'bold'
                        }
                    },
                    data: data
                }]
            };
            
            chart.setOption(option);
        }
        
        // 更新页面类型列表
        function updatePageTypeList(pageTypeStats) {
            let html = '';
            for (const [key, value] of Object.entries(pageTypeStats)) {
                html += '<div style="display:flex;justify-content:space-between;align-items:center;padding:12px;margin-bottom:8px;background:#f5f7fa;border-radius:4px;border-left:3px solid #409eff;">';
                html += '<div style="font-size:14px;font-weight:500;color:#303133;">' + key + '</div>';
                html += '<div style="display:flex;gap:15px;font-size:13px;color:#606266;">';
                html += '<span>PV: ' + formatNumber(value.pv || 0) + '</span>';
                html += '<span>UV: ' + formatNumber(value.uv || 0) + '</span>';
                html += '<span>平均: ' + formatDuration(value.avgDuration || 0) + '</span>';
                html += '</div></div>';
            }
            $('#pageTypeList').html(html || '<div style="text-align:center;color:#909399;padding:20px;">暂无数据</div>');
        }
        
        // 更新页面统计
        function updateDashboardPageStats(data) {
            if (typeof echarts === 'undefined') return;
            const chartDom = document.getElementById('pageRankChart');
            if (!chartDom) return;
            
            let chart = echarts.getInstanceByDom(chartDom);
            if (!chart) chart = echarts.init(chartDom);
            
            // 先清空图表
            chart.clear();
            
            if (!data || data.length === 0) {
                chart.setOption({
                    title: {
                        text: ' ',
                        left: 'center',
                        top: 'middle',
                        textStyle: { color: '#909399', fontSize: 14 }
                    }
                });
                return;
            }
            
            const topPages = data.slice(0, 10);
            
            const pageKeys = topPages.map(item => {
                const key = item.pageKey || '';
                return key.length > 20 ? key.substring(0, 20) + '...' : key;
            });
            const pvData = topPages.map(item => item.pv || 0);
            
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { type: 'shadow' }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    axisLabel: { color: '#606266' }
                },
                yAxis: {
                    type: 'category',
                    data: pageKeys,
                    axisLabel: { color: '#606266' }
                },
                series: [{
                    name: '访问量',
                    type: 'bar',
                    data: pvData,
                    itemStyle: {
                        color: '#409eff'
                    }
                }]
            };
            
            chart.setOption(option);
        }
        
        // 更新IP统计
        function updateDashboardIpStats(data) {
            let html = '';
            data.forEach(function(item) {
                html += '<div style="display:flex;justify-content:space-between;align-items:center;padding:12px;margin-bottom:8px;background:#f5f7fa;border-radius:4px;border-left:3px solid #67c23a;">';
                html += '<div style="font-family:monospace;font-size:14px;color:#303133;font-weight:500;">' + (item.ipAddress || '-') + '</div>';
                html += '<div style="display:flex;gap:15px;font-size:13px;color:#606266;">';
                html += '<span>访问: ' + formatNumber(item.visitCount || 0) + '</span>';
                html += '<span>访客: ' + formatNumber(item.uniqueVisitorCount || 0) + '</span>';
                html += '</div></div>';
            });
            $('#ipList').html(html || '<div style="text-align:center;color:#909399;padding:20px;">暂无数据</div>');
        }
        
        // 更新商品排行
        function updateDashboardProductRanking(data, fullData) {
            if (typeof echarts === 'undefined') return;
            const chartDom = document.getElementById('productRankChart');
            if (!chartDom) return;
            
            // 获取或初始化图表实例
            let chart = echarts.getInstanceByDom(chartDom);
            if (!chart) {
                chart = echarts.init(chartDom);
            }
            
            // 先清空图表
            chart.clear();
            
            console.debug('更新商品排行数据:', data, '数据长度:', data ? data.length : 0);
            
            if (!data || data.length === 0) {
                chart.setOption({
                    title: {
                        text: ' ',
                        left: 'center',
                        top: 'middle',
                        textStyle: {
                            color: '#909399',
                            fontSize: 14
                        }
                    }
                });
                // 清空点击事件
                chart.off('click');
                return;
            }
            
            // 保存完整数据到全局变量
            if (fullData) {
                window.productRankingFullData = fullData;
            }
            
            const productNames = data.map(item => {
                const name = item.productName || ('商品ID: ' + (item.productId || '未知'));
                // 优化：小屏显示时截断，但tooltip显示完整名称
                return name.length > 20 ? name.substring(0, 20) + '...' : name;
            });
            const pvData = data.map(item => item.pv || 0);
            
            console.debug('商品排行图表数据:', {productNames, pvData});
            
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { type: 'shadow' },
                    formatter: function(params) {
                        const item = data[params[0].dataIndex];
                        const fullName = item.productName || ('商品ID: ' + (item.productId || '未知'));
                        return '<div style="text-align:left;">' +
                               '<div style="font-weight:600;margin-bottom:8px;color:#303133;font-size:14px;">' + fullName + '</div>' +
                               '<div style="color:#606266;font-size:13px;line-height:1.8;">' +
                               '访问量: <span style="color:#409eff;font-weight:600;">' + (item.pv || 0) + '</span><br/>' +
                               '独立访客: <span style="color:#67c23a;font-weight:600;">' + (item.uv || 0) + '</span><br/>' +
                               '平均停留: <span style="color:#e6a23c;font-weight:600;">' + formatDuration(item.avgDuration || 0) + '</span>' +
                               '</div></div>';
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    axisLabel: { color: '#606266' }
                },
                yAxis: {
                    type: 'category',
                    data: productNames,
                    triggerEvent: true, // 启用Y轴标签的点击事件
                    axisLabel: { 
                        color: '#606266',
                        fontSize: 11,
                        interval: 0, // 强制显示所有标签
                        formatter: function(value) {
                            // 优化显示：如果名称太长，显示省略号，但tooltip会显示完整名称
                            if (value.length > 25) {
                                return value.substring(0, 25) + '...';
                            }
                            return value;
                        },
                        rich: {
                            clickable: {
                                color: '#409eff',
                                cursor: 'pointer',
                                textDecoration: 'underline'
                            },
                            truncate: {
                                color: '#606266',
                                fontSize: 11
                            }
                        }
                    }
                },
                series: [{
                    name: '访问量',
                    type: 'bar',
                    data: pvData,
                    itemStyle: {
                        color: '#67c23a'
                    },
                    // 添加点击事件，跳转到商品详情页
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(103, 194, 58, 0.5)'
                        }
                    }
                }]
            };
            
            chart.setOption(option);
            
            // 添加点击事件：点击Y轴标签（商品名称）时跳转到商品详情页
            chart.off('click');
            chart.on('click', function(params) {
                // 判断是否点击的是Y轴标签
                if (params.componentType === 'yAxis' && params.value !== undefined) {
                    // 通过商品名称找到对应的数据
                    const clickedName = params.value;
                    // 查找匹配的商品（考虑截断的情况）
                    let clickedIndex = -1;
                    for (let i = 0; i < productNames.length; i++) {
                        const displayName = productNames[i];
                        const fullName = data[i] ? (data[i].productName || ('商品ID: ' + (data[i].productId || '未知'))) : '';
                        // 匹配显示名称或完整名称
                        if (displayName === clickedName || 
                            (displayName.length > 25 && clickedName === displayName.substring(0, 25) + '...') ||
                            fullName === clickedName) {
                            clickedIndex = i;
                            break;
                        }
                    }
                    
                    if (clickedIndex !== -1 && data[clickedIndex]) {
                        const productId = data[clickedIndex].productId;
                        if (productId) {
                            // 使用框架方法打开商品详情页（模仿 shopProtal.ftl 的方式）
                            $.acooly.framework.show('/manage/shop/shopProducts/show.html?id=' + productId, 600, 600);
                        }
                    }
                }
            });
        }
        
        // 更新停留时间分析
        function updateDurationChart(pageTypeStats) {
            if (typeof echarts === 'undefined') return;
            const chartDom = document.getElementById('durationChart');
            if (!chartDom) return;
            
            let chart = echarts.getInstanceByDom(chartDom);
            if (!chart) chart = echarts.init(chartDom);
            
            // 先清空图表
            chart.clear();
            
            if (!pageTypeStats || Object.keys(pageTypeStats).length === 0) {
                chart.setOption({
                    title: {
                        text: ' ',
                        left: 'center',
                        top: 'middle',
                        textStyle: { color: '#909399', fontSize: 14 }
                    }
                });
                return;
            }
            
            const categories = [];
            const avgDurationData = [];
            const pvData = [];
            
            // 按平均停留时间排序
            const sortedStats = Object.entries(pageTypeStats).sort((a, b) => {
                return (b[1].avgDuration || 0) - (a[1].avgDuration || 0);
            });
            
            sortedStats.forEach(([key, value]) => {
                categories.push(key);
                avgDurationData.push(value.avgDuration || 0);
                pvData.push(value.pv || 0);
            });
            
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { type: 'cross' },
                    formatter: function(params) {
                        let result = params[0].name + '<br/>';
                        params.forEach(function(item) {
                            if (item.seriesName === '平均停留时间') {
                                result += item.marker + item.seriesName + ': ' + formatDuration(item.value) + '<br/>';
                            } else {
                                result += item.marker + item.seriesName + ': ' + formatNumber(item.value) + '<br/>';
                            }
                        });
                        return result;
                    }
                },
                legend: {
                    data: ['平均停留时间', '访问量'],
                    textStyle: { color: '#606266' }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    data: categories,
                    axisLabel: { color: '#606266' }
                },
                yAxis: [
                    {
                        type: 'value',
                        name: '停留时间',
                        position: 'left',
                        axisLabel: { 
                            color: '#606266',
                            formatter: function(value) {
                                return formatDuration(value);
                            }
                        }
                    },
                    {
                        type: 'value',
                        name: '访问量',
                        position: 'right',
                        axisLabel: { 
                            color: '#606266',
                            formatter: function(value) {
                                return formatNumber(value);
                            }
                        }
                    }
                ],
                series: [
                    {
                        name: '平均停留时间',
                        type: 'bar',
                        yAxisIndex: 0,
                        data: avgDurationData,
                        itemStyle: { color: '#409eff' }
                    },
                    {
                        name: '访问量',
                        type: 'line',
                        yAxisIndex: 1,
                        data: pvData,
                        itemStyle: { color: '#67c23a' },
                        lineStyle: { width: 2 }
                    }
                ]
            };
            
            chart.setOption(option);
        }
        
        // 格式化数字
        function formatNumber(num) {
            if (num >= 10000) {
                return (num / 10000).toFixed(1) + '万';
            }
            return num.toString();
        }
        
        // 格式化时长
        function formatDuration(ms) {
            if (ms < 1000) {
                return ms + 'ms';
            } else if (ms < 60000) {
                return (ms / 1000).toFixed(1) + 's';
            } else {
                return (ms / 60000).toFixed(1) + 'min';
            }
        }
    </script>
</div>
