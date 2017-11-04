# logx
Log specification, centralized processing, query interface, page display

## 打包方式
> 以myeclipse为例

1. 项目右击选择"Export..."
2. 选择"JAR file"
3. 仅选中"src/main/java"目录
4. 根据需要选择"Export Java source files and resources"是否输出源码
5. 选择打包路径和文件名，点击"Finish"

## 特性如下

1. 多日志版本定制(目前只有一版)
2. 多日志输出适配器，目前只有log4j、slf4j
3. 提供filter用于自动记录请求日志
4. 提供LogManage进行自定义日志记录
5. 提供多种API调用，用于查询日志、查看当前请求情况等
6. 提供可视化页面展示日志(基于angular1.3)，目前为初版页面

## 同时Logx也是个轻量级的工具集
1. json字符串和map互转(无需其他依赖)
2. MapKit、StrKit、HttpKit等多种工具类用于简化操作
3. 安全关闭IO、日期操作、异常消息发送、XML工具等大量有用工具
