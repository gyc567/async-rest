package com.github.ericguo.servlet.disruptor;

import javax.servlet.AsyncContext;

public class AsyncCallEvent
{
    private long value;
    private AsyncContext asyncContext;
    public void set(long value)
    {
        this.value = value;
    }


    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }


    void clear()
    {
        asyncContext = null;
    }

}