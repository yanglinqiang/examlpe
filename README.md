#dataFilter
##Reader

##Changer
###MetaBuilderChanger
* 将数据包按照一定逻辑区分不同的事件，事件信息存放在List<MTMessage> mTMessages，此changer将每个事件属于哪一类放入 public Map<String, List<Integer>> tmsgsMeta。key值如下：
	
		package:list<此类事件在mTMessages的index>
		exception_pb
		exception
		terminate（退出）
		keyvalue
		eventrule
		event（灵动？）
		activity（活跃）
		launch（初始化）
		init（首次使用）

###DiscardOnlyInitChanger
* 判断如果只有init事件的包直接丢弃

###SimpleDataChanger：
* 对网络类型进行标准化，标准化前：－1（离线），0（wifi），1（ 离线(m2G_3G是OFFLINE)或蜂窝网络）；标准化后：-1(离线),0(wifi),1(2G/3G),2(4G) 
* 对数据包里面的数据进行合法话验证并修正，例如对字段长度、空格的控制，对数值的范围验证和修正。

###AppkeyChanger
* 对appKey处理，如果为空，丢弃；
* 如果appkey中包含逗号，替换为下划线；如果有回车键，去掉。

###PlatformidChanger
* 对平台字段转化
		
		_android->1
		_ios->2
		_wp->4
		_applewatch->8
		_h5->16
		其他->丢弃整个包

###DeviceidChanger
* 判断mDeviceId是否合法，为空丢弃整个包
* 整个字符串只能有大小写字母、数字、横线、冒号。如果包含其他字符，丢弃
* 如果平台是applewatch，在mDeviceId前加入“w_”前缀

###ProductidChanger
* 根据Appkey和Platformid从数据库中查询productid

###DeveloperidChanger
* 根据Appkey和Platformid从数据库中查询Developerid

###GroupidChanger
* 根据Developerid从数据库中查询groupid

###ProductRegisterTimeChanger
* 根据Appkey和Platformid从数据库中查询ProductRegisters的时间

###ProductCompensateChanger
* 根据Appkey和Platformid从数据库中查询iscompensate

###EventFilterChanger

* 取出Event事件包MTMessage
* 取出List<MAppEvent>类型，在MTMessage.session.appEvents
* 对每一个MAppEvent的id标准化，如果ID的开头是“__”，从集合中删除，如果id为空，标识为丢弃
* 对event和lable进行黑白名单过滤，过滤规则如下：

event判断流程图
	
```flow

eventid=>start: eventid
e=>end: 结束
	
eventid存在=>condition: eventid
存在
	
eventidList超出限制=>condition: eventidList
超出限制
	
	
event黑名单=>condition: event
黑名单
	
丢弃=>operation: 丢弃
	
eventFlag2白名单=>operation: eventFlag=2
确定是白名单
	
暂定白名单=>operation: eventFlag=1
labelFlag=1
暂定白名单
	
lableid=>subroutine: lableId子流程
	
eventid->eventid存在(yes)->event黑名单(no,l)->eventFlag2白名单->lableid->e
eventid存在(no)->eventidList超出限制(no)->暂定白名单->e
eventid存在(yes)->event黑名单
eventidList超出限制(yes)->丢弃
event黑名单(yes)->丢弃
丢弃->e
	
```

eventLable子流程


```flow

lableId=>start: lableId
e=>end: 结束
	
lableId存在=>condition: lableId
存在
	
超过限制=>condition: lable数量
超过限制
	
lableId白名单=>condition: lableId
白名单
	
确定是白名单=>operation: labelFlag=2
确定是白名单
	
black=>operation: label="__black"
黑名单
	
outoflimit=>operation: label=__outoflimit
	
labelFlag=>operation: labelFlag=1
待确认
	
lableId->lableId存在(yes)->lableId白名单(yes)->确定是白名单->e
lableId存在(no)->超过限制(yes)->outoflimit->e
超过限制(no)->labelFlag->e
lableId白名单(no)->black->e

```
		
###PartnerNameChanger
* 标准化PartnerName＝eventPackage.mAppProfile.mPartnerName
* 如果PartnerName为空，设置为"default"
* 如果sequencenumber是“6f8199d5090442c6bf2e27a673ed4d14”，并且PartnerName中存在“-android”去掉最后一个“-android”及以后的字符。
* 去该字段的前50个字符

###PartneridChanger
* 根据productid，platformid，mPartnerName查询合作伙伴信息
* 标准化mAppProfile.partnerid 

###SessionidChanger
* 标准化sessionid
* 如果是sessionid为空或者sessionid长度既不是36也不是32，则丢弃activity/launch/terminate数据

###TimeChanger
* 将产品注册时间转化成时间戳
* 标准化mAppProfile，activity，event，exception，session的开始时间
* 标准化规则
		
		如果系统配置时间补偿：直接取发生时间
		如果系统没有配置时间补偿，判断对应的事件时间补偿，如果是：取发生时间
		如果系统没有配置时间补偿，判断对应的事件时间补偿，如果否：取收集时间
		
		发生时间：如果事件发生时间比服务器接收时间早两天,或者比应用创建时间早，或者比服务器接收时间晚4个小时（手机时间不正确）。取服务器接收消息时间。否则取服务器接收消息的时间。
		即：事件发生时间在服务器接收时间两天前或者四个小时后的范围内，是正常的。不在此范围。则用接收时间校准。
		收集时间：直接纠正事件发生时间的日期部分等于接收时间。其他部分不变。

###UseTimeChanger
* 取出terminate事件列表
* 判断duration
* duration等于－1.设置usetimelevelid＝－1，usetimelevelname＝‘exception’，usetime＝0
* duration小于等于零，设置usetime＝0，duration大于三小时，设置usetime＝10800.然后查询数据库设置对应的usetimelevelname和usetimelevelid。如果查询出为空，terminateDiscard＝true，丢弃该terminate事件

###UseIntervalChanger
* 取出launch事件列表
* 取出launch时间的duration
* 如果duration时长比产品注册时间早，intervalTime＝－1.属于异常数据
* 如果intervalTime<0且不是－1。intervalTime＝0；
* 查询数据库匹配intervalTime，给intervalid和intervalname赋值，如果查询不到丢弃该launch事件

###VersionNameChanger
* 如果是ios 取mAppVersionCode作为mAppVersionName
* 如果mAppVersionName中包含逗号，替换为下划线；如果有回车键，去掉。


