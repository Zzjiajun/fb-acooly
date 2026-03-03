<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.11.2/css/all.css" >
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.11.2/css/v4-shims.css">
<section class="content">
    <div class="container-fluid">
        <!-- 第一行：核心指标卡片 -->
        <div class="row">
            <div class="col-12 col-sm-6 col-md-3">
                <div class="info-box">
                    <span class="info-box-icon bg-info elevation-1"><i class="fas fa-shopping-bag"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">总订单数</span>
                        <span class="info-box-number">${(totalOrders)!0}</span>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-6 col-md-3">
                <div class="info-box mb-3">
                    <span class="info-box-icon bg-success elevation-1"><i class="fas fa-dollar-sign"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">总销售额</span>
                        <span class="info-box-number">$${(totalSales?string('0.00'))!'0.00'}</span>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-6 col-md-3">
                <div class="info-box mb-3">
                    <span class="info-box-icon bg-warning elevation-1"><i class="fas fa-users"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">总用户数</span>
                        <span class="info-box-number">${(totalUsers)!0}</span>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-6 col-md-3">
                <div class="info-box mb-3">
                    <span class="info-box-icon bg-danger elevation-1"><i class="fas fa-box"></i></span>
                    <div class="info-box-content">
                        <span class="info-box-text">总商品数</span>
                        <span class="info-box-number">${(totalProducts)!0}</span>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.row -->

        <!-- 第二行：待处理事务、商品总览、用户总览 -->
        <div class="row">
            <!-- 左侧：待处理事务 -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fas fa-tasks mr-1"></i>待处理事务</h3>
                    </div>
                    <div class="card-body p-0">
                        <ul class="nav nav-pills flex-column">
                            <li class="nav-item">
                                <a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'待付款订单',value:'/manage/shop/shopOrders/index.html?search_EQ_status=PENDING',showMode:'1',icon:'fa-credit-card'})" class="nav-link">
                                    待付款订单
                                    <span class="float-right text-danger">
                                        <b>${(pendingTasks.pendingPayment)!0}</b>
                                    </span>
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'待发货订单',value:'/manage/shop/shopOrders/index.html?search_EQ_status=PROCESSING',showMode:'1',icon:'fa-truck'})" class="nav-link">
                                    待发货订单
                                    <span class="float-right text-warning">
                                        <b>${(pendingTasks.pendingShipment)!0}</b>
                                    </span>
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'已发货订单',value:'/manage/shop/shopOrders/index.html?search_EQ_status=SHIPPED',showMode:'1',icon:'fa-shipping-fast'})" class="nav-link">
                                    已发货订单
                                    <span class="float-right text-info">
                                        <b>${(pendingTasks.shipped)!0}</b>
                                    </span>
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'已完成订单',value:'/manage/shop/shopOrders/index.html?search_EQ_status=DELIVERED',showMode:'1',icon:'fa-check-circle'})" class="nav-link">
                                    已完成订单
                                    <span class="float-right text-success">
                                        <b>${(pendingTasks.completed)!0}</b>
                                    </span>
                                </a>
                            </li>
                            <li class="nav-item">
                                <a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'已取消订单',value:'/manage/shop/shopOrders/index.html?search_EQ_status=CANCELLED',showMode:'1',icon:'fa-times-circle'})" class="nav-link">
                                    已取消订单
                                    <span class="float-right text-secondary">
                                        <b>${(pendingTasks.cancelled)!0}</b>
                                    </span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- 中间：商品总览 -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fas fa-boxes mr-1"></i>商品总览</h3>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-6">
                                <div class="description-block border-right">
                                    <h5 class="description-header text-danger">${(productOverview.total)!0}</h5>
                                    <span class="description-text">全部商品</span>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="description-block">
                                    <h5 class="description-header text-warning">${(productOverview.featured)!0}</h5>
                                    <span class="description-text">推荐商品</span>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-6">
                                <div class="description-block border-right">
                                    <h5 class="description-header text-success">${(productOverview.freeShipping)!0}</h5>
                                    <span class="description-text">包邮商品</span>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="description-block">
                                    <h5 class="description-header text-info">${(productOverview.discounted)!0}</h5>
                                    <span class="description-text">打折商品</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 右侧：用户总览 -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fas fa-user-friends mr-1"></i>用户总览</h3>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-6">
                                <div class="description-block border-right">
                                    <h5 class="description-header text-danger">${(userOverview.todayNew)!0}</h5>
                                    <span class="description-text">今日新增</span>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="description-block">
                                    <h5 class="description-header text-warning">${(userOverview.yesterdayNew)!0}</h5>
                                    <span class="description-text">昨日新增</span>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-6">
                                <div class="description-block border-right">
                                    <h5 class="description-header text-success">${(userOverview.monthNew)!0}</h5>
                                    <span class="description-text">本月新增</span>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="description-block">
                                    <h5 class="description-header text-info">${(userOverview.total)!0}</h5>
                                    <span class="description-text">会员总数</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.row -->

        <!-- 第三行：订单统计和趋势图 -->
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title"><i class="fas fa-chart-line mr-1"></i>订单统计</h5>
                        <div class="card-tools">
                            <!-- 日期选择器 -->
                            <div class="input-group input-group-sm" style="width: 350px; margin-right: 10px; display: inline-flex;">
                                <input type="text" id="orderStatsStartDate" class="form-control form-control-sm" 
                                       placeholder="开始日期" style="width: 120px;" 
                                       onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'orderStatsEndDate\')}'})" 
                                       onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'orderStatsEndDate\')}'})" />
                                <span class="input-group-text" style="padding: 0.25rem 0.5rem;">至</span>
                                <input type="text" id="orderStatsEndDate" class="form-control form-control-sm" 
                                       placeholder="结束日期" style="width: 120px;" 
                                       onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'orderStatsStartDate\')}'})" 
                                       onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'orderStatsStartDate\')}'})" />
                                <div class="input-group-append">
                                    <button class="btn btn-link btn-sm" type="button" id="queryOrderStatsBtn" title="查询">
                                        <i class="fas fa-search"></i> 查询
                                    </button>
                                    <button class="btn btn-link btn-sm" type="button" id="resetOrderStatsBtn" title="重置">
                                        <i class="fas fa-redo"></i> 重置
                                    </button>
                                </div>
                            </div>
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <!-- 左侧：统计卡片 -->
                            <div class="col-md-4" id="orderStatsCards">
                                <div class="description-block border-right" id="statsTotalOrders">
                                    <span class="description-percentage text-info" id="statsOrderCompare">
                                        <i class="fas fa-caret-up"></i>
                                        <span id="statsOrderCompareValue">0.0</span>%
                                    </span>
                                    <h5 class="description-header" id="statsTotalOrderCount">${(orderStats.monthOrderCount)!0}</h5>
                                    <span class="description-text" id="statsDateRangeText">本月订单总数</span>
                                </div>
                                <hr>
                                <div class="description-block border-right" id="statsTotalSales">
                                    <span class="description-percentage text-info" id="statsSalesCompare">
                                        <i class="fas fa-caret-up"></i>
                                        <span id="statsSalesCompareValue">0.0</span>%
                                    </span>
                                    <h5 class="description-header" id="statsTotalSalesAmount">$${((orderStats.monthSalesAmount)!0)?string('0.00')}</h5>
                                    <span class="description-text">销售总额</span>
                                </div>
                                <hr>
                                <div class="description-block border-right" id="statsAvgOrders">
                                    <h5 class="description-header" id="statsAvgOrderCount">0</h5>
                                    <span class="description-text">日均订单数</span>
                                </div>
                                <hr>
                                <div class="description-block border-right" id="statsAvgSales">
                                    <h5 class="description-header" id="statsAvgSalesAmount">$0.00</h5>
                                    <span class="description-text">日均销售额</span>
                                </div>
                            </div>
                            <!-- 右侧：趋势图表 -->
                            <div class="col-md-8">
                                <div id="orderTrendChart" style="height: 350px; width: 100%;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.row -->

        <!-- 第四行：最新订单和最新商品 -->
        <div class="row">
            <!-- 左侧：最新订单 -->
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header border-transparent">
                        <h3 class="card-title"><i class="fas fa-list mr-1"></i>最新订单</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table m-0">
                                <thead>
                                <tr>
                                    <th>订单编号</th>
                                    <th>订单金额</th>
                                    <th>订单状态</th>
                                    <th>创建时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#if latestOrders?? && (latestOrders?size > 0)>
                                    <#list latestOrders as order>
                                    <tr>
                                        <td><a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'订单详情-${(order.orderId)!''?js_string}',value:'/manage/shop/shopOrders/index.html?id=${order.id}',showMode:'1',icon:'fa-file-text-o'})">${(order.orderId)!''}</a></td>
                                        <td>$${(order.totalPrice?string('0.00'))!'0.00'}</td>
                                        <td>
                                            <#if order.status??>
                                                <#if order.status == "PENDING">
                                                    <span class="badge badge-warning">待付款</span>
                                                <#elseif order.status == "PROCESSING">
                                                    <span class="badge badge-info">处理中</span>
                                                <#elseif order.status == "SHIPPED">
                                                    <span class="badge badge-primary">已发货</span>
                                                <#elseif order.status == "DELIVERED">
                                                    <span class="badge badge-success">已完成</span>
                                                <#elseif order.status == "CANCELLED">
                                                    <span class="badge badge-danger">已取消</span>
                                                <#else>
                                                    <span class="badge badge-secondary">${(order.status)!''}</span>
                                                </#if>
                                            </#if>
                                        </td>
                                        <td>${(order.createTime?string('yyyy-MM-dd HH:mm:ss'))!''}</td>
                                    </tr>
                                    </#list>
                                <#else>
                                    <tr>
                                        <td colspan="4" class="text-center">暂无订单数据</td>
                                    </tr>
                                </#if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer clearfix">
                        <a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'订单列表',value:'/manage/shop/shopOrders/index.html?',showMode:'1',icon:'fa-list'})" class="btn btn-sm btn-secondary float-right">查看所有订单</a>
                    </div>
                </div>
            </div>

            <!-- 右侧：最新商品 -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title"><i class="fas fa-box mr-1"></i>最新上架商品</h3>
                        <div class="card-tools">
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body p-0">
                        <ul class="products-list product-list-in-card pl-2 pr-2">
                            <#if latestProducts?? && (latestProducts?size > 0)>
                                <#list latestProducts as product>
                                <li class="item">
                                    <div class="product-img">
                                        <#if product.imageUrl?? && product.imageUrl != ''>
                                            <img src="${product.imageUrl}" alt="商品图片" class="img-size-50">
                                        <#else>
                                            <img src="dist/img/default-150x150.png" alt="商品图片" class="img-size-50">
                                        </#if>
                                    </div>
                                    <div class="product-info">
