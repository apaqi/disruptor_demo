package com.leaderus.cache.group;

import com.lmax.disruptor.EventTranslator;

import java.util.Random;
import java.util.UUID;

/**
 * @author: <a href="mailto:wangpeixuaninfo@jd.com">王培轩</a>
 * @date: 2018/11/28
 */
public class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {
    private Random random=new Random();
    @Override
    public void translateTo(TradeTransaction event, long sequence) {
        this.generateTradeTransaction(event);
    }
    private TradeTransaction generateTradeTransaction(TradeTransaction trade){
        trade.setPrice(random.nextDouble()*9999);
        trade.setId(UUID.randomUUID().toString());
        return trade;
    }
}
