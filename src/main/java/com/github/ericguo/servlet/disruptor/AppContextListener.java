package com.github.ericguo.servlet.disruptor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {

		//create disruptor to handle the async event
		AsyncCallEventDisruptor asyncCallEventDisruptor=new AsyncCallEventDisruptor();


		servletContextEvent.getServletContext().setAttribute("asyncCallEventDisruptor",
				asyncCallEventDisruptor);


	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {


		AsyncCallEventDisruptor asyncCallEventDisruptor= (AsyncCallEventDisruptor) servletContextEvent
				.getServletContext().getAttribute("asyncCallEventDisruptor");
		asyncCallEventDisruptor.shutdown();
	}

}
