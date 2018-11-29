package com.leaderus.cache.group;

import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.CountDownLatch;

/**
 * @author: <a href="mailto:wangpeixuaninfo@jd.com">王培轩</a>
 * @date: 2018/11/28
 */
public class TradeTransactionPublisher implements Runnable {

    Disruptor<TradeTransaction> disruptor;
    private CountDownLatch latch;
    private static int LOOP=10;//模拟一千万次交易的发生

    public TradeTransactionPublisher(CountDownLatch latch,Disruptor<TradeTransaction> disruptor) {
        this.disruptor=disruptor;
        this.latch=latch;
    }

    @Override
    public void run() {
        TradeTransactionEventTranslator tradeTransloator=new TradeTransactionEventTranslator();
        for(int i=0;i<LOOP;i++){
            disruptor.publishEvent(tradeTransloator);
        }
        latch.countDown();
    }
}
