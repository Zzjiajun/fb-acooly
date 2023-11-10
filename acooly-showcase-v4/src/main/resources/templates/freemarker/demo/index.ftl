<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="X-CSRF-TOKEN" content="${(Request["org.springframework.security.web.csrf.CsrfToken"].token)!}"/>
    <title>Acooly Freemarker demo</title>
    <style>
        body {
            margin: 0;
            font-size: 14px;
            /*overflow: hidden;*/
            height: 100%;
        }

        .container {
            width: 100%;
            height: calc(100vh - 100px);
            display: flex;
            color: #333;
        }

        .container a, .container a:visited {
            color: #777;
            text-decoration: none;
        }

        .container a:hover {
            color: steelblue;
        }

        .container .menu {
            width: 200px;
            padding: 0 5px 0 15px;
            border-right: 1px solid #eee;
        }

        .container .menu ul li small {
            color: #ddd;
        }

        .container .main {
            flex: 1;
            font-size: 16px;
            line-height: 25px;
            padding: 0 15px;
            -webkit-overflow-scrolling: touch;
            overflow-x: auto;
        }

        .container .main h2{
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
        }

        header {
            background-color: #333333;
            color: #fff;
            height: 60px;
            line-height: 60px;
            padding-left: 15px;
            font-size: 24px;
            /*font-weight: bold;*/
            font-style: italic;
            font-family: Georgia, serif;
        }

        footer {
            background-color: #333333;
            color: #fff;
            text-align: center;
            height: 40px;
            line-height: 40px;
            font-size: 12px;
        }

        .demolist {
            width: 1000px;
            display: flex
        }

        .demolist div {
            min-width: 10%;
            padding: 5px 10px;
            border: 1px solid #eee;
        }

        select {
            height: 30px;
            line-height: 30px;
            padding: 0 10px;
            min-width: 100px;
        }

        keypoint { color: red; font-weight: bold;}
    </style>
</head>

