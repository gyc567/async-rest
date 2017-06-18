package com.github.ericguo.servlet.disruptor;

import com.lmax.disruptor.EventFactory;

public class AsyncCallEventFactory implements EventFactory<AsyncCallEvent>
{
    public AsyncCallEvent newInstance()
    {
        return new AsyncCallEvent();
    }
}