# SpringBoot集成Redis并实现消息队列的发布订阅功能

支持多个消费者消费同一条消息

一般来说，消息队列有两种场景，一种是发布者订阅者模式，一种是生产者消费者模式。利用redis这两种场景的消息队列都能够实现。

定义：
生产者消费者模式：生产者生产消息放到队列里，多个消费者同时监听队列，谁先抢到消息谁就会从队列中取走消息；即对于每个消息只能被最多一个消费者拥有。（pop操作）
发布者订阅者模式：发布者生产消息放到队列里，多个监听队列的消费者都会收到同一份消息；即正常情况下每个消费者收到的消息应该都是一样的。

Redis不仅可作为缓存服务器，还可用作消息队列。它的列表类型天生支持用作消息队列。如下图所示：

由于Redis的列表是使用双向链表实现的，保存了头尾节点，所以在列表头尾两边插取元素都是非常快的。

**1、Redis服务宕机，重启后，订阅者可以连接上Redis无需重启WEB服务**
**2、当生产者发送消息后，消费者不在启动状态，则接收不到消息**