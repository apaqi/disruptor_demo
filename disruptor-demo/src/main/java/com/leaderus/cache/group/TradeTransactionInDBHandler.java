package com.leaderus.cache.group;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author: <a href="mailto:wangpeixuaninfo@jd.com">王培轩</a>
 * @date: 2018/11/28
 */
public class TradeTransactionInDBHandler implements EventHandler<TradeTransaction>,WorkHandler<TradeTransaction> {
    @Override
    public void onEvent(TradeTransaction event, long sequence,
                        boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(TradeTransaction event) throws Exception {
        //这里做具体的消费逻辑
        System.out.println("消费者C2 "+event.getId() + " do something~~");
    }
}
