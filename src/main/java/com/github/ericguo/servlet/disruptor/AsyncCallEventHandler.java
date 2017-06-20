package com.github.ericguo.servlet.disruptor;

import com.lmax.disruptor.EventHandler;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;

public class AsyncCallEventHandler implements EventHandler<AsyncCallEvent>
{
    public void onEvent(AsyncCallEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println("AsyncCallEventHandler--Event: " + event);
        AsyncContext asyncContext=event.getAsyncContext();
        System.out.println("AsyncCallEventHandler---Async Supported? "
                + asyncContext.getRequest().isAsyncSupported());
       // longProcessing(100);
        try {
            PrintWriter out = asyncContext.getResponse().getWriter();
            out.write("From AsyncRequestProcessor:Processing done for Disruptor!Hello my hero!");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //complete the processing
        asyncContext.complete();
    }

    private void longProcessing(int secs) {
        // wait for given time before finishing
        try {
            Thread.sleep(secs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}