<#--                                        <a href="javascript:;" onclick="$.acooly.framework.show('/manage/shop/shopProducts/show.html?id=${product.id}',600,600);" class="product-title">-->
<#--                                            ${(product.name)!''}-->
<#--                                            <span class="badge badge-warning float-right">$${(product.price?string('0.00'))!'0.00'}</span>-->
<#--                                        </a>-->
                                        <button onclick="$.acooly.framework.show('/manage/shop/shopProducts/show.html?id=${product.id}',600,600);" class="btn btn-outline-info btn-xs" type="button" title="查看详情">
                                            ${(product.name)!''}
                                        </button>
                                        <span class="badge badge-warning float-right">$${(product.price?string('0.00'))!'0.00'}</span>
                                        <span class="product-description">
                                            <#if product.description?? && product.description != ''>
                                                ${product.description?substring(0, (product.description?length > 50)?then(50, product.description?length))}...
                                            <#else>
                                                暂无描述
                                            </#if>
                                        </span>
                                    </div>
                                </li>
                                </#list>
                            <#else>
                                <li class="item">
                                    <div class="product-info">
                                        <span class="product-description text-center">暂无商品数据</span>
                                    </div>
                                </li>
                            </#if>
                        </ul>
                    </div>
                    <div class="card-footer text-center">
                        <a href="javascript:;" onclick="$.acooly.layout.accessResource({type:'URL',name:'商品列表',value:'/manage/shop/shopProducts/list.html',showMode:'1',icon:'fa-boxes'})" class="uppercase">查看所有商品</a>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.row -->
    </div><!--/. container-fluid -->
