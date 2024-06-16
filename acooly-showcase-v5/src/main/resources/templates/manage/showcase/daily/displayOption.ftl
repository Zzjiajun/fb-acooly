<table id="manage_displayOption_datagrid" class="easyui-datagrid"
       url="/manage/showcase/daily/dmCenter/listDisplayOption?type=${k}" toolbar="#manage_displayOption_toolbar"
       fit="true" border="false" fitColumns="false"
       pagination="true" idField="id" pageSize="20" pageList="[ 10, 20, 30, 40, 50 ]" sortName="id" sortOrder="desc"
       checkOnSelect="true" selectOnCheck="true" singleSelect="true"  style="width: 100%; height: 100%;">
    <thead>
    <tr>
        <th field="showCheckboxWithId" checkbox="true" formatter="idFormatter">编号</th>
        <th field="id" sortable="true">id</th>
        <th field="userName" formatter="contentFormatter">用户名</th>
        <th field="region" formatter="contentFormatter">地区</th>
        <th field="displayOption" formatter="displayOptionFunction">广告类型</th>
        <th field="link" formatter="domainFunction1">链接地址</th>
        <th field="domain" formatter="domainFunction">访问域名</th>
        <th field="diversion" formatter="displayOptionTwoFunction">是否轮询</th>
        <th field="createTime" formatter="dateTimeFormatter">创建时间</th>
        <th field="updateTime" formatter="dateTimeFormatter">修改时间</th>
    </tr>
    </thead>
</table>

<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.8/clipboard.min.js"></script>
<link href="//unpkg.com/layui@2.9.8/dist/css/layui.css" rel="stylesheet">
<script type="text/javascript">
    function domainFunction(value, row) {
        return "<div style='text-align: center;'><button onclick='copyToClipboard3(" + JSON.stringify(row) + ")' class='layui-btn layui-btn-radius'>" + value + ' / ' + row.secondaryDomain + "</button></div>";
    }


    function displayOptionTwoFunction(value) {
        if (value == '1') {
            return ' <i class="fa fa-check-square fa-fw fa-col" style="color: cornflowerblue" />'
        } else {
            return '<i class="fa  fa-remove fa-fw fa-col" style="color: red"/>'
        }
    }


    function domainFunction1(value, row) {
        if (row.diversion == '1') {
            return "<button    onclick='manage_showcase_dmCenter_add()' class='btns' style=''>跳转到轮询链接</button>";
        } else {
            return "<button  onclick='copyToClipboard2(" + JSON.stringify(value) + ")'  class='btn btn-link'>" + value + "</button>";
        }
        // return '<span class="btn btn-outline-info">'+value+' / '+row.secondaryDomain+'</span>'
    }


    function copyToClipboard2(text) {
        var clipboard = new ClipboardJS('.btn', {
            text: function () {
                return text;
            }
        });

        clipboard.on('success', function (e) {
            $.messager.alert('复制成功', e.text);
            clipboard.destroy(); // 销毁 clipboard 实例
        });

        clipboard.on('error', function (e) {
            $.messager.alert('复制失败', e.action);
            // console.error('复制失败:', e.action);
            // alert('复制失败，请手动复制！');
            clipboard.destroy(); // 销毁 clipboard 实例
        });

        clipboard.onClick(event); // 手动触发复制操作
    }


    function copyToClipboard3(row) {
        var clipboard = new ClipboardJS('.btn', {
            text: function () {
                return row.domain + '/' + row.secondaryDomain;
            }
        });

        clipboard.on('success', function (e) {
            $.messager.alert('复制成功', e.text);
            clipboard.destroy(); // 销毁 clipboard 实例
        });

        clipboard.on('error', function (e) {
            $.messager.alert('复制失败', e.action);
            // console.error('复制失败:', e.action);
            // alert('复制失败，请手动复制！');
            clipboard.destroy(); // 销毁 clipboard 实例
        });

        clipboard.onClick(event); // 手动触发复制操作
    }


    function manage_showcase_dmCenter_add() {
        $('#layout_center_tabs').tabs("add", {
            title: '轮询链接',
            closable: true,
            iconCls: 'fa fa-commenting',
            href: '/manage/link/linkSrcs/index.html',
        });
    }

    function displayOptionFunction(value, row) {
        var buttonHtml;
        if (value == '1') {
            buttonHtml = "<div style='text-align: center;'><button class='btn btn-success' onclick='openboard4(" + JSON.stringify(row) + ")'>落地页</button></div>";
        } else {
            buttonHtml = "<div style='text-align: center;'><button class='btn btn-primary' onclick='openboard4(" + JSON.stringify(row) + ")'>表单</button></div>";
        }
        return buttonHtml;
    }

    function openboard4(row) {
        window.open("http://" + row.domain + '/' + row.secondaryDomain, '_blank');
    }


</script>
<style>
    .btns {
        background: #eb94d0;
        /* 创建渐变 */
        background-image: -webkit-linear-gradient( #eb94d0, #2079b0);
        background-image: -moz-linear-gradient( #eb94d0, #2079b0);
        background-image: -ms-linear-gradient( #eb94d0, #2079b0);
        background-image: -o-linear-gradient( #eb94d0, #2079b0);
        background-image: linear-gradient(to bottom, #eb94d0, #2079b0);
        /* 给按钮添加圆角 */
        border-radius: 14px;
        text-shadow: 3px 2px 1px #9daef5;
        font-family: Arial;
        color: #fafafa;
        font-size: 12px;
        padding: 9px;
        text-decoration: none;
    }
    /* 悬停样式 */
    .btns:hover {
        background: #2079b0;
        background-image: -webkit-linear-gradient( #2079b0, #eb94d0);
        background-image: -moz-linear-gradient( #2079b0, #eb94d0);
        background-image: -ms-linear-gradient( #2079b0, #eb94d0);
        background-image: -o-linear-gradient( #2079b0, #eb94d0);
        background-image: linear-gradient(to bottom, #2079b0, #eb94d0);
        text-decoration: none;
    }
</style>