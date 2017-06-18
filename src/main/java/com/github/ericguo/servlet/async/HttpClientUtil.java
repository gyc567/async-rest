package com.github.ericguo.servlet.async;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.IOException;


/**
 * Created by eric567 [email:gyc567@126.com]
 * on 5/9/2017.
 */
public class HttpClientUtil {

    public static final int TOTAL_REQUEST_NUMBER = 50000;

    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        int n= TOTAL_REQUEST_NUMBER;
        System.out.println("----start with ----"+n);
        for(int i=0;i<n;i++){

            doTest();
        }
        long end=System.currentTimeMillis();
        System.out.println("spend time :"+(end-start));
        System.out.println("----end with total request:----"+n);
    }

    public static String doSend()
    {
        // The fluent API relieves the user from having to deal with manual deallocation of system
// resources at the cost of having to buffer response content in memory in some cases.
        Content content=null;
        try {
            content=Request.Get("http://localhost:8080/asyncServlet/AsyncLongRunningServlet?time=1")
                    .execute().returnContent();

            System.out.println(content);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();

    }

    public static String doTest()
    {
        // The fluent API relieves the user from having to deal with manual deallocation of system
// resources at the cost of having to buffer response content in memory in some cases.
        Content content=null;
        try {
            content=Request.Get("http://localhost:8180/ussd/AsyncLongWorkerServlet?time=2")
                    .execute().returnContent();

            System.out.println(content);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();

    }
}
