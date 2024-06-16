
    <div class="container-fluid">
        <div class="row">
            <#list dmCenterBoArrayMap as k,v>
                <div class="container-fluid">
                    <div class="card bg-gradient-primary">
                        <div class="card-header border-0">
                            <h3 class="card-title">
                                <i class="fas fa-map-marker-alt mr-1"></i>
                                ${k}
                            </h3>
                            <div class="card-tools">
                                <button type="button" class="btn btn-tool" data-card-widget="collapse"><i class="fas fa-minus"></i>
                                </button>
                                <#--                            <button type="button" class="btn btn-tool" data-card-widget="remove"><i class="fas fa-times"></i>-->
                                <#--                            </button>-->
                            </div>
                        </div>
                        <div class="card-body" style="background-color: #fff">
                            <div class="chart">
                                <div id="canvast_${k?index}" style="height: 350px; width: 100%;"></div>
                            </div>
                        </div>
                        <!-- /.card-body -->
                    </div>
                </div>
            </#list>
        </div>
    </div>
<script>
        <#list dmCenterBoArrayMap as k,v>
        // 获取当前 canvas 元素的 id
        var canvastId_${k?index} = "canvast_${k?index}";

        // 初始化图表
        var chartt_${k?index} = echarts.init(document.getElementById(canvastId_${k?index}));

        // 图表数据
        var charttData_${k?index} = [
            <#list v as data>
            ['${data.userName}', ${data.formsCount}, ${data.landingCount}, ${data.landingButtonViews}],
            </#list>
        ];

        // 图表配置项
        var option_${k?index} = {
            legend: {},
            tooltip: {},
            dataset: {
                source: [
                    ['用户名称', '表单点击数', '落地页点击数', '落地页按钮点击次数'],
                    <#list v as data>
                    ['${data.userName}', ${data.formsCount}, ${data.landingCount}, ${data.landingButtonViews}],
                    </#list>
                ]
            },
            xAxis: { type: 'category' },
            yAxis: {},
            series: [{ type: 'bar' }, { type: 'bar' }, { type: 'bar' }]
        };

        // 使用配置项渲染图表
        chartt_${k?index}.setOption(option_${k?index});
        </#list>
</script>

