# android-robot
聊天机器人 android sdk 实现。

网页版演示：[http://www.smartnlp.cn/demo.html](http://www.smartnlp.cn/demo.html)

## 使用方法：
Comming soon
## REST API 文档：
[http://cloud.smartnlp.cn/cloud/cloudApiDoc](http://cloud.smartnlp.cn/cloud/cloudApiDoc)
## SDK文档：
SDK 是对 REST API 的二次封，使能够在 android 平台上快速实现聊天机器人功能。

文档：[http://cloud.smartnlp.cn/cloud/developDoc](http://cloud.smartnlp.cn/cloud/developDoc)

## 如何定制知识库？
  如果您的机器人需要定制自己专属的聊天知识库
  
  (1) 请到 http://www.smartnlp.cn 去申请一个appid
  
  (2) 在后台配置问题答案 http://cloud.smartnlp.cn 
  
  (3) 用新的appid 来初始化机器人
```sh
      Robot robot = new Robot("appid");
```
## 其他平台接入：
 [weixin](http://cloud.smartnlp.cn/cloud/developDoc)
