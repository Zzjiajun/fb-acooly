<#if ssoEnable><#include "/manage/common/ssoInclude.ftl"></#if>
<div>
    <div id="manage_showcase_demo_dynamicTabs">
        <!-- tab1：文件表单 -->
        <div title="文件表单" href="/manage/showcase/demo/dynamicTab/view1.html"></div>
        <!-- tab2：联系信息 -->
        <div title="联系表单" href="/manage/showcase/demo/dynamicTab/view2.html"></div>
    </div>

    <div style="padding: 10px;margin-top: 10px;">

        <button type="button" onclick="manage_showcase_demo_dynamicTabs_add()" class="btn btn-primary">动态添加一个tab</button>
    </div>

    <div style="margin-top: 10px; padding: 10px;">
        已注册Tabs事件(请打开浏览器console查看log)：
        <li>1、onSelect: 选中某个tab时触发</li>
        <li>2、onLoad: 动态加载tab完成时触发</li>
        <li>3、onUnSelect: 某个tabX从选中状态离开时触发，主要，虽然离开了，但该tabX内的内容仍然存在，你可以在这个时间进行自动保存操作。</li>
    </div>

    <script>

        function manage_showcase_demo_dynamicTabs_add() {
            $('#manage_showcase_demo_dynamicTabs').tabs("add", {
                title: '我是动态添加的tab',
                closable: true,
                iconCls: 'fa fa-circle-o',
                href: '/manage/showcase/demo/dynamicTab/view2.html',
            });
        }

        $(function () {
            // 初始渲染tabs，也可以通过html标签上设置属性方式
            $('#manage_showcase_demo_dynamicTabs').tabs({
                // justified:true,
                // narrow:true,
                pill:true,
                onLoad: function (panel) {
                    console.info("tabs-event: onLoad", panel);
                },
                onSelect: function (title, index) {
                    console.info("tabs-event: onSelect", title, index);
                },
                onUnselect: function (title, index) {
                    console.info("tabs-event: onUnselect", title, index);
                }
            });
        });


    </script>

</div>
