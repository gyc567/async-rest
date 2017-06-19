package com.github.ericguo.servlet.rest;

import com.github.ericguo.servlet.async.AppAsyncListener;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/AsyncRestServlet/*", asyncSupported = true)
public class AsyncRestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;



	private void asyncHandleRequest(HttpServletRequest request,Map<Pattern, Class> handlers ,Pattern pattern, Object args) {
		long startTime = System.currentTimeMillis();
		System.out.println("AsyncLongRunningServlet Start::Name="
				+ Thread.currentThread().getName() + "::ID="
				+ Thread.currentThread().getId());

		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);


		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AppAsyncListener());
		asyncCtx.setTimeout(90000);

		ThreadPoolExecutor executor = (ThreadPoolExecutor) request
				.getServletContext().getAttribute("executor");

		executor.execute(new AsyncRestProcessor(asyncCtx,handlers,pattern, args));

		long endTime = System.currentTimeMillis();
		System.out.println("AsyncLongRunningServlet End::Name="
				+ Thread.currentThread().getName() + "::ID="
				+ Thread.currentThread().getId() + "::Time Taken="
				+ (endTime - startTime) + " ms.");
	}

	/**
	 * URL与请求处理器的映射
	 */
	private Map<Pattern, Class> handlers = new HashMap<Pattern, Class>() {{
		put(Pattern.compile("^/devices/([^/]+)$"), DeviceHandler.class);
	}};


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			boolean matched = false;
			String path = request.getPathInfo();
			for (Pattern pattern : handlers.keySet()) {
				Matcher matcher = pattern.matcher(path);
				if (matcher.lookingAt()) {
					int groupCount = matcher.groupCount();
					String[] args = new String[groupCount];
					if (groupCount > 0) {
						for (int i = 0; i < matcher.groupCount(); i++) {
							args[i] = matcher.group(i + 1);
						}
					}
					//matched = matchedInvoke(request, response, pattern, args);

					asyncHandleRequest(request,handlers,pattern,args);

					break;
				}

			}
			if (!matched) {
				throw new Exception(String.format("No handler found to deal with path \"%s\"", path));
			}
		} catch (Exception ex) {
			response.setStatus(500);
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			ex.printStackTrace(out);
			out.flush();
			out.close();
		}
	}



}
