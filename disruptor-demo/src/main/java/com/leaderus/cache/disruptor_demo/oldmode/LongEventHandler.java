package com.leaderus.cache.disruptor_demo.oldmode;

import com.leaderus.cache.disruptor_demo.oldmode.LongEvent;
import com.lmax.disruptor.EventHandler;

/**
 * 我们还需要一个事件消费者，也就是一个事件处理器。这个事件处理器简单地把事件中存储的数据打印到终端：
 */
public class LongEventHandler implements EventHandler<LongEvent> {

	public void onEvent(LongEvent longEvent, long sequence, boolean endOfBanch) throws Exception {
		System.out.println("Event : = " + longEvent);
	}

}