<body>
<#--无法使用相对路径-->
<#--<#include "../test/test.ftl">-->
<#--需要使用绝对路径-->
<#--<#include "/freemarker/test/test.ftl">-->
<@includePage path="/freemarker/demo/header.html"/>
<div class="container">
    <aside class="menu">
        <h3>必备基础</h3>
        <ul>
            <li><a href="#varOutput">变量输出
                    <small>枚举,Map转换</small>
                </a></li>
            <li><a href="#format">格式化
                    <small>数字,日期时间</small>
                </a></li>
            <li><a href="#iterator">迭代输出
                    <small>集合,Map</small>
                </a></li>
            <li><a href="#nullHandle">空值处理</a></li>
            <li><a href="#condition">条件判断</a></li>
            <li><a href="#servlet">Servlet指令</a></li>
        </ul>
        <h3>扩展指令</h3>
        <ul>
            <li><a href="#includePage">@includePage</a></li>
            <li>@shiroPrincipal</li>
        </ul>
        <h3>参考文档</h3>
        <ul>
            <li><a href="http://freemarker.foofun.cn/index.html" target="_blank">中文文档</a></li>
        </ul>
    </aside>
    <section class="main">

        <div id="varOutput">
            <h2>变量输出</h2>
            <ul>
                <li>
                    <#assign assignVarName = "acooly">
                    页面通过assign指令赋值的变量assignVarName：${assignVarName}
                </li>
                <li>
                    对象属性直接输出 entity.username: ${entity.username}
                </li>
                <li>
                    对象属性直接输出空值 entity.fee: ${entity.fee}
                </li>
                <li>
                    枚举值输出 ${r"$"}{entity.idcardType.message()}: ${entity.idcardType.message()}
                </li>
                <li>
                    Map&lt;String,Object&gt;转换输出 ${r"$"}{allCustomerTypes[entity.customerType]: ${allCustomerTypes[entity.customerType]}
                </li>
                <li>
                    Map&lt;Number,Object&gt;转换输出 ${r"$"}{allStatuss?api.get(entity.status): ${allStatuss?api.get(entity.status)}
                </li>
                <li>
                    空对象属性访问 ${obj.name} 不抛异常
                </li>
            </ul>
        </div>

        <div id="format">
            <h2>格式化输出</h2>
            <h3>数字格式化</h3>
            <#assign x = 102042.0083>
            assign设置数字变量 x=${x}
            <ul>
                <li>原始格式 - ${r"$"}{x} = ${r"$"}{x?string}: ${x!}</li>
                <li>内建number(最多3位小数) - ${r"$"}{x?string.number}: ${(x?string.number)!}</li>
                <li>currency货币两位小数四舍五入 - ${r"$"}{x?string.currency}: ${(x?string.currency)!}</li>
                <li>percent百分数 - ${r"$"}{x?string.percent}: ${(x?string.percent)!}</li>
                <li>Java格式化: 两位小数 - ${r"$"}{x?string["0.##"]}: ${x?string["0.##"]}</li>
                <li>Java格式化: 科学计数两位小数 - ${r"$"}{x?string["0.##"]}: ${x?string["#,###.##"]}</li>
            </ul>

            <h3>日期时间格式化</h3>
            这里以 entity.createTime 和 entity.updateTime 两个变量演示。
            <ul>
                <li>SimpleDateFormat格式化：${r"$"}{(entity.updateTime?string["yyyy-MM-dd HH:mm:ss.SSS"])!} --> ${(entity.updateTime?string["yyyy-MM-dd HH:mm:ss.SSS"])!}  </li>
                <li>SimpleDateFormat格式化：${r"$"}{(entity.createTime?string["yyyy-MM-dd HH:mm:ss"])!} --> ${(entity.createTime?string["yyyy-MM-dd HH:mm:ss"])!}  </li>
                <li>SimpleDateFormat格式化：${r"$"}{(entity.createTime?string["yyyy-MM-dd"])!} --> ${(entity.createTime?string["yyyy-MM-dd"])!}  </li>
            </ul>
        </div>

        <div id="iterator">
            <h2>迭代输出</h2>
            <h3>集合迭代</h3>
            这里直接使用控制层返回的Customer对象的集合：entities为例子。</br>
            ?is_first:表示是否为第一个变量，?is_last:表示是否为最后一个变量，?index:循环中当前项的索引(从0开始的数字)，?has_next:辨别当前项是否是序列的最后一项的布尔值
            <div>
                <#list entities as e>
                    <div class="demolist">

                        <div>${e.id}</div>
                        <div>${e?index}<#if e?is_first>first-</#if><#if e?is_last>last</#if><#if e?has_next>hasNext</#if></div>
                        <div>${e.username}</div>
                        <div>${e.realName}</div>
                        <div>${e.age}</div>
                        <div>${e.idcardType.message()}</div>
                        <div>${allCustomerTypes[e.customerType]}</div>
                        <div>${allStatuss?api.get(e.status)}</div>
                        <div>${(e.updateTime?string["yyyy-MM-dd"])!}</div>
                        <div>${e.createTime?string["yyyy-MM-dd"]}</div>
                    </div>
                </#list>
            </div>
            <#--<h3>数组(不推荐)</h3>-->
            <#--<p>下标取值:${arrays[0]}</p>-->
            <#--<p>数组长度:${arrays?size}</p>-->
            <#--<div class="demolist">-->
            <#--<#list arrays as e>-->
            <#--<div>${e}</div>-->
            <#--</#list>-->
            <#--</div>-->
            <h3>Map迭代</h3>
            <p>这里使用控制层返回的allStatuss(Map&lt;Integer,String&gt;)和allCustomerTypes(Map&lt;String,String&gt;)。</p>
            <div>
                1. allStatuss(Map&lt;Integer,String&gt;):
                <select name="status">
                    <#list allStatuss as k,v>
                        <option value="${k}">${v}</option>
                    </#list>
                </select>
            </div>
            <div style="margin-top: 15px;">
                2. allCustomerTypes(Map&lt;String,String&gt;):
                <select name="customerType">
                    <#list allCustomerTypes as k,v>
                        <option value="${k}">${v}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div id="nullHandle">
            <h2>空值处理</h2>
            <h3>默认值方式: ()!</h3>
            <p>默认值方式处理空值是最常见的方法，框架默认支持字符串，数字等类型的处理（classic_compatible=true），但如果对空值进行函数处理，比如：日期时间格式化则不能自动处理。</p>
            <ul>
                <li>空变量（一个不存在的变量）${r"$"}{anyVari} ：${anyVari}</li>
                <li>对象的属性为空 ${r"$"}{entity.fee} ：${entity.fee}</li>
                <li><keypoint>[重点]</keypoint> 被格式化输出的日期为空 ${r"$"}{(entity.updateTime?string["yyyy-MM-dd"])!} ：${(entity.updateTime?string["yyyy-MM-dd"])!}</li>
            </ul>

            <h3>判断是否为空：(var)??</h3>
            采用判断语句判断变量是否为空，然后在输出。
            <ul>
                <li>判断变量 &lt;#if anyVari??&gt;${r"$"}{anyVari}&lt;/#if&gt; ：<#if anyVari??>${anyVari}</#if></li>
                <li>判断对象及子属性变量 &lt;#if (entity.fee)??&gt;${r"$"}{entity.fee}&lt;/#if&gt; ：<#if (entity.fee)??>${entity.fee}</#if></li>
            </ul>
            这里表示同时判断entity和entity.fee是否为空。

        </div>

        <div id="condition">
            <h2>条件判断</h2>
            这里重点介绍的是条件判断表达式，而不是if语句的语法。

            <ul>
                <li>
                    <keypoint>[重点]</keypoint> 比较运算：大于，等于，大于等于，小于，小于等于，不等于的判断，
                    大于和小于相关的比较因为&lt;和&gt;与freemarker的语法关键字冲突，建议采用转移符号，否则请使用括号来包含表达式也是可以的。比较运算必须是精确类型间的比较，否则会报错
                    &lt;#assign x = 100&gt;: <#assign x = 100>
                    <ul>
                        <li>&lt;#if x gt 10 &gt;100大于10&lt;/#if&gt;: <#if x gt 10 >100大于10</#if></li>
                        <li>&lt;#if (x > 10) &gt;100大于10&lt;/#if&gt;: <#if (x > 10) >100大于10</#if></li>
                        <li>&lt;#if (x >= 10) &gt;100大于10&lt;/#if&gt;: <#if (x >= 10) >100大于等于10</#if></li>
                        <li>&lt;#if x gte 10 &gt;100大于10&lt;/#if&gt;: <#if x gte 10 >100大于等于10</#if></li>
                        <li>&lt;#if x != 10 &gt;100大于10&lt;/#if&gt;: <#if x != 10 >100不等于10</#if></li>
                        <li>&lt;#if x == 100 &gt;100大于10&lt;/#if&gt;: <#if x == 100 >100等于100</#if></li>
                    </ul>
                </li>
                <li>
                    逻辑判断：
                    <ul>
                        <li>逻辑 或：||</li>
                        <li>逻辑 与：&&</li>
                        <li><keypoint>[重点]</keypoint> 逻辑 非：!  注意:只能对boolean类型使用。
                            &lt;#assign b = false&gt; <#assign b = false>
                            &lt;#if !b &gt;b=false&lt;/#if&gt;: <#if !b >b=false</#if>
                        </li>
                    </ul>
                </li>
                <li>判断null和空字符串

                    <ul>
                        <li>判断不为null: ()??表示被判断的对变量不为空返回true。  &lt;#if (entity.fee)??&gt;${r"$"}{entity.fee}&lt;/#if&gt; --> <#if (entity.fee)??>不为空${entity.fee}</#if></li></li>
                        <li>判断为null可以使用!(x??)。  &lt;#if !(entity.fee)??&gt;${r"$"}{entity.fee}&lt;/#if&gt; --> <#if !(entity.fee)??>entity.fee为空</#if></li>
                        <li>判断为是否为空字符串，直接使用 == 或 != ""。 &lt;#if entity.subject == ""&gt;${r"$"}{entity.fee}&lt;/#if&gt; --> <#if entity.subject == "">entity.subject为空字符串</#if></li>
                        <li>判断字符串不为null也不为空字符串: &lt;#if (entity.username)?? && entity.username != "" &gt;用户名username不为空也不为空字符串&lt;/#if&gt;  --> <#if (entity.username)?? && entity.username != "">用户名username不为空也不为空字符串</#if></li>
                    </ul>



            </ul>

        </div>


        <div id="servlet">
            <h2>Servlet指令</h2>
            <ul>
                <li>Request：请求属性(request.setAttribute, model.addAttribute)可直接输出或使用Request对象。
                    例如: request.setAttribute("requestAttr") -->  ${r"$"}{requestAttr} = ${requestAttr} 或 ${r"$"}{Request.requestAttr} = ${Request.requestAttr}
                </li>
                <li>RequestParameters: 请在本页面url后增加：?requestAttr=121212 ${r"$"}{RequestParameters.requestAttr} --> ${RequestParameters.requestAttr}</li>
                <li>Session: ${r"$"}{Session.sessionAttr} --> ${Session.sessionAttr}</li>
                <li>Applcation: ${r"$"}{Application.applcationAttr} --> ${Application.applcationAttr}</li>
                <#--org.springframework.web.servlet.support.RequestContext 对象在freemarker中的名称-->
                <li>RequestContext: requestUri:${rc.requestUri}，queryString:${rc.queryString}</li>
            </ul>
        </div>

        <div id="includePage">
            <h2>@includePage</h2>
            @includePage是acooly框架扩展的freemarker指令，实现服务器端include（RequestDispatcher.include），区别freemarker的#include是静态ftl的引入。

            <ul>
                <li>&lt;@includePage path="/xxx/xxx/xx.html"&gt; : 服务器端include</li>
                <li>&lt;#include "/xxx/xxx/xx.ftl"&gt; : 静态include(由于开启classic_compatible=true，必须采用绝对路径，无法使用相对路径)</li>
            </ul>
        </div>


        <div id="apiCall">
            <h2>方法调用: ?api</h2>

            <ul>
                <li>List entities.size: ${r"$"}{entities?api.size()}  --> ${entities?api.size()}</li>
                <li>entity.hashCode(): ${r"$"}{entity?api.hashCode()}  --> ${entity?api.hashCode()}</li>
                <li>entity.toString(): ${r"$"}{entity?api.toString()}  --> ${entity?api.toString()}</li>
            </ul>
        </div>
    </section>
</div>

<#--<footer>Copyright 2012-2019 Acooly</footer>-->
</body>
</html>