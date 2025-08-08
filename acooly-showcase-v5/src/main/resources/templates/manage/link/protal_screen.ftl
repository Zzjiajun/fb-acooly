<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>IP分布大屏</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
        
        /* 粒子效果 */
        #particles-js { 
            position: fixed; 
            width: 100vw; 
            height: 100vh; 
            z-index: 0; 
            top: 0; 
            left: 0; 
        }
        
        .dashboard { 
            min-height: 100vh; 
            padding: 20px; 
            position: relative; 
            z-index: 1;
            backdrop-filter: blur(10px);
        }
        
        /* 炫酷头部 */
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
            text-shadow: 0 0 20px rgba(58, 142, 230, 0.5);
        }
        
        @keyframes gradientShift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }
        
        .header .time { 
            font-size: 1.3rem; 
            color: #f6c177; 
            text-shadow: 0 0 10px #f6c177;
            animation: pulse 2s ease-in-out infinite;
        }
        
        @keyframes pulse {
            0%, 100% { opacity: 1; transform: scale(1); }
            50% { opacity: 0.8; transform: scale(1.05); }
        }
        
        /* 统计卡片区 */
        .stats-row { 
            display: flex; 
            gap: 24px; 
            margin: 24px 0 30px 0; 
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
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            border: 1px solid rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(15px);
        }
        
        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.05));
            opacity: 0;
            transition: opacity 0.3s;
        }
        
        .stat-card:hover {
            transform: translateY(-10px) scale(1.05);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
        }
        
        .stat-card:hover::before {
            opacity: 1;
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
            background: linear-gradient(135deg, rgba(230, 162, 60, 0.2), rgba(34, 41, 70, 0.8));
            box-shadow: 0 0 30px rgba(230, 162, 60, 0.3);
        }
        
        .gradient-red { 
            background: linear-gradient(135deg, rgba(245, 108, 108, 0.2), rgba(34, 41, 70, 0.8));
            box-shadow: 0 0 30px rgba(245, 108, 108, 0.3);
        }
        
        .stat-card .icon { 
            font-size: 3rem; 
            margin-bottom: 15px; 
            filter: drop-shadow(0 0 15px rgba(255, 255, 255, 0.5));
            animation: iconFloat 3s ease-in-out infinite;
        }
        
        @keyframes iconFloat {
            0%, 100% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
        }
        
        .stat-card .value { 
            font-size: 2.5rem; 
            font-weight: bold; 
            margin-bottom: 8px;
            background: linear-gradient(45deg, #fff, #f6c177);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: valueCount 2s ease-out;
        }
        
        @keyframes valueCount {
            0% { transform: scale(0.5); opacity: 0; }
            50% { transform: scale(1.2); }
            100% { transform: scale(1); opacity: 1; }
        }
        
        .stat-card .label { 
            font-size: 1.2rem; 
            color: rgba(255, 255, 255, 0.8);
            text-shadow: 0 0 8px rgba(255, 255, 255, 0.3);
        }
        
        /* 主体区 */
        .main-row { 
            display: flex; 
            gap: 30px; 
            margin-bottom: 30px;
        }
        
        .main-left { 
            flex: 2; 
        }
        
        .main-right { 
            flex: 1; 
            display: flex; 
            flex-direction: column; 
            gap: 30px; 
        }
        
        .panel { 
            background: rgba(35, 41, 70, 0.6);
            border-radius: 20px; 
            padding: 25px; 
            margin-bottom: 0; 
            position: relative; 
            overflow: hidden;
            border: 1px solid rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(15px);
            transition: all 0.3s ease;
        }
        
        .panel:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
        }
        
        .panel::before { 
            content: ""; 
            position: absolute; 
            top: 0; 
            left: 0; 
            width: 100%; 
            height: 4px; 
            background: linear-gradient(90deg, #3a8ee6, #f6c177, #67c23a, #f56c6c); 
            filter: blur(2px); 
            opacity: 0.8; 
        }
        
        .panel::after {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.05) 50%, transparent 70%);
            animation: panelShine 4s ease-in-out infinite;
        }
        
        @keyframes panelShine {
            0%, 100% { transform: translateX(-100%); }
            50% { transform: translateX(100%); }
        }
        
        .footer-row { 
            display: flex; 
            gap: 30px; 
        }
        
        .footer-panel { 
            flex: 1; 
        }
        
        /* 数据流动画 */
        .data-flow {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            pointer-events: none;
        }
        
        .data-particle {
            position: absolute;
            width: 2px;
            height: 2px;
            background: #3a8ee6;
            border-radius: 50%;
            animation: dataFlow 3s linear infinite;
        }
        
        @keyframes dataFlow {
            0% { transform: translateY(-100px); opacity: 0; }
            10% { opacity: 1; }
            90% { opacity: 1; }
            100% { transform: translateY(100px); opacity: 0; }
        }
        
        /* 响应式设计 */
        @media (max-width: 1200px) {
            .stats-row { flex-wrap: wrap; }
            .stat-card { min-width: calc(50% - 12px); }
            .main-row { flex-direction: column; }
        }
        
        @media (max-width: 768px) {
            .stat-card { min-width: 100%; }
            .header .title { font-size: 1.8rem; }
            .dashboard { padding: 10px; }
        }
    </style>
