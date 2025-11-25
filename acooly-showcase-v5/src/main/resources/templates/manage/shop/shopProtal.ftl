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
                            <button type="button" class="btn btn-tool" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <!-- 左侧：统计卡片 -->
                            <div class="col-md-4">
                                <div class="description-block border-right">
                                    <#assign monthOrderCompareValue = ((orderStats.monthOrderCompare)!0)>
                                    <span class="description-percentage ${(monthOrderCompareValue >= 0)?then('text-success', 'text-danger')}">
                                        <i class="fas ${(monthOrderCompareValue >= 0)?then('fa-caret-up', 'fa-caret-down')}"></i>
                                        ${monthOrderCompareValue?string('0.0')}%
                                    </span>
                                    <h5 class="description-header">${(orderStats.monthOrderCount)!0}</h5>
                                    <span class="description-text">本月订单总数</span>
                                </div>
                                <hr>
                                <div class="description-block border-right">
                                    <#assign weekOrderCompareValue = ((orderStats.weekOrderCompare)!0)>
                                    <span class="description-percentage ${(weekOrderCompareValue >= 0)?then('text-success', 'text-danger')}">
                                        <i class="fas ${(weekOrderCompareValue >= 0)?then('fa-caret-up', 'fa-caret-down')}"></i>
                                        ${weekOrderCompareValue?string('0.0')}%
                                    </span>
                                    <h5 class="description-header">${(orderStats.weekOrderCount)!0}</h5>
                                    <span class="description-text">本周订单总数</span>
                                </div>
                                <hr>
                                <div class="description-block border-right">
                                    <#assign monthSalesCompareValue = ((orderStats.monthSalesCompare)!0)>
                                    <span class="description-percentage ${(monthSalesCompareValue >= 0)?then('text-success', 'text-danger')}">
                                        <i class="fas ${(monthSalesCompareValue >= 0)?then('fa-caret-up', 'fa-caret-down')}"></i>
                                        ${monthSalesCompareValue?string('0.0')}%
                                    </span>
                                    <h5 class="description-header">$${((orderStats.monthSalesAmount)!0)?string('0.00')}</h5>
                                    <span class="description-text">本月销售总额</span>
                                </div>
                                <hr>
                                <div class="description-block border-right">
                                    <#assign weekSalesCompareValue = ((orderStats.weekSalesCompare)!0)>
                                    <span class="description-percentage ${(weekSalesCompareValue >= 0)?then('text-success', 'text-danger')}">
                                        <i class="fas ${(weekSalesCompareValue >= 0)?then('fa-caret-up', 'fa-caret-down')}"></i>
                                        ${weekSalesCompareValue?string('0.0')}%
                                    </span>
                                    <h5 class="description-header">$${((orderStats.weekSalesAmount)!0)?string('0.00')}</h5>
                                    <span class="description-text">本周销售总额</span>
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
                                            <img src="${product.imageUrl}" alt="商品图片" class="img-size-50" onerror="this.src='dist/img/default-150x150.png'">
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
    $(function() {
        // 初始化订单趋势图表
        <#if trendData?? && (trendData?size > 0)>
        var trendChart = echarts.init(document.getElementById('orderTrendChart'));
        
        // 准备数据
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
            trendChart.resize();
        });
        </#if>
    });
</script>
