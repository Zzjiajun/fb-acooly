<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>访问统计大屏</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        
        body { 
            background: linear-gradient(135deg, #0a0e27 0%, #1a1f3a 25%, #2d1b69 50%, #1a1f3a 75%, #0a0e27 100%);
            color: #fff; 
            font-family: 'Microsoft YaHei', Arial, sans-serif; 
            overflow-x: hidden;
            position: relative;
        }
        
        /* 动态背景 */
        .bg-animation {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: -1;
            background: 
                radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
                radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.3) 0%, transparent 50%),
                radial-gradient(circle at 40% 40%, rgba(120, 219, 255, 0.2) 0%, transparent 50%);
            animation: bgMove 20s ease-in-out infinite;
        }
        
        @keyframes bgMove {
            0%, 100% { transform: translate(0, 0) scale(1); }
            25% { transform: translate(-10px, -10px) scale(1.1); }
            50% { transform: translate(10px, -5px) scale(0.9); }
            75% { transform: translate(-5px, 10px) scale(1.05); }
        }
        
        .dashboard { 
            min-height: 100vh; 
            padding: 20px; 
            position: relative; 
            z-index: 1;
        }
        
        /* 头部 */
        .header { 
            height: 80px; 
            display: flex; 
            align-items: center; 
            justify-content: space-between; 
            margin-bottom: 30px;
            background: linear-gradient(90deg, rgba(58, 142, 230, 0.1), rgba(246, 193, 119, 0.1), rgba(103, 194, 58, 0.1));
            border-radius: 20px;
            padding: 0 30px;
            border: 1px solid rgba(255, 255, 255, 0.1);
            position: relative;
            overflow: hidden;
        }
        
        .header::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
            animation: shine 3s infinite;
        }
        
        @keyframes shine {
            0% { left: -100%; }
            100% { left: 100%; }
        }
        
        .header .title { 
            font-size: 2.5rem; 
            font-weight: bold;
            background: linear-gradient(45deg, #3a8ee6, #f6c177, #67c23a, #f56c6c);
            background-size: 400% 400%;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: gradientShift 3s ease-in-out infinite;
        }
        
        @keyframes gradientShift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }
        
        .header .time { 
            font-size: 1.3rem; 
            color: #f6c177; 
            text-shadow: 0 0 10px #f6c177;
        }
        
        .header .date-selector {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .header .date-selector input {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 8px;
            padding: 8px 15px;
            color: #fff;
            font-size: 14px;
        }
        
        .header .date-selector button {
            background: linear-gradient(135deg, #3a8ee6, #67c23a);
            border: none;
            border-radius: 8px;
            padding: 8px 20px;
            color: #fff;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.3s;
        }
        
        .header .date-selector button:hover {
            transform: scale(1.05);
            box-shadow: 0 0 20px rgba(58, 142, 230, 0.5);
        }
        
        /* 统计卡片区 */
        .stats-row { 
            display: flex; 
            gap: 24px; 
            margin-bottom: 30px; 
        }
        
        .stat-card {
            flex: 1;
            border-radius: 20px;
            padding: 30px 25px;
            display: flex; 
            flex-direction: column; 
            align-items: center;
            min-width: 200px;
            position: relative;
            overflow: hidden;
            transition: all 0.4s;
            border: 1px solid rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(15px);
        }
        
        .stat-card:hover {
            transform: translateY(-10px) scale(1.05);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
        }
        
        .gradient-blue { 
            background: linear-gradient(135deg, rgba(58, 142, 230, 0.2), rgba(34, 41, 70, 0.8));
            box-shadow: 0 0 30px rgba(58, 142, 230, 0.3);
        }
        
        .gradient-green { 
            background: linear-gradient(135deg, rgba(103, 194, 58, 0.2), rgba(34, 41, 70, 0.8));
            box-shadow: 0 0 30px rgba(103, 194, 58, 0.3);
        }
        
        .gradient-orange { 
            background: linear-gradient(135deg, rgba(246, 193, 119, 0.2), rgba(34, 41, 70, 0.8));
            box-shadow: 0 0 30px rgba(246, 193, 119, 0.3);
        }
        
        .gradient-purple { 
            background: linear-gradient(135deg, rgba(156, 39, 176, 0.2), rgba(34, 41, 70, 0.8));
            box-shadow: 0 0 30px rgba(156, 39, 176, 0.3);
        }
        
        .stat-card .icon {
            font-size: 3rem;
            margin-bottom: 15px;
            opacity: 0.9;
        }
        
        .stat-card .value {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 10px;
            background: linear-gradient(45deg, #fff, #f6c177);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        
        .stat-card .label {
            font-size: 1rem;
            color: rgba(255, 255, 255, 0.7);
        }
        
        /* 主体区 */
        .main-row {
            display: flex;
            gap: 24px;
            margin-bottom: 30px;
        }
        
        .main-left {
            flex: 2;
        }
        
        .main-right {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 24px;
        }
        
        .panel {
            background: rgba(34, 41, 70, 0.6);
            border-radius: 20px;
            padding: 25px;
            border: 1px solid rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(15px);
            position: relative;
            overflow: hidden;
        }
        
        .panel::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, #3a8ee6, #67c23a, #f6c177);
            animation: borderFlow 3s linear infinite;
        }
        
        @keyframes borderFlow {
            0% { transform: translateX(-100%); }
            100% { transform: translateX(100%); }
        }
        
        .panel-title {
            font-size: 1.3rem;
            font-weight: bold;
            margin-bottom: 20px;
            color: #f6c177;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .panel-title i {
            font-size: 1.5rem;
        }
        
        .chart-container {
            width: 100%;
            height: 100%;
            min-height: 300px;
        }
        
        /* 底部区 */
        .footer-row {
            display: flex;
            gap: 24px;
        }
        
        .footer-panel {
            flex: 1;
        }
        
        /* IP列表 */
        .ip-list {
            max-height: 400px;
            overflow-y: auto;
        }
        
        .ip-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            margin-bottom: 10px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 10px;
            border-left: 3px solid #3a8ee6;
            transition: all 0.3s;
        }
        
        .ip-item:hover {
            background: rgba(255, 255, 255, 0.1);
            transform: translateX(5px);
        }
        
        .ip-item .ip-address {
            font-family: 'Courier New', monospace;
            font-size: 1.1rem;
            color: #67c23a;
        }
        
        .ip-item .ip-stats {
            display: flex;
            gap: 20px;
            font-size: 0.9rem;
        }
        
        .ip-item .ip-stats span {
            color: rgba(255, 255, 255, 0.7);
        }
        
        /* 页面类型统计 */
        .page-type-list {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        
        .page-type-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 10px;
            border-left: 3px solid #f6c177;
        }
        
        .page-type-item .page-type-name {
            font-size: 1rem;
            font-weight: bold;
        }
        
        .page-type-item .page-type-stats {
            display: flex;
            gap: 20px;
            font-size: 0.9rem;
        }
        
        /* 滚动条样式 */
        ::-webkit-scrollbar {
            width: 8px;
        }
        
        ::-webkit-scrollbar-track {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
        }
        
        ::-webkit-scrollbar-thumb {
            background: rgba(58, 142, 230, 0.5);
            border-radius: 10px;
        }
        
        ::-webkit-scrollbar-thumb:hover {
            background: rgba(58, 142, 230, 0.8);
        }
    </style>
</head>
<body>
    <div class="bg-animation"></div>
    <div class="dashboard">
        <div class="header">
            <div class="title">
                <i class="fas fa-chart-line"></i> 访问统计大屏
            </div>
            <div class="date-selector">
                <input type="date" id="statDate" value="" />
                <button onclick="loadStats()"><i class="fas fa-sync-alt"></i> 刷新</button>
            </div>
            <div class="time" id="currentTime"></div>
        </div>
        
        <!-- 统计卡片区 -->
        <div class="stats-row">
            <div class="stat-card gradient-blue">
                <div class="icon"><i class="fas fa-eye"></i></div>
                <div class="value" id="totalPv">-</div>
                <div class="label">总访问量 (PV)</div>
            </div>
            <div class="stat-card gradient-green">
                <div class="icon"><i class="fas fa-users"></i></div>
                <div class="value" id="totalUv">-</div>
                <div class="label">独立访客 (UV)</div>
            </div>
            <div class="stat-card gradient-orange">
                <div class="icon"><i class="fas fa-clock"></i></div>
                <div class="value" id="avgDuration">-</div>
                <div class="label">平均停留时间</div>
            </div>
            <div class="stat-card gradient-purple">
                <div class="icon"><i class="fas fa-shopping-cart"></i></div>
                <div class="value" id="productVisitRate">-</div>
                <div class="label">商品访问率</div>
            </div>
        </div>
        
        <!-- 主体区 -->
        <div class="main-row">
            <div class="main-left">
                <div class="panel" style="height: 500px;">
                    <div class="panel-title">
                        <i class="fas fa-chart-pie"></i> 页面类型分布
                    </div>
                    <div class="chart-container" id="pageTypeChart"></div>
                </div>
            </div>
            <div class="main-right">
                <div class="panel" style="height: 240px;">
                    <div class="panel-title">
                        <i class="fas fa-list"></i> 页面类型统计
                    </div>
                    <div class="page-type-list" id="pageTypeList"></div>
                </div>
                <div class="panel" style="height: 240px;">
                    <div class="panel-title">
                        <i class="fas fa-globe"></i> IP分布 TOP10
                    </div>
                    <div class="ip-list" id="ipList"></div>
                </div>
            </div>
        </div>
        
        <!-- 底部区 -->
        <div class="footer-row">
            <div class="panel footer-panel" style="height: 300px;">
                <div class="panel-title">
                    <i class="fas fa-chart-bar"></i> 页面访问排行
                </div>
                <div class="chart-container" id="pageRankChart"></div>
            </div>
            <div class="panel footer-panel" style="height: 300px;">
                <div class="panel-title">
                    <i class="fas fa-network-wired"></i> IP访问趋势
                </div>
                <div class="chart-container" id="ipTrendChart"></div>
            </div>
        </div>
    </div>

    <script>
        // 初始化
        $(function() {
            updateTime();
            setInterval(updateTime, 1000);
            loadStats();
            // 每30秒自动刷新
            setInterval(loadStats, 30000);
        });
        
        // 更新时间
        function updateTime() {
            const now = new Date();
            const timeStr = now.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
            $('#currentTime').text(timeStr);
        }
        
        // 加载统计数据
        function loadStats() {
            const date = $('#statDate').val() || '';
            
            // 加载网站统计
            $.ajax({
                url: '/manage/shop/shopTrackStats/siteStatsJson.html',
                type: 'GET',
                data: { date: date },
                success: function(result) {
                    if (result.success && result.data) {
                        updateSiteStats(result.data);
                    }
                },
                error: function() {
                    console.error('加载网站统计失败');
                }
            });
            
            // 加载页面统计
            $.ajax({
                url: '/manage/shop/shopTrackStats/pageStatsJson.html',
                type: 'GET',
                data: { date: date },
                success: function(result) {
                    if (result.success && result.rows) {
                        updatePageStats(result.rows);
                    }
                },
                error: function() {
                    console.error('加载页面统计失败');
                }
            });
            
            // 加载IP统计
            $.ajax({
                url: '/manage/shop/shopTrackStats/ipStatsJson.html',
                type: 'GET',
                data: { date: date, limit: 10 },
                success: function(result) {
                    if (result.success && result.rows) {
                        updateIpStats(result.rows);
                    }
                },
                error: function() {
                    console.error('加载IP统计失败');
                }
            });
        }
        
        // 更新网站统计
        function updateSiteStats(data) {
            $('#totalPv').text(formatNumber(data.totalPv || 0));
            $('#totalUv').text(formatNumber(data.totalUv || 0));
            $('#avgDuration').text(formatDuration(data.avgDuration || 0));
            $('#productVisitRate').text((data.productVisitRate || 0).toFixed(1) + '%');
            
            // 更新页面类型分布图表
            if (data.pageTypeStats) {
                updatePageTypeChart(data.pageTypeStats);
                updatePageTypeList(data.pageTypeStats);
            }
        }
        
        // 更新页面类型分布图表
        function updatePageTypeChart(pageTypeStats) {
            const chart = echarts.init(document.getElementById('pageTypeChart'));
            const data = [];
            const names = [];
            
            for (const [key, value] of Object.entries(pageTypeStats)) {
                names.push(key);
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
                    textStyle: {
                        color: '#fff'
                    }
                },
                series: [{
                    name: '页面类型',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#0a0e27',
                        borderWidth: 2
                    },
                    label: {
                        show: true,
                        color: '#fff'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: 20,
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
                html += `
                    <div class="page-type-item">
                        <div class="page-type-name">${key}</div>
                        <div class="page-type-stats">
                            <span>PV: ${formatNumber(value.pv || 0)}</span>
                            <span>UV: ${formatNumber(value.uv || 0)}</span>
                            <span>平均: ${formatDuration(value.avgDuration || 0)}</span>
                        </div>
                    </div>
                `;
            }
            $('#pageTypeList').html(html || '<div style="text-align:center;color:rgba(255,255,255,0.5);">暂无数据</div>');
        }
        
        // 更新页面统计
        function updatePageStats(data) {
            // 更新页面访问排行图表
            const topPages = data.slice(0, 10);
            const chart = echarts.init(document.getElementById('pageRankChart'));
            
            const pageKeys = topPages.map(item => {
                const key = item.pageKey || '';
                return key.length > 20 ? key.substring(0, 20) + '...' : key;
            });
            const pvData = topPages.map(item => item.pv || 0);
            
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
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
                    axisLabel: {
                        color: '#fff'
                    }
                },
                yAxis: {
                    type: 'category',
                    data: pageKeys,
                    axisLabel: {
                        color: '#fff'
                    }
                },
                series: [{
                    name: '访问量',
                    type: 'bar',
                    data: pvData,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                            offset: 0,
                            color: '#3a8ee6'
                        }, {
                            offset: 1,
                            color: '#67c23a'
                        }])
                    }
                }]
            };
            
            chart.setOption(option);
        }
        
        // 更新IP统计
        function updateIpStats(data) {
            let html = '';
            data.forEach(function(item) {
                html += `
                    <div class="ip-item">
                        <div class="ip-address">${item.ipAddress || '-'}</div>
                        <div class="ip-stats">
                            <span>访问: ${formatNumber(item.visitCount || 0)}</span>
                            <span>访客: ${formatNumber(item.uniqueVisitorCount || 0)}</span>
                        </div>
                    </div>
                `;
            });
            $('#ipList').html(html || '<div style="text-align:center;color:rgba(255,255,255,0.5);">暂无数据</div>');
            
            // 更新IP访问趋势图表
            const chart = echarts.init(document.getElementById('ipTrendChart'));
            const ipAddresses = data.map(item => item.ipAddress || '');
            const visitCounts = data.map(item => item.visitCount || 0);
            
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'line'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    data: ipAddresses,
                    axisLabel: {
                        color: '#fff',
                        rotate: 45
                    }
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        color: '#fff'
                    }
                },
                series: [{
                    name: '访问次数',
                    type: 'line',
                    data: visitCounts,
                    smooth: true,
                    lineStyle: {
                        color: '#3a8ee6',
                        width: 3
                    },
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(58, 142, 230, 0.3)'
                        }, {
                            offset: 1,
                            color: 'rgba(58, 142, 230, 0.1)'
                        }])
                    }
                }]
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
</body>
</html>

