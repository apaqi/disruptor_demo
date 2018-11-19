package com.leaderus.cache.disruptor_demo;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Java8DemoMain {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// Executor that will be used to construct new threads for consumers
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		//用来指定ring buffer的大小，必须是2的倍数
		final int bufferSize = 1024;
		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, threadFactory);
		// Connect the handler 
		disruptor.handleEventsWith(Java8DemoMain::handleEvent);
		disruptor.start();
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		ByteBuffer bb = ByteBuffer.allocate(8);
		for( long l = 0; true; l++) {
			bb.putLong(0, l);
			ringBuffer.publishEvent(Java8DemoMain::translate, bb);
		}
	}

	public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
		System.out.println(event);
	}
	
	public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
		event.setValue(buffer.getLong(0));
	}
	
}
