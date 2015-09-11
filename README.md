# coolweather
《Android第一行代码》项目示例
- activity包     	用于存放所有活动相关的代码
- db包			用于存放所有数据库相关的代码
- model包		用于存放所有模型相关的代码
- receiver包		用于存放所有广播接收器相关的代码
- service包		用于存放所有服务相关的代码
- util包			用于存放所有工具相关的代码

## 天气访问接口无效
http://www.weather.com.cn/data/cityInfo/天气ID.html - 返回404页面
http://www.weather.com.cn/adat/sk/天气ID.html - 返回JSON格式数据，但是内容不全
天气ID = 101+县级ID

## 编程问题
城市活动页面一开始就读取共享文件中city_selected标识，为真直接跳转天气活动页面，但是有没有传入县级代号，
天气活动页面无法读取intent中country_code字段，导致启动崩溃。不明白作者为什么这么做，而且功能上没有任何问题。

##中国天气新API说明
http://openweather.weather.com.cn/Home/Help/Using.html
书中提供的天气访问地址已不可用，需要注册后完善个人信息，获取公钥和私钥。

