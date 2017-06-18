package com.github.ericguo.servlet.disruptor;

import com.lmax.disruptor.RingBuffer;

import javax.servlet.AsyncContext;

public class AsyncCallEventProducer
{
    private final RingBuffer<AsyncCallEvent> ringBuffer;

    public AsyncCallEventProducer(RingBuffer<AsyncCallEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void onData(AsyncContext asyncContext)
    {

        System.out.println("-----AsyncCallEventProducer start----");
        long sequence = ringBuffer.next();  // Grab the next sequence
        try
        {
            AsyncCallEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            event.setAsyncContext(asyncContext);
        }
        finally
        {
            ringBuffer.publish(sequence);
        }
        System.out.println("-----AsyncCallEventProducer end----");
    }
}