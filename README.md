## 项目说明

### 开发语言

Kotlin、Java1.8、Gradle（在Studio中配置Java1.8）

### WeBot

#### 主入口：Main.java

#### 长链接中心：

NettyHandleProtoEnum.java

#### 长链接通信：

WssProtocalManager.java

#### 监听： HookMethodEnum.java

##### 消息上报：MSG_REV("MSG_REV", "消息接收", new HookMsgRevMethod()),

备注：新增消息上报解析类型可以在MsgHandleEnum.java中参考消息解析方法，优先不解析使用透传方式上报

#### 任务下发：ActionEnum.java

##### 消息下发：PUSH_USER_MEDIA_MSG(10100L, "sendMsg", "私聊群发中")

备注：新增消息下发类型可以在遗留类MessageRunnable.java中参考消息构建方法

### 数据模拟

#### 上传全量好友

#### 主动发送文本消息

```json
{
  "actionType": "task",
  "taskType": "sendMsg",
  "msgType": "txt",
  "result": {
    "remoteId": "7881301092905731",
    "content": "哈喽 早好1"
  },
  "timestamp": 1716170746794,
  "reqid": "123"
}
```

#### 主动发送图片消息

```json
{
  "actionType": "task",
  "taskType": "sendMsg",
  "msgType": "img",
  "result": {
    "remoteId": "7881301092905731",
    "content": "https://image.baidu.com/front/aigc?atn=aigc&fr=home&imgcontent=%7B%22aigcQuery%22%3A%22%22%2C%22imageAigcId%22%3A%224199096378%22%7D&isImmersive=1&pd=image_content&quality=1&ratio=9%3A16&sa=searchpromo_shijian_photohp_inspire&tn=aigc&top=%7B%22sfhs%22%3A1%7D&word=%E5%B9%BF%E5%91%8A%E5%AE%A3%E4%BC%A0%E5%9B%BE%EF%BC%8C%E5%AE%A4%E5%86%85%E9%AB%98%E6%B8%85%EF%BC%8C%E5%B9%BF%E5%91%8A%EF%BC%8C%E6%B5%B7%E6%8A%A5%E8%B4%A8%E6%84%9F%EF%BC%8C%E9%AB%98%E6%B8%85%E6%91%84%E5%BD%B1%EF%BC%8C%E5%86%B0%E5%87%89%E7%9A%84%E6%B1%BD%E6%B0%B4%EF%BC%8C%E6%B0%B4%E6%9E%9C%E6%B1%BD%E6%B0%B4%EF%BC%8C%E6%B8%85%E6%96%B0%E7%9A%84%E8%87%AA%E7%84%B6%E7%8E%AF%E5%A2%83%EF%BC%8C%E8%B6%85%E9%AB%98%E6%B8%85%E5%88%86%E8%BE%A8%E7%8E%87%EF%BC%8C%E9%B2%9C%E8%89%B3%E8%89%B2%E5%BD%A9%EF%BC%8C%E6%B4%BB%E5%8A%9B%E5%9B%9B%E6%BA%A2%EF%BC%8C%E8%87%AA%E7%84%B6%E9%98%B3%E5%85%89%E7%85%A7%E5%B0%84%EF%BC%8C%E5%85%89%E6%BB%91%E5%86%B0%E5%87%89"
  },
  "timestamp": 1716170746794,
  "reqid": "123"
}
```

#### 主动发送卡片消息

```json
{
  "actionType": "task",
  "taskType": "sendMsg",
  "msgType": "txt",
  "result": {
    "remoteId": "1888887797879898",
    "linkUrl": "哈喽 早上好",
    "linkDesc": "哈喽 早上好",
    "linkImgUrl": "哈喽 早上好"
  },
  "timestamp": 1716170746794,
  "reqid": "123"
}
```

#### 主动发送小程序消息

```json
{
  "actionType": "task",
  "taskType": "sendMsg",
  "msgType": "miniProgram",
  "result": {
    "remoteId": "7881301092905731",
    "miniProgramThumbUrl": "https://yj-wx-hook.oss-cn-guangzhou.aliyuncs.com/wecom/robot/1688854358651905/png/b53821511abd1dbf1f162fc5546212cf.jpg",
    "miniProgramAppId": "wx974b793334f3667b",
    "miniProgramUsername": "gh_1bbc95f9877e@app",
    "miniProgramPagePath": "pro/pages/homepage/group-homepage/group-homepage.html?groupId=PsE8lKS_bsdICP3_88e7ab36&groupType=105&hotPointValue=PsE8lKS_bsdICP3_88e7ab36&inviteUid=180529422&isInvitedByManager=true",
    "miniProgramTitle": "快来看看我的社群吧！活动持续上新中，不要错过哦~",
    "miniProgramLogoUrl": "http://mmbiz.qpic.cn/mmbiz_png/ADcgxtNgPTOtaHlhicdgvBsbDlZicKiaJg4Id6gnQxRYibeqx4GePspLGia9bpkmGN2oicuXkpkZhyqjRCh3VIsRiaibibQ/640?wx_fmt=png&wxfrom=200",
    "miniProgramAppName": "群接龙活动"
  },
  "timestamp": 1716170746794,
  "reqid": "123"
}
```

#### 上报消息

```json
{
  "actionType": "uploadMsg",
  "data": {
    "content": "",
    "contentType": 78,
    "imgUrl": "",
    "linkDesc": "",
    "linkImgUrl": "",
    "linkTitle": "",
    "linkUrl": "",
    "miniProgramAppId": "wx974b793334f3667b",
    "miniProgramAppName": "群接龙活动",
    "miniProgramLogoUrl": "http://mmbiz.qpic.cn/mmbiz_png/ADcgxtNgPTOtaHlhicdgvBsbDlZicKiaJg4Id6gnQxRYibeqx4GePspLGia9bpkmGN2oicuXkpkZhyqjRCh3VIsRiaibibQ/640?wx_fmt=png&wxfrom=200",
    "miniProgramPagePath": "",
    "miniProgramThumbUrl": "https://om/wecom/robot/1688854358651905/png/b53821511abd1dbf1f162fc5546212cf.jpg",
    "miniProgramTitle": "快来看看我的社群吧！活动持续上新中，不要错过哦~",
    "miniProgramUsername": "g9877e@app",
    "msgId": 7390405786603427361,
    "receiverRemoteId": "1688854358651905",
    "senderRemoteId": "7881301092905731"
  },
  "msgType": "miniProgram",
  "reqid": "b76717bb-c123-4f0f-9759-c9241da824aa",
  "taskType": "msg"
}
```