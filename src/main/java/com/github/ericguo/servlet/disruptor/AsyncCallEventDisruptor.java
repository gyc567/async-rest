package com.github.ericguo.servlet.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import javax.servlet.AsyncContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncCallEventDisruptor {

    public AsyncCallEventDisruptor() {
        init();
    }

    AsyncCallEventProducer producer;
    RingBuffer<AsyncCallEvent> ringBuffer;
    Disruptor disruptor;
    ExecutorService executor;

    public void init() {
        WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
        WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
        WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
        // Executor that will be used to construct new threads for consumers
        executor = Executors.newFixedThreadPool(300);

        // The factory for the event
        AsyncCallEventFactory factory = new AsyncCallEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        //disruptor = new Disruptor<>(factory, bufferSize, executor);
       disruptor = new Disruptor(
                factory, bufferSize, executor, ProducerType.SINGLE, YIELDING_WAIT);

        // Connect the handler
        disruptor.handleEventsWith(new AsyncCallEventHandler()).then(new ClearingEventHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        ringBuffer = disruptor.getRingBuffer();

        producer = new AsyncCallEventProducer(ringBuffer);


    }

    public void submitEvent(AsyncContext asyncContext) {
        producer.onData(asyncContext);
    }

    public void shutdown() {
        disruptor.shutdown();
        executor.shutdown();

    }


}