package com.github.ericguo.servlet.disruptor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/AsyncLongRunningServletWithDisruptor", asyncSupported = true)
public class AsyncLongRunningServletWithDisruptor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		System.out.println("AsyncLongRunningServletWithDisruptor Start::Name="
				+ Thread.currentThread().getName() + "::ID="
				+ Thread.currentThread().getId());

		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);


		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AppAsyncListener());
		asyncCtx.setTimeout(9000);

		System.out.println("---asyncCtx init done,and the ASYNC_SUPPORTED---"+asyncCtx.getRequest().isAsyncSupported());
		AsyncCallEventDisruptor asyncCallEventDisruptor = (AsyncCallEventDisruptor) request
				.getServletContext().getAttribute("asyncCallEventDisruptor");
		System.out.println("---asyncCallEventDisruptor init done,and the producer---"+asyncCallEventDisruptor.producer);
		asyncCallEventDisruptor.submitEvent(asyncCtx);
		System.out.println("---asyncCallEventDisruptor submitEvent done,and the asyncCtx---"+asyncCtx);


		long endTime = System.currentTimeMillis();
		System.out.println("AsyncLongRunningServletWithDisruptor End::Name="
				+ Thread.currentThread().getName() + "::ID="
				+ Thread.currentThread().getId() + "::Time Taken="
				+ (endTime - startTime) + " ms.");
	}

}
