# 1.RabbitMQ是一种异步通信机制，消息的发送者和接收者之间不建立直接的联系，而是通过RabbitMQ服务器去做中间代理.
生产者向服务器发布消息，消费者向服务器去订阅消息；生产者与服务器建立连接，
将消息发给服务器，服务器通过映射关系将消息缓存到指定队列中，消费者再与RabbitMQ服务器建立连接，
队列中有消息时，服务器会将消息发给消费者；这样做可以降低发送者和接受者之间的耦合度，一方断开连接，消息也不会丢失，
会在服务器中进行缓存；通过中间服务器代理可以做到负载均衡、集群扩展、 优先级分配等

# 2.发布者和接收者都是作为客户端和RabbitMq服务器建立一个TCP Connection连接，也可以建立多个TCP connection连接；
在一个TCP连接之上又可以创建多个通道Channel与一个Exchange建立连接；发布者就像是淘宝卖家一样，exchange就像是快递公司，
卖家与多家快递公司建立合作连接，一个卖家的多个分店和快递公司建立多个通道，rootingkey就像是快递地址，queue就像是集散中心，
接收者就像是收快递的买家；相关概念如下所示：
#### Producer：消息的发布者；相当于淘宝卖家；
#### Consumer：消息的接收者；相当于淘宝买家；
#### Connection： 就是一个TCP的连接。Producer和Consumer都是通过TCP连接到RabbitMQ Server的。相当于卖家和快递公司建立合作协议；
#### Channels： 虚拟连接。它建立在上述的TCP连接中。数据流动都是在Channel中进行的。相当于卖家的分店与快递公司之间的生意往来；
建立和关闭TCP连接耗资源，影响性能，而且TCP的连接数也有限制，限制了系统处理高并发的能力。但是，在TCP连接中建立Channel是没有上述代价的。
对于Producer或者Consumer来说，可以并发的使用多个Channel进行Publish或者Receive；
### Exchange：交换机，将消息路由到指定的消费者；相当于快递公司，将接到的快递集散之后发给各个城市的集散中心；
#### 有三种exchange，通过参数来设置；第一个模式是定向模式Direct exchange，只有当routing key 匹配时, 消息才会被传递到相应的queue中。
#### 第二种模式是广播模式Fanout exchange， 会向所有绑定的队列发送消息。
#### 第三种模式是模糊匹配模式Topic exchange，routing key由通配符构成，对routing key进行模式匹配，比如ab*可以传递到所有ab*的queue。
#### RootingKey：消息发送给谁的标示符，用来连接Exchange和queue；相当于快递中的地址，让快递公司知道将快递发给哪个集散中心；
#### Queue：消息队列，用于缓存消息的队，Consumer和Procuder都可以创建queue，队列的持久化也可以设置；相当于快递的集散中心，用来暂时存放快递；
消费者从队列中取消息；相当于买家从集散中心取快递，多个买家可以从同一个集散中心取快递；相当于多个客户端从队列里取消息；一
个客户端也可以创建多个通道从队列里取消息，相当于一个家庭的不同成员去取快递； 
程序中就是开启多个线程，通过通道从队列中取消息，实现高并发；
#### Binding：绑定exchange和queue，建立联系；

### 3. Exchange种类和消息发送模式
#### （1）default exchange
default exchange是一个没有名称的（空字符串）被broker预先申明的direct exchange。
它所拥有的一个特殊属性使它对于简单的应用程序很有作用：
每个创建的queue会与它自动绑定，使用queue名称作为routing key。举例说，当你申明一个名称为“search-indexing-online”的queue时，
AMQP broker使用“search-indexing-online”作为routing key将它绑定到default exchange。
因此，一条被发布到default exchange并且routing key为"search-indexing-online"将被路由到名称为"search-indexing-online"的queue。

#### （2）定向模式direct exchange
direct exchange严格根据消息的routing key来传送消息。direct exchange是单一传播路由消息的最佳选择，routing key将queue与exchange进行绑定，
消息根据routing key分配到指定的queue中；一个rooting key可以绑定多个queue，多个rooting key也可以绑定到同一个queue上面；

#### （3）广播模式Fanout exchange
fanout exchange路由消息到所有的与其绑定的queue中，忽略routing key。
如果N个queue被绑定到一个fanout exchange，当一条新消息被发布到exchange时，消息会被复制并且传送到这N个queue。
fanout exchange是广播路由的最佳选择。
因为一个fanout exchange传送消息的副本到每一个与其绑定的queue，它的使用情况很相似：
#### 1）大量的多用户在线（multi-player online MMO）游戏使用它更新排行榜或者其他的全体事件
#### 2）体育新闻网站使用fanout exchange向手机客户端实时发送比分更新
#### 3)分布式系统可以广播各种状态与配置更新
#### 4)群聊可以使用fanout exchange让消息在参与者之间传输

#### （4）模糊匹配模式Topic exchange
Topic exchange路由消息到一个或者多个queue， routing key可以是包含通配符的字符串，用于模糊匹配多个rooting key。
Topic exchange经常被用于实现各种发布/订阅模式的变化。Topic exchanges通常被用于多路广播路由消息。
topic类型的Exchange在匹配规则上进行了扩展，它与direct类型的Exchage相似，也是将消息路由到binding key与routing key相匹配的Queue中，
但这里的匹配规则有些不同，它约定：
#### 1）routing key与binding key是用一个句点号“. ”分隔的字符串（我们将被句点号“. ”分隔开的每一段独立的字符串称为一个单词），
如“stock.usd.nyse”、“nyse.vmw”、“quick.orange.rabbit”
#### 2）binding key中可以存在两种特殊字符“*”与“#”，用于做模糊匹配，其中“*”用于匹配一个单词，“#”用于匹配多个单词（可以是零个）

