package com.github.ericguo.servlet.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * REST请求处理器
 */
public abstract class Handler {
    public HttpServletRequest request;
    public HttpServletResponse response;

    public void get(String[] args) throws Exception {
        throw new Exception("Not implemented");
    }

    public void post(String[] args) throws Exception {
        throw new Exception("Not implemented");
    }

    public void put(String[] args) throws Exception {
        throw new Exception("Not implemented");
    }

    public void delete(String[] args) throws Exception {
        throw new Exception("Not implemented");
    }

    public void writeJsonObject(Object object) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(object.toString());
        out.flush();
        out.close();
    }
}