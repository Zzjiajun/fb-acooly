提供acooly框架的综合演示demo。

## 说明

启动应用：

1. 使用mysql 5.6+数据库，创建schema=showcase，字符集utf-8
2. 使用框架提供的maven settings [settings.xml](res/maven/settings.xml),并导入项目到ide
3. 修改数据库帐号密码，配置文件[application-dev.properties](acooly-showcase-assemble/src/main/resources/application-dev.properties)
3. 执行`com.acooly.showcase.Main`
4. 浏览器打开`http://127.0.0.1:8081`,帐号：admin 密码：111111
5. 浏览器打开`http://127.0.0.1:8081/docs`,查看openapi文档

开启dubbo服务：

1. 修改配置文件[`application.properties`](acooly-showcase-assemble/src/main/resources/application.properties) `acooly.dubbo.enable=true`
2. 本地启动zookeeper
3. 启动应用
4. 运行`com.acooly.showcase.test.dubbo.CustomerFacadeTest.testCreate`,测试dubbo服务。
5. 如果有网络隔离，也可以直接访问：/showcase/dubbo/testCreate.html测试



## 常用功能demo

* 父子结构的客户管理
* 树型结构的商品管理
* [dubbo服务](acooly-showcase-core/src/main/java/com/acooly/showcase/customer/facade/CustomerFacadeImpl.java),[dubbo测试](acooly-showcase-test/src/test/java/com/acooly/showcase/test/dubbo/CustomerFacadeTest.java)
* [openapi服务](acooly-showcase-core/src/main/java/com/acooly/showcase/misc/openapi),[openapi测试](acooly-showcase-test/src/test/java/com/acooly/showcase/test/openapi/PayOrderApiServiceTest.java)
* [cache](acooly-showcase-core/src/main/java/com/acooly/showcase/misc/service/CacheService.java)
* [发布事件](acooly-showcase-core/src/main/java/com/acooly/showcase/misc/web/EventController.java),[监听事件](acooly-showcase-core/src/main/java/com/acooly/showcase/misc/event/CustomerEventHandler.java)
* [mybatis常用例子](acooly-showcase-core/src/main/java/com/acooly/showcase/misc/web/CityDaoController.java)
* [web测试](acooly-showcase-test/src/test/java/com/acooly/showcase/test/web/DemoTest.java)
* [代码生成器运行类](acooly-showcase-test/src/test/java/com/acooly/showcase/test/AcoolyCoder.java),[代码生成器配置](acooly-showcase-test/src/test/resources/acoolycoder.properties)



