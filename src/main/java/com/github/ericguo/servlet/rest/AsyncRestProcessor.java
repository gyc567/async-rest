package com.github.ericguo.servlet.rest;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Pattern;

public class AsyncRestProcessor implements Runnable {

    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    private AsyncContext asyncContext;
    private int secs;
    private Pattern pattern;
    private Object args;
    private Map<Pattern, Class> handlers;

    public AsyncRestProcessor() {
    }

    public AsyncRestProcessor(AsyncContext asyncCtx, Map<Pattern, Class> handlers, Pattern pattern, Object args) {
        this.asyncContext = asyncCtx;
        this.handlers = handlers;
        this.pattern = pattern;
        this.args = args;

    }

    @Override
    public void run() {
        System.out.println("Async Supported? "
                + asyncContext.getRequest().isAsyncSupported());
        try {
//			PrintWriter out = asyncContext.getResponse().getWriter();
//			out.write("From AsyncRequestProcessor:Processing done for " + secs + " milliseconds!!Hi Hero!");
            matchedInvoke(asyncContext, handlers, pattern, args);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //complete the processing
        asyncContext.complete();
    }


    private boolean matchedInvoke(AsyncContext asyncContext, Map<Pattern, Class> handlers, Pattern pattern, Object args) throws IllegalAccessException, InstantiationException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        boolean matched;
        Class handlerClass = handlers.get(pattern);
        Object handlerInstance = handlerClass.newInstance();

        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        handlerClass.getField(REQUEST).set(handlerInstance, request);
        handlerClass.getField(RESPONSE).set(handlerInstance, response);
        handlerClass.getMethod(request.getMethod().toLowerCase(), String[].class).invoke(
                handlerInstance,
                args
        );
        matched = true;
        return matched;
    }

}