</section>

<script type="text/javascript">
    // 全局变量：图表实例
    var trendChart = null;
    
    $(function() {
        // 初始化订单趋势图表
        <#if trendData?? && (trendData?size > 0)>
        trendChart = echarts.init(document.getElementById('orderTrendChart'));
        
        // 准备初始数据
        var dateList = [
            <#list trendData as data>
            '${(data.date)!''}'<#if data_has_next>,</#if>
            </#list>
        ];
        var orderCountList = [
            <#list trendData as data>
            ${(data.orderCount)!0}<#if data_has_next>,</#if>
            </#list>
        ];
        var salesAmountList = [
            <#list trendData as data>
            <#if data.salesAmount??>
                ${data.salesAmount?string('0.00')}
            <#else>
                0.00
            </#if><#if data_has_next>,</#if>
            </#list>
        ];

        var option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            legend: {
                data: ['订单数', '销售额'],
                top: '5%',
                left: 'center',
                itemGap: 30,
                textStyle: {
                    fontSize: 12
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                top: '15%',
                bottom: '10%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: dateList
            },
            yAxis: [
                {
                    type: 'value',
                    name: '订单数',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}'
                    }
                },
                {
                    type: 'value',
                    name: '销售额',
                    position: 'right',
                    axisLabel: {
                        formatter: function(value) {
                            return '$' + value;
                        }
                    }
                }
            ],
            series: [
                {
                    name: '订单数',
                    type: 'line',
                    areaStyle: {
                        color: 'rgba(54, 162, 235, 0.2)'
                    },
                    itemStyle: {
                        color: 'rgba(54, 162, 235, 1)'
                    },
                    data: orderCountList
                },
                {
                    name: '销售额',
                    type: 'line',
                    areaStyle: {
                        color: 'rgba(75, 192, 192, 0.2)'
                    },
                    itemStyle: {
                        color: 'rgba(75, 192, 192, 1)'
                    },
                    yAxisIndex: 1,
                    data: salesAmountList
                }
            ]
        };

        trendChart.setOption(option);
        
        // 响应式调整
        window.addEventListener('resize', function() {
            if (trendChart) {
                trendChart.resize();
            }
        });
        </#if>
        
        // 初始化日期选择器（默认最近7天）
        var today = new Date();
        var sevenDaysAgo = new Date();
        sevenDaysAgo.setDate(today.getDate() - 6);
        
        var formatDate = function(date) {
            var year = date.getFullYear();
            var month = String(date.getMonth() + 1).padStart(2, '0');
            var day = String(date.getDate()).padStart(2, '0');
            return year + '-' + month + '-' + day;
        };
        
        // 设置默认日期值
        $('#orderStatsStartDate').val(formatDate(sevenDaysAgo));
        $('#orderStatsEndDate').val(formatDate(today));
        
        // 确保日期选择器绑定（如果HTML属性方式不生效，使用jQuery绑定）
        if (typeof WdatePicker !== 'undefined') {
            // 绑定开始日期选择器
            $('#orderStatsStartDate').on('click focus', function() {
                WdatePicker({
                    readOnly: true,
                    dateFmt: 'yyyy-MM-dd',
                    maxDate: '#F{$dp.$D(\'orderStatsEndDate\')}',
                    onpicked: function() {
                        // 如果开始日期大于结束日期，自动调整结束日期
                        var startDate = $('#orderStatsStartDate').val();
                        var endDate = $('#orderStatsEndDate').val();
                        if (startDate && endDate && startDate > endDate) {
                            $('#orderStatsEndDate').val(startDate);
                        }
                    }
                });
            });
            
            // 绑定结束日期选择器
            $('#orderStatsEndDate').on('click focus', function() {
                WdatePicker({
                    readOnly: true,
                    dateFmt: 'yyyy-MM-dd',
                    minDate: '#F{$dp.$D(\'orderStatsStartDate\')}',
                    onpicked: function() {
                        // 如果结束日期小于开始日期，自动调整开始日期
                        var startDate = $('#orderStatsStartDate').val();
                        var endDate = $('#orderStatsEndDate').val();
                        if (startDate && endDate && endDate < startDate) {
                            $('#orderStatsStartDate').val(endDate);
                        }
                    }
                });
            });
        }
        
        // 绑定查询按钮事件
        $('#queryOrderStatsBtn').on('click', function() {
            loadOrderStats();
        });
        
        // 绑定重置按钮事件
        $('#resetOrderStatsBtn').on('click', function() {
            $('#orderStatsStartDate').val(formatDate(sevenDaysAgo));
            $('#orderStatsEndDate').val(formatDate(today));
            loadOrderStats();
        });
        
        // 回车键触发查询
        $('#orderStatsStartDate, #orderStatsEndDate').on('keypress', function(e) {
            if (e.which === 13) {
                loadOrderStats();
            }
        });
    });
    
    /**
     * 加载订单统计数据
     */
    function loadOrderStats() {
        var startDate = $('#orderStatsStartDate').val();
        var endDate = $('#orderStatsEndDate').val();
        
        if (!startDate || !endDate) {
            alert('请选择开始日期和结束日期');
            return;
        }
        
        // 显示加载状态
        var $queryBtn = $('#queryOrderStatsBtn');
        var originalHtml = $queryBtn.html();
        $queryBtn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin"></i> 加载中...');
        
        $.ajax({
            url: '/manage/shop/shopOrders/orderStats',
            type: 'GET',
            data: {
                startDate: startDate,
                endDate: endDate
            },
            success: function(response) {
                if (response.success && !response.error) {
                    // 更新统计卡片
                    updateOrderStatsCards(response.orderStats);
                    // 更新趋势图
                    updateTrendChart(response.trendData);
                } else {
                    alert(response.error || '加载数据失败，请重试');
                }
            },
            error: function(xhr, status, error) {
                console.error('加载数据失败:', error);
                alert('加载数据失败，请检查网络连接或稍后重试');
            },
            complete: function() {
                // 恢复按钮状态
                $queryBtn.prop('disabled', false).html(originalHtml);
            }
        });
    }
    
    /**
     * 更新订单统计卡片
     */
    function updateOrderStatsCards(orderStats) {
        if (!orderStats) return;
        
        // 更新总订单数
        $('#statsTotalOrderCount').text(orderStats.totalOrderCount || 0);
        
        // 更新总销售额
        var totalSales = parseFloat(orderStats.totalSalesAmount || 0).toFixed(2);
        $('#statsTotalSalesAmount').text('$' + totalSales);
        
        // 更新同比数据
        var orderCompare = parseFloat(orderStats.orderCompare || 0);
        var salesCompare = parseFloat(orderStats.salesCompare || 0);
        
        // 更新订单数同比
        var $orderCompare = $('#statsOrderCompare');
        var $orderCompareValue = $('#statsOrderCompareValue');
        $orderCompareValue.text(Math.abs(orderCompare).toFixed(1));
        if (orderCompare >= 0) {
            $orderCompare.removeClass('text-danger').addClass('text-success');
            $orderCompare.find('i').removeClass('fa-caret-down').addClass('fa-caret-up');
        } else {
            $orderCompare.removeClass('text-success').addClass('text-danger');
            $orderCompare.find('i').removeClass('fa-caret-up').addClass('fa-caret-down');
        }
        
        // 更新销售额同比
        var $salesCompare = $('#statsSalesCompare');
        var $salesCompareValue = $('#statsSalesCompareValue');
        $salesCompareValue.text(Math.abs(salesCompare).toFixed(1));
        if (salesCompare >= 0) {
            $salesCompare.removeClass('text-danger').addClass('text-success');
            $salesCompare.find('i').removeClass('fa-caret-down').addClass('fa-caret-up');
        } else {
            $salesCompare.removeClass('text-success').addClass('text-danger');
            $salesCompare.find('i').removeClass('fa-caret-up').addClass('fa-caret-down');
        }
        
        // 计算日均数据
        var startDate = new Date(orderStats.startDate);
        var endDate = new Date(orderStats.endDate);
        var daysDiff = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;
        
        if (daysDiff > 0) {
            var avgOrders = Math.round((orderStats.totalOrderCount || 0) / daysDiff);
            var avgSales = (parseFloat(orderStats.totalSalesAmount || 0) / daysDiff).toFixed(2);
            $('#statsAvgOrderCount').text(avgOrders);
            $('#statsAvgSalesAmount').text('$' + avgSales);
        }
        
        // 更新日期范围文本
        var dateRangeText = orderStats.startDate + ' 至 ' + orderStats.endDate;
        $('#statsDateRangeText').text('订单总数 (' + dateRangeText + ')');
    }
    
    /**
     * 更新趋势图表
     */
    function updateTrendChart(trendData) {
        if (!trendData || trendData.length === 0) {
            console.warn('趋势数据为空');
            return;
        }
        
        // 如果图表未初始化，则初始化
        if (!trendChart) {
            trendChart = echarts.init(document.getElementById('orderTrendChart'));
        }
        
        // 准备数据
        var dateList = trendData.map(function(item) {
            return item.date;
        });
        var orderCountList = trendData.map(function(item) {
            return item.orderCount || 0;
        });
        var salesAmountList = trendData.map(function(item) {
            return parseFloat(item.salesAmount || 0);
        });
        
        // 更新图表选项
        var option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            legend: {
                data: ['订单数', '销售额'],
                top: '5%',
                left: 'center',
                itemGap: 30,
                textStyle: {
                    fontSize: 12
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                top: '15%',
                bottom: '10%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: dateList
            },
            yAxis: [
                {
                    type: 'value',
                    name: '订单数',
                    position: 'left',
                    axisLabel: {
                        formatter: '{value}'
                    }
                },
                {
                    type: 'value',
                    name: '销售额',
                    position: 'right',
                    axisLabel: {
                        formatter: function(value) {
                            return '$' + value;
                        }
                    }
                }
            ],
            series: [
                {
                    name: '订单数',
                    type: 'line',
                    areaStyle: {
                        color: 'rgba(54, 162, 235, 0.2)'
                    },
                    itemStyle: {
                        color: 'rgba(54, 162, 235, 1)'
                    },
                    data: orderCountList
                },
                {
                    name: '销售额',
                    type: 'line',
                    areaStyle: {
                        color: 'rgba(75, 192, 192, 0.2)'
                    },
                    itemStyle: {
                        color: 'rgba(75, 192, 192, 1)'
                    },
                    yAxisIndex: 1,
                    data: salesAmountList
                }
            ]
        };
        
        trendChart.setOption(option, true);
    }
</script>
