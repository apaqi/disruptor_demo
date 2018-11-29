package com.leaderus.cache.group;

import com.lmax.disruptor.EventHandler;

/**
 * @author: <a href="mailto:wangpeixuaninfo@jd.com">王培轩</a>
 * @date: 2018/11/28
 */
public class TradeTransactionVasConsumer implements EventHandler<TradeTransaction> {
    @Override
    public void onEvent(TradeTransaction event, long sequence,
                        boolean endOfBatch) throws Exception {
        //do something....
        System.out.println("消费者C1 "+event.getId()+" do something.... ");
    }
}
