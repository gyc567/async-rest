package com.github.ericguo.servlet.rest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * REST请求分派器
 */
@WebServlet(name = "Dispatcher", urlPatterns = "/rest/*")
public class Dispatcher extends HttpServlet {
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
                    Class handlerClass = handlers.get(pattern);
                    Object handlerInstance = handlerClass.newInstance();
                    handlerClass.getField("request").set(handlerInstance, request);
                    handlerClass.getField("response").set(handlerInstance, response);
                    handlerClass.getMethod(request.getMethod().toLowerCase(), String[].class).invoke(
                            handlerInstance,
                            (Object) args
                    );
                    matched = true;
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