</head>
<body>
    <div class="bg-animation"></div>
    <div id="particles-js"></div>
    <div class="dashboard">
        <div class="header">
            <div class="title">
                <i class="fas fa-chart-line"></i> IP分布大屏监控
            </div>
            <div class="time" id="currentTime"></div>
        </div>
        
        <!-- 统计卡片区 -->
        <div class="stats-row">
            <div class="stat-card gradient-blue">
                <div class="icon"><i class="fas fa-globe"></i></div>
                <div class="value" id="totalVisits">-</div>
                <div class="label">总访问量</div>
            </div>
            <div class="stat-card gradient-green">
                <div class="icon"><i class="fas fa-users"></i></div>
                <div class="value" id="uniqueIp">-</div>
                <div class="label">独立IP数</div>
            </div>
            <div class="stat-card gradient-orange">
                <div class="icon"><i class="fas fa-building"></i></div>
                <div class="value" id="supplierCount">-</div>
                <div class="label">供应商数</div>
            </div>
            <div class="stat-card gradient-red">
                <div class="icon"><i class="fas fa-user-friends"></i></div>
                <div class="value" id="userCount">-</div>
                <div class="label">活跃用户</div>
            </div>
        </div>
        
        <!-- 主体区 -->
        <div class="main-row">
            <div class="main-left">
                <div class="panel" style="height: 500px;" id="ipMap">
                    <div class="data-flow" id="mapDataFlow"></div>
                </div>
            </div>
            <div class="main-right">
                <div class="panel" style="height: 240px;" id="supplierStats">
                    <div class="data-flow" id="supplierDataFlow"></div>
                </div>
                <div class="panel" style="height: 240px;" id="userStats">
                    <div class="data-flow" id="userDataFlow"></div>
                </div>
            </div>
        </div>
        
        <!-- 底部区 -->
        <div class="footer-row">
            <div class="panel footer-panel" style="height: 200px;" id="trendChart">
                <div class="data-flow" id="trendDataFlow"></div>
            </div>
            <div class="panel footer-panel" style="height: 200px;" id="regionStats">
                <div class="data-flow" id="regionDataFlow"></div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/particles.js/2.0.0/particles.min.js"></script>
    <script>
        $(function() {
            // 粒子背景初始化
            particlesJS('particles-js', {
                "particles": {
                    "number": {
                        "value": 80,
                        "density": {
                            "enable": true,
                            "value_area": 800
                        }
                    },
                    "color": {
                        "value": ["#3a8ee6", "#f6c177", "#67c23a", "#f56c6c"]
                    },
                    "shape": {
                        "type": "circle",
                        "stroke": {
                            "width": 0,
                            "color": "#000000"
                        }
                    },
                    "opacity": {
                        "value": 0.6,
                        "random": true,
                        "anim": {
                            "enable": true,
                            "speed": 1,
                            "opacity_min": 0.1,
                            "sync": false
                        }
                    },
                    "size": {
                        "value": 3,
                        "random": true,
                        "anim": {
                            "enable": true,
                            "speed": 2,
                            "size_min": 0.1,
                            "sync": false
                        }
                    },
                    "line_linked": {
                        "enable": true,
                        "distance": 150,
                        "color": "#3a8ee6",
                        "opacity": 0.4,
                        "width": 1
                    },
                    "move": {
                        "enable": true,
                        "speed": 2,
                        "direction": "none",
                        "random": true,
                        "straight": false,
                        "out_mode": "out",
                        "bounce": false,
                        "attract": {
                            "enable": true,
                            "rotateX": 600,
                            "rotateY": 1200
                        }
                    }
                },
                "interactivity": {
                    "detect_on": "canvas",
                    "events": {
                        "onhover": {
                            "enable": true,
                            "mode": "repulse"
                        },
                        "onclick": {
                            "enable": true,
                            "mode": "push"
                        },
                        "resize": true
                    },
                    "modes": {
                        "grab": {
                            "distance": 400,
                            "line_linked": {
                                "opacity": 1
                            }
                        },
                        "bubble": {
                            "distance": 400,
                            "size": 40,
                            "duration": 2,
                            "opacity": 8,
                            "speed": 3
                        },
                        "repulse": {
                            "distance": 200,
                            "duration": 0.4
                        },
                        "push": {
                            "particles_nb": 4
                        },
                        "remove": {
                            "particles_nb": 2
                        }
                    }
                },
                "retina_detect": true
            });

            // 动态显示时间
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
                document.getElementById('currentTime').innerHTML = `<i class="fas fa-clock"></i> ${timeStr}`;
            }
            updateTime();
            setInterval(updateTime, 1000);

            // 数据流动画
            function createDataFlow(containerId) {
                const container = document.getElementById(containerId);
                setInterval(() => {
                    const particle = document.createElement('div');
                    particle.className = 'data-particle';
                    particle.style.left = Math.random() * 100 + '%';
                    particle.style.animationDelay = Math.random() * 2 + 's';
                    container.appendChild(particle);
                    
                    setTimeout(() => {
                        if (particle.parentNode) {
                            particle.parentNode.removeChild(particle);
                        }
                    }, 3000);
                }, 200);
            }

            createDataFlow('mapDataFlow');
            createDataFlow('supplierDataFlow');
            createDataFlow('userDataFlow');
            createDataFlow('trendDataFlow');
            createDataFlow('regionDataFlow');

            // 统计卡片数据填充
            function fetchStatsSummary() {
                // 模拟数据
                const mockData = {
                    totalVisits: Math.floor(Math.random() * 10000) + 5000,
                    uniqueIp: Math.floor(Math.random() * 2000) + 1000,
                    supplierCount: Math.floor(Math.random() * 500) + 200,
                    userCount: Math.floor(Math.random() * 3000) + 1500
                };
                
                animateValue('totalVisits', mockData.totalVisits);
                animateValue('uniqueIp', mockData.uniqueIp);
                animateValue('supplierCount', mockData.supplierCount);
                animateValue('userCount', mockData.userCount);
            }

            function animateValue(elementId, finalValue) {
                const element = document.getElementById(elementId);
                const startValue = 0;
                const duration = 2000;
                const startTime = performance.now();
                
                function updateValue(currentTime) {
                    const elapsed = currentTime - startTime;
                    const progress = Math.min(elapsed / duration, 1);
                    
                    // 使用缓动函数
                    const easeOutQuart = 1 - Math.pow(1 - progress, 4);
                    const currentValue = Math.floor(startValue + (finalValue - startValue) * easeOutQuart);
                    
                    element.textContent = currentValue.toLocaleString();
                    
                    if (progress < 1) {
                        requestAnimationFrame(updateValue);
                    }
                }
                
                requestAnimationFrame(updateValue);
            }

            // 1. IP分布地图（世界）
            var ipMap = echarts.init(document.getElementById('ipMap'));
            var ipMapOption = {
                backgroundColor: 'transparent',
                title: { 
                    text: '全球IP分布热力图', 
                    left: 'center', 
                    top: 10,
                    textStyle: { 
                        color: '#fff', 
                        fontSize: 18,
                        fontWeight: 'bold',
                        textShadowColor: '#3a8ee6',
                        textShadowBlur: 10
                    } 
                },
                tooltip: { 
                    trigger: 'item',
                    backgroundColor: 'rgba(35, 41, 70, 0.9)',
                    borderColor: '#3a8ee6',
                    borderWidth: 1,
                    textStyle: { color: '#fff' }
                },
                visualMap: {
                    min: 0, 
                    max: 1000, 
                    left: 'left', 
                    top: 'bottom',
                    text: ['高','低'], 
                    inRange: { 
                        color: ['#e0ffff', '#3a8ee6', '#f6c177', '#f56c6c'] 
                    },
                    textStyle: { color: '#fff' }, 
                    calculable: true
                },
                series: [{
                    name: 'IP数',
                    type: 'map',
                    map: 'world',
                    roam: true,
                    label: { show: false },
                    data: [
                        {name: 'China', value: 856},
                        {name: 'United States', value: 723},
                        {name: 'Japan', value: 456},
                        {name: 'Germany', value: 389},
                        {name: 'United Kingdom', value: 234},
                        {name: 'France', value: 198},
                        {name: 'Canada', value: 167},
                        {name: 'Australia', value: 145},
                        {name: 'Brazil', value: 123},
                        {name: 'India', value: 234}
                    ],
                    itemStyle: {
                        areaColor: '#232946',
                        borderColor: '#3a8ee6',
                        borderWidth: 1,
                        shadowColor: '#3a8ee6',
                        shadowBlur: 20
                    },
                    emphasis: {
                        itemStyle: {
                            areaColor: '#f6c177',
                            shadowColor: '#f6c177',
                            shadowBlur: 30
                        }
                    },
                    animation: true,
                    animationDuration: 1500,
                    animationEasing: 'cubicOut'
                }]
            };
            ipMap.setOption(ipMapOption);

            // 2. 供应商统计
            var supplierStats = echarts.init(document.getElementById('supplierStats'));
            var supplierOption = {
                backgroundColor: 'transparent',
                title: { 
                    text: '供应商分布TOP10', 
                    left: 'center', 
                    top: 10,
                    textStyle: { 
                        color: '#fff', 
                        fontSize: 16, 
                        fontWeight: 'bold',
                        textShadowColor: '#67c23a', 
                        textShadowBlur: 8 
                    } 
                },
                tooltip: {
                    backgroundColor: 'rgba(35, 41, 70, 0.9)',
                    borderColor: '#67c23a',
                    borderWidth: 1,
                    textStyle: { color: '#fff' }
                },
                grid: {
                    left: '10%',
                    right: '10%',
                    top: '25%',
                    bottom: '15%'
                },
                xAxis: { 
                    type: 'category', 
                    data: ['阿里云', '腾讯云', '华为云', 'AWS', 'Azure', '百度云', '金山云', 'UCloud', '青云', '七牛云'], 
                    axisLabel: { 
                        color: '#fff',
                        fontSize: 10,
                        rotate: 45
                    }, 
                    axisLine: { 
                        lineStyle: { color: '#67c23a' } 
                    } 
                },
                yAxis: { 
                    axisLabel: { color: '#fff' }, 
                    axisLine: { 
                        lineStyle: { color: '#67c23a' } 
                    }, 
                    splitLine: { 
                        lineStyle: { color: 'rgba(103, 194, 58, 0.2)' } 
                    } 
                },
                series: [{ 
                    type: 'bar', 
                    data: [156, 134, 98, 87, 76, 65, 54, 43, 32, 21],
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            {offset: 0, color: '#67c23a'},
                            {offset: 1, color: 'rgba(103, 194, 58, 0.3)'}
                        ]),
                        barBorderRadius: [8, 8, 0, 0],
                        shadowColor: '#67c23a',
                        shadowBlur: 10
                    },
                    emphasis: { 
                        itemStyle: { 
                            color: '#f6c177', 
                            shadowColor: '#f6c177', 
                            shadowBlur: 20 
                        } 
                    },
                    animation: true,
                    animationDuration: 1000
                }]
            };
            supplierStats.setOption(supplierOption);

            // 3. 用户统计
            var userStats = echarts.init(document.getElementById('userStats'));
            var userOption = {
                backgroundColor: 'transparent',
                title: { 
                    text: '用户活跃度分析', 
                    left: 'center', 
                    top: 10,
                    textStyle: { 
                        color: '#fff', 
                        fontSize: 16, 
                        fontWeight: 'bold',
                        textShadowColor: '#f56c6c', 
                        textShadowBlur: 8 
                    } 
                },
                tooltip: {
                    backgroundColor: 'rgba(35, 41, 70, 0.9)',
                    borderColor: '#f56c6c',
                    borderWidth: 1,
                    textStyle: { color: '#fff' }
                },
                grid: {
                    left: '10%',
                    right: '10%',
                    top: '25%',
                    bottom: '15%'
                },
                xAxis: { 
                    type: 'category', 
                    data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'], 
                    axisLabel: { color: '#fff' }, 
                    axisLine: { 
                        lineStyle: { color: '#f56c6c' } 
                    } 
                },
                yAxis: { 
                    axisLabel: { color: '#fff' }, 
                    axisLine: { 
                        lineStyle: { color: '#f56c6c' } 
                    }, 
                    splitLine: { 
                        lineStyle: { color: 'rgba(245, 108, 108, 0.2)' } 
                    } 
                },
                series: [{ 
                    type: 'bar', 
                    data: [234, 156, 456, 789, 567, 345],
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            {offset: 0, color: '#f56c6c'},
                            {offset: 1, color: 'rgba(245, 108, 108, 0.3)'}
                        ]),
                        barBorderRadius: [8, 8, 0, 0],
                        shadowColor: '#f56c6c',
                        shadowBlur: 10
                    },
                    emphasis: { 
                        itemStyle: { 
                            color: '#f6c177', 
                            shadowColor: '#f6c177', 
                            shadowBlur: 20 
                        } 
                    },
                    animation: true,
                    animationDuration: 1000
                }]
            };
            userStats.setOption(userOption);

            // 4. 趋势图
            var trendChart = echarts.init(document.getElementById('trendChart'));
            var trendOption = {
                backgroundColor: 'transparent',
                title: { 
                    text: '访问量趋势分析', 
                    left: 'center', 
                    top: 10,
                    textStyle: { 
                        color: '#fff', 
                        fontSize: 16, 
                        fontWeight: 'bold',
                        textShadowColor: '#3a8ee6', 
                        textShadowBlur: 8 
                    } 
                },
                tooltip: {
                    backgroundColor: 'rgba(35, 41, 70, 0.9)',
                    borderColor: '#3a8ee6',
                    borderWidth: 1,
                    textStyle: { color: '#fff' }
                },
                grid: {
                    left: '10%',
                    right: '10%',
                    top: '25%',
                    bottom: '15%'
                },
                legend: {
                    data: ['IP访问量', '用户访问量'],
                    textStyle: { color: '#fff' },
                    top: 25
                },
                xAxis: { 
                    type: 'category', 
                    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'], 
                    axisLabel: { color: '#fff' }, 
                    axisLine: { 
                        lineStyle: { color: '#3a8ee6' } 
                    } 
                },
                yAxis: { 
                    axisLabel: { color: '#fff' }, 
                    axisLine: { 
                        lineStyle: { color: '#3a8ee6' } 
                    }, 
                    splitLine: { 
                        lineStyle: { color: 'rgba(58, 142, 230, 0.2)' } 
                    } 
                },
                series: [
                    { 
                        name: 'IP访问量',
                        type: 'line', 
                        data: [120, 132, 101, 134, 90, 230, 210],
                        smooth: true,
                        itemStyle: {
                            color: '#3a8ee6',
                            shadowColor: '#3a8ee6',
                            shadowBlur: 10
                        },
                        lineStyle: {
                            width: 4,
                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                                {offset: 0, color: '#3a8ee6'},
                                {offset: 1, color: '#f6c177'}
                            ])
                        },
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                {offset: 0, color: 'rgba(58, 142, 230, 0.3)'},
                                {offset: 1, color: 'rgba(58, 142, 230, 0.1)'}
                            ])
                        },
                        symbol: 'circle',
                        symbolSize: 8,
                        animation: true,
                        animationDuration: 1200
                    },
                    { 
                        name: '用户访问量',
                        type: 'line', 
                        data: [220, 182, 191, 234, 290, 330, 310],
                        smooth: true,
                        itemStyle: {
                            color: '#f6c177',
                            shadowColor: '#f6c177',
                            shadowBlur: 10
                        },
                        lineStyle: {
                            width: 4,
                            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                                {offset: 0, color: '#f6c177'},
                                {offset: 1, color: '#f56c6c'}
                            ])
                        },
                        areaStyle: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                {offset: 0, color: 'rgba(246, 193, 119, 0.3)'},
                                {offset: 1, color: 'rgba(246, 193, 119, 0.1)'}
                            ])
                        },
                        symbol: 'diamond',
                        symbolSize: 8,
                        animation: true,
                        animationDuration: 1200
                    }
                ]
            };
            trendChart.setOption(trendOption);

            // 5. 地区统计
            var regionStats = echarts.init(document.getElementById('regionStats'));
            var regionOption = {
                backgroundColor: 'transparent',
                title: { 
                    text: '地区访问分布', 
                    left: 'center', 
                    top: 10,
                    textStyle: { 
                        color: '#fff', 
                        fontSize: 16, 
                        fontWeight: 'bold',
                        textShadowColor: '#67c23a', 
                        textShadowBlur: 8 
                    } 
                },
                tooltip: {
                    backgroundColor: 'rgba(35, 41, 70, 0.9)',
                    borderColor: '#67c23a',
                    borderWidth: 1,
                    textStyle: { color: '#fff' }
                },
                series: [{
                    name: '访问量',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    center: ['50%', '60%'],
                    data: [
                        {value: 335, name: '华东'},
                        {value: 310, name: '华北'},
                        {value: 234, name: '华南'},
                        {value: 135, name: '华中'},
                        {value: 148, name: '西南'},
                        {value: 120, name: '西北'},
                        {value: 98, name: '东北'}
                    ],
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        color: '#fff',
                        fontSize: 12
                    },
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    animation: true,
                    animationDuration: 1000
                }]
            };
            regionStats.setOption(regionOption);

            // 初始化数据
            fetchStatsSummary();
            
            // 定时刷新数据
            setInterval(fetchStatsSummary, 30000);
            
            // 窗口大小改变时重绘图表
            window.addEventListener('resize', function() {
                ipMap.resize();
                supplierStats.resize();
                userStats.resize();
                trendChart.resize();
                regionStats.resize();
            });
        });
    </script>
</body>
</html>