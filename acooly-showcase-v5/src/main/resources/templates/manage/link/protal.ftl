<#--<link rel="stylesheet" href="https://adminlte.io/themes/dev/AdminLTE/plugins/fontawesome-free/css/all.min.css"/>-->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title></title>
</head>

<body>
<#--<!-- 引入 ECharts 库的主题 CSS 文件 &ndash;&gt;-->
<#--<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.css">-->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.11.2/css/all.css" >
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.11.2/css/v4-shims.css">
<section class="content" style="min-height: 10px; padding-top: 5px">
<#-- 显示总用例数 （分系统展示）-->
    <div class="container-fluid">

        <div class="row">
            <div class="col-lg-4 col-6">
                <!-- small box -->
                <div class="small-box bg-success">
                    <div class="inner">
                        <h3><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">${allLandingPages}</font></font></h3>

                        <p><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">落地页数</font></font></p>
                    </div>
                    <div class="icon">
                        <i class="fa fa-cube fa-lg fa-fw fa-col"></i>
                    </div>
                    <a  onclick="show1('/manage/showcase/daily/dmCenter/buildCanonicalUrl.html?displayOption=1',1200,600);" class="small-box-footer"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">更多信息 </font></font><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
            <!-- ./col -->
            <div class="col-lg-4 col-6">
                <!-- small box -->
                <div class="small-box bg-warning">
                    <div class="inner">
                        <h3><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">${allForms}</font></font></h3>

                        <p><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">表单数</font></font></p>
                    </div>
                    <div class="icon">
                        <i class="fa fa-link fa-lg fa-fw fa-col"></i>
                    </div>
                    <a   onclick="show1('/manage/showcase/daily/dmCenter/buildCanonicalUrl.html?displayOption=2',1200,600);" class="small-box-footer"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">更多信息 </font></font><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
            <!-- ./col -->
            <div class="col-lg-4 col-6">
                <!-- small box -->
                <div class="small-box bg-danger">
                    <div class="inner">
                        <h3><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">${allPolling}</font></font></h3>

                        <p><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">轮询模版数</font></font></p>
                    </div>
                    <div class="icon">
                        <i class="fa fa-exchange fa-lg fa-fw fa-col"></i>
                    </div>
                    <a  onclick="show1('/manage/showcase/daily/dmCenter/buildCanonicalUrl.html?displayOption=3',1200,600);" class="small-box-footer"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">更多信息 </font></font><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
            <!-- ./col -->
        </div>
        <div class="row">
            <#--        <section class="col-lg-push-12 connectedSortable ui-sortable">-->
            <div class="container-fluid">
                <div class="card bg-gradient-info">
                    <div class="card-header border-0">
                        <h3 class="card-title">
                            <i class="fas fa-th mr-1"></i>
                            分地区广告情况
                        </h3>

                        <div class="card-tools">
                            <button type="button" class="btn bg-info btn-sm" data-card-widget="collapse">
                                <i class="fas fa-minus"></i>
                            </button>
                            <#--                            <button type="button" class="btn bg-info btn-sm" data-card-widget="remove">-->
                            <#--                                <i class="fas fa-times"></i>-->
                            <#--                            </button>-->
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-valign-middle">
                            <thead>
                            <tr>
                                <th>地区名称</th>
                                <th>广告模版数量</th>
                                <th>落地页数</th>
                                <th>表单数</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list parameterStatusDto as parameterStatus>
                                <tr>
                                    <td>
                                        ${parameterStatus.region}
                                    </td>
                                    <td>
                                        ${parameterStatus.allCount}
                                    </td>
                                    <td>
                                        ${parameterStatus.landingCount}
                                    </td>
                                    <td>
                                        ${parameterStatus.formsCount}
                                    </td>
                                </tr>
                            </#list>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <#--        </section>-->
        </div>
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
                            <div id="canvas_${k?index}" style="height: 350px; width: 100%;"></div>
                        </div>
                    </div>
                    <!-- /.card-body -->
                </div>
            </div>
            </#list>
        </div>

    </div>
</section>

<div style="padding: 0 15px;">
<#-- 分系统展示用例成功和失败数据 -->
</div>
<div >
<#--    <iframe  src="/manage/layout/portalCharts.html" frameborder="0" scrolling="yes" style="border:0;height: 100%;width:100%;background-color: #fff;"></iframe>-->

</div>
<script  type="text/javascript">

    $(function() {
        <#list dmCenterBoArrayMap as k,v>
        // 获取当前 canvas 元素的 id
        var canvasId_${k?index} = "canvas_${k?index}";

        // 初始化图表
        var chart_${k?index} = echarts.init(document.getElementById(canvasId_${k?index}));

        // 图表数据
        var chartData_${k?index} = [
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
        chart_${k?index}.setOption(option_${k?index});
        </#list>
    });






     function show1(url, width, height) {
        return this.get1({
            url: url,
            width: width,
            height: height
        });
    }
     function get1(opts) {
        var url = opts.url;
        var width = opts.width != null ? opts.width : 400;
        var height = opts.height != null ? opts.height : 300;
        var title = opts.title != null ? opts.title : '<i class="fa fa-file-o fa-lg fa-fw fa-col"></i>查看'
        var buttonLable = opts.buttonLable != null ? opts.buttonLable : '关闭';
        var d = $('<div/>').dialog({
            href: contextPath + url,
            width: width,
            height: height,
            modal: true,
            title: title,
            buttons: [{
                text: '<i class="fa fa-times-circle fa-lg fa-fw fa-col" ></i>关闭',
                handler: function () {
                    // var d = $(this).closest('.window-body');
                    d.dialog('close');
                }
            }],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
        return d;
    }
</script>
</body>
</html>