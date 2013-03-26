服务端通用框架
使用方法
1、从svn检出以后断开svn依赖，具体操作为右击项目然后选择term，从后侧列表选择断开连接，弹出框选择删除svn信息就可
2、然后重命名项目，比方对应要想变成统计服务的，可以改名为statistics-netty-service，修改pom.xml中响应的projectid等
可以通过eclipse工具批量查找替换
3、修改响应的配置文件，如果经常对配置文件进行变动，则在applicationContext.xml中使用file://去引用外部配置，
将以将env.properties文件重名，参考browser-netty-service,在SpringNettyContainer中修改响应的log4j路径
支持log4j的热部署

如何添加handler？
myHandler演示orm层处理,baseHandler是基础handler
在响应的handler包下，添加你自己定义的handler，并通过在application-netty.xml中配置相应的handler即可
通过optype来确定需要的handler,可以参考urlHandler的map键值处理,默认有个baseHandler可以在里面重新根据
optype定义相应的handler

makefile使用
参考makefile源码就可以知道
svn up
make clean
make jar
make pid
kill -9 pid
make run 
make see

