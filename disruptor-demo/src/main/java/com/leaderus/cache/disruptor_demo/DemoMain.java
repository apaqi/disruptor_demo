package com.leaderus.cache.disruptor_demo;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DemoMain {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// Executor that will be used to construct new threads for consumers
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		// The factory for the event
		LongEventFactory factory = new LongEventFactory();
		//用来指定ring buffer的大小，必须是2的倍数
		final int bufferSize = 1024;
		Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, threadFactory);
		// Connect the handler
		disruptor.handleEventsWith(new LongEventHandler());
		// Start the Disruptor, starts all threads running
		disruptor.start();
		// Get the ring buffer from the Disruptor to be used for publishing.
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		LongEventProducer producer = new LongEventProducer(ringBuffer);
		ByteBuffer bb = ByteBuffer.allocate(8);
		for( long l = 0; true; l++) {
			bb.putLong(0, l);
			producer.onData(bb);
			//Disruptor 3.0提供了lambda式的API。这样可以把一些复杂的操作放在Ring Buffer，
			// 所以在Disruptor3.0以后的版本最好使用Event Publisher或者Event Translator来发布事件。
			//即如下方式，而不用自己发布事件（producer.onData(bb)）
			//ringBuffer.publishEvent(DemoMain::translate, bb);
		}
	}
	
	public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
		event.setValue(buffer.getLong(0));
	}
	
}
