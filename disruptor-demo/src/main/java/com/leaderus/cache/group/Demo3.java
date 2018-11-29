package com.leaderus.cache.group;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: <a href="mailto:wangpeixuaninfo@jd.com">王培轩</a>
 * @date: 2018/11/28
 *
 * 假设如下场景：
 * 1、交易网关收到交易(P1)把交易数据发到RingBuffer中，
 * 2、负责处理增值业务的消费者C1和负责数据存储的消费者C2负责处理交易
 * 3、负责发送JMS消息的消费者C3在C1和C2处理完成后再进行处理。
 */
public class Demo3 {
    public static void main(String[] args) throws InterruptedException {
        long beginTime = System.currentTimeMillis();

        int bufferSize = 1024;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        //这个构造函数参数，相信你在了解上面2个demo之后就看下就明白了，不解释了~
        Disruptor<TradeTransaction> disruptor = new Disruptor<TradeTransaction>(new EventFactory<TradeTransaction>() {
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        }, bufferSize, executor, ProducerType.SINGLE, new BusySpinWaitStrategy());

        //使用disruptor创建消费者组C1,C2
        EventHandlerGroup<TradeTransaction> handlerGroup = disruptor.handleEventsWith(new TradeTransactionVasConsumer(), new TradeTransactionInDBHandler());

        TradeTransactionJMSNotifyHandler jmsConsumer = new TradeTransactionJMSNotifyHandler();
        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
        handlerGroup.then(jmsConsumer);


        disruptor.start();//启动
        CountDownLatch latch = new CountDownLatch(1);
        //生产者准备
        executor.submit(new TradeTransactionPublisher(latch, disruptor));
        latch.await();//等待生产者完事.
        disruptor.shutdown();
        executor.shutdown();

        System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
    }
}
