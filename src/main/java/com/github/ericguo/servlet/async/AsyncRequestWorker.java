package com.github.ericguo.servlet.async;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.io.PrintWriter;

public class AsyncRequestWorker implements Runnable {

	private AsyncContext asyncContext;
	private int secs;

	public AsyncRequestWorker() {
	}

	public AsyncRequestWorker(AsyncContext asyncCtx, int secs) {
		this.asyncContext = asyncCtx;
		this.secs = secs;
	}

	@Override
	public void run() {
		System.out.println("Async Supported? "
				+ asyncContext.getRequest().isAsyncSupported());
		longProcessing(secs);
		try {
			PrintWriter out = asyncContext.getResponse().getWriter();
			String str=HttpClientUtil.doSend();
			out.write("From AsyncRequestWorker: "+str);

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
