package com.leaderus.cache.disruptor_demo;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

/**
 * 事件都会有一个生成事件的源，这个例子中假设事件是由于磁盘IO或者network读取数据的时候触发的，
 * 事件源使用一个ByteBuffer来模拟它接受到的数据，也就是说，事件源会在IO读取到一部分数据的时候
 * 触发事件（触发事件不是自动的，程序员需要在读取到数据的时候自己触发事件并发布）：
 */
public class LongEventProducer {
	
	private final RingBuffer<LongEvent> ringBuffer;

	public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
		super();
		this.ringBuffer = ringBuffer;
	}
	/**
	 * onData用来发布事件，每调用一次就发布一次事件事件
	 * 它的参数会通过事件传递给消费者
	 *
	 * @param bb
	 */
	public void onData(ByteBuffer bb) {
		//1.可以把ringBuffer看做一个事件队列，那么next就是得到下面一个事件槽
		long sequence = ringBuffer.next();
		try {
			//2.用上面的索引取出一个空的事件用于填充（获取该序号对应的事件对象）
			LongEvent event = ringBuffer.get(sequence);
			//3.获取要通过事件传递的业务数据
			event.setValue(bb.getLong(0));
		}
		finally {
			//发布事件
			//很明显的是：当用一个简单队列来发布事件的时候会牵涉更多的细节，
			// 这是因为事件对象还需要预先创建。发布事件最少需要两步：
			// 获取下一个事件槽并发布事件（发布事件的时候要使用try/finnally保证事件一定会被发布）。
			// 如果我们使用RingBuffer.next()获取一个事件槽，那么一定要发布对应的事件。如果不能发布事件，
			// 那么就会引起Disruptor状态的混乱。尤其是在多个事件生产者的情况下会导致事件消费者失速，
			// 从而不得不重启应用才能会恢复。
			//Disruptor 3.0提供了lambda式的API。这样可以把一些复杂的操作放在Ring Buffer，所以在Disruptor3.0以后的版本最好使用Event Publisher或者Event Translator来发布事件。
			ringBuffer.publish(sequence);
		}
	}
	

}
